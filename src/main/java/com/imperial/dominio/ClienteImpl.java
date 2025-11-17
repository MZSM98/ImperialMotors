
package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.ClienteDAO;
import com.imperial.modelo.pojo.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ClienteImpl {
    
    private ClienteImpl(){
        
    }
    
    public static HashMap<String, Object> registrarCliente(Cliente cliente){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            int filasAfectadas = ClienteDAO.registrarCliente(ConexionBD.abrirConexion(), cliente);
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "La información del cliente " + cliente.getNombreCompleto()+ " se ha guardado de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo guardar la información del Cliente");
            }
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editarCliente(Cliente cliente){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            int filasAfectadas = ClienteDAO.editarCliente(ConexionBD.abrirConexion(), cliente);
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "La información del cliente " + cliente.getNombreCompleto()+ " se ha actualizado de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo actualizar la información del Cliente");
            }
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerClientes(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            ArrayList<Cliente> clientes = new ArrayList<>();
            ResultSet resultado = ClienteDAO.obtenerClientes(ConexionBD.abrirConexion());
            while(resultado.next()){
                Cliente cliente = new Cliente();
                cliente.setNombre(resultado.getString("nombre"));
                cliente.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                cliente.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                cliente.setTelefono(resultado.getString("telefono"));
                cliente.setCorreo(resultado.getString("correo"));
                clientes.add(cliente);
            }
            respuesta.put("error", false);
            respuesta.put("clientes", clientes);
            ConexionBD.cerrarConexionBD();
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
}
