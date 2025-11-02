
package com.imperial.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDAO {

    public static ResultSet autenticarUsuario(String noPersonal,
            String password, Connection conexionBD) throws SQLException{
        
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
            sentencia.setString(1, noPersonal);
            sentencia.setString(2, password);
            resultado = sentencia.executeQuery();
            
            return resultado;
        }
        
        return null;
    }
}
