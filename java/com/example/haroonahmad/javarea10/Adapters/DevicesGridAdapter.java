package com.example.haroonahmad.javarea10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.R;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 2/23/2017.
 */

public class DevicesGridAdapter extends BaseAdapter {

    ArrayList<Device> devices=new ArrayList<Device>();
    Context context;
    long room_id;
    private static LayoutInflater inflater=null;
    public DevicesGridAdapter(Context map, ArrayList<Device> devices) {
        this.devices=devices;
        context=map;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return devices.size();
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
        View view = convertView;

        view= inflater.inflate(R.layout.add_devices_cell, null);

        TextView text = (TextView) view.findViewById(R.id.CellDeviceName);
        text.setText(devices.get(position).getName());
        TextView text2 = (TextView) view.findViewById(R.id.CellDeviceType);
        //get name of the class
        text2.setText(devices.get(position).getClass().getName().substring(41));
        TextView text3 = (TextView) view.findViewById(R.id.CellDevicePort);
        text3.setText(((Integer)devices.get(position).getPortNum()).toString());


        return view;
    }
}

