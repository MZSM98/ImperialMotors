/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Hi-ya
 */
public class FXMLFormularioClienteController implements Initializable {

    @FXML
    private TextField textNombre;
    @FXML
    private TextField textApPaterno;
    @FXML
    private TextField textApMaterno;
    @FXML
    private TextField textCorreo;
    @FXML
    private TextField textTelefono;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicCancelar(ActionEvent event) {
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
    }
    
}
