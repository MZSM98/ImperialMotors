
package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.dominio.ClienteImpl;
import com.imperial.modelo.pojo.Cliente;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hi-ya
 */
public class FormularioClienteController implements Initializable {

    @FXML
    private TextField textNombre;
    @FXML
    private TextField textApPaterno;
    @FXML
    private TextField textApMaterno;
    @FXML
    private TextField textCorreo;
    @FXML
    private TextField textTelefono;
    @FXML
    private Label labelErrorNombre;
    @FXML
    private Label labelErrorCorreo;
    @FXML
    private Label labelErrorApPaterno;
    @FXML
    private Label labelErrorTelefono;
    @FXML
    private Label labelErrorApMaterno;
    
    Cliente clienteEdicion;
    
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
        if (validarCampos()){
            if(clienteEdicion == null){
                registrarCliente();
            }else{
                editarCliente();
            }
           
        }
    }
    
            
    private boolean validarCampos(){
        boolean esValido = true;
        
        labelErrorNombre.setText("");
        labelErrorCorreo.setText("");
        labelErrorApMaterno.setText("");
        labelErrorApPaterno.setText("");
        labelErrorTelefono.setText("");
        
        if(textNombre.getText() == null || textNombre.getText().isEmpty()){
            esValido = false;
            labelErrorNombre.setText("Campo obligatorio");
        }
        if(textApPaterno.getText() == null || textApPaterno.getText().isEmpty()){
            esValido = false;
            labelErrorApPaterno.setText("Campo obligatorio");
        }
        if(textApMaterno.getText() == null || textApMaterno.getText().isEmpty()){
            esValido = false;
            labelErrorApMaterno.setText("Campo obligatorio");
        }
        if(textCorreo.getText() == null || textCorreo.getText().isEmpty()){
            esValido = false;
            labelErrorCorreo.setText("Campo obligatorio");
        }
        if(textTelefono.getText() == null || textTelefono.getText().isEmpty()){
            esValido = false;
            labelErrorTelefono.setText("Campo obligatorio");
        }
        
        return esValido;
    }
    
    private void registrarCliente(Cliente cliente){
        HashMap<String, Object> respuesta = ClienteImpl.registrarCliente(cliente);

        if(!(boolean) respuesta.get("error")){
            if(usuarioSesion != null){
                 BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), "Registro de nuevo cliente: " + cliente.getNombreCompleto());
            }
            Utilidades.mostrarAlerta("Registro Exitoso", (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
            cerrarVentana(); 
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void registrarCliente(){
        Cliente clienteNuevo = obtenerCliente();
        
        HashMap<String, Object> resultado = ClienteImpl.registrarCliente(clienteNuevo);
        if(!(boolean)resultado.get(("error") )){
            Utilidades.mostrarAlerta("Profesor regitrado correctamente", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlerta("Error al registrar", resultado.get("error").toString(), Alert.AlertType.ERROR);
        }
    }
    private void editarCliente(){
        Cliente clienteEdicion = obtenerCliente();
        clienteEdicion.setIdCliente(this.clienteEdicion.getIdCliente());
        HashMap<String, Object> resultado = ClienteImpl.editarCliente(clienteEdicion);
        if(!(boolean)resultado.get("error")){
            if(usuarioSesion != null){
                 BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), 
                                      usuarioSesion.getNombre(), 
                                      "Edición de cliente: " + clienteEdicion.getNombreCompleto());
            }
            Utilidades.mostrarAlerta("Cliente actualizado correctamente", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlerta("Error al editar", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) textCorreo.getScene().getWindow();
        escenario.close();
    }
    
    public void inicializarDatos(Cliente cliente){
        clienteEdicion = cliente;
        
        if (cliente != null){
            textNombre.setText(cliente.getNombre());
            textApPaterno.setText(cliente.getApellidoPaterno());
            textApMaterno.setText(cliente.getApellidoMaterno());
            textCorreo.setText(cliente.getCorreo());
            textTelefono.setText(cliente.getTelefono());
            
        }
    }
    
    private Cliente obtenerCliente(){
        Cliente cliente = new Cliente();
        cliente.setNombre(textNombre.getText());
        cliente.setApellidoPaterno(textApPaterno.getText());
        cliente.setApellidoMaterno(textApMaterno.getText());
        cliente.setCorreo(textCorreo.getText());
        cliente.setTelefono(textTelefono.getText());
        return cliente;
    }
    
}
