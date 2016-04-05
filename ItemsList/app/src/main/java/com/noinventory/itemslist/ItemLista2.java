package com.noinventory.itemslist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemLista2 extends AppCompatActivity {

    ArrayAdapter adaptador;
    HttpURLConnection con;

    private Spinner spinner1;
    private List<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_lista2);

        System.out.print("METODO on create");

        /*
        Comprobar la disponibilidad de la Red
         */
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new JsonTask().
                        execute(
                                new URL("http://192.168.1.33:8000/noinventory/itemsJson/"));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public class JsonTask extends AsyncTask<URL, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(URL... urls) {
            List<Item> items = new ArrayList<>();

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    items = new ArrayList<>();
                    items.add(new Item("Error",null,null));

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonItemParser parser = new JsonItemParser();
                    //GsonItemParser parser = new GsonItemParser();
                    //items = parser.leerFlujoJson(in);
                    items = parser.readJsonStream(in);



                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(items!=null) {
                System.out.print("OnPOSTEXECUTEEEEEE");
                DatosPorDefecto(items);
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }
    public void DatosPorDefecto(List <Item> l) {
        spinner1 = (Spinner) findViewById(R.id.spinner);
        lista = new ArrayList<String>();
        spinner1 = (Spinner) this.findViewById(R.id.spinner);
        for (int i=0;i<l.size();i++)
            lista.add(l.get(i).getNombre());

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }



}
