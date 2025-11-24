package com.imperial.controlador;

import com.imperial.dominio.ProveedorImpl;
import com.imperial.modelo.pojo.Proveedor;
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
    private TableColumn<?, ?> columnaTelefono;
    
    private ObservableList<Proveedor> proveedores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        llenarTablaProveedores();
    }    

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage escenario = (Stage) tablaProveedores.getScene().getWindow();
        escenario.close();
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario();
    }

    @FXML
    private void clicEditar(ActionEvent event) {
    }
    
    private void configurarTabla(){
        proveedores = FXCollections.observableArrayList();
        columnaRFC.setCellValueFactory( new PropertyValueFactory("RFC"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        tablaProveedores.setItems(proveedores);
    }
    
    private void abrirFormulario(){
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioProveedor.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Registrar Proveedor");
            escenario.initModality(Modality.APPLICATION_MODAL); 
            escenario.showAndWait(); 
            
            llenarTablaProveedores();
            
        }catch (IOException ioe){
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }
    
    private void llenarTablaProveedores(){
        HashMap<String, Object> respuesta = ProveedorImpl.obtenerProveedores();
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            ArrayList<Proveedor> listaProveedores = (ArrayList<Proveedor>) respuesta.get("proveedor");
            proveedores.clear();
            proveedores.addAll(listaProveedores);
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
}
