package com.noinventory.noinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        webView.loadUrl("http://192.168.1.33:8000/catalogo/"+catalogo);
        //webView.loadUrl("https://www.google.es/?gfe_rd=cr&ei=2U00V6HBIK2p8wfdsoW4BQ&gws_rd=ssl");

        webView.setWebViewClient(new WebViewClient());

    }


}
