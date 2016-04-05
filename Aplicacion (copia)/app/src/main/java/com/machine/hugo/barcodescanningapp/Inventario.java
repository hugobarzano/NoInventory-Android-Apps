package com.machine.hugo.barcodescanningapp;

/**
 * Created by hugo on 25/03/16.
 */
public class Inventario {
    private String _id;
    private String nombre_inventario;

    public Inventario(String _id,String nombre) {
        this._id = _id;
        this.nombre_inventario=nombre;
    }

    public String get_id() {
        return _id;
    }

    public String getNombre() {
        return nombre_inventario;
    }

    public void setNombre_inventario(String nombre) {
        this.nombre_inventario = nombre;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
