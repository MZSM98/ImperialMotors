package com.imperial.modelo;

import com.imperial.utilidad.Constantes;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConexionBD {
    
    private static final Logger logger = LogManager.getLogger(ConexionBD.class);
    
    private static Properties propiedades = new Properties();
    private static String connectionUrl;
    
    // ThreadLocal para manejo seguro de hilos (evita conflictos entre UI y Bitácora)
    private static final ThreadLocal<Connection> hiloConexion = new ThreadLocal<>();
    
    private ConexionBD(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }
    
    static {
        try (InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (entrada == null) {
                logger.fatal("CRÍTICO: No se encontró el archivo 'db.properties' en el classpath.");
            } else {
                propiedades.load(entrada);
                
                String ip = propiedades.getProperty("db.ip");
                String port = propiedades.getProperty("db.port");
                String name = propiedades.getProperty("db.name");
                String timezone = propiedades.getProperty("db.timezone");
                
                connectionUrl = "jdbc:mysql://" + ip + ":" + port + "/" + name
                        + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=" + timezone;
                
                logger.info("Configuración de base de datos cargada correctamente. URL: jdbc:mysql://{}:{}/{}", ip, port, name);
            }
        } catch (IOException ex) {
            logger.error("Error de E/S al cargar db.properties", ex);
        }
    }
    
    public static Connection abrirConexion(){
        Connection conexion = hiloConexion.get();
        try{
            if (conexion == null || conexion.isClosed()) {
                String user = propiedades.getProperty("db.user");
                String pass = propiedades.getProperty("db.password");
                
                if (user == null || pass == null) {
                    logger.error("Credenciales de base de datos vacías en db.properties");
                    return null;
                }

                conexion = DriverManager.getConnection(connectionUrl, user, pass);
                hiloConexion.set(conexion);
            }
        } catch (SQLException sqle){ 
            logger.error("Error al abrir conexión con BD: {}", sqle.getMessage());
        }
        return conexion; 
    }
    
    public static void cerrarConexionBD(){
        try{
            Connection conexion = hiloConexion.get();
            if(conexion != null && !conexion.isClosed()){
                conexion.close();
            }
            hiloConexion.remove(); 
        } catch (SQLException sqle){
            logger.error("Error al cerrar la conexión: {}", sqle.getMessage());
        }
    }
}