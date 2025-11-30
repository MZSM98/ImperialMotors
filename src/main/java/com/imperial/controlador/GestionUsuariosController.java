package com.imperial.controlador;

import com.imperial.dominio.UsuarioImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionUsuariosController implements Initializable {

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> columnaApellidoPaterno;
    @FXML
    private TableColumn<Usuario, String> columnaApellidoMaterno;
    @FXML
    private TableColumn<Usuario, String> columnaNombre;
    @FXML
    private TableColumn<Usuario, String> columnaCorreo;
    @FXML
    private TableColumn<Usuario, String> columnaRol;
    @FXML
    private TextField textBuscar;

    private ObservableList<Usuario> usuarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaUsuarios();
        llenarTablaUsuarios();
    }    

    @FXML
    private void regresarVentanaAnterior(ActionEvent event) {
        Stage escenario = (Stage) tablaUsuarios.getScene().getWindow();
        escenario.close();
    }
    
    private void configurarTablaUsuarios(){
        usuarios = FXCollections.observableArrayList();
        columnaApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        columnaApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        tablaUsuarios.setItems(usuarios);
    }
    
    private void llenarTablaUsuarios(){
        HashMap<String, Object> respuesta = UsuarioImpl.obtenerUsuarios();
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            ArrayList<Usuario> listaUsuarios = (ArrayList<Usuario>) respuesta.get("usuarios");
            usuarios.clear();
            usuarios.addAll(listaUsuarios);
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            abrirFormulario(usuario);
        } else {
            Utilidades.mostrarAlerta("Selección requerida", "Debe seleccionar un usuario para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void cambiarEstado(ActionEvent event) {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            // Alternar estado (asumiendo que manejas "Activo" e "Inactivo")
            String nuevoEstado = "Activo".equals(usuario.getEstado()) ? "Inactivo" : "Activo";
            usuario.setEstado(nuevoEstado);
            
            // Reutilizamos editarUsuario para guardar el cambio de estado
            HashMap<String, Object> respuesta = UsuarioImpl.editarUsuario(usuario);
            
            if (!(boolean) respuesta.get("error")) {
                Utilidades.mostrarAlerta("Éxito", "El estado del usuario se actualizó correctamente", Alert.AlertType.INFORMATION);
                llenarTablaUsuarios();
            } else {
                Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlerta("Selección requerida", "Selecciona un usuario para cambiar su estado", Alert.AlertType.WARNING);
        }
    }

    private void abrirFormulario(Usuario usuarioEdicion) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioUsuario.fxml");
            Parent vista = cargador.load();

            if (usuarioEdicion != null) {
                FormularioUsuarioController ctrl = cargador.getController();
                ctrl.inicializarDatos(usuarioEdicion); // Asegúrate de tener este método en tu formulario
            }

            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle(usuarioEdicion == null ? "Registrar Usuario" : "Editar Usuario");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

            llenarTablaUsuarios();
            
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }
}