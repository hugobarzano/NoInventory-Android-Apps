package com.machine.hugo.barcodescanningapp;

import android.util.JsonReader;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 25/03/16.
 */
public class GsonInventarioParser {
        public List<Inventario> leerFlujoJson(InputStream in) throws IOException {
            // Nueva instancia de la clase Gson
            Gson gson = new Gson();

            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<Inventario> inventarios = new ArrayList<>();

            // Iniciar el array
            reader.beginArray();

            while (reader.hasNext()) {
                // Lectura de objetos
                Inventario elemnt = gson.fromJson(String.valueOf(reader), Inventario.class);
                inventarios.add(elemnt);
            }


            reader.endArray();
            reader.close();
            return inventarios;
        }
}
