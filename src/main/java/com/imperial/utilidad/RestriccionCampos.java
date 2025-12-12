
package com.imperial.utilidad;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;


public class RestriccionCampos {
    private static final int LIMITE_CAMPO_TEXTO = 200; 
    
    private RestriccionCampos(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }
    
    public static void limitarLongitud(TextInputControl campotexto) {
        campotexto.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > LIMITE_CAMPO_TEXTO) {
                return null; 
            }
            return change;
        }));
    }

    public static void soloLetras(TextInputControl campotexto) {
        aplicarPatron(campotexto, "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]*");
    }
    
    public static void limitarCantidadNumeros(TextInputControl campoTexto, int maxLength) {
         campoTexto.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        }));
    }

    public static void aplicarPatron(TextInputControl campoTexto, String regex) {
        campoTexto.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches(regex)) {
                return change;
            }
            return null;
        }));
    }
    
    public static void soloCaracteresValidosCorreo(TextInputControl input) {
    aplicarPatron(input, "[a-zA-Z0-9@._-]*");
    }
}
