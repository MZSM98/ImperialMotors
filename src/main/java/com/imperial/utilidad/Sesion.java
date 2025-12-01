package com.imperial.utilidad;

import com.imperial.modelo.pojo.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.Alert;

public class Sesion {
    
    private Sesion(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }
    
    private static Usuario usuarioActual;
    private static Timer timer;
    private static Stage stageActual; 
    private static final List<Stage> ventanasAbiertas = new ArrayList<>();

    public static void iniciar(Usuario usuario, Stage stage) {
        usuarioActual = usuario;
        stageActual = stage;
        iniciarTemporizador();
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }
    
    public static void setStageActual(Stage stage){
        stageActual = stage;
    }
    
    public static void registrarVentana(Stage stage) {
        ventanasAbiertas.add(stage);
        stage.setOnHidden(e -> ventanasAbiertas.remove(stage));
    }

    public static void renovarTemporizador() {
        if (timer != null) {
            timer.cancel();
        }
        iniciarTemporizador();
    }

    private static void iniciarTemporizador() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cerrarSesionPorInactividad();
            }
        }, 10 * 60 * 1000); 
    }

    private static void cerrarSesionPorInactividad() {
        Platform.runLater(() -> {
            try {
                List<Stage> copiaVentanas = new ArrayList<>(ventanasAbiertas);
                for (Stage s : copiaVentanas) {
                    if (s.isShowing()) {
                        s.close();
                    }
                }
                ventanasAbiertas.clear();

                if (stageActual != null) {
                    Utilidades.mostrarAlerta("Sesión Expirada", "Su sesión se cerró por 10 minutos de inactividad.", Alert.AlertType.INFORMATION);
                    
                    FXMLLoader loader = new FXMLLoader(Sesion.class.getResource("/com/imperial/vista/FXMLInicioSesion.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stageActual.setScene(scene);
                    stageActual.setTitle("Iniciar Sesión");
                    stageActual.centerOnScreen();
                    stageActual.show();
                    
                    usuarioActual = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public static void cerrarSesionManual() {
        if (timer != null) timer.cancel();
        usuarioActual = null;
        ventanasAbiertas.clear();
    }
}