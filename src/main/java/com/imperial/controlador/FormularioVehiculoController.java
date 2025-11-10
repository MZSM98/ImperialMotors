package com.imperial.controlador;

import java.net.URL;
import java.util.ResourceBundle;
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
    private ComboBox<?> comboColor;
    @FXML
    private ComboBox<?> comboAnio;
    @FXML
    private ComboBox<?> comboEstado;
    @FXML
    private ComboBox<?> comboProveedor;
    @FXML
    private ComboBox<?> comboTipo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarComboColor();
    }    
    
    private void configurarComboColor(){
        
    }
}
