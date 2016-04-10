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


public class CatalogoAdapter extends ArrayAdapter {
    private RequestQueue requestQueue;
    // Atributos
    private String URL_BASE = "http://192.168.1.33:8000/noinventory";
    private static final String URL_JSON = "/catalogosJson/";
    private static final String TAG = "PostAdaptercatalogo";
    List<Catalogo> catalogos;

    public CatalogoAdapter(Context context, String clave, String valor) {
        super(context, 0);


        //Funcionaaaaaaaaaaaaaaaaaaaaaaaaaaaa///////////////////////
        requestQueue = Volley.newRequestQueue(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put(clave, valor);
        if(clave.equals("username"))
            params.put("flag", "True");
        else
            params.put("flag", "False");



        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respuesta catalogo: ", response.toString());
                catalogos = parseJson(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        requestQueue.add(jsObjRequest);
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
                            objeto.getString("descripcion"));


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
        TextView textoId = (TextView) listItemView.findViewById(R.id.id_catalogo);
        TextView textoNombre = (TextView) listItemView.findViewById(R.id.nombre);
        TextView textoDescripcion = (TextView) listItemView.findViewById(R.id.descripcion);

        // Actualizar los Views
        textoId.setText(catalogo.get_id());
        textoNombre.setText(catalogo.getNombre());
        textoDescripcion.setText(catalogo.getDescripcion());



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