package com.noinventory.pruebass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private GsonRequest g;
    HttpURLConnection con = null;
    List comments;

    //JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Method.GET,URL_BASE + URL_JSON, null,null,null);
    private static final String URL_BASE = "http://servidorexterno.site90.com/datos";
    private static final String URL_JSON = "/social_media.json";


    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultado = (TextView) findViewById(R.id.resultado);
        URL url = null;
        try {
            url = new URL("http://192.168.1.100:8000/noinventory/addItemFromQr/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            // Construir los datos a enviar
            String data = "body=" + URLEncoder.encode("mimimimimi", "UTF-8");

            con = (HttpURLConnection) url.openConnection();

            // Activar método POST
            con.setDoOutput(true);

            // Tamaño previamente conocido
            con.setFixedLengthStreamingMode(data.getBytes().length);

            // Establecer application/x-www-form-urlencoded debido a la simplicidad de los datos
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }


    }
}

