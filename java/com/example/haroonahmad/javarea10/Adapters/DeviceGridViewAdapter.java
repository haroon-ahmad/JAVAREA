package com.example.haroonahmad.javarea10.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.haroonahmad.javarea10.DeviceFragment;
import com.example.haroonahmad.javarea10.HelperClasses.DeviceHolder;
import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.R;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 2/26/2017.
 */

public class DeviceGridViewAdapter extends BaseAdapter {
    ArrayList<Device> allDevices;
    Context context;
    public DeviceGridViewAdapter(Context context, ArrayList<Device> arr){
        this.context=context;
        allDevices=arr;
        Resources res = context.getResources();


    }
    @Override
    public int getCount() {
        return allDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return allDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        DeviceHolder holder = null;
        if(row==null){
            LayoutInflater layoutInflater  = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_device,parent,false);
            holder = new DeviceHolder(row);
            row.setTag(holder);

        }
        else
        {
            holder=(DeviceHolder) row.getTag();
        }
        Device device = allDevices.get(position);
        if (device.isState()){
            holder.getImageView().setImageResource(R.drawable.light_on_small);
        }
        else {
            holder.getImageView().setImageResource(R.drawable.light_off_small);
        }

        holder.getTextView().setText(device.getName());
        return row;
    }
}
