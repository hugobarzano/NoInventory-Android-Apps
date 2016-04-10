package com.noinventory.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button login_btn;
    private TextView username, pass, resultado_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = (Button) findViewById(R.id.login_btn);
        username = (TextView) findViewById(R.id.username);
        pass = (TextView) findViewById(R.id.pass);
        resultado_login = (TextView) findViewById(R.id.resultado_logint);

        login_btn.setOnClickListener(this);
         if (android.os.Build.VERSION.SDK_INT > 9) {
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         StrictMode.setThreadPolicy(policy);
        }


    }



    void procesarRespuesta(String nombre) {
        this.resultado_login.setText("usuario conectado:" + nombre);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn: {
                String nombre = String.valueOf(this.username.getText());
                String pass = String.valueOf(this.pass.getText());
                MakeLogin login = new MakeLogin(nombre);
                login.execute();
                //httpHandler manejadorPost=new httpHandler();
                //String aux= "mimimimimi";
                //manejadorPost.post("http://192.168.1.100:8000/noinventory/androidLogin/", aux);
                //manejadorPost.post2("http://192.168.1.100:8000/noinventory/addItemFromQr/");

                //startActivity(intent);
                break;
            }

        }
    }

    public class MakeLogin extends AsyncTask<String,Integer,String> {

        int error=0;
        String texto="";

        public MakeLogin(String texto) {
            this.texto = texto;
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
                //URL url=new URL("\"http://hmkcode.appspot.com/jsonservlet\"");
                URL url=new URL("\"http://192.168.1.100:8000/noinventory/androidLogin/\"");
                //URL url=new URL("http://192.168.1.100:8000/noinventory/addItemFromQr/");
                // Abrimos una conexión hacia esa URl, pero interpretado como una conex. HTTP
                HttpURLConnection conexionHttp= (HttpURLConnection) url.openConnection();
                // hay que decirle que vamos a enviar algo, par aque use POST:
                conexionHttp.setDoOutput(true);
                //escribir sin saber cuanto vas a escribir
                PrintWriter out=new PrintWriter(conexionHttp.getOutputStream());
                //leer respuesta
                BufferedReader in=new BufferedReader(new InputStreamReader(conexionHttp.getInputStream()));

                //out.println("{ name: \"Juanjo es la caña\",Country:pais,Twitter:tweeter}");
                out.println(this.texto);
                out.flush();

                // Leemos sólo una línea... debeíamos leer todas las líneas.
                //String lineaRespuesta=in.readLine();
                // leemos todos los tokens
                //String []tokens=lineaRespuesta.split(" ");

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


        public void execute(URL url) {
        }
    }
}

