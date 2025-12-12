package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.UsuarioDAO;
import com.imperial.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UsuarioImpl {
    
    private UsuarioImpl(){
    }
    
    public static HashMap<String, Object> obtenerUsuarios(){
        
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            ResultSet resultado = UsuarioDAO.obtenerUsuarios(conexion);
            ArrayList<Usuario> usuarios = new ArrayList<>();
            
            while (resultado.next()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                usuario.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                usuario.setCorreo(resultado.getString("correo"));
                usuario.setIdRol(resultado.getInt("idRol"));
                usuario.setRol(resultado.getString("rol"));
                usuario.setContrasena(resultado.getString("contrasena"));
                usuario.setEstado(resultado.getString("estado")); 
                usuarios.add(usuario);
            }
            respuesta.put("error", false);
            respuesta.put("usuarios", usuarios);
        } catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al obtener usuarios: " + sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> registrarUsuario(Usuario usuario){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            if(UsuarioDAO.verificarCorreo(conexion, usuario.getCorreo())){
                respuesta.put("error", true);
                respuesta.put("mensaje", "El correo electrónico ya está registrado en el sistema.");
                return respuesta;
            }

            int filasAfectadas = UsuarioDAO.registrarUsuario(usuario, conexion);
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "Usuario " + usuario.getNombre() + " registrado exitosamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo registrar el usuario.");
            }
        } catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error en base de datos: " + sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    public static boolean verificarDuplicado(String correo){
        boolean existe = false;
        Connection conexion = ConexionBD.abrirConexion();
        try {
            existe = UsuarioDAO.verificarCorreo(conexion, correo);
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return existe;
    }
    
    public static HashMap<String, Object> editarUsuario(Usuario usuario){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            
            int filasAfectadas = UsuarioDAO.editarUsuario(usuario, conexion);
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "Información del usuario actualizada correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo actualizar la información.");
            }
        } catch (SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al editar: " + sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> cambiarEstadoUsuario(int idUsuario, String nuevoEstado){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            int filas = UsuarioDAO.cambiarEstado(idUsuario, nuevoEstado, conexion);
            if(filas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "Estado del usuario actualizado a: " + nuevoEstado);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo cambiar el estado.");
            }
        } catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
}