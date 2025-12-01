package com.imperial.controlador;

import com.imperial.utilidad.GeneradorPDF;
import com.imperial.dominio.BitacoraImpl;
import com.imperial.modelo.pojo.Usuario;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.stage.FileChooser;
import com.imperial.dominio.VehiculoImpl;
import com.imperial.modelo.pojo.Vehiculo;
import com.imperial.utilidad.InterfazSeleccion;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventarioVehiculoController implements Initializable {

    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn columnVIN;
    @FXML
    private TableColumn columnMarca;
    @FXML
    private TableColumn columnModelo;
    @FXML
    private TableColumn columnAnio;
    @FXML
    private TableColumn columnPrecio;
    @FXML
    private TableColumn columnTipo;
    @FXML
    private TextField textBuscar;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonEditar;
    @FXML
    private Button botonEliminar;
    private ObservableList vehiculos;
    
    private InterfazSeleccion<Vehiculo> observador;
    private boolean modoSeleccion = false;
    @FXML
    private Button botonExportar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        llenarTablaVehiculos();
        
        tablaVehiculos.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && modoSeleccion){
                seleccionarVehiculo();
            }
        });
    }    

    @FXML
    private void clicEnRegistrar(ActionEvent event) {
        abrirFormulario();
    }


    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage)textBuscar.getScene().getWindow();
        stage.close();
    }
    
    private void configurarTabla(){
        vehiculos = FXCollections.observableArrayList();
        columnVIN.setCellValueFactory(new PropertyValueFactory("VIN"));
        columnMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        columnModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        columnAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        columnPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        columnTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        tablaVehiculos.setItems(vehiculos);
    }
    
    private void llenarTablaVehiculos(){
        HashMap<String, Object> respuesta = VehiculoImpl.obtenerVehiculos();
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            ArrayList<Vehiculo> listaVehiculos = (ArrayList<Vehiculo>) respuesta.get("vehiculos");
            vehiculos.clear();
            vehiculos.addAll(listaVehiculos);
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void abrirFormulario(){
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioVehiculo.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Registrar Vehiculo");
            escenario.initModality(Modality.APPLICATION_MODAL); 
            escenario.showAndWait(); 
            
            llenarTablaVehiculos();
            
        }catch (IOException ioe){
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }

    
    private void seleccionarVehiculo(){
        Vehiculo auto = tablaVehiculos.getSelectionModel().getSelectedItem();
        if(auto != null){
            observador.notificarSeleccion(auto);
            cerrarVentana(null);
        }
    }

    public void iniciarModoSeleccion(InterfazSeleccion<Vehiculo> observador){
        this.observador = observador;
        this.modoSeleccion = true;
        
        if(botonRegistrar != null) botonRegistrar.setVisible(false);
        if(botonEditar != null) botonEditar.setVisible(false);
        if(botonEliminar != null) botonEliminar.setVisible(false);
        if(botonExportar != null) botonExportar.setVisible(false);
    }
    
    
    @FXML
    private void clicEnEditar(ActionEvent event) {
    }

    @FXML
    private void clicEnEliminar(ActionEvent event) {
    }

    @FXML
    private void clicExportar(ActionEvent event) {
        
        if (vehiculos == null || vehiculos.isEmpty()) {
            Utilidades.mostrarAlerta("Sin datos", "No hay vehículos para exportar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Inventario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        
        Stage stageActual = (Stage) tablaVehiculos.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stageActual);

        if (archivo != null) {
            try {
                List<String> encabezados = Arrays.asList("VIN", "Marca", "Modelo", "Año", "Tipo", "Precio", "Estado");

                List<List<String>> datosTabla = new ArrayList<>();

                for (Object obj : vehiculos) {
                    Vehiculo v = (Vehiculo) obj;
                    List<String> fila = new ArrayList<>();
                    
                    fila.add(v.getVIN() != null ? v.getVIN() : "");
                    fila.add(v.getMarca() != null ? v.getMarca() : "");
                    fila.add(v.getModelo() != null ? v.getModelo() : "");
                    fila.add(v.getAnio() != null ? v.getAnio() : "");
                    fila.add(v.getTipo() != null ? v.getTipo() : "");
                    fila.add(String.format("$%,.2f", v.getPrecio())); // Formato moneda
                    fila.add(v.getEstado() != null ? v.getEstado() : "Disponible");
                    
                    datosTabla.add(fila);
                }

                GeneradorPDF.generarReporteTabla(archivo, "Inventario General", encabezados, datosTabla);

                Usuario usuarioSesion = Sesion.getUsuario();
                if (usuarioSesion != null) {
                    BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), 
                                         usuarioSesion.getNombre(), 
                                         "Exportó PDF (Tabla): Inventario Vehicular");
                }

                Utilidades.mostrarAlerta("Éxito", "Reporte generado correctamente.", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                Utilidades.mostrarAlerta("Error", "Error al generar PDF: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }
}
