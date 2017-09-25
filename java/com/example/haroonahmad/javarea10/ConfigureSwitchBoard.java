package com.example.haroonahmad.javarea10;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigureSwitchBoard extends Fragment implements   AdapterView.OnItemLongClickListener {

    GridView devicesGrid;
    public ConfigureSwitchBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configure_switch_board, container, false);

        devicesGrid= (GridView) view.findViewById(R.id.switchboarddevicedGridview);
        devicesGrid.setAdapter(new DeviceSwitchBoardGridViewAdapter(this));

        devicesGrid.setOnItemLongClickListener(this);

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PopupMenu pop =  new PopupMenu(getActivity().getApplicationContext(),view);
        pop.getMenuInflater().inflate(R.menu.devicemenu, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.deleteDevice){
                    Toast.makeText(getContext(), "Device Should be Deleted", Toast.LENGTH_SHORT).show();

                }
                else if(item.getItemId()==R.id.editDevice){
                    Toast.makeText(getContext(), "Open Device Edit Fragment", Toast.LENGTH_SHORT).show();
                }
                else if(item.getItemId()==R.id.addnewboard){
                    AddDevice ff=new AddDevice();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment,ff,"A");
                    transaction.addToBackStack("addA");
                    transaction.commit();
                    Toast.makeText(getContext(), "Add device", Toast.LENGTH_SHORT).show();
                }

                // arr.remove(position);
                //   GridView lv = (GridView) findViewById(R.id.gridView);
                // lv.setAdapter(adapter);
                // adapter.notifyDataSetChanged();

                return false;
            }
        });
        pop.show();

        return true;
    }
}
class SingleSwitchBoardDevice
{
    int imageId;
    String deviceName;
    SingleSwitchBoardDevice(int _imageId,String _deviceName){
        this.imageId = _imageId;
        this.deviceName=_deviceName;

    }

}

class DeviceSwitchBoardHolder{
    ImageView imageView;
    TextView textView;
    DeviceSwitchBoardHolder(View v){
        imageView = (ImageView) v.findViewById(R.id.deviceSwitchBoardIcon);
        textView = (TextView) v.findViewById(R.id.deviceSwitchBoardName);
    }
}


class DeviceSwitchBoardGridViewAdapter extends BaseAdapter
{

    ArrayList<SingleSwitchBoardDevice> allSwitchBoardDevices;
    ConfigureSwitchBoard context;
    DeviceSwitchBoardGridViewAdapter(ConfigureSwitchBoard context){
        this.context=context;
        allSwitchBoardDevices=new ArrayList<SingleSwitchBoardDevice>();
        Resources res = context.getResources();
        String [] deviceSwitchBoardNames = res.getStringArray(R.array.deviceswitchboard_names);
        int[]SwitchBoardimages= {R.drawable.device,R.drawable.device,R.drawable.device};
        //Log.d("check", (String) deviceNames.length);
//       Toast.makeText(context,deviceNames.length,Toast.LENGTH_SHORT).show();
        for (int i=0;i<SwitchBoardimages.length;i++){
            SingleSwitchBoardDevice temp=new SingleSwitchBoardDevice(SwitchBoardimages[i],deviceSwitchBoardNames[i]);
            allSwitchBoardDevices.add(temp);
        }

    }
    @Override
    public int getCount() {
        return allSwitchBoardDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return allSwitchBoardDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        DeviceSwitchBoardHolder holder = null;
        if(row==null){
            LayoutInflater layoutInflater  = (LayoutInflater) context.getActivity().getSystemService(context.getActivity().LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_switchboarddevice,parent,false);
            holder = new DeviceSwitchBoardHolder(row);
            row.setTag(holder);

        }
        else
        {
            holder=(DeviceSwitchBoardHolder) row.getTag();
        }
        SingleSwitchBoardDevice device = allSwitchBoardDevices.get(position);
        holder.imageView.setImageResource(device.imageId);
        holder.textView.setText(device.deviceName);
        return row;
    }
}