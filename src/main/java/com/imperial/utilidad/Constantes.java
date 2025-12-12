
package com.imperial.utilidad;


public class Constantes {
    
    private Constantes(){
        throw new UnsupportedOperationException(ERROR_CLASE_UTILERIA);
    }
    
    public static final String ERROR_BD = "¡Oh, no! Algo salió mal… :( No pudimos procesar tu operación,"
                               + " por favor intenta más tarde";
    public static final String ERROR_CLASE_UTILERIA = "Esta clase no debe ser instanciada...";
    public static final String ERROR_ABRIR_VENTANA= "!Ups¡ algo salió mal y no pudimos abrir la ventana, intenta más tarde";
}
