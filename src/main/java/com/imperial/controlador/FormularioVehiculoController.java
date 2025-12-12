package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.dominio.ProveedorImpl;
import com.imperial.dominio.VehiculoImpl;
import com.imperial.modelo.pojo.Proveedor;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.modelo.pojo.Vehiculo;
import com.imperial.utilidad.RestriccionCampos;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class FormularioVehiculoController implements Initializable {

    @FXML private TextField textVin;
    @FXML private TextField textMarca;
    @FXML private TextField textModelo;
    @FXML private TextField textPrecio;
    @FXML private Label labelErrorVIN;
    @FXML private Label labelErrorMarca;
    @FXML private Label labelErrorModelo;
    @FXML private Label labelErrorPrecio;
    @FXML private ComboBox<String> comboAnio;
    @FXML private ComboBox<Proveedor> comboProveedor;
    @FXML private ComboBox<String> comboTipo;
    
    private Usuario usuarioSesion;
    private Vehiculo vehiculoEdicion;
    private static final String CAMPO_REQUERIDO = "Campo requerido";
    private static final int LIMITE_VIN = 17;
    private static final String VIN_REGEX = "(?i)^[A-HJ-NPR-Z0-9]{17}$";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioSesion = Sesion.getUsuario();
        configurarCombos();
        aplicarRestricciones();
    }    
    
    private void configurarCombos() {
        ObservableList<String> anios = FXCollections.observableArrayList();
        for (int i = 2026; i >= 1990; i--) {
            anios.add(String.valueOf(i));
        }
        comboAnio.setItems(anios);
        
        comboTipo.setItems(FXCollections.observableArrayList("Nuevo", "Seminuevo"));
        
        cargarProveedores();
    }

    private void cargarProveedores() {
        HashMap<String, Object> resp = ProveedorImpl.obtenerProveedores();
        if (!(boolean) resp.get("error")) {
            ArrayList<Proveedor> lista = (ArrayList<Proveedor>) resp.get("proveedores");
            comboProveedor.setItems(FXCollections.observableArrayList(lista));
        } else {
            Utilidades.mostrarAlerta("Error", (String) resp.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    public void inicializarDatos(Vehiculo vehiculo) {
        this.vehiculoEdicion = vehiculo;
        if (vehiculo != null) {
            textVin.setText(vehiculo.getVIN());
            textVin.setEditable(false);
            textMarca.setText(vehiculo.getMarca());
            textModelo.setText(vehiculo.getModelo());
            textPrecio.setText(String.valueOf(vehiculo.getPrecio()));
            comboAnio.setValue(vehiculo.getAnio());
            comboTipo.setValue(vehiculo.getTipo());
            
            for (Proveedor p : comboProveedor.getItems()) {
                if (p.getIdProveedor() == vehiculo.getIdProveedor()) {
                    comboProveedor.setValue(p);
                    break;
                }
            }
        }
    }

    // En com.imperial.controlador.FormularioVehiculoController

@FXML
private void clicEnRegistrar(ActionEvent event) {
    if (validarCampos()) {
        
        if (vehiculoEdicion == null && VehiculoImpl.comprobarExistenciaVin(textVin.getText())) {
                labelErrorVIN.setText("VIN ya registrado");
                return; 
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setVIN(vehiculoEdicion != null ? vehiculoEdicion.getVIN() : textVin.getText());
        vehiculo.setMarca(textMarca.getText());
        vehiculo.setModelo(textModelo.getText());
        vehiculo.setAnio(comboAnio.getValue());
        vehiculo.setPrecio(Double.parseDouble(textPrecio.getText()));
        vehiculo.setTipo(comboTipo.getValue());
        vehiculo.setIdProveedor(comboProveedor.getValue().getIdProveedor());

        HashMap<String, Object> respuesta;
        if (vehiculoEdicion == null) {
            respuesta = VehiculoImpl.registrarVehiculo(vehiculo);
        } else {
            respuesta = VehiculoImpl.editarVehiculo(vehiculo);
        }

        if (!(boolean) respuesta.get("error")) {
            Utilidades.mostrarAlerta("Éxito", (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
            if (usuarioSesion != null) {
                String accion = (vehiculoEdicion == null) ? "Registró vehículo: " : "Editó vehículo: ";
                BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), accion + vehiculo.getVIN());
            }
            cerrarVentana(null);
        } else {
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
}
    
    private boolean validarCampos() {
        boolean valido = true;
        labelErrorVIN.setText(""); labelErrorMarca.setText(""); 
        labelErrorModelo.setText(""); labelErrorPrecio.setText("");

        if (textVin.getText().isEmpty()) { labelErrorVIN.setText(CAMPO_REQUERIDO); valido = false; }
        if (textMarca.getText().isEmpty()) { labelErrorMarca.setText(CAMPO_REQUERIDO); valido = false; }
        if (textModelo.getText().isEmpty()) { labelErrorModelo.setText(CAMPO_REQUERIDO); valido = false; }
        
        try {
            Double.parseDouble(textPrecio.getText());
        } catch (NumberFormatException e) {
            labelErrorPrecio.setText("Debe ser numérico");
            valido = false;
        }
        
        if(!textVin.getText().matches(VIN_REGEX)){
            labelErrorVIN.setText("Formato no valido");
            valido = false;
        }

        if (comboProveedor.getValue() == null || comboAnio.getValue() == null || comboTipo.getValue() == null) {
            Utilidades.mostrarAlerta("Campos vacíos", "Por favor seleccione"
                    + " todas las caracteristicas", Alert.AlertType.WARNING);
            valido = false;
        }
        return valido;
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) textVin.getScene().getWindow();
        stage.close();
    }
    
    private void aplicarRestricciones(){
        RestriccionCampos.soloNumeros(textPrecio);
        RestriccionCampos.soloLetras(textMarca);
        RestriccionCampos.limitarLongitud(textModelo);
        RestriccionCampos.limitarLongitudCampo(textVin, LIMITE_VIN );
    }
}