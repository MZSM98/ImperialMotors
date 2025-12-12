package com.imperial.controlador;

import com.imperial.dominio.VentaImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.modelo.pojo.Venta;
import com.imperial.utilidad.Constantes;
import com.imperial.utilidad.Sesion;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionVentasController implements Initializable {

    @FXML
    private TableView<Venta> tablaVentas;
    @FXML
    private TableColumn<Venta, String> columnFecha;
    @FXML
    private TableColumn<Venta, String> columnCliente;
    @FXML
    private TableColumn<Venta, Float> columnImporte;
    
    private ObservableList<Venta> listaVentas;
    private Usuario usuarioSesion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarVentas();
        usuarioSesion = Sesion.getUsuario();
    }    

    private void configurarTabla() {
        listaVentas = FXCollections.observableArrayList();
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente")); 
        columnImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        
        columnImporte.setCellFactory(tc -> new javafx.scene.control.TableCell<Venta, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("$ %.2f", item));
                }
            }
        });

        tablaVentas.setItems(listaVentas);
    }

    private void cargarVentas() {
        HashMap<String, Object> respuesta = VentaImpl.obtenerVentas();
        if (!(boolean) respuesta.get("error")) {
            ArrayList<Venta> ventasBD = (ArrayList<Venta>) respuesta.get("ventas");
            listaVentas.clear();
            listaVentas.addAll(ventasBD);
        } else {
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) tablaVentas.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicEnConsultar(ActionEvent event) {
        Venta ventaSeleccionada = tablaVentas.getSelectionModel().getSelectedItem();
        
        if (ventaSeleccionada != null) {
            try {
                FXMLLoader loader = Utilidades.obtenerVistaMemoria("vista/FXMLRegistroVenta.fxml");
                Parent root = loader.load();
                
                RegistroVentaController controller = loader.getController();
                controller.inicializarConsulta(ventaSeleccionada);
                
                Scene escena = new Scene(root);
                Stage escenario = new Stage();
                Sesion.registrarVentana(escenario);
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.setTitle("Detalle de Venta (Solo Lectura)");
                escenario.setScene(escena);
                escenario.showAndWait();
                
            } catch (IOException ex) {
                Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlerta("Selección requerida", "Selecciona una venta de la tabla para ver sus detalles.", Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void clicEnRegistrarNueva(ActionEvent event) {
        try {
            FXMLLoader loader = Utilidades.obtenerVistaMemoria("vista/FXMLRegistroVenta.fxml");
            Parent root = loader.load();
            
            Scene escena = new Scene(root);
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.setTitle("Registrar Nueva Venta");
            escenario.setScene(escena);
            escenario.showAndWait();
            
            cargarVentas();
            
        } catch (IOException ex) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
        }
    }
}   