package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author User
 */
public class GestionUsuariosController implements Initializable {

    @FXML
    private TableView<?> tablaUsuarios;
    @FXML
    private TableColumn<?, ?> columnaApellidoPaterno;
    @FXML
    private TableColumn<?, ?> columnaApellidoMaterno;
    @FXML
    private TableColumn<?, ?> columnaNombre;
    @FXML
    private TableColumn<?, ?> columnaCorreo;
    @FXML
    private TableColumn<?, ?> columnaRol;
    @FXML
    private TextField textBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaUsuarios();
        llenarTablaUsuarios();
    }    

    @FXML
    private void regresarVentanaAnterior(ActionEvent event) {
    }

    @FXML
    private void abrirFormularioRegistroUsuarios(ActionEvent event) {
    }
    
    private void configurarTablaUsuarios(){
        
    }
    private void llenarTablaUsuarios(){
        
    }
    
}
