package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.modelo.pojo.Bitacora;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Utilidades;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AuditoriaLogsController implements Initializable {

    @FXML
    private TableView<Bitacora> tablaBitacora; 
    @FXML
    private TableColumn<Bitacora, String> columnFecha;
    @FXML
    private TableColumn<Bitacora, String> columnUsuario;
    @FXML
    private TableColumn<Bitacora, String> columnTipoMovimiento;
   
    private ObservableList<Bitacora> listaLogs;
    private Usuario usuarioSesion;

    public void setUsuario(Usuario usuario){
        this.usuarioSesion = usuario;
        if(usuario != null){
            BitacoraImpl.registrar(usuario.getIdUsuario(), usuario.getNombre(), "Consulta de Reportes/Bitácora");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla();
    }    

    @FXML
    private void clicEnExportar(ActionEvent event) {
        if(usuarioSesion != null){
            BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), "Exportación de Bitácora a CSV");
        }
        exportarReporteCSV();
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage escenario = (Stage) tablaBitacora.getScene().getWindow();
        escenario.close();
    }
    
    private void configurarTabla(){
        listaLogs = FXCollections.observableArrayList();
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("usuarioNombre"));
        columnTipoMovimiento.setCellValueFactory(new PropertyValueFactory<>("accion"));
        tablaBitacora.setItems(listaLogs);
    }
    
    private void cargarDatosTabla(){
        HashMap<String, Object> respuesta = BitacoraImpl.obtenerBitacora();
        if(!(boolean) respuesta.get("error")){
            ArrayList<Bitacora> logsBD = (ArrayList<Bitacora>) respuesta.get("logs");
            listaLogs.clear();
            listaLogs.addAll(logsBD);
        } else {
            Utilidades.mostrarAlerta("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private void exportarReporteCSV(){
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Guardar Reporte de Actividades");
        dialogoSeleccion.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        Stage escenario = (Stage) tablaBitacora.getScene().getWindow();
        File archivo = dialogoSeleccion.showSaveDialog(escenario);
        if(archivo != null){
            guardarArchivo(archivo);
        }
    }
    
    private void guardarArchivo(File archivo){
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write("Fecha,Usuario,Accion");
            escritor.newLine();
            for (Bitacora log : listaLogs) {
                String linea = String.format("%s,%s,%s", log.getFecha(), log.getUsuarioNombre().replace(",", " "), log.getAccion().replace(",", " "));
                escritor.write(linea);
                escritor.newLine();
            }
            Utilidades.mostrarAlerta("Éxito", "Reporte exportado correctamente", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            Utilidades.mostrarAlerta("Error", "No se pudo guardar el archivo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}