package com.noinventory.itemslist;

import android.util.JsonReader;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 21/03/16.
 */
public class GsonItemParser {
    public List<Item> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia de la clase Gson
        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Item> items = new ArrayList<>();

        // Iniciar el array
        reader.beginArray();

        while (reader.hasNext()) {
            // Lectura de objetos
            Item elemnt = gson.fromJson(String.valueOf(reader), Item.class);
            items.add(elemnt);
        }


        reader.endArray();
        reader.close();
        return items;
    }
}
