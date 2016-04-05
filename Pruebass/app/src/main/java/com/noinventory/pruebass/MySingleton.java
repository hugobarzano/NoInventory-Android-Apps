package com.noinventory.pruebass;

/**
 * Created by hugo on 5/04/16.
 */
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public final class MySingleton {
    // Atributos
    private static MySingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context context) {
        MySingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new MySingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}