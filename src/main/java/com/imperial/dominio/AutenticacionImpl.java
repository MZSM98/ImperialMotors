package com.imperial.dominio;
import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.AutenticacionDAO;
import com.imperial.modelo.pojo.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AutenticacionImpl {
    
    public static HashMap<String, Object> verificarCredencialesUsuario(
            String correo, String contrasena){
        
        HashMap<String, Object> respuesta = new HashMap<>();
        
        try{
            ResultSet resultado = AutenticacionDAO.autenticarUsuario(correo,
                    contrasena, ConexionBD.abrirConexion());
            
            if (resultado.next()){
                Usuario usuarioSesion = new Usuario();
                usuarioSesion.setIdUsuario(resultado.getInt("idUsuario"));
                usuarioSesion.setNombre(resultado.getString("nombre"));
                usuarioSesion.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                usuarioSesion.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                usuarioSesion.setCorreo(resultado.getString("correo"));
                usuarioSesion.setIdRol(resultado.getInt("idRol"));
                usuarioSesion.setRol(resultado.getString("rol"));
                
                respuesta.put("Error", false);
                respuesta.put("mensaje", "Credenciales correctas");
                respuesta.put("Profesor", usuarioSesion);
            }else{
                respuesta.put("Error", true);
                respuesta.put("mensaje", "Las credenciales proporcionadas son incorrectas, por favor verifica la información");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException sqle){
            respuesta.put("Error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        
        return respuesta;
    }
            
}