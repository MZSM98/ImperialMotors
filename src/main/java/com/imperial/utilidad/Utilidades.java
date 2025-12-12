
package com.imperial.utilidad;

import com.imperial.ImperialMotors;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


public class Utilidades {
    
    private Utilidades(){
        throw new UnsupportedOperationException("Es una clase de utileria y no debe ser instanciada");
    }
    
    public static void mostrarAlerta(String titulo, String mensaje, AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.show();
    }
    
    public static FXMLLoader obtenerVistaMemoria(String url){
        
        return new FXMLLoader(ImperialMotors.class.getResource(url));
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String encabezado, String contenido){
        boolean confirmacion = false;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType ("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        Optional<ButtonType> opcion = alerta.showAndWait();
        
        return (opcion.isPresent() && opcion.get() == botonSi);
    }
}
