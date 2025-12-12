package com.imperial.controlador;

import com.imperial.dominio.AutenticacionImpl;
import com.imperial.dominio.BitacoraImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.RestriccionCampos;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

public class InicioSesionController implements Initializable {
    
    @FXML
    private TextField textCorreo;
    @FXML
    private TextField textContrasena;
    @FXML
    private Label labelErrorContrasena;
    @FXML
    private Label labelErrorCorreo;
    private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";

    private static final Map<String, Integer> intentosFallidos = new HashMap<>();
    private static final Map<String, Long> tiempoBloqueo = new HashMap<>();
    
    public static final String ERROR_INICIO_SESION = "!Ups¡ algo salió mal, "
            + " no tenemos servicio por el momento, intenta más tarde";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aplicarRestricciones();
    }    

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String correo = textCorreo.getText();
        String contrasena = textContrasena.getText();
        
        if (sonDatosValidos(correo, contrasena)){
            if (verificarBloqueo(correo)) {
                Utilidades.mostrarAlerta("Bloqueo Temporal", "Ha"
                                             + " excedido el número de intentos."
                                             + " Espere 1 minuto.", Alert.AlertType.WARNING);
                return;
            }
            validarSesion(correo, contrasena);
        }
    }
    
    private boolean verificarBloqueo(String correo) {
        if (tiempoBloqueo.containsKey(correo)) {
            if (System.currentTimeMillis() < tiempoBloqueo.get(correo)) {
                return true;
            } else {
                tiempoBloqueo.remove(correo);
                intentosFallidos.remove(correo);
            }
        }
        return false;
    }

    private void registrarFallo(String correo) {
        int intentos = intentosFallidos.getOrDefault(correo, 0) + 1;
        intentosFallidos.put(correo, intentos);
        
        if (intentos >= 3) {
            tiempoBloqueo.put(correo, System.currentTimeMillis() + 60000);
            BitacoraImpl.registrar(0, correo, "Bloqueo de cuenta por exceso de intentos");
        }
    }

    private boolean sonDatosValidos(String correo, String contrasena){
        boolean correctos = true;
        labelErrorCorreo.setText("");
        labelErrorContrasena.setText("");
        
        if (correo == null || correo.isEmpty()){
            correctos = false;
            labelErrorCorreo.setText(CAMPO_OBLIGATORIO);
        }
        
        if (contrasena == null || contrasena.isEmpty()){
            correctos = false;
            labelErrorContrasena.setText(CAMPO_OBLIGATORIO);
        }
        
        return correctos;
    }
    
    private void validarSesion(String correo, String contrasena){
        
        HashMap<String, Object> respuesta = AutenticacionImpl.verificarCredencialesUsuario(correo, contrasena);
        boolean error = (boolean) respuesta.get("error");
        
        if (!error){
            Usuario usuarioSesion = (Usuario)respuesta.get("Usuario");
            intentosFallidos.remove(correo);
            tiempoBloqueo.remove(correo);
            
            BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), "Inicio de sesión exitoso");
            
            Utilidades.mostrarAlerta("Bienvenido", "Bienvenido(a) " + usuarioSesion.getNombre(), Alert.AlertType.INFORMATION);
            irPantallaPrincipal(usuarioSesion);
        } else {
            registrarFallo(correo);
            BitacoraImpl.registrar(0, correo, "Intento de inicio de sesión fallido");
            Utilidades.mostrarAlerta("Error", "Credenciales incorrectas", Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal(Usuario usuario) {
        try {
            FXMLLoader cargador;
            String titulo;

            if ("Administrador".equals(usuario.getRol())) {
                cargador = Utilidades.obtenerVistaMemoria("vista/FXMLPrincipalAdmin.fxml");
                titulo = "Menú Principal - Administrador";
            } else {
                cargador = Utilidades.obtenerVistaMemoria("vista/FXMLPrincipalVendedor.fxml");
                titulo = "Menú Principal - Vendedor";
            }

            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            escena.addEventFilter(javafx.scene.input.InputEvent.ANY, event -> {
                Sesion.renovarTemporizador();
            });

            Stage escenario = (Stage) textCorreo.getScene().getWindow();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle(titulo);
            escenario.show();

            Sesion.iniciar(usuario, escenario);

        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", ERROR_INICIO_SESION, Alert.AlertType.ERROR);
        }
    }
    
    private void aplicarRestricciones (){
        RestriccionCampos.limitarLongitud(textContrasena);
        RestriccionCampos.limitarLongitud(textContrasena);
    }
}