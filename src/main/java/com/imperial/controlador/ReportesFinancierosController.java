package com.imperial.controlador;

import com.imperial.dominio.BitacoraImpl;
import com.imperial.dominio.ReporteImpl;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Constantes;
import com.imperial.utilidad.GeneracionPDF;
import com.imperial.utilidad.Sesion;
import com.imperial.utilidad.Utilidades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportesFinancierosController implements Initializable {

    @FXML private ComboBox<String> comboReporte;
    @FXML private VBox contenedorGrafica;
    
    private BarChart<String, Number> graficaBarras;
    private List<String> datosTexto;
    private Usuario usuarioSesion;
    @FXML
    private Button botonAuditoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioSesion = Sesion.getUsuario();
        comboReporte.getItems().addAll(
            "Ventas del Mes", 
            "Ventas por Semana", 
            "Ventas por Día", 
            "Inventario Actual", 
            "KPIs y Anomalías"
        );
        inicializarGrafica();
    }    

    private void inicializarGrafica() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        graficaBarras = new BarChart<>(xAxis, yAxis);
        graficaBarras.setAnimated(false);
        contenedorGrafica.getChildren().add(graficaBarras);
    }

    @FXML
    private void clicGenerar(ActionEvent event) {
        String seleccion = comboReporte.getValue();
        if (seleccion == null) return;

        graficaBarras.getData().clear();
        datosTexto = new ArrayList<>();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Datos");

        if ("Ventas del Mes".equals(seleccion)) {
            cargarVentas(serie, "MES");
        } else if ("Ventas por Semana".equals(seleccion)) {
            cargarVentas(serie, "SEMANA");
        } else if ("Ventas por Día".equals(seleccion)) {
            cargarVentas(serie, "DIA");
        } else if ("Inventario Actual".equals(seleccion)) {
            cargarInventario(serie);
        } else if ("KPIs y Anomalías".equals(seleccion)) {
            cargarKPI(serie);
        }
        graficaBarras.getData().add(serie);
        if (usuarioSesion != null) {
            BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), "Generó vista previa: " + seleccion);
        }
    }

    private void cargarVentas(XYChart.Series<String, Number> serie, String periodo) {
        HashMap<String, Object> resp = ReporteImpl.obtenerDatosVentas(periodo);
        if (!(boolean) resp.get("error")) {
            Map<String, Double> datos = (Map<String, Double>) resp.get("datos");
            for (Map.Entry<String, Double> entry : datos.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                datosTexto.add("Periodo: " + entry.getKey() + " | Total: $" + String.format("%.2f", entry.getValue()));
            }
        }
    }

    private void cargarInventario(XYChart.Series<String, Number> serie) {
        HashMap<String, Object> resp = ReporteImpl.obtenerDatosInventario();
        if (!(boolean) resp.get("error")) {
            Map<String, Integer> datos = (Map<String, Integer>) resp.get("datos");
            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                datosTexto.add("Modelo: " + entry.getKey() + " | Cantidad: " + entry.getValue());
            }
        }
    }

    private void cargarKPI(XYChart.Series<String, Number> serie) {
        HashMap<String, Object> resp = ReporteImpl.analizarAnomaliasYKPI();
        if (!(boolean) resp.get("error")) {
            double promedio = (double) resp.get("promedio");
            List<String> anomalias = (List<String>) resp.get("anomalias");
            
            serie.getData().add(new XYChart.Data<>("Promedio Venta", promedio));
            serie.getData().add(new XYChart.Data<>("Anomalías", anomalias.size()));
            
            datosTexto.add("KPI - Venta Promedio Global: $" + String.format("%.2f", promedio));
            datosTexto.add(" ");
            datosTexto.add("--- Listado de Anomalías Detectadas ---");
            
            if(anomalias.isEmpty()){
                datosTexto.add("No se detectaron ventas fuera del rango normal.");
            } else {
                datosTexto.addAll(anomalias);
            }
        }
    }

    @FXML
    private void clicPDF(ActionEvent event) {
        if (datosTexto == null || datosTexto.isEmpty()) {
            Utilidades.mostrarAlerta("Error", "Primero genere una vista previa", Alert.AlertType.WARNING);
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                GeneracionPDF.generarReporte(archivo, "Reporte: " + comboReporte.getValue(), datosTexto, graficaBarras);
                if (usuarioSesion != null) {
                    BitacoraImpl.registrar(usuarioSesion.getIdUsuario(), usuarioSesion.getNombre(), "Exportó PDF: " + comboReporte.getValue());
                }
                Utilidades.mostrarAlerta("Éxito", "Reporte guardado con portada y formato profesional.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                Utilidades.mostrarAlerta("Error", "No se pudo guardar el PDF", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        ((Stage) contenedorGrafica.getScene().getWindow()).close();
    }

    @FXML
    private void clicEnReportes(ActionEvent event) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLAuditoriaLogs.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);      
            Stage escenario = new Stage();
            Sesion.registrarVentana(escenario); 
            escenario.setScene(escena);
            escenario.setTitle("Auditoria y LOGS");
            escenario.initModality(Modality.APPLICATION_MODAL);
            
            Sesion.registrarVentana(escenario); 
            escenario.showAndWait();
            
        } catch (IOException ioe) {
            Utilidades.mostrarAlerta("Error", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.ERROR);
            ioe.printStackTrace();
        }
    }
}