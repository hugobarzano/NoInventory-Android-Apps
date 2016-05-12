package com.noinventory.noinventory;

/**
 * Created by hugo on 21/03/16.
 */
public class Item {
    private String _id;
    private String nombre;
    private String descripcion;
    private String localizador;
    private String fecha;
    public Item(String _id, String nombre, String descripcion,String localizador,String fecha) {
        this._id = _id;
        this.nombre=nombre;
        this.descripcion = descripcion;
        this.localizador=localizador;
        this.fecha=fecha;

    }

    public String getLocalizador() {
        return localizador;
    }

    public void setLocalizador(String localizador) {
        this.localizador = localizador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}

