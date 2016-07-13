package com.noinventory.noinventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NuevoCatalogo extends AppCompatActivity {

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
        webView.loadUrl("http://noinventory.cloudapp.net/nuevoCatalogoAndroid/?organizacion=" + datosUsuario.getOrganizacion() + "&usuario=" + datosUsuario.getNombre_usuario());
        webView.setWebViewClient(new WebViewClient());

    }
    /*@Override
    public void onBackPressed(){
        String URL_BASE =  "http://noinventory.cloudapp.net";
        String URL_JSON = "/catalogosJson/";
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
                CatalogoAdapter adapter = new CatalogoAdapter(c,response);
                Catalogos.getListView().setAdapter(adapter);


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

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }*/

}
