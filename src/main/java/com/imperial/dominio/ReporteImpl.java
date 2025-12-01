package com.imperial.dominio;

import com.imperial.modelo.ConexionBD;
import com.imperial.modelo.dao.ReporteDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReporteImpl {

    private ReporteImpl() {
    }

    public static HashMap<String, Object> obtenerDatosVentas(String tipoPeriodo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String formato = "%Y-%m"; 

        if ("DIA".equals(tipoPeriodo)) {
            formato = "%Y-%m-%d";
        } else if ("SEMANA".equals(tipoPeriodo)) {
            formato = "%Y-Semana %u";
        }

        try {
            ResultSet rs = ReporteDAO.obtenerVentasPorPeriodo(ConexionBD.abrirConexion(), formato);
            Map<String, Double> datos = new LinkedHashMap<>();
            while (rs.next()) {
                datos.put(rs.getString("periodo"), rs.getDouble("total"));
            }
            respuesta.put("error", false);
            respuesta.put("datos", datos);
        } catch (SQLException ex) {
            respuesta.put("error", true);
            respuesta.put("mensaje", ex.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerDatosInventario() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            ResultSet rs = ReporteDAO.obtenerInventarioPorModelo(ConexionBD.abrirConexion());
            Map<String, Integer> datos = new LinkedHashMap<>();
            while (rs.next()) {
                datos.put(rs.getString("modelo"), rs.getInt("cantidad"));
            }
            respuesta.put("error", false);
            respuesta.put("datos", datos);
        } catch (SQLException ex) {
            respuesta.put("error", true);
            respuesta.put("mensaje", ex.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }

    public static HashMap<String, Object> analizarAnomaliasYKPI() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            ResultSet rs = ReporteDAO.obtenerTodasLasVentas(ConexionBD.abrirConexion());
            List<Double> importes = new ArrayList<>();
            List<String> reporteAnomalias = new ArrayList<>();
            double sumaTotal = 0;

            while (rs.next()) {
                double importe = rs.getDouble("importe");
                importes.add(importe);
                sumaTotal += importe;
            }

            double promedio = importes.isEmpty() ? 0 : sumaTotal / importes.size();
            double umbralAnomalia = promedio * 2.0; 

            for (Double importe : importes) {
                if (importe > umbralAnomalia || importe < (promedio * 0.1)) {
                    reporteAnomalias.add("Venta atípica detectada de: $" + String.format("%.2f", importe));
                }
            }

            respuesta.put("error", false);
            respuesta.put("promedio", promedio);
            respuesta.put("anomalias", reporteAnomalias);
        } catch (SQLException ex) {
            respuesta.put("error", true);
            respuesta.put("mensaje", ex.getMessage());
        } finally {
            ConexionBD.cerrarConexionBD();
        }
        return respuesta;
    }
}   