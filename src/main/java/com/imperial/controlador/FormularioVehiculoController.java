package com.imperial.controlador;

import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Sesion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormularioVehiculoController implements Initializable {

    @FXML
    private TextField textVin;
    @FXML
    private TextField textMarca;
    @FXML
    private TextField textModelo;
    @FXML
    private TextField textPrecio;
    @FXML
    private Label labelErrorVIN;
    @FXML
    private Label labelErrorMarca;
    @FXML
    private Label labelErrorModelo;
    @FXML
    private Label labelErrorPrecio;
    @FXML
    private ComboBox<?> comboAnio;
    @FXML
    private ComboBox<?> comboEstado;
    @FXML
    private ComboBox<?> comboProveedor;
    @FXML
    private ComboBox<?> comboTipo;
 
    private Usuario usuarioSesion;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioSesion = Sesion.getUsuario();
    }    
    
    
    @FXML
    private void clicEnRegistrar(ActionEvent event) {
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
    }
}
