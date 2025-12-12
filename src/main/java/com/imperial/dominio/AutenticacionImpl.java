package com.imperial.dominio;
import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.AutenticacionDAO;
import com.imperial.modelo.pojo.Usuario;
import com.imperial.utilidad.Constantes;
import com.imperial.utilidad.Encriptacion; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AutenticacionImpl {
    
    private AutenticacionImpl(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }
    
    public static HashMap<String, Object> verificarCredencialesUsuario(
            String correo, String contrasenaPlana){ 
        
        HashMap<String, Object> respuesta = new HashMap<>();
        
        try{
            ResultSet resultado = AutenticacionDAO.getUsuarioPorCorreo(correo,
                    ConexionBD.abrirConexion());
            
            if (resultado.next()){
                
                String hashGuardado = resultado.getString("contrasena");
                
                if (Encriptacion.checkPassword(contrasenaPlana, hashGuardado)) {
                    Usuario usuarioSesion = new Usuario();
                    usuarioSesion.setIdUsuario(resultado.getInt("idUsuario"));
                    usuarioSesion.setNombre(resultado.getString("nombre"));
                    usuarioSesion.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    usuarioSesion.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    usuarioSesion.setCorreo(resultado.getString("correo"));
                    usuarioSesion.setIdRol(resultado.getInt("idRol"));
                    usuarioSesion.setRol(resultado.getString("rol"));
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Credenciales correctas");
                    respuesta.put("Usuario", usuarioSesion);
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "Las credenciales proporcionadas son incorrectas...");
                }
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "Las credenciales proporcionadas son incorrectas, por favor verifica la información");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", Constantes.ERROR_BD);
        }
        
        return respuesta;
    }
            
}