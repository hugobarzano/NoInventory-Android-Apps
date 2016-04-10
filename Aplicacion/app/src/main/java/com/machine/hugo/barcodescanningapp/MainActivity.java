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
    private Button lista_items;
    private Button lista_inventarios;
    private Button escritor_nfc;
    private Button escritor_items_nfc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obteniendo una instancia del boton show_pet_button
        Scaner = (Button)findViewById(R.id.scaner);
        nuevo_item = (Button) findViewById(R.id.nuevo_item);
        lista_items = (Button) findViewById(R.id.lista_items);
        lista_inventarios = (Button) findViewById(R.id.lista_inventarios);
        escritor_nfc = (Button) findViewById(R.id.escritor_nfc);
        escritor_items_nfc = (Button) findViewById(R.id.escritor_items_nfc);

       // if (android.os.Build.VERSION.SDK_INT > 9) {
         //   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
           // StrictMode.setThreadPolicy(policy);
        //}

        //Registrando la escucha sobre la actividad Main
        Scaner.setOnClickListener(this);
        nuevo_item.setOnClickListener(this);
        lista_items.setOnClickListener(this);
        lista_inventarios.setOnClickListener(this);
        escritor_nfc.setOnClickListener(this);
        escritor_items_nfc.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.scaner: {
                Intent intent = new Intent(this, Scaner.class);
                intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                System.out.print("Realizando post desde scanerrr");
                //String aux = "mimimimi";
                //AddItemFromQR tarea = new  AddItemFromQR(aux);
                //tarea.execute();
               // AddItemFromQR tarea = new  AddItemFromQR("mimimimimi");
                //tarea.execute();
                //httpHandler manejadorPost=new httpHandler();
                //String aux= "mimimimimi";
                //manejadorPost.post("http://192.168.1.100:8000/noinventory/addItemFromQr/", aux);
                //manejadorPost.post2("http://192.168.1.100:8000/noinventory/addItemFromQr/");

                startActivity(intent);
                break;
            }
            case R.id.nuevo_item: {
                Uri webpage = Uri.parse("http://192.168.43.104:8000/noinventory/nuevoItem/?organizacion=osl");
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
            case R.id.lista_items: {
                Intent intent = new Intent(this, ItemList.class);
                intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
            case R.id.lista_inventarios: {
                Intent intent = new Intent(this, InventariosList.class);
                intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
            case R.id.escritor_nfc: {
                Intent intent = new Intent(this, Escritor_nfc.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
            case R.id.escritor_items_nfc: {
                Intent intent = new Intent(this, Escritor_nfc.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
        }
    }



}