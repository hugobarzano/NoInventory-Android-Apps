package com.noinventory.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by hugo on 20/03/16.
 */
public class PostAdapter extends ArrayAdapter {

    // Atributos
    private RequestQueue requestQueue;
    //JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Method.GET,URL_BASE + URL_JSON, null,null,null);
    private static final String URL_BASE = "http://servidorexterno.site90.com/datos";
    private static final String URL_JSON = "/social_media.json";
    private static final String TAG = "PostAdapter";
    ListaPost items;

    public PostAdapter(Context context) {
        super(context,0);

        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(context);

        MySocialMediaSingleton.getInstance(getContext()).addToRequestQueue(
                new GsonRequest<ListaPost>(
                        URL_BASE + URL_JSON,
                        ListaPost.class,
                        null,
                        new Response.Listener<ListaPost>(){
                            @Override
                            public void onResponse(ListaPost response) {
                                items = response;
                                notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley:"+ error.getMessage());
                            }
                        }
                )

        );
        // Añadir petición a la cola
        //requestQueue.add(jsObjRequest);
    }



    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
       // return 6;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Referencia del view procesado
        View listItemView;

        //Comprobando si el View no existe
        listItemView = null == convertView ? layoutInflater.inflate(
                R.layout.inventario,
                parent,
                false) : convertView;


        // Obtener el item actual
        Post item = items.get(0);
       // Post item= new Post("titulo", "String descripcion", "String imagen");

        // Obtener Views
        TextView textoTitulo = (TextView) listItemView.
                findViewById(R.id.textoTitulo);
        TextView textoDescripcion = (TextView) listItemView.
                findViewById(R.id.textoDescripcion);
        final ImageView imagenPost = (ImageView) listItemView.
                findViewById(R.id.imagenPost);

        // Actualizar los Views
        textoTitulo.setText(item.getTitulo());
        textoDescripcion.setText(item.getDescripcion());

        // Petición para obtener la imagen


        // Añadir petición a la cola
        ImageRequest request = new ImageRequest(
                URL_BASE + item.getImagen(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imagenPost.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imagenPost.setImageResource(R.drawable.error);
                        Log.d(TAG, "Error en respuesta Bitmap: "+ error.getMessage());
                    }
                });

        // Añadir petición a la cola
        requestQueue.add(request);


        return listItemView;
    }






}