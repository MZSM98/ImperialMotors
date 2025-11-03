package com.imperial.modelo.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutenticacionDAO {
    
    private AutenticacionDAO(){
        throw new UnsupportedOperationException("Es una clase de utilería y no debe ser instanciada...");
    }
    
    public static ResultSet autenticarUsuario(String correo,
            String contrasena, Connection conexionBD) throws SQLException{
        
        ResultSet resultado = null;
        
        if (conexionBD != null){
            
            //Hay conexion con la BD
            String consulta = "SELECT idUsuario, nombre, " +
                "apellidoPaterno, apellidoMaterno, correo, u.idRol, rol " +
                "FROM usuario u " +
                "INNER JOIN rol r ON r.idRol = p.idRol " +
                "WHERE correo = ? " +
                "AND contrasena = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, correo);
            sentencia.setString(2, contrasena);
            resultado = sentencia.executeQuery();
            
            return resultado;
        }
        
        return null;
    }
}