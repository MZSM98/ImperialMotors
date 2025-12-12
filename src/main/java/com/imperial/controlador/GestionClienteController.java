/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.imperial.controlador;

import com.imperial.dominio.ClienteImpl;
import com.imperial.modelo.pojo.Cliente;
import com.imperial.utilidad.InterfazSeleccion;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hi-ya
 */
public class GestionClienteController implements Initializable {

    @FXML
    private TextField textBuscar;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaApPaterno;
    @FXML
    private TableColumn columnaApMaterno;
    @FXML
    private TableColumn columnaTelefono;
    @FXML
    private TableColumn columnaCorreo;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonEditar;
    
    ObservableList<Cliente> clientes;
    private InterfazSeleccion<Cliente> observador;
    private boolean modoSeleccion = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        llenarTabla();
        tablaClientes.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && modoSeleccion){
                seleccionarCliente();
            }
        });
        configurarBusqueda();
    }    

    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
        if (cliente!= null){
            abrirFormulario(cliente);
        }else{
            Utilidades.mostrarAlerta("Alerta", "Debe seleccionar un Cliente para poder editarlo", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        ((Stage)tablaClientes.getScene().getWindow()).close();
    }
    
    private void configurarTabla(){
        clientes = FXCollections.observableArrayList();
        columnaNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        columnaApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        columnaApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        tablaClientes.setItems(clientes);
               
    }
    private void llenarTabla(){
        HashMap<String, Object> respuesta = ClienteImpl.obtenerClientes();
        
        if(!(boolean) respuesta.get("error")){
            ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) respuesta.get("clientes");
            clientes.clear();
            clientes.addAll(listaClientes);
        }else{
            Utilidades.mostrarAlerta("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void abrirFormulario(Cliente cliente){
        
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioCliente.fxml");
            Parent vista = cargador.load();
            FormularioClienteController controlador = cargador.getController();
            controlador.inicializarDatos(cliente);
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Registrar Cliente");
            escenario.initModality(Modality.APPLICATION_MODAL); 
            escenario.showAndWait(); 
            
            llenarTabla();
            
        }catch (IOException ioe){
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }
    
    public void iniciarModoSeleccion(InterfazSeleccion<Cliente> observador){
        this.observador = observador;
        this.modoSeleccion = true;
        
        if(botonRegistrar != null) botonRegistrar.setVisible(false);
        if(botonEditar != null) botonEditar.setVisible(false);
        
    }
    
    private void seleccionarCliente(){
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            observador.notificarSeleccion(cliente); 
            cerrarVentana(null);
        } else {
            Utilidades.mostrarAlerta("Aviso", "Selecciona un cliente", Alert.AlertType.WARNING);
        }
    }
    
    private void configurarBusqueda() {
        if (clientes.size() > 0) {
            FilteredList<Cliente> filtrado = new FilteredList<>(clientes, p -> true);
            
            textBuscar.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtrado.setPredicate(cliente -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lower = newValue.toLowerCase();
                        
                        if (cliente.getNombre().toLowerCase().contains(lower)) {
                            return true;
                        }
                        if (cliente.getApellidoPaterno().toLowerCase().contains(lower)) {
                            return true;
                        }
                        if (cliente.getApellidoMaterno() != null && cliente.getApellidoMaterno().toLowerCase().contains(lower)) {
                            return true;
                        }
                        return false;
                    });
                }
            });
            
            SortedList<Cliente> ordenados = new SortedList<>(filtrado);
            ordenados.comparatorProperty().bind(tablaClientes.comparatorProperty());
            tablaClientes.setItems(ordenados);
        }
    }
}
