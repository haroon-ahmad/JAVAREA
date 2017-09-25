package com.example.haroonahmad.javarea10;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.haroonahmad.javarea10.Adapters.DeviceGridViewAdapter;
import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.SetupActivity.addDevices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment implements  AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    GridView devicesGrid;
    String RoomID;
    String BoardID;
    String MacAddress;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyRef;
    DatabaseReference BoardSwicthesRef;
    DatabaseReference DevicesRef;
    DatabaseReference BoardIDRef;
    ArrayList<String> deviceids=new ArrayList<>();
    ArrayList<Device> Devices=new ArrayList<>();
    DeviceGridViewAdapter adapter;

    public DeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        BoardID=getArguments().getString("SwitchBoardID");
        RoomID=getArguments().getString("RoomID");
        MacAddress=getArguments().getString("MacAddress");
        FamilyRef=mRootRef.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DevicesRef=FamilyRef.child("Devices");
        BoardSwicthesRef=FamilyRef.child("SwitchBoardDevices");
        BoardIDRef=BoardSwicthesRef.child(BoardID);
        devicesGrid= (GridView) view.findViewById(R.id.deviceGridView);
        adapter=new DeviceGridViewAdapter(getActivity().getApplicationContext(),Devices);
        devicesGrid.setAdapter(adapter);

        Button button=(Button) view.findViewById(R.id.addNewDevice);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addDevices ff=new addDevices();
                Bundle args = new Bundle();
                args.putString("RoomID", RoomID);
                args.putString("SwitchBoardID",BoardID);
                args.putString("Flag","Devices");
                ff.setArguments(args);
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
               // manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.relativefragment, ff, "A");
                transaction.addToBackStack("addA");
                transaction.commit();
            }
        });
        BoardIDRef.keepSynced(true);
        DevicesRef.keepSynced(true);
        BoardIDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deviceids.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    deviceids.add(child.getValue().toString());
                }

               // BoardIDRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DevicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Devices.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if(deviceids.contains(child.getKey()))
                    {
                        Device d=child.getValue(Device.class);
                        Devices.add(d);
                        adapter.notifyDataSetChanged();
                    }
                }

                // DevicesRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        devicesGrid.setOnItemClickListener(this);
        devicesGrid.setOnItemLongClickListener(this);

        return view;
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DatabaseReference commandRef=FamilyRef.child("Command");
        commandRef.child("MacAddress").setValue(MacAddress);
        commandRef.child("Portnum").setValue(Devices.get(position).getPortNum());
        int command;
        if(Devices.get(position).isState())
        {
            command=0;
        }
        else
        {
            command=1;
        }
        commandRef.child("State").setValue(command);
        commandRef.child("DeviceID").setValue(Devices.get(position).getDeviceID());
        Toast.makeText(getContext(), "Device State Should Change", Toast.LENGTH_SHORT).show();

    }
    public boolean onItemLongClick (AdapterView<?> parent, View view, final int position, long id){
        PopupMenu pop =  new PopupMenu(getActivity().getApplicationContext(),view);
        pop.getMenuInflater().inflate(R.menu.devicemenu, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.deleteDevice){
                    Toast.makeText(getContext(), "Device Should be Deleted", Toast.LENGTH_SHORT).show();
                    final DatabaseReference deleteDevice=DevicesRef.child(Devices.get(position).getDeviceID());
                    deleteDevice.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                child.getRef().removeValue();
                            }
                            deleteDevice.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });

                    BoardIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                if(child.getValue().equals(Devices.get(position).getDeviceID()))
                                {
                                    child.getRef().removeValue();
                                    Devices.remove(position);
                                    adapter.notifyDataSetChanged();

                                }
                            }
                            BoardIDRef.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });

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
