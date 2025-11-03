package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormularioUsuarioController implements Initializable {

    @FXML
    private TextField textCorreo;
    @FXML
    private ComboBox<?> comboRoles;
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textContrasena;
    @FXML
    private Label labelErrorCorreo;
    @FXML
    private Label labelErrorNombre;
    @FXML
    private TextField textConfirmarContrasena;
    @FXML
    private TextField textApellidoPaterno;
    @FXML
    private TextField textApellidoMaterno;
    @FXML
    private Label labelErrorApellidoMaterno;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cerrarRegistro(ActionEvent event) {
    }

    @FXML
    private void crearRegistro(ActionEvent event) {
    }
    
}
