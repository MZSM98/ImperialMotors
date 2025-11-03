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

    @FXML
    private void abrirFormularioRegistroUsuarios(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioUsuario.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Registrar Usuario");
            escenario.initModality(Modality.APPLICATION_MODAL); 
            escenario.showAndWait(); 
            
            llenarTablaUsuarios();
            
        }catch (IOException ioe){
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }
    
    //TODO: Implementar lógica de editar
    @FXML
    private void abrirFormularioEditarUsuarios(ActionEvent event) {
    }
    
    //TODO: Implementar lógica de eliminar
    @FXML
    private void eliminarUsuario(ActionEvent event) {
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
}