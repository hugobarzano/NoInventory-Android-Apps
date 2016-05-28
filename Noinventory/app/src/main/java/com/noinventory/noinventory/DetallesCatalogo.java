package com.noinventory.noinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class DetallesCatalogo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_catalogo);
        Intent intent = getIntent();
        String catalogo = intent.getStringExtra(Catalogos.ACTIVIDAD_DETALLES_CATALOGO);

        WebView webView = (WebView) this.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("http://noinventory.cloudapp.net/catalogoAndroid/"+catalogo+"?organizacion="+datosUsuario.getOrganizacion());
        //webView.loadUrl("https://www.google.es/?gfe_rd=cr&ei=2U00V6HBIK2p8wfdsoW4BQ&gws_rd=ssl");

        webView.setWebViewClient(new WebViewClient());

    }
    @Override
    public void onBackPressed(){
        gestorGlobal.setListaCatalogosUsuario(this);
        CatalogoAdapter adapter = new CatalogoAdapter(this,gestorGlobal.getListaCatalogosUsuario());
        Catalogos.getListView().setAdapter(adapter);
        this.finish();

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
