package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.dominio.VentaImpl;
import com.imperial.modelo.pojo.Cliente;
import com.imperial.modelo.pojo.DetalleVenta;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.modelo.pojo.Vehiculo;
import com.imperial.modelo.pojo.Venta;
import com.imperial.utilidad.Constantes;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class RegistroVentaController implements Initializable {

    @FXML
    private TextField textVendedor;
    @FXML
    private TextField textCliente;
    @FXML
    private TextField textImporte;
    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn<Vehiculo, String> columnAuto;
    @FXML
    private TableColumn<Vehiculo, String> columnTipo;
    @FXML
    private TableColumn<Vehiculo, Double> columnImporte;
    
    private ObservableList<Vehiculo> carritoCompras;
    private Cliente clienteSeleccionado;
    private double totalVenta = 0.0;
    Usuario usuarioSesion;
    @FXML
    private Button btnElegirCliente;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnAnadirAuto;
    @FXML
    private Button btnEliminarAuto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarCamposSoloLectura();
        usuarioSesion = Sesion.getUsuario();
        if (usuarioSesion != null) {
            textVendedor.setText(usuarioSesion.getNombre() + " " + usuarioSesion.getApellidoPaterno());
        }
    }
    
    
    private void configurarCamposSoloLectura(){
        textVendedor.setEditable(false);
        textCliente.setEditable(false);
        textImporte.setEditable(false);
    }

    private void configurarTabla(){
        carritoCompras = FXCollections.observableArrayList();
        
        columnAuto.setCellValueFactory(cellData -> {
            Vehiculo v = cellData.getValue();
            return new SimpleStringProperty(v.getMarca() + " " + v.getModelo() + " (" + v.getAnio() + ")");
        });
        
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnImporte.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        tablaVehiculos.setItems(carritoCompras);
    }

    @FXML
    private void clicElegirCliente(ActionEvent event) {
        
        try {
            FXMLLoader loader = Utilidades.obtenerVistaMemoria("vista/FXMLGestionCliente.fxml");
            Parent root = loader.load();
            GestionClienteController controller = loader.getController();
            controller.iniciarModoSeleccion((cliente) -> {
                this.clienteSeleccionado = cliente;
                textCliente.setText(cliente.getNombreCompleto());
            });
            
            abrirModal(root, "Seleccionar Cliente");
        } catch (IOException ex) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicEnAnadir(ActionEvent event) {
        try {
            FXMLLoader loader = Utilidades.obtenerVistaMemoria("vista/FXMLInventarioVehiculo.fxml");
            Parent root = loader.load();
            InventarioVehiculoController controller = loader.getController();
            controller.setVehiculosExcluidos(new ArrayList<>(carritoCompras));
            controller.iniciarModoSeleccion((vehiculo) -> {
                carritoCompras.add(vehiculo);
                calcularTotal();
            });
            abrirModal(root, "Seleccionar Vehículo");
        } catch (IOException ex) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
            
        }
    }

    @FXML
    private void clicEnEliminar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if(seleccionado != null){
            carritoCompras.remove(seleccionado);
            calcularTotal();
        } else {
            Utilidades.mostrarAlerta("Aviso", "Selecciona un vehículo de la tabla para eliminarlo", Alert.AlertType.WARNING);
        }
    }
    
    private void calcularTotal(){
        totalVenta = 0.0;
        for(Vehiculo v : carritoCompras){
            totalVenta += v.getPrecio();
        }
        textImporte.setText(String.format("$ %.2f", totalVenta));
    }

    @FXML
    private void clicEnRegistrar(ActionEvent event) {
        boolean confirmacion = Utilidades.mostrarAlertaConfirmacion("Confirmar Venta",
                "¿Está seguro de registrar la venta?"
                , "Está a punto de registrar una venta,"
                        + " verifique que los datos sean correctos antes de continuar, la información no se puede modificar");
        
        if(!confirmacion){
            return;
        }
        
        if(validarVenta()){
            Venta venta = new Venta();
            venta.setIdUsuario(usuarioSesion.getIdUsuario());
            venta.setIdCliente(clienteSeleccionado.getIdCliente());
            venta.setImporte((float) totalVenta);
            
            ArrayList<DetalleVenta> detalles = new ArrayList<>();
            for(Vehiculo v : carritoCompras){
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVIN(v.getVIN());
                detalle.setPrecioUnitario((float) v.getPrecio());
                detalle.setCantidad(1);
                detalles.add(detalle);
            }
            
            HashMap<String, Object> respuesta = VentaImpl.registrarVenta(venta, detalles);
            
            if(!(boolean) respuesta.get("error")){
                String folio = (String) respuesta.get("mensaje");
                Utilidades.mostrarAlerta("Venta Exitosa", "Venta registrada con folio: " + folio, Alert.AlertType.INFORMATION);
                
                BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), 
                                       usuarioSesion.getNombre(), 
                                       "Venta Registrada - Folio: " + folio + " - Total: " + totalVenta);
                
                cerrar();
            } else {
                Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
            }
        }
    }
    
    private boolean validarVenta(){
        if(usuarioSesion == null){
            Utilidades.mostrarAlerta("Error", "Sesión no válida", Alert.AlertType.ERROR);
            return false;
        }
        if(clienteSeleccionado == null){
            Utilidades.mostrarAlerta("Campos incompletos", "Debe seleccionar un cliente", Alert.AlertType.WARNING);
            return false;
        }
        if(carritoCompras.isEmpty()){
            Utilidades.mostrarAlerta("Campos incompletos", "Debe agregar al menos un vehículo", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private void abrirModal(Parent root, String titulo){
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        boolean confirmacion = Utilidades.mostrarAlertaConfirmacion("Confirmar cancelación de venta",
                "¿Está seguro de cancelar la venta?", "Los cambios no se guardarán");
        
        if (confirmacion){
            cerrar();
        }
    }
    
    private void cerrar(){
        Stage stage = (Stage) textVendedor.getScene().getWindow();
        stage.close();
    }
    private void clicCambiarCliente(ActionEvent event) {
        clicElegirCliente(event);
    }
    
    public void inicializarConsulta(Venta venta) {
        textCliente.setText(venta.getNombreCliente());
        textImporte.setText(String.format("$ %.2f", venta.getImporte()));
        
        ArrayList<Vehiculo> listaAutos = VentaImpl.obtenerVehiculosVenta(venta.getIdVenta());
        carritoCompras.setAll(listaAutos);
        
        textCliente.setDisable(true);
        
        if(btnElegirCliente != null) btnElegirCliente.setVisible(false);
        if(btnAnadirAuto != null) btnAnadirAuto.setVisible(false);
        if(btnEliminarAuto != null) btnEliminarAuto.setVisible(false);
        if(btnRegistrar != null) btnRegistrar.setVisible(false);
    }
}