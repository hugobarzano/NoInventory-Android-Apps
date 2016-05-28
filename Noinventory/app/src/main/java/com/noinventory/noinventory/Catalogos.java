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

public class Catalogos extends AppCompatActivity implements View.OnClickListener {

    public static ListView getListView() {
        return listView;
    }

    public static void setListView(ListView listView) {
        Catalogos.listView = listView;
    }

    // Atributos
    public static ListView listView;
    private EditText busqueda;
    private Button buscar;
    //adaptador con la lista de objetos item
    CatalogoAdapter adapter;
    Context c;

    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";
    public final static String ACTIVIDAD_NFC = "com.machine.hugo.NFC";
    public final static String ACTIVIDAD_DETALLES_CATALOGO = "com.noinventory.noinventory.DETALLES_CATALOGO";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogos);

        // Obtener instancia de la lista
        busqueda = (EditText) findViewById(R.id.busqueda);
        buscar = (Button) findViewById(R.id.buscar);
        listView = (ListView) findViewById(R.id.listView);
        c=this.getBaseContext();


        String URL_BASE =  "http://noinventory.cloudapp.net";
        String URL_JSON = "/catalogosJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("username", datosUsuario.getNombre_usuario());
        params.put("organizacion", datosUsuario.getOrganizacion());
        params.put("flag", "True");
        params.put("busqueda","");


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

        // Crear y setear adaptador
        //adapter = new CatalogoAdapter(this,gestorGlobal.getListaCatalogosUsuario());
        //listView.setAdapter(adapter);
        buscar.setOnClickListener(this);

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
            case R.id.detalles:
                c=this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", c.getNombre());
                intent = new Intent(this, DetallesCatalogo.class);
                intent.putExtra(ACTIVIDAD_DETALLES_CATALOGO, c.get_id());
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nuevo:
                Intent intent = new Intent(this, NuevoCatalogo.class);
                this.startActivity(intent);
                return true;
            case R.id.update:
                //peticion
                String URL_BASE =  "http://noinventory.cloudapp.net";
                String URL_JSON = "/catalogosJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", datosUsuario.getNombre_usuario());
                params.put("organizacion", datosUsuario.getOrganizacion());
                params.put("flag", "True");
                params.put("busqueda","");


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buscar: {
                String URL_BASE = "http://noinventory.cloudapp.net";
                String URL_JSON = "/catalogosJson/";
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





                Toast.makeText(this, "Busqueda Realizada", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}