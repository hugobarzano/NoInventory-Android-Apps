package com.noinventory.noinventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class Items extends ActionBarActivity implements View.OnClickListener {

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
    static final String MIME_TEXT_PLAIN = "text/plain";

    //adaptador con la lista de objetos item
    ItemAdapter adapter;
    public final static String ACTIVIDAD_NFC = "com.noinventory.noinventory.NFC";
    public final static String ACTIVIDAD_DETALLES = "com.noinventory.noinventory.DETALLES";
    public final static int REQUEST_WEB_P = 2;
    public final static int REQUEST_WEB_C = 3;


    Context c;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        c=this.getBaseContext();
        // Obtener instancia de la lista
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