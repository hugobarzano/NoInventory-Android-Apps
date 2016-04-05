package com.machine.hugo.barcodescanningapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scaner extends AppCompatActivity implements OnClickListener {

    private Button scanBtn;
    private TextView formatTxt, contentTxt,texto_enviado;
    private Spinner inventarios_desplegables;
    public List<String> lista_inventarios;
    private HttpURLConnection con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        texto_enviado = (TextView)findViewById(R.id.texto_enviado);
        Intent intent = getIntent();
        String datos = intent.getStringExtra(MainActivity.ACTIVIDAD_SCANER);
        texto_enviado.setText(datos);
        scanBtn.setOnClickListener(this);
        //DatosPorDefecto();

        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new ReadInventarios().
                        execute(
                                new URL("http://172.20.10.8:8000/noinventory/inventariosJson/"));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private void DatosPorDefecto() {
        inventarios_desplegables = (Spinner) findViewById(R.id.inventarios_desplegables);
        lista_inventarios = new ArrayList<String>();
        inventarios_desplegables = (Spinner) this.findViewById(R.id.inventarios_desplegables);
        //if(lista_inventarios.size()==0)
            lista_inventarios.add("Vacio");

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_inventarios);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inventarios_desplegables.setAdapter(adaptador);
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
                    inventarios.add(new Inventario("Error", "Error"));

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                     JsonInventarioParser parser = new JsonInventarioParser();
                    //GsonInventarioParser parser = new GsonInventarioParser();
                    //inventarios = parser.leerFlujoJson(in);
                    inventarios = parser.readJsonStream(in);
                    System.out.print("Inventarioss\n");
                    for(int i=0;i<inventarios.size();i++)
                        System.out.print(inventarios.get(i).getNombre());
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

        public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: "+ scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No hay datos tio!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
