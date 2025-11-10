
package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.ProveedorDAO;
import com.imperial.modelo.pojo.Proveedor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ProveedorImpl {
    private ProveedorImpl(){
        
    }
    
    public static HashMap<String, Object> registrarProveedor(Proveedor proveedor){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            int filasAfectadas = ProveedorDAO.registrarProveedor(ConexionBD.abrirConexion(), proveedor);
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "La información del Proveedor se guardó de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo guardar el registro del Proveedor, intente más tarde");
            }
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editarProveedor(Proveedor proveedor){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            int filasAfectadas = ProveedorDAO.editarProveedor(ConexionBD.abrirConexion(), proveedor);
            if (filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "La información del Proveedor fue actualizada de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo actualizar la información del Proveedor, intente más tarde");
            }
        }catch (SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerProveedores(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            ResultSet resultado = ProveedorDAO.obtenerProveedores(ConexionBD.abrirConexion());
            ArrayList<Proveedor> proveedores = new ArrayList<>();
            while (resultado.next()){
                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(resultado.getInt("idProveedor"));
                proveedor.setNombre(resultado.getString("nombre"));
                proveedor.setTelefono(resultado.getString("telefono"));
                proveedor.setCorreo(resultado.getString("correo"));
                proveedor.setRFC(resultado.getString("RFC"));
                
                proveedores.add(proveedor);
            }
            respuesta.put("error", false);
            respuesta.put("proveedores", proveedores);
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
}
