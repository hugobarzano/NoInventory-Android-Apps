package com.noinventory.itemslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hugo on 21/03/16.
 */
public class AdaptadorDeItems extends ArrayAdapter {

    public AdaptadorDeItems(Context context, List objects) {
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
                    R.layout.item_lista,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView _idItem = (TextView)v.findViewById(R.id._idItem);
        TextView nombreItem = (TextView)v.findViewById(R.id.nombreItem);
        TextView descItem = (TextView)v.findViewById(R.id.descItem);

        //Obteniendo instancia de la Tarea en la posición actual
        Item element = (Item) getItem(position);

        _idItem.setText(element.get_id());
        nombreItem.setText(element.getNombre());
        descItem.setText(element.getDescripcion());
        //Devolver al ListView la fila creada
        return v;

    }


}

