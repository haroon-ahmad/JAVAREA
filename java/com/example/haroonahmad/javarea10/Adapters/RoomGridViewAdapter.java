package com.example.haroonahmad.javarea10.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haroonahmad.javarea10.HelperClasses.ImageHolder;
import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.R;
import com.example.haroonahmad.javarea10.RoomFragment;


import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 2/25/2017.
 */

public class RoomGridViewAdapter extends BaseAdapter {

    ArrayList<Room> allRooms;
    Context context;
     public  RoomGridViewAdapter(Context context, ArrayList<Room> arr){
        this.context=context;
        allRooms=arr;
    }
    @Override
    public int getCount() {
        return allRooms.size();
    }

    @Override
    public Object getItem(int position) {
        return allRooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        ImageHolder holder = null;
        if(row==null){
            LayoutInflater layoutInflater  = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_room,parent,false);
            holder = new ImageHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder=(ImageHolder) row.getTag();
        }
        Room room = allRooms.get(position);

        if (room.getType().equals("Bedroom")){
            holder.getImageView().setImageResource(R.drawable.bed);
        }
        else if (room.getType().equals("Washroom")) {
            holder.getImageView().setImageResource(R.drawable.washroom);
        }
        else if (room.getType().equals("Garage")) {
            holder.getImageView().setImageResource(R.drawable.car);
        }
        else if (room.getType().equals("Study")) {
            holder.getImageView().setImageResource(R.drawable.study);
        }
        else if (room.getType().equals("Kitchen")) {
            holder.getImageView().setImageResource(R.drawable.kitchen);
        }
        else if (room.getType().equals("Balcony")) {
            holder.getImageView().setImageResource(R.drawable.balcony);
        }
        else if (room.getType().equals("Roof")) {
            holder.getImageView().setImageResource(R.drawable.roof);
        }
        else if (room.getType().equals("Lounge")) {
            holder.getImageView().setImageResource(R.drawable.lounge);
        }
        else {
            holder.getImageView().setImageResource(R.drawable.bedroom);
        }

        holder.getTextView().setText(room.getName());
        return row;
    }

}



