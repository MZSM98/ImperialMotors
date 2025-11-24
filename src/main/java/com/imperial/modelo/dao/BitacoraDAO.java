package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.Bitacora;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BitacoraDAO {
    
    private BitacoraDAO(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }

    public static int registrarMovimiento(Connection conexionBD, Bitacora bitacora) throws SQLException {
        if (conexionBD != null) {
            String insercion = "INSERT INTO bitacora (idUsuario, usuarioNombre, accion, fecha) VALUES (?, ?, ?, NOW());";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setInt(1, bitacora.getIdUsuario());
            sentencia.setString(2, bitacora.getUsuarioNombre());
            sentencia.setString(3, bitacora.getAccion());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }

    public static ResultSet obtenerBitacora(Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String consulta = "SELECT idBitacora, fecha, idUsuario, usuarioNombre, accion FROM bitacora ORDER BY fecha DESC;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}