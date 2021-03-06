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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Items extends ActionBarActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // Atributos

    public static ListView getListView() {
        return listView;
    }

    public static void setListView(ListView listView) {
        Items.listView = listView;
    }

    public static ListView listView;
    private EditText busqueda;
    private Button buscar;
    IntentResult scanningResult;
    private NfcAdapter mNfcAdapter;
    static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcTag";

    //adaptador con la lista de objetos item
    ItemAdapter adapter;
    public final static String ACTIVIDAD_NFC = "com.noinventory.noinventory.NFC";
    public final static String ACTIVIDAD_DETALLES = "com.noinventory.noinventory.DETALLES";
    public final static int REQUEST_WEB_P = 2;
    public final static int REQUEST_WEB_C = 3;
    private SwipeRefreshLayout swipeRefreshLayout;



    Context c;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        c=this.getBaseContext();
        // Obtener instancia de la lista
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        listView= (ListView) findViewById(R.id.listView);
        busqueda= (EditText) findViewById(R.id.busqueda);
        buscar= (Button) findViewById(R.id.buscar);

        // Crear y setear adaptador
        //adapter = new ItemAdapter(this,"username",datosUsuario.getNombre_usuario());
        String URL_BASE = "http://noinventory.cloudapp.net";
        String URL_JSON = "/itemsJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("username", datosUsuario.getNombre_usuario());
        params.put("organizacion", datosUsuario.getOrganizacion());
        params.put("flag", "True");
        params.put("busqueda", "");


        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                //actualizar lista
                adapter = new ItemAdapter(c, response);
                listView.setAdapter(adapter);
                //Asocio el menu contextual a la vista de la lista
                registerForContextMenu(listView);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        gestorPeticiones.setCola(c);
        gestorPeticiones.getCola().add(jsObjRequest);
        Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();

        //gestorGlobal.setListaItemsUsuario(this);
       // adapter = new ItemAdapter(this,gestorGlobal.getListaItemsUsuario());
        //listView.setAdapter(adapter);
        //Asocio el menu contextual a la vista de la lista
        registerForContextMenu(listView);
        buscar.setOnClickListener(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (mNfcAdapter == null) {
            // Es necesario nfc
            Toast.makeText(this, "El dispositivo no soporta NFC.", Toast.LENGTH_LONG).show();
            finish();
            //return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "Activa NFC para continuar.", Toast.LENGTH_LONG).show();

        }

        handleIntent(getIntent());
        swipeRefreshLayout.setRefreshing(false);


    }





    //CReacion del menu contextual para cada item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);

    }
    //Controlo del elemento dle menu selecionado
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Item i;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nfc:
                //AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
                //menu.setHeaderTitle(lstLista.getAdapter().getItem(info.position).toString());
                i=this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", i.getNombre());
                intent = new Intent(this, NFC_item_writer.class);
                intent.putExtra(ACTIVIDAD_NFC, i.getLocalizador());
                startActivity(intent);
                return true;
            case R.id.detalles:
                i=this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", i.getNombre());
                intent = new Intent(this, DetallesItem.class);
                intent.putExtra(ACTIVIDAD_DETALLES, i.get_id());
                startActivityForResult(intent, REQUEST_WEB_P);
                //startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nuevo:
                Intent intent = new Intent(this, NuevoItem.class);
                this.startActivity(intent);
                break;
            case R.id.scaner:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            case R.id.update:
                String URL_BASE = "http://noinventory.cloudapp.net";
                String URL_JSON = "/itemsJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");
                params.put("busqueda", "");


                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        adapter = new ItemAdapter(c, response);
                        listView.setAdapter(adapter);
                        //Asocio el menu contextual a la vista de la lista
                        registerForContextMenu(listView);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Response: ", response.toString());
                    }
                });
                gestorPeticiones.setCola(c);
                gestorPeticiones.getCola().add(jsObjRequest);
                Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                String URL_BASE =  "http://noinventory.cloudapp.net";
                String URL_JSON = "/itemsJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");
                params.put("busqueda", scanContent);


                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        adapter = new ItemAdapter(c, response);
                        listView.setAdapter(adapter);
                        //Asocio el menu contextual a la vista de la lista
                        registerForContextMenu(listView);

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

            } else {
                System.out.print("no hay datos");
               // Toast toast = Toast.makeText(getApplicationContext(),"No hay datos tio!", Toast.LENGTH_SHORT);
               // toast.show();
            }

    }


    @Override
    protected void onResume() {
        super.onResume();

        //Es necesario que la actividad se desarrolle en segundo plano o se producirá una excepción
        setupForegroundDispatch(this, mNfcAdapter);
        String URL_BASE = "http://noinventory.cloudapp.net";
        String URL_JSON = "/itemsJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("username", datosUsuario.getNombre_usuario());
        params.put("organizacion", datosUsuario.getOrganizacion());
        params.put("flag", "True");
        params.put("busqueda", "");


        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                //actualizar lista
                adapter = new ItemAdapter(c, response);
                listView.setAdapter(adapter);
                //Asocio el menu contextual a la vista de la lista
                registerForContextMenu(listView);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        gestorPeticiones.setCola(c);
        gestorPeticiones.getCola().add(jsObjRequest);
        Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        String URL_BASE = "http://noinventory.cloudapp.net";
        String URL_JSON = "/itemsJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("username", datosUsuario.getNombre_usuario());
        params.put("organizacion", datosUsuario.getOrganizacion());
        params.put("flag", "True");
        params.put("busqueda", "");


        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                //actualizar lista
                adapter = new ItemAdapter(c, response);
                listView.setAdapter(adapter);
                //Asocio el menu contextual a la vista de la lista
                registerForContextMenu(listView);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        gestorPeticiones.setCola(c);
        gestorPeticiones.getCola().add(jsObjRequest);
        Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);

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
                String URL_BASE =  "http://noinventory.cloudapp.net";
                String URL_JSON = "/itemsJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");
                params.put("busqueda", result);



                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        adapter = new ItemAdapter(c,response);
                        listView.setAdapter(adapter);
                        //Asocio el menu contextual a la vista de la lista
                        registerForContextMenu(listView);

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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buscar: {
                //peticion
                String URL_BASE =  "http://noinventory.cloudapp.net";
                String URL_JSON = "/itemsJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");
                params.put("busqueda",busqueda.getText().toString());



                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        adapter = new ItemAdapter(c,response);
                        listView.setAdapter(adapter);
                        //Asocio el menu contextual a la vista de la lista
                        registerForContextMenu(listView);

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
        }
    }
}