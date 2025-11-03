
package com.imperial.utilidad;

import com.imperial.ImperialMotors;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


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
}
