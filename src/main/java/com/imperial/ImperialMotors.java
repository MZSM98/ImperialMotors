
package com.imperial;

import com.imperial.utilidad.Constantes;
import com.imperial.utilidad.Utilidades;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ImperialMotors extends Application {
    
    
    
    @Override
    public void start (Stage primaryStage){
        
        try{
            Parent parent = FXMLLoader.load(getClass().getResource("/com/imperial/vista/FXMLInicioSesion.fxml"));
            Scene scene = new Scene (parent);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ioe){
            Utilidades.mostrarAlerta("Inicio de Sesión", Constantes.ERROR_ABRIR_VENTANA, Alert.AlertType.NONE);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
