package com.imperial.modelo.pojo;

public class Bitacora {
    
    private int idBitacora;
    private String fecha;
    private int idUsuario;
    private String usuarioNombre;
    private String accion;

    public Bitacora() {
    }

    public Bitacora(int idUsuario, String usuarioNombre, String accion) {
        this.idUsuario = idUsuario;
        this.usuarioNombre = usuarioNombre;
        this.accion = accion;
    }

    public int getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(int idBitacora) {
        this.idBitacora = idBitacora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
}