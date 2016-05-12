package com.noinventory.noinventory;

/**
 * Created by hugo on 6/04/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends ArrayAdapter {
    //private RequestQueue requestQueue;
    // Atributos

    private static final String TAG = "PostAdapterItem";
    List<Item> items;


    public ItemAdapter(Context context, JSONObject jsonObject) {
        super(context, 0);

        items = parseJson(jsonObject);
        //Funcionaaaaaaaaaaaaaaaaaaaaaaaaaaaa///////////////////////
       // requestQueue = Volley.newRequestQueue(context);

    }




    public List<Item> parseJson(JSONObject jsonObject){
        // Variables locales
        List<Item> items = new ArrayList();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("items");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);

                    Item item = new Item(
                            objeto.getString("_id"),
                            objeto.getString("nombre"),
                            objeto.getString("descripcion"),
                            objeto.getString("localizador"),
                            objeto.getString("fecha"));


                    items.add(item);

                } catch (JSONException e) {
                    Log.e(TAG, "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return items;
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // View auxiliar
        View listItemView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            listItemView = layoutInflater.inflate(
                    R.layout.item,
                    parent,
                    false);
        } else listItemView = convertView;


        // Obtener el item actual
        Item item = items.get(position);
        //Item item = items.getItems().get(position);

        // Obtener Views
        TextView textoNombre = (TextView) listItemView.findViewById(R.id.nombre);
        TextView textoFecha = (TextView) listItemView.findViewById(R.id.fecha);
        TextView textoLocalizador = (TextView) listItemView.findViewById(R.id.localizador);

        // Actualizar los Views
        textoNombre.setText(item.getNombre());
        textoFecha.setText(item.getFecha());
        textoLocalizador.setText(item.getLocalizador());



        return listItemView;
    }
    //metodos para acceder a la lista de objetos items
    public List<Item> getListaItems(){
        return items;
    }
    public Item getItemFromAdapter(int posicion){
        return items.get(posicion);
    }
}