package com.noinventory.noinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetallesItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_item);
        Intent intent = getIntent();
        String item = intent.getStringExtra(Items.ACTIVIDAD_DETALLES);

        WebView webView = (WebView) this.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("http://noinventory.cloudapp.net:80/itemAndroid/"+item+"?organizacion="+datosUsuario.getOrganizacion());
        //webView.loadUrl("https://www.google.es/?gfe_rd=cr&ei=2U00V6HBIK2p8wfdsoW4BQ&gws_rd=ssl");

        webView.setWebViewClient(new WebViewClient());

        }

    @Override
    public void onBackPressed(){
        gestorGlobal.setListaItemsUsuario(this);
        setResult(RESULT_OK, null);
        this.finish();

    }



}
