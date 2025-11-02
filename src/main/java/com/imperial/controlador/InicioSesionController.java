package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InicioSesionController implements Initializable {

    @FXML
    private TextField textCorreo;
    @FXML
    private TextField textContrasena;
    @FXML
    private Label labelErrorUsuario;
    @FXML
    private Label labelErrorContrasena;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
    }    

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String noPersonal = textCorreo.getText();
        String contrasena = textContrasena.getText();
        
        if (sonDatosValidos(noPersonal, contrasena)){
            validarSesion(noPersonal, contrasena);
        }
    }
    
}
