package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Constantes;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrincipalAdminController implements Initializable {
    
    @FXML
    private Button botonCerrarSesion;
    private static final String ERROR = "Error";

    @FXML
    private Label labelNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarUsuario();
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        ((Stage) botonCerrarSesion.getScene().getWindow()).close();
    }

    private void configurarUsuario(){
        Usuario usuario = Sesion.getUsuario();
        if (usuario != null) {
            labelNombre.setText(usuario.getNombreCompleto());
        }
    }

    @FXML
    private void abrirGestionClientes(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionCliente.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Clientes");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            Utilidades.mostrarAlerta(ERROR, Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirGestionVehiculos(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLInventarioVehiculo.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Inventario");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            Utilidades.mostrarAlerta(ERROR, Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirGestionProveedores(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionProveedor.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Gestion de Proveedores");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            Utilidades.mostrarAlerta(ERROR, Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirGestionVentas(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionVentas.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Historial de Ventas");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta(ERROR, Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirGestionUsuarios(ActionEvent event) {
        try{
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionUsuarios.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            
            Sesion.registrarVentana(escenario); 
            
            escenario.setScene(escena);
            escenario.setTitle("Gestión de Usuarios");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ioe){
            Utilidades.mostrarAlerta(ERROR, Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicEnReportes(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLReportesFinancieros.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Reportes y KPIs");
            escenario.initModality(Modality.APPLICATION_MODAL);
            
            Sesion.registrarVentana(escenario); 
            escenario.showAndWait();
            
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta(ERROR, Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }
}
