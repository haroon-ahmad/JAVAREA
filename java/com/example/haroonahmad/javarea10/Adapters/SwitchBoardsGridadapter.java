package com.example.haroonahmad.javarea10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.haroonahmad.javarea10.HelperClasses.ImageHolder;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
import com.example.haroonahmad.javarea10.R;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 1/8/2017.
 */

public class SwitchBoardsGridadapter extends BaseAdapter {

    ArrayList<SwitchBoard> r=new ArrayList<SwitchBoard>();
    Context context;
    String room_id;
    private static LayoutInflater inflater=null;
    public SwitchBoardsGridadapter(Context map, ArrayList<SwitchBoard> R, String roomid) {
        r=R;
        context=map;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        room_id=roomid;
    }

    @Override
    public int getCount() {
        return r.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        // if (view == null) {
        //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        //view= inflater.inflate(R.layout.cell, null);
        //}
        if(convertView==null){
            LayoutInflater layoutInflater  = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.switchboard_cell,parent,false);
        }
        else
        {
            row = convertView;
        }
        TextView txt=(TextView) row.findViewById(R.id.SwitchboardName);
        txt.setText(r.get(position).getName());
        final int  pos=position;


        return row;
    }
}

