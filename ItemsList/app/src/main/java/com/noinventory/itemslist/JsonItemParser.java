package com.noinventory.itemslist;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 21/03/16.
 */
public class JsonItemParser {

    //leer el flujo json
    public List<Item> readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerArrayItems(reader);
        } finally {
            reader.close();
        }

    }
    //metodo para ller un array de items
    public List leerArrayItems(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList items = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            items.add(leerItem(reader));
        }
        reader.endArray();
        return items;
    }
    //Lee campo a campo las clase y devuelve un objeto item formado con ellas
    public Item leerItem(JsonReader reader) throws IOException {
        String _id = null;
        String nombre = null;
        String descripcion = null;

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
                case "descripcion":
                    descripcion = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Item(_id, nombre, descripcion);
    }
}
