package com.noinventory.noinventory;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hugo on 12/04/16.
 */
public class gestorGlobal {

    private static JSONObject listaItemsUsuario;
    private static JSONObject listaCatalogosUsuario;



    public static JSONObject getListaItemsUsuario() {
        return listaItemsUsuario;
    }

    public static void setListaItemsUsuario(Context c) {
        String URL_BASE = "http://192.168.1.101:8000";
        String URL_JSON = "/itemsJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("username", datosUsuario.getNombre_usuario());
        params.put("organizacion", datosUsuario.getOrganizacion());
        params.put("flag", "True");



        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                gestorGlobal.listaItemsUsuario = response;
                //adapter = new ItemAdapter(c,response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
       // RequestQueue requestQueue;
        //requestQueue.add(jsObjRequest);
        gestorPeticiones.setCola(c);
        gestorPeticiones.getCola().add(jsObjRequest);


    }





    public static JSONObject getListaCatalogosUsuario() {
        return listaCatalogosUsuario;
    }

    public static void setListaCatalogosUsuario(Context c) {
        String URL_BASE = "http://192.168.1.101:8000";
        String URL_JSON = "/catalogosJson/";


        Map<String, String> params = new HashMap<String, String>();
        params.put("username", datosUsuario.getNombre_usuario());
        params.put("organizacion", datosUsuario.getOrganizacion());

        params.put("flag", "True");



        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respuesta catalogo: ", response.toString());
                gestorGlobal.listaCatalogosUsuario = response;

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        gestorPeticiones.setCola(c);
        gestorPeticiones.getCola().add(jsObjRequest);


    }




}
