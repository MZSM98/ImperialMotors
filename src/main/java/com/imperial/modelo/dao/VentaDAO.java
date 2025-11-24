package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.DetalleVenta;
import com.imperial.modelo.pojo.Venta;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentaDAO {
    
    private VentaDAO() {
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }

    public static int registrarVenta(Connection conexionBD, Venta venta) throws SQLException {
        if (conexionBD != null) {
            String insercion = "INSERT INTO venta (fecha, importe, idUsuario, idCliente) VALUES (CURRENT_DATE(), ?, ?, ?);";
            // Necesitamos RETURN_GENERATED_KEYS para obtener el ID de la venta creada (folio)
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion, Statement.RETURN_GENERATED_KEYS);
            sentencia.setFloat(1, venta.getImporte());
            sentencia.setInt(2, venta.getIdUsuario());
            sentencia.setInt(3, venta.getIdCliente());
            
            int filas = sentencia.executeUpdate();
            if(filas > 0){
                ResultSet rs = sentencia.getGeneratedKeys();
                if(rs.next()){
                    return rs.getInt(1); // Retorna el ID generado
                }
            }
        }
        throw new SQLException(Constantes.ERROR_BD);
    }

    public static int registrarDetalleVenta(Connection conexionBD, DetalleVenta detalle) throws SQLException {
        if (conexionBD != null) {
            String insercion = "INSERT INTO detalleventa (idVenta, VIN, cantidad, precioUnitario) VALUES (?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setInt(1, detalle.getIdVenta());
            sentencia.setString(2, detalle.getVIN());
            sentencia.setInt(3, detalle.getCantidad());
            sentencia.setFloat(4, detalle.getPrecioUnitario());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static ResultSet obtenerVentas(Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String consulta = "SELECT v.idVenta, v.fecha, v.importe, v.idUsuario, v.idCliente, " +
                              "c.nombre, c.apellidoPaterno " +
                              "FROM venta v " +
                              "INNER JOIN cliente c ON v.idCliente = c.idCliente " +
                              "ORDER BY v.fecha DESC;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}