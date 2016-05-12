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


public class CatalogoAdapter extends ArrayAdapter {
    //private RequestQueue requestQueue;
    // Atributoshttp://noinventory.cloudapp.net/noinventory



    private static final String TAG = "PostAdaptercatalogo";
    List<Catalogo> catalogos;

    public CatalogoAdapter(Context context, JSONObject jsonObject) {
        super(context, 0);

        catalogos = parseJson(jsonObject);
        //Funcionaaaaaaaaaaaaaaaaaaaaaaaaaaaa///////////////////////
        //requestQueue = Volley.newRequestQueue(context);

    }


    public List<Catalogo> parseJson(JSONObject jsonObject){
        // Variables locales
        List<Catalogo> catalogos = new ArrayList();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("catalogos");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);

                    Catalogo catalogo = new Catalogo(
                            objeto.getString("_id"),
                            objeto.getString("nombre"),
                            objeto.getString("descripcion"),
                            objeto.getString("fecha"));


                    catalogos.add(catalogo);

                } catch (JSONException e) {
                    Log.e(TAG, "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return catalogos;
    }

    @Override
    public int getCount() {
        return catalogos != null ? catalogos.size() : 0;
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
                    R.layout.catalogo,
                    parent,
                    false);
        } else listItemView = convertView;


        // Obtener el item actual
        Catalogo catalogo = catalogos.get(position);
        //Item item = items.getItems().get(position);

        // Obtener Views
        TextView textoNombre = (TextView) listItemView.findViewById(R.id.nombre);
        TextView textoDescripcion = (TextView) listItemView.findViewById(R.id.descripcion);
        TextView textoFecha = (TextView) listItemView.findViewById(R.id.fecha);


        // Actualizar los Views
        textoNombre.setText(catalogo.getNombre());
        textoDescripcion.setText(catalogo.getDescripcion());
        textoFecha.setText(catalogo.getFecha());



        return listItemView;
    }
    //metodos para acceder a la lista de objetos items
    public List<Catalogo> getListaItems(){
        return catalogos;
    }
    public Catalogo getItemFromAdapter(int posicion){
        return catalogos.get(posicion);
    }
}