
package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.Vehiculo;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class VehiculoDAO {
    
    private VehiculoDAO(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }
    
    public static int registrarVehiculo(Connection conexionBD, Vehiculo vehiculo) throws SQLException{
        
        if (conexionBD != null){
            String insercion = "INSERT INTO vehiculo (VIN, marca, modelo, anio, precio, tipo, idProveedor) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            
            sentencia.setString(1, vehiculo.getVIN());
            sentencia.setString(2,vehiculo.getMarca());
            sentencia.setString(3, vehiculo.getModelo());
            sentencia.setString(4, vehiculo.getAnio());
            sentencia.setDouble(5, vehiculo.getPrecio());
            sentencia.setString(6, vehiculo.getTipo());
            sentencia.setInt(7, vehiculo.getIdProveedor());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static int editarVehiculo(Connection conexionBD, Vehiculo vehiculo) throws SQLException{
        
        if(conexionBD != null){
            String insercion = "UPDATE vehiculo SET marca = ?, modelo = ?, anio = ?,"
                    + " precio = ?, tipo = ?, idProveedor = ? WHERE VIN = ?;";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setString(1, vehiculo.getMarca());
            sentencia.setString(2, vehiculo.getModelo());
            sentencia.setString(3, vehiculo.getAnio());
            sentencia.setDouble(4, vehiculo.getPrecio());
            sentencia.setString(5, vehiculo.getTipo());
            sentencia.setInt(6, vehiculo.getIdProveedor());
            sentencia.setString(7,vehiculo.getVIN());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static ResultSet obtenerVehiculos(Connection conexionBD) throws SQLException{
        
        if (conexionBD != null) {
            String consulta = "SELECT * FROM vehiculo WHERE estado = 'Disponible'";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static int actualizarEstado(String vin, String estado, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String consulta = "UPDATE vehiculo SET estado = ? WHERE VIN = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, estado);
            sentencia.setString(2, vin);
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
}
