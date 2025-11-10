
package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDAO {

    private UsuarioDAO(){
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada...");
    }
    public static int registrarUsuario(Usuario usuario, Connection conexionBD) throws SQLException{
        
        if(conexionBD != null){
            String insert = "INSERT INTO usuario"
                    + " (nombre, apellidoPaterno, apellidoMaterno, correo, contrasena,"
                    + " idRol) VALUES"
                    + " (?, ?, ?, ?, ?, ?);";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(insert);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellidoPaterno());
            sentencia.setString(3, usuario.getApellidoMaterno());
            sentencia.setString(4, usuario.getCorreo());
            sentencia.setString(5, usuario.getContrasena());
            sentencia.setInt(6, usuario.getIdRol());
            return sentencia.executeUpdate();
        }
        throw new SQLException("Error de conexion con la base de datos");
    }
    
    public static ResultSet obtenerUsuarios(Connection conexionBD)
            throws SQLException{
        
        if (conexionBD != null){
           
                String consulta = "SELECT idUsuario, nombre, apellidoPaterno, apellidoMaterno, correo, contrasena" +
                    ", estado, usuario.idRol, Rol " +
                    "FROM " +
                    "usuario " +
                    "INNER JOIN " +
                    "rol on rol.idRol = usuario.idRol;";
                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexion a la base de datos.");
        
    }
    
    public static boolean verificarCorreo(Connection conexionBD, String correo) throws SQLException{
        
        if (conexionBD != null){
            
            String consulta = "SELECT * FROM usuario WHERE correo = ?;";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, correo);
            return sentencia.executeQuery().next();
        }
        throw new SQLException("No hay conexion a la base de datos");
    }
    
    public static int editarUsuario(Usuario usuario, Connection conexionBD) throws SQLException{
        
        if (conexionBD != null ){
            
            return 0;
        }
        throw new SQLException();
    }
    
    public static int eliminarUsuario(int idUsuario, Connection conexionBD) throws SQLException{
        
        if(conexionBD != null){
            
            return 0;
        }
        
        throw new SQLException();
    }
    
    public static int cambiarEstado(String estado, Connection conexionBD) throws SQLException{
        
        if(conexionBD != null){
            return 0;
        }
        throw new SQLException();
    }
}
