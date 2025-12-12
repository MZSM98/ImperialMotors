package com.imperial.controlador;

import com.imperial.utilidad.GeneracionPDF;
import com.imperial.dominio.BitacoraImpl;
import com.imperial.modelo.pojo.Usuario;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.stage.FileChooser;
import com.imperial.dominio.VehiculoImpl;
import com.imperial.modelo.pojo.Vehiculo;
import com.imperial.utilidad.InterfazSeleccion;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
 * @author User
 */
public class InventarioVehiculoController implements Initializable {

    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn columnVIN;
    @FXML
    private TableColumn columnMarca;
    @FXML
    private TableColumn columnModelo;
    @FXML
    private TableColumn columnAnio;
    @FXML
    private TableColumn columnPrecio;
    @FXML
    private TableColumn columnTipo;
    @FXML
    private TextField textBuscar;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonEditar;
    @FXML
    private Button botonEliminar;
    private ObservableList<Vehiculo> vehiculos;
    private InterfazSeleccion<Vehiculo> observador;
    private boolean modoSeleccion = false;
    @FXML
    private Button botonExportar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        llenarTablaVehiculos();
        
        tablaVehiculos.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && modoSeleccion){
                seleccionarVehiculo();
            }
        });
        configurarBusqueda();
    }    

    @FXML
    private void clicEnRegistrar(ActionEvent event) {
        abrirFormulario(null); 
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage)textBuscar.getScene().getWindow();
        stage.close();
    }
    
    private void configurarTabla(){
        vehiculos = FXCollections.observableArrayList();
        columnVIN.setCellValueFactory(new PropertyValueFactory("VIN"));
        columnMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        columnModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        columnAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        columnPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        columnTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        tablaVehiculos.setItems(vehiculos);
    }
    
    private void llenarTablaVehiculos(){
        HashMap<String, Object> respuesta = VehiculoImpl.obtenerVehiculos();
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            ArrayList<Vehiculo> listaVehiculos = (ArrayList<Vehiculo>) respuesta.get("vehiculos");
            vehiculos.clear();
            vehiculos.addAll(listaVehiculos);
        }else{
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void abrirFormulario(Vehiculo vehiculoEdicion) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioVehiculo.fxml");
            Parent vista = cargador.load();
            
            if (vehiculoEdicion != null) {
                FormularioVehiculoController ctrl = cargador.getController();
                ctrl.inicializarDatos(vehiculoEdicion); 
            }
            
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle(vehiculoEdicion == null ? "Registrar Vehículo" : "Editar Vehículo");
            escenario.initModality(Modality.APPLICATION_MODAL); 
            escenario.showAndWait(); 
            
            llenarTablaVehiculos(); 
            
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", "No se pudo cargar la vista", Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }

    private void seleccionarVehiculo(){
        Vehiculo auto = tablaVehiculos.getSelectionModel().getSelectedItem();
        if(auto != null){
            observador.notificarSeleccion(auto);
            cerrarVentana(null);
        }
    }

    public void iniciarModoSeleccion(InterfazSeleccion<Vehiculo> observador){
        this.observador = observador;
        this.modoSeleccion = true;
        
        if(botonRegistrar != null) botonRegistrar.setVisible(false);
        if(botonEditar != null) botonEditar.setVisible(false);
        if(botonEliminar != null) botonEliminar.setVisible(false);
        if(botonExportar != null) botonExportar.setVisible(false);
    }
    
    @FXML
    private void clicEnEditar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado); 
        } else {
            Utilidades.mostrarAlerta("Selección requerida", "Selecciona un vehículo para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEnEliminar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Baja");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Desea quitar el automovil con el VIN: " 
                    + seleccionado.getVIN() + "?\nEsta acción no se puede revertir");
            
            if (confirmacion.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                HashMap<String, Object> respuesta = VehiculoImpl.eliminarVehiculo(seleccionado.getVIN());
                
                if (!(boolean) respuesta.get("error")) {
                    Utilidades.mostrarAlerta("Éxito", (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                    llenarTablaVehiculos(); 
                } else {
                    Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlerta("Selección requerida", "Selecciona un vehículo para dar de baja", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicExportar(ActionEvent event) {
        
        if (vehiculos == null || vehiculos.isEmpty()) {
            Utilidades.mostrarAlerta("Sin datos", "No hay vehículos para exportar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Inventario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        
        Stage stageActual = (Stage) tablaVehiculos.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stageActual);

        if (archivo != null) {
            try {
                List<String> encabezados = Arrays.asList("VIN", "Marca", "Modelo", "Año", "Tipo", "Precio", "Estado");

                List<List<String>> datosTabla = new ArrayList<>();

                for (Object obj : vehiculos) {
                    Vehiculo v = (Vehiculo) obj;
                    List<String> fila = new ArrayList<>();
                    
                    fila.add(v.getVIN() != null ? v.getVIN() : "");
                    fila.add(v.getMarca() != null ? v.getMarca() : "");
                    fila.add(v.getModelo() != null ? v.getModelo() : "");
                    fila.add(v.getAnio() != null ? v.getAnio() : "");
                    fila.add(v.getTipo() != null ? v.getTipo() : "");
                    fila.add(String.format("$%,.2f", v.getPrecio())); // Formato moneda
                    fila.add(v.getEstado() != null ? v.getEstado() : "Disponible");
                    
                    datosTabla.add(fila);
                }

                GeneracionPDF.generarReporteTabla(archivo, "Inventario General", encabezados, datosTabla);

                Usuario usuarioSesion = Sesion.getUsuario();
                if (usuarioSesion != null) {
                    BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), 
                                         usuarioSesion.getNombre(), 
                                         "Exportó PDF (Tabla): Inventario Vehicular");
                }

                Utilidades.mostrarAlerta("Éxito", "Reporte generado correctamente.", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                Utilidades.mostrarAlerta("Error", "Error al generar PDF: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }
    
    public void setVehiculosExcluidos(List<Vehiculo> listaExcluidos) {
        if (listaExcluidos != null && !listaExcluidos.isEmpty()) {
            List<String> vinesOcultos = listaExcluidos.stream()
                                            .map(Vehiculo::getVIN)
                                            .collect(Collectors.toList());

            vehiculos.removeIf(obj -> vinesOcultos.contains(((Vehiculo) obj).getVIN()));
        }
    }
    
    private void configurarBusqueda() {
        if (vehiculos.size() > 0) {
            FilteredList<Vehiculo> filtrado = new FilteredList<>(vehiculos, p -> true);
            
            textBuscar.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtrado.setPredicate(vehiculo -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lower = newValue.toLowerCase();
                        
                        if (vehiculo.getMarca().toLowerCase().contains(lower)) {
                            return true;
                        }
                        if (vehiculo.getModelo().toLowerCase().contains(lower)) {
                            return true;
                        }
                        if (vehiculo.getVIN().toLowerCase().contains(lower)) {
                            return true;
                        }
                        return false;
                    });
                }
            });
            
            SortedList<Vehiculo> ordenados = new SortedList<>(filtrado);
            ordenados.comparatorProperty().bind(tablaVehiculos.comparatorProperty());
            tablaVehiculos.setItems(ordenados);
        }
    }
}
