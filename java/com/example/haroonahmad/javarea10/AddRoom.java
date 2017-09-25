package com.example.haroonahmad.javarea10;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRoom extends Fragment implements AdapterView.OnItemClickListener{

    GridView roomstypesGrid;
    public AddRoom() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_room, container, false);

        roomstypesGrid= (GridView) view.findViewById(R.id.gridView2);
        roomstypesGrid.setAdapter(new RoomAGridViewAdapter(this));
        roomstypesGrid.setOnItemClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle args = new Bundle();

        if(position==0)
        {
            args.putString("Type", "Bedroom");
        }
        else if(position==1)
        {
            args.putString("Type","Washroom");
        }
        else if(position==2)
        {
            args.putString("Type","Garage");
        }
        else if(position==3)
        {
            args.putString("Type","Study");
        }
        else if(position==4)
        {
            args.putString("Type","Kitchen");
        }

        else  if(position==5)
        {
            args.putString("Type","Balcony");
        }
        else if(position==6)
        {
            args.putString("Type","Roof");
        }
        else if(position==7)
        {
            args.putString("Type","Lounge");
        }



        AddRoom2 ff=new AddRoom2();
        ff.setArguments(args);
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.relativefragment,ff,"A");
        transaction.addToBackStack("addA");
        transaction.commit();

    }
}
class SingleARoom
{
    int imageId;
    String roomName;
    SingleARoom(int _imageId,String _roomName){
        this.imageId = _imageId;
        this.roomName=_roomName;

    }

}

class ImageAHolder{
    ImageView imageView;
    TextView textView;
    ImageAHolder(View v){
        imageView = (ImageView) v.findViewById(R.id.roomaIcon);
        textView = (TextView) v.findViewById(R.id.roomaName);
    }
}
class RoomAGridViewAdapter extends BaseAdapter
{

    ArrayList<SingleARoom> allRooms;
    AddRoom context;
    RoomAGridViewAdapter(AddRoom context){
        this.context=context;
        allRooms=new ArrayList<SingleARoom>();
        Resources res = context.getResources();
        String [] roomtypes = res.getStringArray(R.array.room_types);
        int[]images= {R.drawable.bed,R.drawable.washroom,R.drawable.car,R.drawable.study,R.drawable.kitchen,R.drawable.balcony,R.drawable.roof,R.drawable.lounge};
       //Toast.makeText(con,images.length,Toast.LENGTH_SHORT).show();
        for (int i=0;i<images.length;i++){
            SingleARoom temp=new SingleARoom(images[i],roomtypes[i]);
            allRooms.add(temp);
        }

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
        ImageAHolder holder = null;
        if(row==null){
//            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            LayoutInflater layoutInflater  = (LayoutInflater) context.getActivity().getSystemService(context.getActivity().LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_aroom,parent,false);
            holder = new ImageAHolder(row);
            row.setTag(holder);

        }
        else
        {
            holder=(ImageAHolder) row.getTag();
        }
        SingleARoom room = allRooms.get(position);

        holder.imageView.setImageResource(room.imageId);
        holder.textView.setText(room.roomName);
        return row;
    }
}
