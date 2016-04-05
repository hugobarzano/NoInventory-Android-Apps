package com.noinventory.itemslist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener {

    public final static String ACTIVIDAD_LISTA_ITEM = "com.noinventory.itemslist";
    public final static String ACTIVIDAD_LISTA_ITEM2 = "com.noinventory.itemslist2";

    private Button lista_items;
    private Button lista_items2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obteniendo una instancia del boton show_pet_button
        lista_items = (Button) findViewById(R.id.lista_item);
        lista_items2 = (Button) findViewById(R.id.lista_item2);

        //Registrando la escucha sobre la actividad Main
        lista_items.setOnClickListener(this);
        lista_items2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lista_item: {
                Intent intent = new Intent(this, ItemLista.class);
                intent.putExtra(ACTIVIDAD_LISTA_ITEM, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
            case R.id.lista_item2: {
                Intent intent = new Intent(this, ItemLista2.class);
                intent.putExtra(ACTIVIDAD_LISTA_ITEM2, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }

        }
    }



}