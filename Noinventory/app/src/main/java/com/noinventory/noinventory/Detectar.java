package com.noinventory.noinventory;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Detectar extends AppCompatActivity implements View.OnClickListener {

    private TextView nombre;
    private TextView descripcion;
    private TextView fecha;
    private TextView localizador;

    private EditText busqueda;
    private Button buscar;
    private Button scaner;
    private Button detalles;
    Context c;
    Item item;
    IntentResult scanningResult;
    String scanContent;
    String URL_BASE = "http://192.168.1.34:8000";
    String URL_JSON = "/detectarItemJson/";
    private static final String TAG = "PostAdapterItem";
    static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG2 = "NfcTag";
    private NfcAdapter mNfcAdapter;
    public final static String ACTIVIDAD_DETALLES = "com.noinventory.noinventory.DETALLES";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectar);

        nombre = (TextView) findViewById(R.id.nombre);
        descripcion = (TextView) findViewById(R.id.descripcion);
        fecha = (TextView) findViewById(R.id.fecha);
        localizador=(TextView) findViewById(R.id.localizador);

        busqueda = (EditText) findViewById(R.id.busqueda);
        buscar = (Button) findViewById(R.id.buscar);
        scaner = (Button) findViewById(R.id.scaner);
        detalles = (Button) findViewById(R.id.detalles);
        detalles.setVisibility(View.INVISIBLE);

        buscar.setOnClickListener(this);
        scaner.setOnClickListener(this);
        detalles.setOnClickListener(this);
        c=this.getBaseContext();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (mNfcAdapter == null) {
            // Es necesario nfc
            Toast.makeText(this, "El dispositivo no soporta NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "Activa NFC para continuar.", Toast.LENGTH_LONG).show();

        }

        handleIntent(getIntent());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.detalles:{
                Intent intent = new Intent(this, DetallesItem.class);
                intent.putExtra(ACTIVIDAD_DETALLES, item.get_id());
                startActivity(intent);
                break;
            }
            case R.id.buscar: {
                String consulta = busqueda.getText().toString().trim();
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("consulta", consulta);
                params.put("flag", "True");

                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        // Obtener el array del objeto


                        try {
                            JSONObject objeto= response;

                            item = new Item(
                                    objeto.getString("_id"),
                                    objeto.getString("nombre"),
                                    objeto.getString("descripcion"),
                                    objeto.getString("localizador"),
                                    objeto.getString("fecha"));

                            nombre.setText(item.getNombre());
                            descripcion.setText((item.getDescripcion()));
                            fecha.setText(item.getFecha());
                            localizador.setText(item.getLocalizador());
                            detalles.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error de parsing: "+ e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Response: ", response.toString());
                    }
                });
                gestorPeticiones.setCola(c);
                gestorPeticiones.getCola().add(jsObjRequest);





                Toast.makeText(this, "Busqueda Realizada", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.scaner: {
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Map<String, String> params = new HashMap<String, String>();

            params.put("username", datosUsuario.getNombre_usuario());
            params.put("organizacion", datosUsuario.getOrganizacion());
            params.put("consulta", scanContent);
            params.put("flag", "False");

            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response: ", response.toString());
                    // Obtener el array del objeto


                    try {
                        JSONObject objeto= response;

                        item = new Item(
                                objeto.getString("_id"),
                                objeto.getString("nombre"),
                                objeto.getString("descripcion"),
                                objeto.getString("localizador"),
                                objeto.getString("fecha"));

                        nombre.setText(item.getNombre());
                        descripcion.setText((item.getDescripcion()));
                        fecha.setText(item.getFecha());
                        localizador.setText(item.getLocalizador());
                        detalles.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error de parsing: "+ e.getMessage());
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError response) {
                    Log.d("Response: ", response.toString());
                }
            });
            gestorPeticiones.setCola(c);
            gestorPeticiones.getCola().add(jsObjRequest);





            Toast.makeText(this, "Escaneo Realizado", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No hay datos tio!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Es necesario que la actividad se desarrolle en segundo plano o se producirá una excepción
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {

        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //Metodo llamado cuando se produce un nuevo inten, es decir, cuando el cliente acerca una tag al dispositivo
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("comprueba el mime type!");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    //El lector NFC se lleva acabo en una tarea asincrona. Las cuales se definen como una subclase privada
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF no es soportada por la tag
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Codificación no soportada", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {


            byte[] payload = record.getPayload();

            // Codificación
            String textEncoding;
            if ((payload[0] & 128) != 0) textEncoding = "UTF-16";
            else textEncoding = "UTF-8";

            // Lenguaje
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("consulta",result);
                params.put("flag", "False");

                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        // Obtener el array del objeto


                        try {
                            JSONObject objeto= response;

                            item = new Item(
                                    objeto.getString("_id"),
                                    objeto.getString("nombre"),
                                    objeto.getString("descripcion"),
                                    objeto.getString("localizador"),
                                    objeto.getString("fecha"));

                            nombre.setText(item.getNombre());
                            descripcion.setText((item.getDescripcion()));
                            fecha.setText(item.getFecha());
                            localizador.setText(item.getLocalizador());
                            detalles.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error de parsing: "+ e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Response: ", response.toString());
                    }
                });
                gestorPeticiones.setCola(c);
                gestorPeticiones.getCola().add(jsObjRequest);
                Toast toast = Toast.makeText(getApplicationContext(), "¡NFC Detectado! ", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                Toast.makeText(this, "updateeee", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
