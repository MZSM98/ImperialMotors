package com.imperial.dominio;
import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.AutenticacionDAO;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Encriptacion; // <--- IMPORTANTE
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AutenticacionImpl {
    
    public static HashMap<String, Object> verificarCredencialesUsuario(
            String correo, String contrasenaPlana){ 
        
        HashMap<String, Object> respuesta = new HashMap<>();
        
        try{
            // 1. Buscamos al usuario SOLO por correo
            ResultSet resultado = AutenticacionDAO.getUsuarioPorCorreo(correo,
                    ConexionBD.abrirConexion());
            
            // 2. Verificamos si el usuario (correo) existe
            if (resultado.next()){
                
                // 3. Si existe, obtenemos el hash guardado en la BD
                String hashGuardado = resultado.getString("contrasena");
                
                // 4. Comparamos el hash de la BD con la contraseña en texto plano
                if (Encriptacion.checkPassword(contrasenaPlana, hashGuardado)) {
                    
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
                    respuesta.put("Usuario", usuarioSesion);
                    
                } else {
                    respuesta.put("Error", true);
                    respuesta.put("mensaje", "Las credenciales proporcionadas son incorrectas...");
                }
                
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