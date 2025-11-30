package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.dominio.ProveedorImpl;
import com.imperial.modelo.pojo.Proveedor;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormularioProveedorController implements Initializable {

    @FXML
    private TextField textRFC;
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textTelefono;
    @FXML
    private TextField textCorreo;

    private Usuario usuarioSesion;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioSesion = Sesion.getUsuario();
    }    

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        crearRegistro();
    }
    
    private void crearRegistro(){
        Proveedor proveedor = new Proveedor();
        proveedor.setRFC(textRFC.getText());
        proveedor.setNombre(textNombre.getText());
        proveedor.setTelefono(textTelefono.getText());
        proveedor.setCorreo(textCorreo.getText());
        
        registrarProveedor(proveedor);
    }
    
    private void registrarProveedor(Proveedor proveedor){
    HashMap<String, Object> respuesta = ProveedorImpl.registrarProveedor(proveedor);
    boolean error = (boolean) respuesta.get("error");
    
    if(!error){
        if(usuarioSesion != null){
             BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), "Registro de nuevo proveedor: " + proveedor.getNombre());
        }
        Utilidades.mostrarAlerta("Registro Exitoso", (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
        cerrarVentana(); 
    }else{
        Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
    }
}
    
    private void cerrarVentana(){
        Stage stage = (Stage)textRFC.getScene().getWindow();
        stage.close();
    }
    
}
