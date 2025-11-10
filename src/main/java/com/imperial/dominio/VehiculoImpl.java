
package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.VehiculoDAO;
import com.imperial.modelo.pojo.Vehiculo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class VehiculoImpl {


    private VehiculoImpl(){
        
    }
    
    public static HashMap<String, Object> registrarVehiculo(Vehiculo vehiculo){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            int filasAfectadas = VehiculoDAO.registrarVehiculo(ConexionBD.abrirConexion(), vehiculo);
            
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "Se ha registrado el vehículo de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo registrar el vehículo, intente más tarde");
            }
            ConexionBD.cerrarConexionBD();
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editarVehiculo(Vehiculo vehiculo) throws SQLException{
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try{
            int filasAfectadas = VehiculoDAO.editarVehiculo(ConexionBD.abrirConexion(), vehiculo);
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del vehículo fue actualizado de manera exitosa");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo actualizar el registro del vehículo, intente más tarde");
            }
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerVehiculos(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        try{
            ResultSet resultado = VehiculoDAO.obtenerVehiculos(ConexionBD.abrirConexion());
            ArrayList<Vehiculo> vehiculos = new ArrayList<>();
            while(resultado.next()){
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setVIN(resultado.getString("VIN"));
                vehiculo.setMarca(resultado.getString("marca"));
                vehiculo.setModelo(resultado.getString("modelo"));
                vehiculo.setAnio(resultado.getString("anio"));
                vehiculo.setIdProveedor(resultado.getInt("idProveedor"));
                vehiculo.setEstado(resultado.getString("estado"));
                vehiculo.setPrecio(resultado.getDouble("precio"));
                vehiculo.setTipo(resultado.getString("tipo"));
                
                vehiculos.add(vehiculo);
            }
            respuesta.put("error", false);
            respuesta.put("vehiculos", vehiculos);
            ConexionBD.cerrarConexionBD();
        }catch (SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        }
        return respuesta;
    }
    
}
