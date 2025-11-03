package com.imperial.controlador;

import com.imperial.dominio.AutenticacionImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InicioSesionController implements Initializable {

    @FXML
    private TextField textCorreo;
    @FXML
    private TextField textContrasena;
    @FXML
    private Label labelErrorContrasena;
    @FXML
    private Label labelErrorCorreo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
    }    

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String noPersonal = textCorreo.getText();
        String contrasena = textContrasena.getText();
        
        if (sonDatosValidos(noPersonal, contrasena)){
            validarSesion(noPersonal, contrasena);
        }
    }
    
    private boolean sonDatosValidos(String correo, String contrasena){
        
        boolean correctos = true;
        labelErrorCorreo.setText("");
        labelErrorContrasena.setText("");
        
        if (correo == null || correo.isEmpty()){
            correctos = false;
            labelErrorCorreo.setText("Número de personal obligatorio");
        }
        
        if (contrasena == null || contrasena.isEmpty()){
            correctos = false;
            labelErrorContrasena.setText("Contraseña obligatoria");
        }
        
        return correctos;
    }
    
    private void validarSesion(String correo, String contrasena){
        HashMap<String, Object> respuesta = AutenticacionImpl.verificarCredencialesUsuario(correo, contrasena);
        boolean error = (boolean) respuesta.get("Error");
        
        if (!error){
            Usuario usuarioSesion = (Usuario)respuesta.get("Usuario");
            Utilidades.mostrarAlerta("Credenciales Correctas", "Bienvenido(a) " +
                    usuarioSesion.getNombre() + ", al Sistema de Gestión Vehicular" ,Alert.AlertType.INFORMATION);
            irPantallaPrincipal(usuarioSesion);
            
        } else {
            Utilidades.mostrarAlerta("Credenciales Incorrectas", "correo y/o contraseña"
                    + " incorrectos, por favor verifique la información"
                    ,Alert.AlertType.INFORMATION);
        }
        
    }
    
    private void irPantallaPrincipal(Usuario usuario){

            if ("Administrador".equals(usuario.getRol())){
                irMenuPrincipalAdmin();
            }else if("Vendedor".equals(usuario.getRol())){
                irMenuPrincipalVendedor();
            }else{
                Utilidades.mostrarAlerta("Advertencia", "Por el momento no tenemos servicio, intenta más tarde"
                    ,Alert.AlertType.WARNING);
            }
            
    }
    
    private void irMenuPrincipalAdmin(){
        
        try{
            
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLPrincipalAdmin.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = (Stage) textCorreo.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Menu Principal");
            escenario.show();
            
        }catch (IOException ioe){
            ioe.printStackTrace();//Temporal, se cambiará por LOG4J
        }
    }
    
    private void irMenuPrincipalVendedor(){
        
        try{
            
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLPrincipalVendedor.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = (Stage) textCorreo.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Menu Principal");
            escenario.show();
            
        }catch (IOException ioe){
            ioe.printStackTrace();//Temporal, se cambiará por LOG4J
        }
    }
}
