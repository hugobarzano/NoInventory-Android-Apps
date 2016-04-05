package com.noinventory.itemslist;

/**
 * Created by hugo on 21/03/16.
 */
public class Item {
    private String _id;
    private String nombre;
    private String descripcion;

    public Item(String _id, String nombre, String descripcion) {
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

