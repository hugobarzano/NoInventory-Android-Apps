package com.machine.hugo.barcodescanningapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InventariosList extends AppCompatActivity {

    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventarios_list);

        lista= (ListView) findViewById(R.id.listaInventarios);
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
                                new URL("http://192.168.43.104:8000/noinventory/inventariosJson/"));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public class JsonTask extends AsyncTask<URL, Void, List<Inventario>> {

        @Override
        protected List<Inventario> doInBackground(URL... urls) {
            List<Inventario> inventarios = new ArrayList<>();

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    inventarios = new ArrayList<>();
                    inventarios.add(new Inventario("Error",null,null));

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonInventarioParser parser = new JsonInventarioParser();
                    //GsonItemParser parser = new GsonItemParser();
                    //items = parser.leerFlujoJson(in);
                    inventarios = parser.readJsonStream(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return inventarios;
        }


        protected void onPostExecute(List<Inventario> inventarios) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(inventarios!=null) {
                System.out.print("OnPOSTEXECUTEEEEEE");

                adaptador = new AdaptadorDeInventarios(getBaseContext(), inventarios);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

}
