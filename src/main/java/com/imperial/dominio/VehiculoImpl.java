package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.VehiculoDAO;
import com.imperial.modelo.pojo.Vehiculo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class VehiculoImpl {

    private VehiculoImpl(){
    }
    
    public static HashMap<String, Object> registrarVehiculo(Vehiculo vehiculo){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            int filasAfectadas = VehiculoDAO.registrarVehiculo(conexion, vehiculo);
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "Vehículo registrado correctamente en el inventario.");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo registrar el vehículo.");
            }
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editarVehiculo(Vehiculo vehiculo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            int filasAfectadas = VehiculoDAO.editarVehiculo(conexion, vehiculo);
            if(filasAfectadas > 0){
                respuesta.put("error", false);
                respuesta.put("mensaje", "Información del vehículo actualizada.");
            }else{
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudieron guardar los cambios.");
            }
        }catch(SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    // MÉTODO NUEVO: "Eliminar" es cambiar estado a Vendido
    public static HashMap<String, Object> eliminarVehiculo(String vin) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try {
            // Reutilizamos el método actualizarEstado del DAO
            int filas = VehiculoDAO.actualizarEstado(vin, "Vendido", conexion);
            if (filas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Vehículo dado de baja (Estado actualizado a Vendido).");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo actualizar el estado del vehículo.");
            }
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al dar de baja: " + e.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerVehiculos(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        try{
            ResultSet resultado = VehiculoDAO.obtenerVehiculos(conexion);
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
        }catch (SQLException sqle){
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
}