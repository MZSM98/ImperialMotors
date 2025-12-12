package com.imperial.modelo.dao;

import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteDAO {

    private ReporteDAO() {
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }

    public static ResultSet obtenerVentasPorPeriodo(Connection conexion, String formatoFecha, Integer idUsuario) throws SQLException {
        if (conexion != null) {
            String consulta;
            PreparedStatement sentencia;
            
            if (idUsuario != null) {
                // Filtramos por usuario antes de agrupar
                consulta = "SELECT DATE_FORMAT(fecha, ?) as periodo, SUM(importe) as total FROM venta WHERE idUsuario = ? GROUP BY periodo ORDER BY periodo";
                sentencia = conexion.prepareStatement(consulta);
                sentencia.setString(1, formatoFecha);
                sentencia.setInt(2, idUsuario);
            } else {
                // Consulta original (para administradores)
                consulta = "SELECT DATE_FORMAT(fecha, ?) as periodo, SUM(importe) as total FROM venta GROUP BY periodo ORDER BY periodo";
                sentencia = conexion.prepareStatement(consulta);
                sentencia.setString(1, formatoFecha);
            }
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }

    public static ResultSet obtenerInventarioPorModelo(Connection conexion) throws SQLException {
        if (conexion != null) {
            String consulta = "SELECT modelo, COUNT(*) as cantidad FROM vehiculo WHERE estado = 'Disponible' GROUP BY modelo";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }

    public static ResultSet obtenerTodasLasVentas(Connection conexion, Integer idUsuario) throws SQLException {
        if (conexion != null) {
            String consulta;
            PreparedStatement sentencia;
            
            if (idUsuario != null) {
                consulta = "SELECT idVenta, fecha, importe FROM venta WHERE idUsuario = ?";
                sentencia = conexion.prepareStatement(consulta);
                sentencia.setInt(1, idUsuario);
            } else {
                consulta = "SELECT idVenta, fecha, importe FROM venta";
                sentencia = conexion.prepareStatement(consulta);
            }
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}