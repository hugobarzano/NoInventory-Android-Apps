package com.noinventory.noinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Principal extends AppCompatActivity implements OnClickListener {

    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";
    private Button Scaner;

    private Button mis_items;
    private Button items_organizacion;

    private Button mis_catalogos;
    private Button lista_inventarios;
    private Button escritor_nfc;
    private Button escritor_items_nfc;
    private Button log_out;


    private TextView username;
    private TextView organizacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Obteniendo una instancia del boton show_pet_button
       // Scaner = (Button) findViewById(R.id.scaner);

        mis_items = (Button) findViewById(R.id.mis_items);
        items_organizacion = (Button) findViewById(R.id.items_organizacion);

        mis_catalogos = (Button) findViewById(R.id.mis_catalogos);
        lista_inventarios = (Button) findViewById(R.id.lista_inventarios);
        escritor_nfc = (Button) findViewById(R.id.escritor_nfc);
        escritor_items_nfc = (Button) findViewById(R.id.escritor_items_nfc);
        log_out = (Button) findViewById(R.id.logoutButt);


        //Nombre de usuario y organizacion
        username = (TextView) findViewById(R.id.usernameText);
        organizacion = (TextView) findViewById(R.id.organizacionText);

        username.setText("Usuario: "+datosUsuario.getNombre_usuario());
        organizacion.setText("Organizacion: "+(datosUsuario.getOrganizacion()));


        //Registrando la escucha sobre la actividad Main
        //Scaner.setOnClickListener(this);

        mis_items.setOnClickListener(this);
        items_organizacion.setOnClickListener(this);

        mis_catalogos.setOnClickListener(this);
        lista_inventarios.setOnClickListener(this);
        escritor_nfc.setOnClickListener(this);
        escritor_items_nfc.setOnClickListener(this);
        log_out.setOnClickListener(this);


        ////////////////////Pruebas///////////////////////7
        gestorGlobal.setListaItemsUsuario(this);
        gestorGlobal.setListaItemsOrganizacion(this);
        gestorGlobal.setListaCatalogosUsuario(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.mis_items: {
                Intent intent = new Intent(this, MisItems.class);
                startActivity(intent);
                break;
            }
            case R.id.items_organizacion: {
                Intent intent = new Intent(this, ItemsOrganizacion.class);
                startActivity(intent);
                break;
            }
            case R.id.mis_catalogos: {
                Intent intent = new Intent(this, MisCatalogos.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
            case R.id.lista_inventarios: {
                Intent intent = new Intent(this, MisItems.class);
                startActivity(intent);
                break;
            }
            case R.id.escritor_nfc: {
                //Intent intent = new Intent(this, Escritor_nfc.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                //startActivity(intent);
                break;
            }
            case R.id.escritor_items_nfc: {
                //Intent intent = new Intent(this, Escritor_nfc.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                //startActivity(intent);
                break;
            }
            case R.id.logoutButt: {
                SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", "");
                editor.putString("organizacion", "");
                editor.commit();
                Intent intent = new Intent(this, Login.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
                break;
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bookmark_menu:
                Toast.makeText(this, "You have selected Bookmark Menu", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    //Menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Elemento del menu de opciones selecionado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String position="hola";
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            Toast.makeText(Principal.this,
                    "Item in positn " + position + " clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}


