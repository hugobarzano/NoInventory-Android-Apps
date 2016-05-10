package com.noinventory.noinventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemsOrganizacion extends ActionBarActivity {

    // Atributos
    ListView listView;
    ItemAdapter adapter;

    public final static String ACTIVIDAD_NFC = "com.machine.hugo.NFC";
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_items);
        c=this.getBaseContext();
        // Obtener instancia de la lista
        listView= (ListView) findViewById(R.id.listView);

        gestorGlobal.setListaItemsOrganizacion(this);

        adapter = new ItemAdapter(this,gestorGlobal.getListaItemsOrganizacion());
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.nfc:
                Item i=this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", i.getNombre());
                Intent intent = new Intent(this, NFC_item_writer.class);
                intent.putExtra(ACTIVIDAD_NFC, i.get_id());
                startActivity(intent);


                return true;
            case R.id.otro:
                // Tareas a realizar
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
        switch (item.getItemId()) {
            case R.id.update:
                //peticion
                String URL_BASE = "http://192.168.1.101:8000";
                String URL_JSON = "/itemsJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "False");



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





                Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}