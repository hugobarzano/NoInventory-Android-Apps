package com.noinventory.noinventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Catalogos extends AppCompatActivity {

    // Atributos
    ListView listView;
    //adaptador con la lista de objetos item
    CatalogoAdapter adapter;
    Context c;

    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";
    public final static String ACTIVIDAD_NFC = "com.machine.hugo.NFC";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogos);

        // Obtener instancia de la lista
        listView = (ListView) findViewById(R.id.listView);

        // Crear y setear adaptador
        adapter = new CatalogoAdapter(this,gestorGlobal.getListaCatalogosUsuario());
        listView.setAdapter(adapter);
        c=this.getBaseContext();
        //Asocio el menu contextual a la vista de la lista
        registerForContextMenu(listView);

    }


    //CReacion del menu contextual para cada item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_catalogo, menu);
    }

    //Controlo del elemento dle menu selecionado
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Catalogo c;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.lector_qr:
                //AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
                //menu.setHeaderTitle(lstLista.getAdapter().getItem(info.position).toString());
                c = this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", c.getNombre());
                intent = new Intent(this, Scaner.class);
                intent.putExtra(ACTIVIDAD_SCANER, c.get_id());
                startActivity(intent);
                return true;
            case R.id.lector_nfc:
                //AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
                //menu.setHeaderTitle(lstLista.getAdapter().getItem(info.position).toString());
                c = this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", c.getNombre());
                intent = new Intent(this, AddNFCItem.class);
                intent.putExtra(ACTIVIDAD_NFC, c.get_id());
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
                String URL_JSON = "/catalogosJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");



                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        adapter = new CatalogoAdapter(c,response);
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