package com.machine.hugo.barcodescanningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hugo on 28/03/16.
 */
public class AdaptadorDeInventarios extends ArrayAdapter {

    public AdaptadorDeInventarios(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View v = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            v = inflater.inflate(
                    R.layout.inventario_lista,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView _idInventario = (TextView)v.findViewById(R.id._idInventario);
        TextView nombreInventario = (TextView)v.findViewById(R.id.nombreInventario);
        TextView descInventario = (TextView)v.findViewById(R.id.descInventario);

        //Obteniendo instancia de la Tarea en la posición actual
        Inventario element = (Inventario) getItem(position);

        _idInventario.setText(element.get_id());
        nombreInventario.setText(element.getNombre());
        descInventario.setText(element.getDescripcion());
        //Devolver al ListView la fila creada
        return v;

    }


}

