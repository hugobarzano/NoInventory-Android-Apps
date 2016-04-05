package com.noinventory.login;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 4/04/16.
 */
public class httpHandler {

    public void post(String posturl, String contenido_scaneo){
        try {

            HttpClient httpclient = new DefaultHttpClient();
            //Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http y HttpPost que nos permite enviar peticiones POST a una url determinada
            HttpPost httppost = new HttpPost(posturl);

            //El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("contenido_scaneo", contenido_scaneo));

            httppost.setEntity(new UrlEncodedFormEntity(params));
            //Enviamos la info al server
            httpclient.execute(httppost);

        }

        catch(Exception e) {
            e.printStackTrace();
            System.out.println("No funciona");
        }

    }

    public void post2(String posturl){
        try {

            HttpClient httpclient = new DefaultHttpClient();
            //Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http y HttpPost que nos permite enviar peticiones POST a una url determinada
            HttpPost httppost = new HttpPost(posturl);

            //El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada
            //AÑADIR PARAMETROS
            // List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("contenido_scaneo", contenido_scaneo));

            //httppost.setEntity(new UrlEncodedFormEntity(params));
            //Enviamos la info al server
            httpclient.execute(httppost);

        }

        catch(Exception e) {
            e.printStackTrace();
            System.out.println("No funciona");
        }

    }

    public void post3(String posturl, String contenido_scaneo, String inventario){
        try {

            HttpClient httpclient = new DefaultHttpClient();
            //Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http y HttpPost que nos permite enviar peticiones POST a una url determinada
            HttpPost httppost = new HttpPost(posturl);

            //El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("contenido_scaneo", contenido_scaneo));
            params.add(new BasicNameValuePair("inventario", inventario));

            httppost.setEntity(new UrlEncodedFormEntity(params));
            //Enviamos la info al server
            httpclient.execute(httppost);

        }

        catch(Exception e) {
            e.printStackTrace();
            System.out.println("No funciona");
        }

    }

}

