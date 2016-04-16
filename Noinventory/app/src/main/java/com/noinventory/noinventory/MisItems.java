package com.noinventory.noinventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

public class MisItems extends ActionBarActivity {

    // Atributos
    ListView listView;
    //adaptador con la lista de objetos item
    ItemAdapter adapter;
    public final static String ACTIVIDAD_NFC = "com.machine.hugo.NFC";
    private String URL_BASE = "http://noinventory.cloudapp.net/noinventory";
    private static final String URL_JSON = "/itemsJson/";
    Context c;
    JSONObject respuesta;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_items);
       c=this.getBaseContext();
        // Obtener instancia de la lista
        listView= (ListView) findViewById(R.id.listView);

        // Crear y setear adaptador
        //adapter = new ItemAdapter(this,"username",datosUsuario.getNombre_usuario());
        gestorGlobal.setListaItemsUsuario(this);

        adapter = new ItemAdapter(this,gestorGlobal.getListaItemsUsuario());
        listView.setAdapter(adapter);
        //Asocio el menu contextual a la vista de la lista
        registerForContextMenu(listView);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Toast.makeText(MisItems.this,
                        "Item in position " + position + " clicked", Toast.LENGTH_LONG).show();
            }
        });*/
    }


    //CReacion del menu contextual para cada item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
    }
    //Controlo del elemento dle menu selecionado
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.nfc:
                //AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
                //menu.setHeaderTitle(lstLista.getAdapter().getItem(info.position).toString());
                Item i=this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", i.getNombre());
                Intent intent = new Intent(this, NFC_item_writer.class);
                intent.putExtra(ACTIVIDAD_NFC, i.get_id());
                startActivity(intent);


                return true;
            case R.id.otro:
                // Tareas a realizar
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}