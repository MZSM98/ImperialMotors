package com.imperial.modelo.dao;

import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private UsuarioDAO(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }

    public static int registrarUsuario(Usuario usuario, Connection conexionBD) throws SQLException{
        if(conexionBD != null){
            String insert = "INSERT INTO usuario (nombre, apellidoPaterno, apellidoMaterno, correo, contrasena, idRol, estado) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVO');";
            PreparedStatement sentencia = conexionBD.prepareStatement(insert);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellidoPaterno());
            sentencia.setString(3, usuario.getApellidoMaterno());
            sentencia.setString(4, usuario.getCorreo());
            sentencia.setString(5, usuario.getContrasena());
            sentencia.setInt(6, usuario.getIdRol());
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static ResultSet obtenerUsuarios(Connection conexionBD) throws SQLException{
        if (conexionBD != null){
            String consulta = "SELECT u.idUsuario, u.nombre, u.apellidoPaterno, u.apellidoMaterno, u.correo, " +
                              "u.contrasena, u.estado, u.idRol, r.rol " +
                              "FROM usuario u " +
                              "INNER JOIN rol r on r.idRol = u.idRol " +
                              "ORDER BY u.nombre ASC;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static boolean verificarCorreo(Connection conexionBD, String correo) throws SQLException{
        if (conexionBD != null){
            String consulta = "SELECT idUsuario FROM usuario WHERE correo = ? LIMIT 1;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, correo);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    // Método implementado
    public static int editarUsuario(Usuario usuario, Connection conexionBD) throws SQLException{
        if (conexionBD != null ){
            String update = "UPDATE usuario SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, " +
                            "correo = ?, contrasena = ?, idRol = ?, estado = ? WHERE idUsuario = ?;";
            PreparedStatement sentencia = conexionBD.prepareStatement(update);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellidoPaterno());
            sentencia.setString(3, usuario.getApellidoMaterno());
            sentencia.setString(4, usuario.getCorreo());
            sentencia.setString(5, usuario.getContrasena());
            sentencia.setInt(6, usuario.getIdRol());
            sentencia.setString(7, usuario.getEstado());
            sentencia.setInt(8, usuario.getIdUsuario());
            
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static int eliminarUsuario(int idUsuario, Connection conexionBD) throws SQLException{
        if(conexionBD != null){
            String delete = "DELETE FROM usuario WHERE idUsuario = ?;";
            PreparedStatement sentencia = conexionBD.prepareStatement(delete);
            sentencia.setInt(1, idUsuario);
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
    
    public static int cambiarEstado(int idUsuario, String estado, Connection conexionBD) throws SQLException{
        if(conexionBD != null){
            String update = "UPDATE usuario SET estado = ? WHERE idUsuario = ?;";
            PreparedStatement sentencia = conexionBD.prepareStatement(update);
            sentencia.setString(1, estado);
            sentencia.setInt(2, idUsuario);
            return sentencia.executeUpdate();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}