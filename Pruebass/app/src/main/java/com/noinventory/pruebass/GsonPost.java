package com.noinventory.pruebass;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.transform.ErrorListener;

/**
 * Created by hugo on 6/04/16.
 */
public class GsonPost<T> extends Request<T> {
    // Atributos
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;

    /**
     * Se predefine para el uso de peticiones GET
     */
    public GsonPost(String url, Class<T> clazz, Map<String, String> params,
                    Response.Listener<T> listener, ErrorListener errorListener) {
        super(Method.GET, url, (Response.ErrorListener) errorListener);
        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
    }




    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };


    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}