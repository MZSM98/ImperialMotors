package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class RegistrarVentaController implements Initializable {

    @FXML
    private TextField textVendedor;
    @FXML
    private TextField textCliente;
    @FXML
    private TextField textImporte;
    @FXML
    private TableView<?> tablaVehiculos;
    @FXML
    private TableColumn<?, ?> columnAuto;
    @FXML
    private TableColumn<?, ?> columnTipo;
    @FXML
    private TableColumn<?, ?> columnImporte;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void clicCambiarCliente(ActionEvent event) {
    }

    @FXML
    private void clicEnRegistrar(ActionEvent event) {
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
    }
}