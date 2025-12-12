package com.imperial.controlador;

import com.imperial.dominio.ProveedorImpl;
import com.imperial.modelo.pojo.Proveedor;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
public class GestionProveedorController implements Initializable {

    @FXML
    private TableView<Proveedor> tablaProveedores;
    @FXML
    private TableColumn columnaRFC;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaCorreo;
    @FXML
    private TextField textBuscar;
    @FXML
    private TableColumn columnaTelefono;
    
    private ObservableList<Proveedor> proveedores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        llenarTablaProveedores();
        configurarBusqueda();
    }    

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage escenario = (Stage) tablaProveedores.getScene().getWindow();
        escenario.close();
    }

    private void configurarTabla(){
        proveedores = FXCollections.observableArrayList();
        columnaRFC.setCellValueFactory( new PropertyValueFactory("RFC"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        tablaProveedores.setItems(proveedores);
    }
    
    private void llenarTablaProveedores(){
        HashMap<String, Object> respuesta = ProveedorImpl.obtenerProveedores();
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            ArrayList<Proveedor> listaProveedores = (ArrayList<Proveedor>) respuesta.get("proveedores");
            proveedores.clear();
            proveedores.addAll(listaProveedores);
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario(null); 
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Proveedor proveedor = tablaProveedores.getSelectionModel().getSelectedItem();
        if (proveedor != null) {
            abrirFormulario(proveedor); 
        } else {
            Utilidades.mostrarAlerta("Selección requerida", "Debe seleccionar un proveedor de la lista para editarlo", Alert.AlertType.WARNING);
        }
    }
    
    private void abrirFormulario(Proveedor proveedorEdicion){
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioProveedor.fxml");
            Parent vista = cargador.load();
            
            if (proveedorEdicion != null) {
                FormularioProveedorController ctrl = cargador.getController();
                ctrl.inicializarDatos(proveedorEdicion); 
            }
            
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle(proveedorEdicion == null ? "Registrar Proveedor" : "Editar Proveedor");
            escenario.initModality(Modality.APPLICATION_MODAL); 
            escenario.showAndWait(); 
            
            llenarTablaProveedores();
            
        }catch (IOException ioe){
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista del formulario", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }
    
    private void configurarBusqueda() {
        if (proveedores.size() > 0) {
            FilteredList<Proveedor> filtrado = new FilteredList<>(proveedores, p -> true);
            
            textBuscar.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtrado.setPredicate(proveedor -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lower = newValue.toLowerCase();
                        
                        if (proveedor.getNombre().toLowerCase().contains(lower)) {
                            return true;
                        }
                        if (proveedor.getRFC().toLowerCase().contains(lower)) {
                            return true;
                        }
                        if (proveedor.getCorreo() != null && proveedor.getCorreo().toLowerCase().contains(lower)) {
                            return true;
                        }
                        return false;
                    });
                }
            });
            
            SortedList<Proveedor> ordenados = new SortedList<>(filtrado);
            ordenados.comparatorProperty().bind(tablaProveedores.comparatorProperty());
            tablaProveedores.setItems(ordenados);
        }
    }
}
