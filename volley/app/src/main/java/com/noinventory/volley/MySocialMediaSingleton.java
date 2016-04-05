package com.noinventory.volley;

/**
 * Created by hugo on 5/04/16.
 */
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public final class MySocialMediaSingleton {
    // Atributos
    private static MySocialMediaSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private MySocialMediaSingleton(Context context) {
        MySocialMediaSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySocialMediaSingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new MySocialMediaSingleton(context);
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
