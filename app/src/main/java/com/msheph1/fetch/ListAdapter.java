package com.msheph1.fetch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ArrayList<String>> {
    private ArrayList<ArrayList<String>> info;
    public ListAdapter(Context context, ArrayList<ArrayList<String>> dataArrayList)
    {
        super(context,R.layout.list_item, dataArrayList);
        this.info = dataArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ArrayList<String> arr = getItem(position);
        Log.i("MainActivity", arr.toString());

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView listId = convertView.findViewById(R.id.ListId);
        TextView name = convertView.findViewById(R.id.Name);
        TextView id = convertView.findViewById(R.id.ID);
        listId.setText(arr.get(0));
        name.setText(arr.get(1));
        id.setText(arr.get(2));

        return convertView;
    }
}
