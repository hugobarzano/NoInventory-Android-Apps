package com.noinventory.noinventory;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NFC_item_writer extends AppCompatActivity {

    private NFCManager nfcMger;

    private View v;
    private NdefMessage message = null;
    private ProgressDialog dialog;
    Tag currentTag;
    private TextView a_escribir;


    private RequestQueue requestQueue;
    String item;
    // Atributos
    private String URL_BASE = "http://192.168.1.33:8000/noinventory";
    private static final String URL_JSON = "/itemJson/";
    private static final String TAG = "PostNFCItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_item_writer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nfcMger = new NFCManager(this);
        Intent intent = getIntent();
        String datos = intent.getStringExtra(MisItems.ACTIVIDAD_NFC);
        item=datos;

        requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("item_id", datos);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                item = response.toString();
                v = findViewById(R.id.mainLyt);
                a_escribir= (TextView) findViewById(R.id.a_escribir);
                a_escribir.setText(item);

                message =  nfcMger.createTextMessage(response.toString());



                if (message != null) {

                    dialog = new ProgressDialog(NFC_item_writer.this);
                    dialog.setMessage("Tag NFC Tag please");
                    dialog.show();;
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        requestQueue.add(jsObjRequest);


        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onResume() {
        super.onResume();

        try {
            nfcMger.verifyNFC();
            //nfcMger.enableDispatch();

            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[] {};
            String[][] techList = new String[][] { { android.nfc.tech.Ndef.class.getName() }, { android.nfc.tech.NdefFormatable.class.getName() } };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
        }
        catch(NFCManager.NFCNotSupported nfcnsup) {
            Snackbar.make(v, "NFC not supported", Snackbar.LENGTH_LONG).show();
        }
        catch(NFCManager.NFCNotEnabled nfcnEn) {
            Snackbar.make(v, "NFC Not enabled", Snackbar.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        nfcMger.disableDispatch();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("Nfc", "New intent");
        // It is the time to write the tag
        currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (message != null) {
            Log.d("mensaje: ", String.valueOf(message));
            nfcMger.writeTag(currentTag, message);
            dialog.dismiss();
            Snackbar.make(v, "Tag written", Snackbar.LENGTH_LONG).show();

        }
        else {
            // Handle intent

        }
    }


}
