
package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.Proveedor;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProveedorDAO {
    
    private ProveedorDAO(){
        
    }
    
    public static int registrarProveedor(Connection conexionBD, Proveedor proveedor) throws SQLException{
        
        if(conexionBD != null){
            String insercion = "INSERT INTO proveedor (nombre, telefono, correo, RFC) VALUES (?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setString(1, proveedor.getNombre());
            sentencia.setString(2, proveedor.getTelefono());
            sentencia.setString(3, proveedor.getCorreo());
            sentencia.setString(4, proveedor.getRFC());
            
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static ResultSet obtenerProveedores(Connection conexionBD) throws SQLException{
        
        if(conexionBD != null){
            String consulta = "select * from proveedor;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);       
    }
    
    public static int editarProveedor(Connection conexionBD, Proveedor proveedor) throws SQLException{
        
        if(conexionBD != null){
            String edicion = "UPDATE proveedor SET nombre = ?, telefono = ?, correo = ? WHERE RFC = ?;";
            PreparedStatement sentencia = conexionBD.prepareStatement(edicion);
            sentencia.setString(1, proveedor.getNombre());
            sentencia.setString(2, proveedor.getTelefono());
            sentencia.setString(3, proveedor.getCorreo());
            sentencia.setString(4, proveedor.getRFC());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}
