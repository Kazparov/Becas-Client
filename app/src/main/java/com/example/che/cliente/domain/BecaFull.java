package com.example.che.cliente.domain;

/**
 * Created by Che on 11/06/2015.
 */
public class BecaFull extends Beca {
    String fecha_inicio, fecha_fin;
    String descripcion_caracteristicas;
    String descripcion_requisitos;
    String descripcion_solicitud;
    String tipoBeca;
    String telefonoEntidad;
    String direccionEntidad;

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getDescripcion_caracteristicas() {
        return descripcion_caracteristicas;
    }

    public void setDescripcion_caracteristicas(String descripcion_caracteristicas) {
        this.descripcion_caracteristicas = descripcion_caracteristicas;
    }

    public String getDescripcion_requisitos() {
        return descripcion_requisitos;
    }

    public void setDescripcion_requisitos(String descripcion_requisitos) {
        this.descripcion_requisitos = descripcion_requisitos;
    }

    public String getDescripcion_solicitud() {
        return descripcion_solicitud;
    }

    public void setDescripcion_solicitud(String descripcion_solicitud) {
        this.descripcion_solicitud = descripcion_solicitud;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public String getTelefonoEntidad() {
        return telefonoEntidad;
    }

    public void setTelefonoEntidad(String telefonoEntidad) {
        this.telefonoEntidad = telefonoEntidad;
    }

    public String getDireccionEntidad() {
        return direccionEntidad;
    }

    public void setDireccionEntidad(String direccionEntidad) {
        this.direccionEntidad = direccionEntidad;
    }


}
