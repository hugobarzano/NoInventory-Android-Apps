package com.noinventory.noinventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NuevoItem extends AppCompatActivity {
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_item);
        c=this.getBaseContext();
        WebView webView = (WebView) this.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("http://noinventory.cloudapp.net/nuevoItemAndroid/?organizacion="+ datosUsuario.getOrganizacion()+"&usuario="+datosUsuario.getNombre_usuario());
        webView.setWebViewClient(new WebViewClient());

    }
    @Override
    public void onBackPressed(){
        String URL_BASE = "http://noinventory.cloudapp.net";
        String URL_JSON = "/itemsJson/";
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
                ItemAdapter adapter = new ItemAdapter(c,response);
                Items.getListView().setAdapter(adapter);
                //Asocio el menu contextual a la vista de la lista
                // registerForContextMenu(listView);

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
        this.finish();
        //gestorGlobal.setListaItemsUsuario(this);
        // ItemAdapter adapter = new ItemAdapter(this,gestorGlobal.getListaItemsUsuario());
        // Items.getListView().setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                Toast.makeText(this, "detalles", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
