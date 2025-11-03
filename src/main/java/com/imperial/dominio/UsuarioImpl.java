
package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.UsuarioDAO;
import com.imperial.modelo.pojo.Usuario;
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
        try{
            
            ResultSet resultado = UsuarioDAO.obtenerUsuarios(ConexionBD.abrirConexion());
            ArrayList<Usuario> usuarios = new ArrayList<>();
            
            while (resultado.next()){
                Usuario usuario = new Usuario ();
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                usuario.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                usuario.setCorreo(resultado.getString("correo"));
                usuario.setIdRol(resultado.getInt("idRol"));
                usuario.setRol(resultado.getString("rol"));
                usuario.setContrasena(resultado.getString("contrasena"));
                usuarios.add(usuario);
                
            }
            respuesta.put("error", false);
            respuesta.put("usuarios", usuarios);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    
    public static HashMap<String, Object> registrarUsuario(Usuario usuario){
        
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        
        try{
            int filasAfectadas = UsuarioDAO.registrarUsuario(usuario, ConexionBD.abrirConexion());
            
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del usuario" + usuario.getNombre() +" fue guardado de manera exitosa");
                
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo guardar la información, inténtelo más tarde");
            }
            
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static boolean verificarDuplicado(String correo){
           
        try {
            return UsuarioDAO.verificarCorreo(ConexionBD.abrirConexion(), correo);
        }catch (SQLException sqle){
            sqle.printStackTrace();//Tengo que cambiar esto
            return false;
        }finally{
            ConexionBD.cerrarConexionBD();
        }
    }
    
    public static HashMap<String, Object> editarUsuario(Usuario usuario){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            int filasAfectadas = UsuarioDAO.editarUsuario(usuario, ConexionBD.abrirConexion());
            
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del usuario" + usuario.getNombre() +" fue modificado de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo modificar la información, inténtelo más tarde");
            }
            ConexionBD.cerrarConexionBD();
        }catch (SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
}
