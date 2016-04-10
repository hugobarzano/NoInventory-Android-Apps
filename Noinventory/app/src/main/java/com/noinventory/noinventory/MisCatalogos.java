package com.noinventory.noinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MisCatalogos extends AppCompatActivity {

    // Atributos
    ListView listView;
    //adaptador con la lista de objetos item
    CatalogoAdapter adapter;
    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_catalogos);

        // Obtener instancia de la lista
        listView = (ListView) findViewById(R.id.listView);

        // Crear y setear adaptador
        adapter = new CatalogoAdapter(this, "username", datosUsuario.getNombre_usuario());
        listView.setAdapter(adapter);
        //Asocio el menu contextual a la vista de la lista
        registerForContextMenu(listView);

    }


    //CReacion del menu contextual para cada item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_catalogo, menu);
    }

    //Controlo del elemento dle menu selecionado
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.lector_qr:
                //AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
                //menu.setHeaderTitle(lstLista.getAdapter().getItem(info.position).toString());
                Catalogo c = this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", c.getNombre());
                Intent intent = new Intent(this, Scaner.class);
                intent.putExtra(ACTIVIDAD_SCANER, c.get_id());
                startActivity(intent);
                return true;
            case R.id.otro:
                // Tareas a realizar
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}