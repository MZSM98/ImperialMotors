package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl; 
import com.imperial.dominio.UsuarioImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Encriptacion;
import com.imperial.utilidad.Sesion; 
import com.imperial.utilidad.Utilidades;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioUsuarioController implements Initializable {

    @FXML
    private TextField textCorreo;
    @FXML
    private ComboBox<String> comboRoles; 
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textContrasena;
    @FXML
    private Label labelErrorCorreo;
    @FXML
    private Label labelErrorNombre;
    @FXML
    private TextField textConfirmarContrasena;
    @FXML
    private TextField textApellidoPaterno;
    @FXML
    private TextField textApellidoMaterno;
    @FXML
    private Label labelErrorApellidoMaterno;
    @FXML
    private Label labelErrorApellidoPaterno; 
    @FXML
    private Label labelErrorContrasena; 
    @FXML
    private Label labelErrorConfirmar; 
    @FXML
    private Label labelErrorRol; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarComboBoxRoles();
    }    

    @FXML
    private void cerrarRegistro(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void crearRegistro(ActionEvent event) {
        if(validarCampos()){
            Usuario usuario = new Usuario();
            usuario.setNombre(textNombre.getText());
            usuario.setApellidoPaterno(textApellidoPaterno.getText());
            usuario.setApellidoMaterno(textApellidoMaterno.getText());
            usuario.setCorreo(textCorreo.getText());
            String passwordPlano = textContrasena.getText();
            String passwordHash = Encriptacion.hashPassword(passwordPlano);
            usuario.setContrasena(passwordHash);
            
            String rolSeleccionado = comboRoles.getValue();
            int idRol = "Administrador".equals(rolSeleccionado) ? 1 : 2;
            usuario.setIdRol(idRol);
            
            registrarUsuario(usuario);
        }
    }
    
    private void configurarComboBoxRoles(){
        ObservableList<String> roles = FXCollections.observableArrayList("Administrador", "Vendedor");
        comboRoles.setItems(roles);
    }
    
    private boolean validarCampos(){
        boolean esValido = true;
        
        labelErrorNombre.setText("");
        labelErrorApellidoPaterno.setText("");
        labelErrorApellidoMaterno.setText("");
        labelErrorCorreo.setText("");
        labelErrorContrasena.setText("");
        labelErrorConfirmar.setText("");
        labelErrorRol.setText("");

        if(textNombre.getText() == null || textNombre.getText().isEmpty()){
            esValido = false;
            labelErrorNombre.setText("Campo obligatorio");
        }
        if(textApellidoPaterno.getText() == null || textApellidoPaterno.getText().isEmpty()){
            esValido = false;
            labelErrorApellidoPaterno.setText("Campo obligatorio");
        }
        if(textApellidoMaterno.getText() == null || textApellidoMaterno.getText().isEmpty()){
            esValido = false;
            labelErrorApellidoMaterno.setText("Campo obligatorio");
        }
        if(textCorreo.getText() == null || textCorreo.getText().isEmpty()){
            esValido = false;
            labelErrorCorreo.setText("Campo obligatorio");
        }
        if(textContrasena.getText() == null || textContrasena.getText().isEmpty()){
            esValido = false;
            labelErrorContrasena.setText("Campo obligatorio");
        }
        if(textConfirmarContrasena.getText() == null || textConfirmarContrasena.getText().isEmpty()){
            esValido = false;
            labelErrorConfirmar.setText("Campo obligatorio");
        }
        if(comboRoles.getValue() == null){
            esValido = false;
            labelErrorRol.setText("Debe seleccionar un rol");
        }
        
        if(esValido){ 
            esValido = validarFormatos();
        }
        
        return esValido;
    }
    
    private void registrarUsuario(Usuario usuario){
        HashMap<String, Object> respuesta = UsuarioImpl.registrarUsuario(usuario);
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            Usuario usuarioSesion = Sesion.getUsuario();
            if(usuarioSesion != null){
                BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), 
                                     usuarioSesion.getNombre(), 
                                     "Registro de nuevo usuario: " + usuario.getCorreo());
            }
            Utilidades.mostrarAlerta("Registro Exitoso", (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
            cerrarRegistro(null); 
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private boolean validarFormatos(){
        boolean esValido = true;
        if(!textCorreo.getText().contains("@") || !textCorreo.getText().contains(".")){
            esValido = false;
            labelErrorCorreo.setText("Formato de correo no válido");
        }
        if(textContrasena.getText().length() < 6){
            esValido = false;
            labelErrorContrasena.setText("Debe tener al menos 6 caracteres");
        }
        if(!textContrasena.getText().equals(textConfirmarContrasena.getText())){
            esValido = false;
            labelErrorConfirmar.setText("Las contraseñas no coinciden");
        }
        if(UsuarioImpl.verificarDuplicado(textCorreo.getText())){
            esValido = false;
            labelErrorCorreo.setText("El correo ya está registrado");
        }
        return esValido; 
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) textCorreo.getScene().getWindow();
        escenario.close();
    }
    
}