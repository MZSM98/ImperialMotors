
package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.Cliente;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClienteDAO {
    private ClienteDAO(){
        
    }
    
    public static int registrarCliente(Connection conexionBD, Cliente cliente) throws SQLException{
        
        if(conexionBD != null){
            String insercion = "INSERT into cliente (nombre, apellidoPaterno, apellidoMaterno, telefono,"
                    + " correo)"
                    + " values (?,?,?,?,?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(insercion);
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getApellidoPaterno());
            sentencia.setString(3, cliente.getApellidoMaterno());
            sentencia.setString(4, cliente.getTelefono());
            sentencia.setString(5, cliente.getCorreo());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static ResultSet obtenerClientes(Connection conexionBD) throws SQLException{
        
        if(conexionBD!= null){
            String consulta = "SELECT * from cliente;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static int editarCliente(Connection conexionBD, Cliente cliente) throws SQLException{
        
        if(conexionBD != null){
            String edicion = "UPDATE cliente SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, telefono = ?, correo = ? where idCliente = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(edicion);
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getApellidoPaterno());
            sentencia.setString(3, cliente.getApellidoMaterno());
            sentencia.setString(4, cliente.getTelefono());
            sentencia.setString(5, cliente.getCorreo());
            sentencia.setInt(6, cliente.getIdCliente());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}
