package com.machine.hugo.barcodescanningapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;


public class MainActivity extends Activity implements OnClickListener {

    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";
    private Button Scaner;
    private Button nuevo_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obteniendo una instancia del boton show_pet_button
        Scaner = (Button)findViewById(R.id.scaner);
        nuevo_item = (Button) findViewById(R.id.nuevo_item);

        //Registrando la escucha sobre la actividad Main
        Scaner.setOnClickListener(this);
        nuevo_item.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.scaner: {
                Intent intent = new Intent(this, Scaner.class);
                intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
            case R.id.nuevo_item: {
                Uri webpage = Uri.parse("http://172.20.10.8:8000/noinventory/nuevoItem/");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

                // Verificar si hay aplicaciones disponibles
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(webIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                // Si hay, entonces ejecutamos la actividad
                if (isIntentSafe) {
                    startActivity(webIntent);
                }

                break;
            }
        }
    }



}