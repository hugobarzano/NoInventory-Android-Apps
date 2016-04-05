package com.machine.hugo.barcodescanningapp;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 25/03/16.
 */
public class JsonInventarioParser {
    //leer el flujo json
    public List<Inventario> readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            System.out.print("En la clase json");
            return leerArrayInventarios(reader);
        } finally {
            reader.close();
        }

    }
    //metodo para ller un array de items
    public List leerArrayInventarios(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList inventarios = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            inventarios.add(leerInventario(reader));
            System.out.print("En la clase json");
            System.out.print(leerArrayInventarios(reader));
        }
        reader.endArray();
        return inventarios;
    }
    //Lee campo a campo las clase y devuelve un objeto item formado con ellas
    public Inventario leerInventario(JsonReader reader) throws IOException {
        String _id = null;
        String nombre = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "_id":
                    _id = reader.nextString();
                    break;
                case "nombre":
                    nombre = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Inventario(_id, nombre);
    }
}


