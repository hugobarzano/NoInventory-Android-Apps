package com.machine.hugo.barcodescanningapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scaner extends AppCompatActivity implements OnClickListener {

    private Button scanBtn;
    private TextView formatTxt, contentTxt,texto_enviado,estado_tarea;
    private Spinner spinner1;
    public List<String> lista;
    private HttpURLConnection con;
    private httpHandler manejadorPost;
    IntentResult scanningResult;
    String scanContent;
    String inventario_selecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        texto_enviado = (TextView)findViewById(R.id.texto_enviado);
        estado_tarea = (TextView)findViewById(R.id.estado_tarea);
        Intent intent = getIntent();
        String datos = intent.getStringExtra(MainActivity.ACTIVIDAD_SCANER);
        texto_enviado.setText(datos);
        scanBtn.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       // this.manejadorPost=new httpHandler();
       // this.manejadorPost.post("http://192.168.1.35:8000/noinventory/addItemFromQr", "mimimimimi");


        //DatosPorDefecto();

        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new ReadInventarios().
                        execute(
                                new URL("http://192.168.43.104:8000/noinventory/inventariosJson/"));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    public class ReadInventarios extends AsyncTask<URL, Void, List<Inventario>> {

        @Override
        protected List<Inventario> doInBackground(URL... urls) {
            List<Inventario> inventarios = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection) urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if (statusCode != 200) {
                    inventarios = new ArrayList<>();
                    inventarios.add(new Inventario("Error", "Error","Error"));

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                     JsonInventarioParser parser = new JsonInventarioParser();
                    //GsonInventarioParser parser = new GsonInventarioParser();
                    //inventarios = parser.leerFlujoJson(in);
                    inventarios = parser.readJsonStream(in);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return inventarios;
        }

        @Override
        protected void onPostExecute(List<Inventario> inventarios) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if (inventarios != null) {
                DatosPorDefecto(inventarios);

                //inventarios_desplegables = (Spinner) findViewById(R.id.inventarios_desplegables);
                //lista_inventarios = new ArrayList<String>();
                //inventarios_desplegables = (Spinner) this.par.findViewById(R.id.inventarios_desplegables);
                //for(int i=0;i<inventarios.size();i++){
                    //lista_inventarios.add(inventarios.get(i).getNombre());
               // }
                //lista_inventarios.add("Prueba");
                System.out.print("On executeeeee");

            } else {
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }
    void procesarRespuesta(String json){
        estado_tarea.setText(json);
    }

    public class AddItemFromQR extends AsyncTask<String,Integer,String> {

        int error=0;
        String texto="";

        public AddItemFromQR(String texto) {
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
                //URL url=new URL("\"http://192.168.1.115:8000/noinventory/addItemFromQr/\"");
                //URL url=new URL("\"http://192.168.1.115:8000/noinventory/addItemFromQr/\"");
                URL url=new URL("http://192.168.43.104:8000/noinventory/addItemFromQr/");
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
    }


    public void DatosPorDefecto(List <Inventario> l) {
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
                inventario_selecionado=arg0.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

        public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

                //modo chapuzaaa gitanada
                 httpHandler manejadorPost = new httpHandler();
                 String aux = scanContent;
                 String aux2 = inventario_selecionado;
                 manejadorPost.post3("http://192.168.43.104:8000/noinventory/addItemFromQr/",aux,aux2);
                //modo juanjo
                //String aux = scanContent;
                //AddItemFromQR tarea = new  AddItemFromQR(aux);
                //tarea.execute();
             }
            else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No hay datos tio!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
