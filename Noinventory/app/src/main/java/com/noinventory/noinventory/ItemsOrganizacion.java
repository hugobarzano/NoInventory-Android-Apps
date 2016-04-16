package com.noinventory.noinventory;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

public class ItemsOrganizacion extends ActionBarActivity {

    // Atributos
    ListView listView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_items);

        // Obtener instancia de la lista
        listView= (ListView) findViewById(R.id.listView);

        gestorGlobal.setListaItemsOrganizacion(this);

        adapter = new ItemAdapter(this,gestorGlobal.getListaItemsOrganizacion());
        listView.setAdapter(adapter);

    }



}