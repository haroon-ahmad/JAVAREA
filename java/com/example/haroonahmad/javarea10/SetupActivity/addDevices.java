package com.example.haroonahmad.javarea10.SetupActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.haroonahmad.javarea10.Adapters.DevicesGridAdapter;
import com.example.haroonahmad.javarea10.DeviceFragment;
import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class addDevices extends Fragment {

    //Firebase database references
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyDataRef=mRootRef.child("FamilyData");

    ArrayList<Device> devices=new ArrayList<>();
    DevicesGridAdapter adapter;
    public addDevices() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_add_devices, container, false);
        final String RoomID = getArguments().getString("RoomID");
        final String SwitchBoardID = getArguments().getString("SwitchBoardID");
        GridView devicesGrid=(GridView) view.findViewById(R.id.DevicesGridView);
        adapter = new DevicesGridAdapter(getActivity().getApplicationContext(), devices);
        devicesGrid.setAdapter(adapter);
        Button button=(Button) view.findViewById(R.id.AddDevice);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Spinner sp=(Spinner) view.findViewById(R.id.devicetype);
                String type=sp.getSelectedItem().toString();
                EditText et=(EditText) view.findViewById(R.id.devicename);
                String name=et.getText().toString();
                //Manaal
        //        if(!name.isEmpty())

                //Manaal
                EditText et2=(EditText) view.findViewById(R.id.deviceport);
                String port=et2.getText().toString();
                Device d=Device.factorymethod(type);
                d.setName(name);
                d.setPortNum(Integer.parseInt(port));

                d.setDeviceID(FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Devices").push().getKey());
                FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Devices").child(d.getDeviceID()).setValue(d);
                FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SwitchBoardDevices").child(SwitchBoardID).child(d.getName()).setValue(d.getDeviceID());
                devices.add(d);
                adapter.notifyDataSetChanged();


            }
        });
        //Done button
        Button btn=(Button) view.findViewById(R.id.DevicesAdded);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle args = new Bundle();
                args.putString("RoomID", RoomID);
                args.putString("SwitchBoardID",SwitchBoardID);
                if(getArguments().getString("Flag").equals("main"))
                {
                    args.putString("Flag","main");
                    allswitchboards ff = new allswitchboards();

                    ff.setArguments(args);
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment, ff, "A");
                    transaction.addToBackStack("addA");
                    transaction.commit();
                }
                else if(getArguments().getString("Flag").equals("Devices"))
                {
                    DeviceFragment ff = new DeviceFragment();

                    ff.setArguments(args);
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment, ff, "A");
                    transaction.addToBackStack("addA");
                    transaction.commit();
                }
                else {
                    args.putString("Flag","notMain");
                    allswitchboards ff = new allswitchboards();

                    ff.setArguments(args);
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.setupfragmentlayout, ff, "A");
                    transaction.addToBackStack("addA");
                    transaction.commit();
                }
            }
        });
        return view;
    }

}
