package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.VehiculoDAO;
import com.imperial.modelo.dao.VentaDAO;
import com.imperial.modelo.pojo.DetalleVenta;
import com.imperial.modelo.pojo.Vehiculo;
import com.imperial.modelo.pojo.Venta;
import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class VentaImpl {
    
    private VentaImpl(){
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }
    
    public static HashMap<String, Object> registrarVenta(Venta venta, ArrayList<DetalleVenta> detalles) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexion();
        
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                int idVentaGenerada = VentaDAO.registrarVenta(conexion, venta);
                for (DetalleVenta detalle : detalles) {
                    detalle.setIdVenta(idVentaGenerada);
                    VentaDAO.registrarDetalleVenta(conexion, detalle);
                    VehiculoDAO.actualizarEstado(detalle.getVIN(), "Vendido", conexion);
                }
                conexion.commit();
                respuesta.put("error", false);
                respuesta.put("mensaje", String.valueOf(idVentaGenerada)); 
            } catch (SQLException e) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                respuesta.put("error", true);
                respuesta.put("mensaje", "Error al registrar la venta: " + e.getMessage());
            } finally {
                ConexionBD.cerrarConexionBD();
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "No hay conexión a la base de datos");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerVentas() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            ResultSet resultado = VentaDAO.obtenerVentas(ConexionBD.abrirConexion());
            ArrayList<Venta> ventas = new ArrayList<>();
            
            while (resultado.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(resultado.getInt("idVenta"));
                venta.setFecha(resultado.getString("fecha"));
                venta.setImporte(resultado.getFloat("importe"));
                venta.setIdUsuario(resultado.getInt("idUsuario"));
                venta.setIdCliente(resultado.getInt("idCliente"));
                
                String nombreCompleto = resultado.getString("nombre") + " " + resultado.getString("apellidoPaterno");
                venta.setNombreCliente(nombreCompleto);
                ventas.add(venta);
            }
            respuesta.put("error", false);
            respuesta.put("ventas", ventas);
            
        } catch (SQLException sqle) {
            respuesta.put("error", true);
            respuesta.put("mensaje", sqle.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
    
    public static ArrayList<Vehiculo> obtenerVehiculosVenta(int idVenta) {
        ArrayList<com.imperial.modelo.pojo.Vehiculo> vehiculos = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        try {
            ResultSet rs = VentaDAO.obtenerDetallesVenta(conexion, idVenta);
            while (rs.next()) {
                com.imperial.modelo.pojo.Vehiculo v = new com.imperial.modelo.pojo.Vehiculo();
                v.setVIN(rs.getString("VIN"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setAnio(rs.getString("anio"));
                v.setPrecio(rs.getDouble("precio")); 
                v.setTipo(rs.getString("tipo"));
                v.setEstado(rs.getString("estado"));
                v.setIdProveedor(rs.getInt("idProveedor"));
                vehiculos.add(v);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return vehiculos;
    }

}