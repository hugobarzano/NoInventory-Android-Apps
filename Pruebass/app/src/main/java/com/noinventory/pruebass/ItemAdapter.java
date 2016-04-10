package com.noinventory.pruebass;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemAdapter extends ArrayAdapter {
    private RequestQueue requestQueue;
    // Atributos
    private String URL_BASE = "http://192.168.1.33:8000/noinventory";
    private static final String URL_JSON = "/itemsJson/";
    private static final String TAG = "PostAdapter";
    List<Item> items;

    public ItemAdapter(Context context) {
        super(context, 0);


        //Funcionaaaaaaaaaaaaaaaaaaaaaaaaaaaa///////////////////////
        requestQueue = Volley.newRequestQueue(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("flag","True");

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                items = parseJson(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        requestQueue.add(jsObjRequest);
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
                            objeto.getString("descripcion"));


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
        TextView textoId = (TextView) listItemView.findViewById(R.id.id_item);
        TextView textoNombre = (TextView) listItemView.findViewById(R.id.nombre);
        TextView textoDescripcion = (TextView) listItemView.findViewById(R.id.descripcion);

        // Actualizar los Views
        textoId.setText(item.get_id());
        textoNombre.setText(item.getNombre());
        textoDescripcion.setText(item.getDescripcion());



        return listItemView;
    }
    public List<Item> getListaItems(){
        return items;
    }
    public Item getItemFromAdapter(int posicion){
        return items.get(posicion);
    }
}