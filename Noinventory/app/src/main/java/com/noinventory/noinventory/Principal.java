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



    private Button items;

    private Button catalogos;
    private Button log_out;


    private TextView username;
    private TextView organizacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Obteniendo una instancia del boton show_pet_button
       // Scaner = (Button) findViewById(R.id.scaner);


        items = (Button) findViewById(R.id.items);

        catalogos = (Button) findViewById(R.id.catalogos);

        log_out = (Button) findViewById(R.id.logoutButt);


        //Nombre de usuario y organizacion
        username = (TextView) findViewById(R.id.usernameText);
        organizacion = (TextView) findViewById(R.id.organizacionText);

        username.setText("Usuario: "+datosUsuario.getNombre_usuario());
        organizacion.setText("Organizacion: "+(datosUsuario.getOrganizacion()));


        //Registrando la escucha sobre la actividad Main
        //Scaner.setOnClickListener(this);


        items.setOnClickListener(this);

        catalogos.setOnClickListener(this);

        log_out.setOnClickListener(this);


        ////////////////////Pruebas///////////////////////7
        //gestorGlobal.setListaItemsUsuario(this);
        //gestorGlobal.setListaCatalogosUsuario(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.items: {
                Intent intent = new Intent(this, Items.class);
                startActivity(intent);
                break;
            }

            case R.id.catalogos: {
                Intent intent = new Intent(this, Catalogos.class);
                //intent.putExtra(ACTIVIDAD_SCANER, "DATOS ENVIADOS DESDE EL ACTIVITI MAIN");
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                Toast.makeText(this, "principal", Toast.LENGTH_SHORT).show();
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


