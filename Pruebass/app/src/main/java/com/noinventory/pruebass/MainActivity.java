package com.noinventory.pruebass;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    // Atributos
    ListView listView;
    //ArrayAdapter adapter;
    ItemAdapter adapter;

    private NFCManager nfcMger;

    private View v;
    private NdefMessage message = null;
    private ProgressDialog dialog;
    Tag currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener instancia de la lista
        listView= (ListView) findViewById(R.id.listView);

        // Crear y setear adaptador
        adapter = new ItemAdapter(this);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        nfcMger = new NFCManager(this);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                //AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
                //menu.setHeaderTitle(lstLista.getAdapter().getItem(info.position).toString());
                Item i=this.adapter.getItemFromAdapter(info.position);
                Log.d("Nombre: ", i.getNombre());
                message =  nfcMger.createTextMessage(i.getNombre());
                if (message != null) {

                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Tag NFC Tag please");
                    dialog.show();;
                }

                return true;
            case R.id.delete:
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
        String position="hola";
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            Toast.makeText(MainActivity.this,
                    "Item in positn " + position + " clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ListView getListView() {
        return listView;
    }
    protected void onResume() {
        super.onResume();


        try {
            this.nfcMger.verifyNFC();
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[] {};
            String[][] techList = new String[][] { { android.nfc.tech.Ndef.class.getName() }, { android.nfc.tech.NdefFormatable.class.getName() } };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);

        } catch (NFCManager.NFCNotSupported nfcNotSupported) {
            nfcNotSupported.printStackTrace();
        } catch (NFCManager.NFCNotEnabled nfcNotEnabled) {
            nfcNotEnabled.printStackTrace();
        }
        //nfcMger.enableDispatch();




    }
    @Override
    protected void onPause() {
        super.onPause();
        nfcMger.disableDispatch();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("Nfc", "New intent");
        // It is the time to write the tag
        currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (message != null) {
            nfcMger.writeTag(currentTag, message);
            dialog.dismiss();
            Toast.makeText(MainActivity.this,
                    "Tag Whirten", Toast.LENGTH_LONG).show();

        }
        else {
            // Handle intent

        }
    }
}