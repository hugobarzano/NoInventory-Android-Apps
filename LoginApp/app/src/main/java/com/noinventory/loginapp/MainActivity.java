package com.noinventory.loginapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button boton;
    TextView etiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton= (Button) findViewById(R.id.boton);
        etiqueta= (TextView) findViewById(R.id.etiqueta);

        boton.setOnClickListener(this);
/*
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaEnviar tarea = new TareaEnviar("enviooooooo");
                tarea.execute();
            }
        });*/
    }


    void procesarRespuesta(String json){
        etiqueta.setText(json);
    }


    //Creamos una clse interna, que así tiene acceso a los métodos de la activity
    // (Ojo, no tocar nada más que en onPostExecute)
    public class TareaEnviar extends AsyncTask<String,Integer,String> {

        int error=0;
        String texto="";
        String envio="";

        public TareaEnviar(String envio) {
            this.envio = envio;
        }

        @Override
        protected void onPostExecute(String texto) {
            super.onPostExecute(texto);

            procesarRespuesta(texto);
        }

        @Override
        protected String doInBackground(String... params) {

            //String texto="";

            try {
                // Según: https://developer.android.com/reference/java/net/HttpURLConnection.html
                // Indicamos la URl a la que conextarse:
                URL url=new URL("http://192.168.1.100:8000/noinventory/androidLogin/");
                //URL url=new URL("\"http://192.168.1.100:8000/noinventory/androidLogin/\"");

                // Abrimos una conexión hacia esa URl, pero interpretado como una conex. HTTP
                HttpURLConnection conexionHttp= (HttpURLConnection) url.openConnection();
                // hay que decirle que vamos a enviar algo, par aque use POST:
                conexionHttp.setDoOutput(true);
                //escribir sin saber cuanto vas a escribir
                PrintWriter out=new PrintWriter(conexionHttp.getOutputStream());
                //leer respuesta
                BufferedReader in=new BufferedReader(new InputStreamReader(conexionHttp.getInputStream()));

                for(int i=0;i<10;i++)
                    out.print("cosas");

                out.println("cositassss");
                out.flush();

                // Leemos sólo una línea... debeíamos leer todas las líneas.
               // String lineaRespuesta=in.readLine();
                // leemos todos los tokens
               // String []tokens=lineaRespuesta.split(" ");

                if(conexionHttp.getResponseCode()==200){
                    error=0;
                    texto="Hecho!";
                } else {
                    error=1;
                    texto="Error!";
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return texto;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.boton: {
                TareaEnviar tarea = new TareaEnviar("enviooooooo");
                tarea.execute();
                //Intent intent = new Intent(this, Scaner.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                //System.out.print("Realizando post desde scanerrr");
                // AddItemFromQR tarea = new  AddItemFromQR("mimimimimi");
                //tarea.execute();
                //httpHandler manejadorPost=new httpHandler();
                //String aux= "mimimimimi";
                //manejadorPost.post("http://192.168.1.100:8000/noinventory/addItemFromQr/", aux);
                //manejadorPost.post2("http://192.168.1.100:8000/noinventory/addItemFromQr/");

              //  startActivity(intent);
                break;
            }

        }
    }
}
