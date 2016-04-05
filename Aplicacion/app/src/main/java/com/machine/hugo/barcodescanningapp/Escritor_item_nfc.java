package com.machine.hugo.barcodescanningapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Escritor_item_nfc extends AppCompatActivity {
    private Button nfcButton;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escritor_item_nfc);

        nfcButton = (Button) findViewById(R.id.nfc_button);
        nfcButton.setOnClickListener((View.OnClickListener) this);
    }











}


