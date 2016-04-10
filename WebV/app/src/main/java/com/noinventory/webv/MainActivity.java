package com.noinventory.webv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";
    private Button Scaner;
    private Button mis_items;
    private Button lista_items;
    private Button lista_inventarios;
    private Button escritor_nfc;
    private Button escritor_items_nfc;
    private Button log_out;


    private TextView username;
    private TextView organizacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        log_out = (Button) findViewById(R.id.nuevo);


        log_out.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.nuevo: {

                Intent intent = new Intent(this, Main2Activity.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
        }
    }
}
