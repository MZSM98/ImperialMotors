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

    public static ResultSet obtenerVentasPorPeriodo(Connection conexion, String formatoFecha) throws SQLException {
        if (conexion != null) {
            String consulta = "SELECT DATE_FORMAT(fecha, ?) as periodo, SUM(importe) as total FROM venta GROUP BY periodo ORDER BY periodo";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, formatoFecha);
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

    public static ResultSet obtenerTodasLasVentas(Connection conexion) throws SQLException {
        if (conexion != null) {
            String consulta = "SELECT idVenta, fecha, importe FROM venta";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}