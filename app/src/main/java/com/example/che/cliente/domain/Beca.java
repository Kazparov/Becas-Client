package com.example.che.cliente.domain;

/**
 * Created by Che on 04/06/2015.
 */
public class Beca {
    public int idBeca;
    public String pais;
    public String entidad;
    public String nombre;

    public Beca(){}

    public Beca(int _idBeca, String _nombre, String _pais, String _entidad){
        pais = _pais;
        idBeca = _idBeca;
        nombre = _nombre;
        entidad = _entidad;
    }

    public String getNombre(){
        return nombre;
    }

    public String getPais(){
        return pais;
    }

    public String getEntidad(){
        return entidad;
    }

    public int getIdBeca(){
        return idBeca;
    }

}
