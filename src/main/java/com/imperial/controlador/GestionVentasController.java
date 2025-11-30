package com.imperial.controlador;

import com.imperial.dominio.VentaImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.modelo.pojo.Venta;
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
        //TODO
    }
    
    @FXML
    private void clicEnRegistrarNueva(ActionEvent event) {
        try {
            FXMLLoader loader = Utilidades.obtenerVistaMemoria("vista/FXMLRegistroVenta.fxml");
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Registrar Nueva Venta");
            stage.setScene(scene);
            stage.showAndWait();
            
            cargarVentas();
            
        } catch (IOException ex) {
            Utilidades.mostrarAlerta("Error", "No se pudo abrir la ventana de registro", Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }
}