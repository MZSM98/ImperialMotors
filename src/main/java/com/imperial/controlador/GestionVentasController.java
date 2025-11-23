package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GestionVentasController implements Initializable {

    @FXML
    private TableView<?> tablaVehiculos;
    @FXML
    private TableColumn<?, ?> columnFecha;
    @FXML
    private TableColumn<?, ?> columnCliente;
    @FXML
    private TableColumn<?, ?> columnImporte;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void cerrarVentana(ActionEvent event) {
    }

    @FXML
    private void clicEnConsultar(ActionEvent event) {
    }
}