package com.noinventory.noinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Scaner extends AppCompatActivity implements View.OnClickListener {

    private Button scanBtn;
    private TextView formatTxt, contentTxt;

    IntentResult scanningResult;
    String scanContent;
    String inventario_selecionado;


    private RequestQueue requestQueue;
    String item;
    // Atributos
    private String URL_BASE = "http://192.168.1.33:8000/noinventory";
    private static final String URL_JSON = "/addItemFromQr/";
    private static final String TAG = "PostQRtoCatalogo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        Intent intent = getIntent();
        inventario_selecionado = intent.getStringExtra(MisCatalogos.ACTIVIDAD_SCANER);
        scanBtn.setOnClickListener(this);


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

            requestQueue = Volley.newRequestQueue(this);
            Map<String, String> params = new HashMap<String, String>();
            params.put("scaner", scanContent);
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response: ", response.toString());
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError response) {
                    Log.d("Response: ", response.toString());
                }
            });
            requestQueue.add(jsObjRequest);

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No hay datos tio!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
