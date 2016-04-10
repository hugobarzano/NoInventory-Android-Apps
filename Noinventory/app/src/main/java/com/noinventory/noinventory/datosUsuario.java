package com.noinventory.noinventory;

/**
 * Created by hugo on 5/04/16.
 */
public class datosUsuario {
    private static String nombre_usuario;
    private static String organizacion;


    public static String getNombre_usuario() {
        return nombre_usuario;
    }

    public static void setNombre_usuario(String nombre_usuario) {
        datosUsuario.nombre_usuario = nombre_usuario;
    }
    public static String getOrganizacion() {
        return organizacion;
    }

    public static void setOrganizacion(String organizacion) {
        datosUsuario.organizacion = organizacion;
    }
}
