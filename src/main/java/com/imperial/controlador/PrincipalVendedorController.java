package com.imperial.controlador;

import com.imperial.modelo.pojo.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class PrincipalVendedorController implements Initializable {

    private Usuario usuarioSesion;
    @FXML
    private Label labelNombre;
    
    public void setUsuario(Usuario usuario){
        this.usuarioSesion = usuario;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void clicVentas(ActionEvent event) {
    }

    @FXML
    private void clicClientes(ActionEvent event) {
    }

    @FXML
    private void clicVehiculos(ActionEvent event) {
    }

    @FXML
    private void clicReportes(ActionEvent event) {
    }
    
}
