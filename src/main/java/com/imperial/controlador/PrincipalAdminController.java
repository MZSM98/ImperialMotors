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
    }

    @FXML
    private void abrirGestionProveedores(ActionEvent event) {
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
            Stage escenario = (Stage) botonCerrarSesion.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Menu Principal");
            escenario.show();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
}
