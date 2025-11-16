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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Hi-ya
 */
public class FXMLGestionClienteController implements Initializable {

    @FXML
    private TextField textBuscar;
    @FXML
    private TableView<?> tablaProveedores;
    @FXML
    private TableColumn<?, ?> columnaNombre;
    @FXML
    private TableColumn<?, ?> columnaApPaterno;
    @FXML
    private TableColumn<?, ?> columnaApMaterno;
    @FXML
    private TableColumn<?, ?> columnaTelefono;
    @FXML
    private TableColumn<?, ?> columnaCorreo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicRegistrar(ActionEvent event) {
    }

    @FXML
    private void clicEditar(ActionEvent event) {
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
    }
    
}
