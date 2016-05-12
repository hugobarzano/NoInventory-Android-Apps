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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Items extends ActionBarActivity {

    // Atributos
    ListView listView;
    //adaptador con la lista de objetos item
    ItemAdapter adapter;
    public final static String ACTIVIDAD_NFC = "com.noinventory.noinventory.NFC";
    public final static String ACTIVIDAD_DETALLES = "com.noinventory.noinventory.DETALLES";

    Context c;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        c=this.getBaseContext();
        // Obtener instancia de la lista
        listView= (ListView) findViewById(R.id.listView);

        // Crear y setear adaptador
        //adapter = new ItemAdapter(this,"username",datosUsuario.getNombre_usuario());
        gestorGlobal.setListaItemsUsuario(this);
        adapter = new ItemAdapter(this,gestorGlobal.getListaItemsUsuario());
        listView.setAdapter(adapter);
        //Asocio el menu contextual a la vista de la lista
        registerForContextMenu(listView);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Toast.makeText(Items.this,
                        "Item in position " + position + " clicked", Toast.LENGTH_LONG).show();
            }
        });*/
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
                startActivity(intent);
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
                String URL_BASE = "http://192.168.1.34:8000";
                String URL_JSON = "/itemsJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");



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