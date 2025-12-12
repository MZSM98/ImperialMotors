package com.imperial.modelo.dao;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutenticacionDAO {
    
    private AutenticacionDAO(){
        throw new UnsupportedOperationException("Es una clase de utilería y no debe ser instanciada...");
    }
    
    public static ResultSet getUsuarioPorCorreo(String correo, Connection conexionBD) throws SQLException{
        
        
        if (conexionBD != null){
            String consulta = "SELECT idUsuario, nombre, " +
                "apellidoPaterno, apellidoMaterno, correo, contrasena, u.idRol, rol " +
                "FROM usuario u " +
                "INNER JOIN rol r ON r.idRol = u.idRol " +
                "WHERE correo = ? AND estado = 'ACTIVO'";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, correo);
            return sentencia.executeQuery();
        }
        
       throw new SQLException (Constantes.ERROR_BD);
    }
}