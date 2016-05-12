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
        webView.loadUrl("http://192.168.1.101:8000/item/"+item);
        //webView.loadUrl("https://www.google.es/?gfe_rd=cr&ei=2U00V6HBIK2p8wfdsoW4BQ&gws_rd=ssl");

        webView.setWebViewClient(new WebViewClient());

        }


    }
