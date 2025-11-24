package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.BitacoraDAO;
import com.imperial.modelo.pojo.Bitacora;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BitacoraImpl {
    
    private static final Logger logger = LogManager.getLogger(BitacoraImpl.class);

    private BitacoraImpl(){
    }

    public static void registrar(int idUsuario, String nombre, String accion) {
        
        Thread hiloRegistro = new Thread(() -> {
            
            logger.info("Usuario ID: {} | Nombre: {} | Accion: {}", idUsuario, nombre, accion);
            registrarEnBaseDeDatos(idUsuario, nombre, accion);
        });
        
        hiloRegistro.setDaemon(true);
        hiloRegistro.start();
    }
    
    private static void registrarEnBaseDeDatos(int idUsuario, String nombre, String accion){
        Connection conexion = ConexionBD.abrirConexion();
        try {
            Bitacora bitacora = new Bitacora(idUsuario, nombre, accion);
            BitacoraDAO.registrarMovimiento(conexion, bitacora);
        } catch (SQLException ex) {
            logger.error("FALLO CRITICO BD: No se pudo insertar en tabla bitacora. Error: {}", ex.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
    }

    public static HashMap<String, Object> obtenerBitacora() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion(); 
        
        try {
            ResultSet resultado = BitacoraDAO.obtenerBitacora(conexion);
            ArrayList<Bitacora> listaLogs = new ArrayList<>();
            
            while (resultado.next()) {
                Bitacora log = new Bitacora();
                log.setIdBitacora(resultado.getInt("idBitacora"));
                log.setFecha(resultado.getString("fecha"));
                log.setIdUsuario(resultado.getInt("idUsuario"));
                log.setUsuarioNombre(resultado.getString("usuarioNombre"));
                log.setAccion(resultado.getString("accion"));
                listaLogs.add(log);
            }
            
            respuesta.put("error", false);
            respuesta.put("logs", listaLogs);
            
        } catch (SQLException sqle) {
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
            logger.error("Error al consultar bitácora desde la UI: {}", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        
        return respuesta;
    }
}