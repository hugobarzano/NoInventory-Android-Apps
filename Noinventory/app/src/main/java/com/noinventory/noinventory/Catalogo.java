package com.noinventory.noinventory;

/**
 * Created by hugo on 10/04/16.
 */
public class Catalogo {
    private String _id;
    private String nombre;
    private String descripcion;

    public Catalogo(String _id, String nombre, String descripcion) {
        this._id = _id;
        this.nombre=nombre;
        this.descripcion = descripcion;

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
