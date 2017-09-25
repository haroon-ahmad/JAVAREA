package com.example.haroonahmad.javarea10;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class ConfigureRoom extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener  {


    GridView switchboardGrid;
    public final Context con = getContext();
    public ConfigureRoom() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configure_room, container, false);
        switchboardGrid= (GridView) view.findViewById(R.id.switchboardGridView);
        switchboardGrid.setAdapter(new SwitchBoardGridViewAdapter(this));
        switchboardGrid.setOnItemClickListener(this);
        switchboardGrid.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PopupMenu pop =  new PopupMenu(getActivity().getApplicationContext(),view);
        pop.getMenuInflater().inflate(R.menu.switchboardmenu,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.deleteSwitchBoard){
                    Toast.makeText(getContext(), "SwitchBoard Should be Deleted", Toast.LENGTH_SHORT).show();

                }
                else if(item.getItemId()==R.id.configureSwitchBoard){
                    ConfigureSwitchBoard ff=new ConfigureSwitchBoard ();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment,ff,"A");
                    transaction.addToBackStack("addA");
                    transaction.commit();

//                    Toast.makeText(getContext(), "Open SwitchBoard Edit Fragment", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        pop.show();

        return true;
    }
}

class SingleSwitchBoard
{
    String swichboardName;
    SingleSwitchBoard(String _swicthboardName){

        this.swichboardName=_swicthboardName;

    }

}

class SwitchBoardHolder{
    TextView textView;
    SwitchBoardHolder(View v){
        textView = (TextView) v.findViewById(R.id.switchboardName);
    }
}
class SwitchBoardGridViewAdapter extends BaseAdapter
{

    ArrayList<SingleSwitchBoard> allSwitchBoards;
    ConfigureRoom context;
    SwitchBoardGridViewAdapter(ConfigureRoom context){
        this.context=context;
        allSwitchBoards=new ArrayList<SingleSwitchBoard>();
        Resources res = context.getResources();
        String [] SwitchBoardNames = res.getStringArray(R.array.switchboard_names);

        Log.d("aalu", " " + SwitchBoardNames.length);
//        Toast.makeText(this,SwitchBoardNames.length,Toast.LENGTH_SHORT).show();
        for (int i=0;i<SwitchBoardNames.length;i++){
            SingleSwitchBoard temp=new SingleSwitchBoard(SwitchBoardNames[i]);
            allSwitchBoards.add(temp);
        }

    }
    @Override
    public int getCount() {
        return allSwitchBoards.size();
    }

    @Override
    public Object getItem(int position) {
        return allSwitchBoards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        SwitchBoardHolder holder = null;
        if(row==null){
//            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            LayoutInflater layoutInflater  = (LayoutInflater) context.getActivity().getSystemService(context.getActivity().LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_switchboard,parent,false);
            holder = new SwitchBoardHolder(row);
            row.setTag(holder);

        }
        else
        {
            holder=(SwitchBoardHolder) row.getTag();
        }
        SingleSwitchBoard SwitchBoard = allSwitchBoards.get(position);

        holder.textView.setText(SwitchBoard.swichboardName);
        return row;
    }
}
