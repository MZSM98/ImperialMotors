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
    
    private static final String CAMPO_OBLIGATORIO = "Campos obligatorios";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    private Usuario usuarioEdicion; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarComboBoxRoles();
    }    

    public void inicializarDatos(Usuario usuario) {
        this.usuarioEdicion = usuario;
        if (usuario != null) {
            textNombre.setText(usuario.getNombre());
            textApellidoPaterno.setText(usuario.getApellidoPaterno());
            textApellidoMaterno.setText(usuario.getApellidoMaterno());
            textCorreo.setText(usuario.getCorreo());
            comboRoles.setValue(usuario.getRol()); 
            
            textCorreo.setEditable(false);
            
            textContrasena.setPromptText("Dejar vacío para mantener actual");
            textConfirmarContrasena.setPromptText("Dejar vacío para mantener actual");
        }
    }

    @FXML
    private void cerrarRegistro(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void crearRegistro(ActionEvent event) {
        if(validarCampos()){
            if (usuarioEdicion == null) {
                registrarNuevoUsuario();
            } else {
                editarUsuarioExistente();
            }
        }
    }
    
    private void registrarNuevoUsuario() {
        Usuario usuario = new Usuario();
        llenarDatosUsuario(usuario);
        
        String passwordHash = Encriptacion.hashPassword(textContrasena.getText());
        usuario.setContrasena(passwordHash);

        HashMap<String, Object> respuesta = UsuarioImpl.registrarUsuario(usuario);
        procesarRespuesta(respuesta, "Registro de nuevo usuario: " + usuario.getCorreo(), "Registro Exitoso");
    }

    private void editarUsuarioExistente() {
        llenarDatosUsuario(usuarioEdicion);
        
        if (!textContrasena.getText().isEmpty()) {
            String passwordHash = Encriptacion.hashPassword(textContrasena.getText());
            usuarioEdicion.setContrasena(passwordHash);
        }
        
        HashMap<String, Object> respuesta = UsuarioImpl.editarUsuario(usuarioEdicion);
        procesarRespuesta(respuesta, "Edición de usuario: " + usuarioEdicion.getCorreo(), "Actualización Exitosa");
    }

    private void llenarDatosUsuario(Usuario usuario) {
        usuario.setNombre(textNombre.getText());
        usuario.setApellidoPaterno(textApellidoPaterno.getText());
        usuario.setApellidoMaterno(textApellidoMaterno.getText());
        usuario.setCorreo(textCorreo.getText());
        
        String rolSeleccionado = comboRoles.getValue();
        int idRol = "Administrador".equals(rolSeleccionado) ? 1 : 2; 
        usuario.setIdRol(idRol);
    }

    private void procesarRespuesta(HashMap<String, Object> respuesta, String accionBitacora, String tituloAlerta) {
        boolean error = (boolean) respuesta.get("error");
        if(!error){
            Usuario usuarioSesion = Sesion.getUsuario();
            if(usuarioSesion != null){
                BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), 
                                     usuarioSesion.getNombre(), 
                                     accionBitacora);
            }
            Utilidades.mostrarAlerta(tituloAlerta, (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
            cerrarRegistro(null); 
        } else {
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void configurarComboBoxRoles(){
        ObservableList<String> roles = FXCollections.observableArrayList("Administrador", "Vendedor");
        comboRoles.setItems(roles);
    }
    
    private boolean validarCampos(){
        boolean esValido = true;
        limpiarErrores();

        if(esVacio(textNombre)) { labelErrorNombre.setText(CAMPO_OBLIGATORIO); esValido = false; }
        if(esVacio(textApellidoPaterno)) { labelErrorApellidoPaterno.setText(CAMPO_OBLIGATORIO); esValido = false; }
        if(esVacio(textApellidoMaterno)) { labelErrorApellidoMaterno.setText(CAMPO_OBLIGATORIO); esValido = false; }
        if(esVacio(textCorreo)) { labelErrorCorreo.setText(CAMPO_OBLIGATORIO); esValido = false; }
        if(comboRoles.getValue() == null) { labelErrorRol.setText(CAMPO_OBLIGATORIO); esValido = false; }

        boolean validandoPassword = (usuarioEdicion == null) || !textContrasena.getText().isEmpty();
        
        if (validandoPassword) {
            if (esVacio(textContrasena)) {
                labelErrorContrasena.setText(CAMPO_OBLIGATORIO);
                esValido = false;
            } else if (textContrasena.getText().length() < 6) {
                labelErrorContrasena.setText("Mínimo 6 caracteres");
                esValido = false;
            }
            
            if (esVacio(textConfirmarContrasena)) {
                labelErrorConfirmar.setText(CAMPO_OBLIGATORIO);
                esValido = false;
            } else if (!textContrasena.getText().equals(textConfirmarContrasena.getText())) {
                labelErrorConfirmar.setText("Las contraseñas no coinciden");
                esValido = false;
            }
        }
        
        if(esValido && (usuarioEdicion == null || !textCorreo.getText().equals(usuarioEdicion.getCorreo()))){ 
             esValido = validarFormatos();
        }
        
        return esValido;
    }
    
    private boolean validarFormatos(){
        
        if(textCorreo.getText().matches(EMAIL_REGEX)){
            labelErrorCorreo.setText("Formato de correo no válido");
            return false;
        }
        if(UsuarioImpl.verificarDuplicado(textCorreo.getText())){
            labelErrorCorreo.setText("El correo ya está registrado");
            return false;
        }
        return true; 
    }
    
    private void limpiarErrores() {
        labelErrorNombre.setText("");
        labelErrorApellidoPaterno.setText("");
        labelErrorApellidoMaterno.setText("");
        labelErrorCorreo.setText("");
        labelErrorContrasena.setText("");
        labelErrorConfirmar.setText("");
        labelErrorRol.setText("");
    }

    private boolean esVacio(TextField campo) {
        return campo.getText() == null || campo.getText().trim().isEmpty();
    }

    private void cerrarVentana(){
        Stage escenario = (Stage) textCorreo.getScene().getWindow();
        escenario.close();
    }
}