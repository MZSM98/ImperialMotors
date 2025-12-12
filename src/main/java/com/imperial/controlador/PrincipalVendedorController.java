package com.imperial.controlador;

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
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrincipalVendedorController implements Initializable {

    private Usuario usuarioSesion;
    
    @FXML
    private Label labelNombre;
    
    public void setUsuario(Usuario usuario){
        this.usuarioSesion = usuario;
        actualizarEtiqueta();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioSesion = Sesion.getUsuario();
        actualizarEtiqueta();
    }    

    private void actualizarEtiqueta() {
        if (usuarioSesion != null) {
            labelNombre.setText("Bienvenido: " + usuarioSesion.getNombreCompleto());
        }
    }

    @FXML
    private void clicVentas(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionVentas.fxml");
            Parent vista = cargador.load();
            
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Mis Ventas");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicClientes(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLGestionCliente.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Gestión de Clientes");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicVehiculos(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLInventarioVehiculo.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Inventario de Vehículos");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicReportes(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLReportesFinancieros.fxml");
            Parent vista = cargador.load();
            
            ReportesFinancierosController controller = cargador.getController();
            controller.configurarVistaVendedor(); 
            
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Mis Reportes");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }
}