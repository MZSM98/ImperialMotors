package com.imperial.controlador;

import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PrincipalAdminController implements Initializable {

    @FXML
    private Button botonCerrarSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    private void cerrarSesion(ActionEvent event) {
    }

    @FXML
    private void abrirGestionClientes(ActionEvent event) {
    }

    @FXML
    private void abrirGestionVehiculos(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLInventarioVehiculo.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Inventario");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @FXML
    private void abrirGestionProveedores(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionProveedor.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Gestion de Proveedores");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @FXML
    private void abrirGestionVentas(ActionEvent event) {
    }

    @FXML
    private void abrirGestionUsuarios(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionUsuarios.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Gestión de Usuarios");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
}
