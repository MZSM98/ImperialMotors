
package com.imperial.utilidad;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Utilidades {
    
    private Utilidades(){
        throw new UnsupportedOperationException("Es una clase de utileria y no deberia ser instanciada");
    }
    
    public static void mostrarAlerta(String titulo, String mensaje, AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.show();
    }
}
