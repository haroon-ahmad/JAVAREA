package com.example.haroonahmad.javarea10.SetupActivity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haroonahmad.javarea10.AddDevice;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
import com.example.haroonahmad.javarea10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class add_Switchboard extends Fragment {


    private BluetoothAdapter BA;
    //Views
    ListView lv;
    //Firebase objects
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyDataRef=mRootRef.child("FamilyData");

    ArrayList<BluetoothDevice> devicelist=new ArrayList<>();
    ArrayList<String> list=new ArrayList<>();
    SwitchBoard board=new SwitchBoard();
    //Adapters
    ArrayAdapter<String> adapter;
    public add_Switchboard() {
        // Required empty public constructor
        BA=BluetoothAdapter.getDefaultAdapter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=inflater.inflate(R.layout.fragment_add__switchboard, container, false);

        final String RoomID = getArguments().getString("RoomID");
        //Register bluetooth receiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        //start bluetooth search button
        Button button = (Button) view.findViewById(R.id.serachforbluetoothdevices);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (BA == null) {
                    Toast.makeText(getContext(),"Bluetooth not availabe",Toast.LENGTH_LONG).show();
                }
                else {
                    if (!BA.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        getActivity().startActivityForResult(enableBtIntent, 1);
                    }
                    BA.startDiscovery();
                }
            }

        });
        Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();


        for(BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName()+ "\n" + bt.getAddress());
            devicelist.add(bt);
        }
        //List view
        lv = (ListView)view.findViewById(R.id.deviceslist);
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.bluetoothrow,R.id.textView6, list);
        lv.setAdapter(adapter);
        //List view on click listner
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                pairDevice(devicelist.get(position));

                board.setBluetoothName(devicelist.get(position).getName());
                board.setMACAddress(devicelist.get(position).getAddress());

                LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
                View promptsView = li.inflate(R.layout.popupwindow, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        board.setName(userInput.getText().toString());

                                        board.setSwitchboardID(FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SwitchBoard").push().getKey());
                                        FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SwitchBoard").child(board.getSwitchboardID()).setValue(board);
                                        FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("RoomSwitchBoards").child(RoomID).child(board.getSwitchboardID()).setValue(board.getMACAddress());

                                        Bundle args = new Bundle();
                                        args.putString("RoomID", RoomID);
                                        args.putString("SwitchBoardID",board.getSwitchboardID());
                                        if(getArguments().getString("Flag").equals("main"))
                                        {
                                            args.putString("Flag","main");
                                            addDevices ff=new addDevices();
                                            ff.setArguments(args);
                                            android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.replace(R.id.relativefragment,ff,"A");
                                            transaction.addToBackStack("addA");
                                            transaction.commit();
                                        }
                                        else
                                        {
                                            args.putString("Flag","notMain");
                                            addDevices ff=new addDevices();
                                            ff.setArguments(args);
                                            android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.replace(R.id.setupfragmentlayout,ff,"A");
                                            transaction.addToBackStack("addA");
                                            transaction.commit();
                                        }



                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        return view;
    }

    /*Add Switchboards functions*/
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                list.add(device.getName() + "\n" + device.getAddress());
                devicelist.add(device);
                //Toast.makeText(ConfigureRoom.this,device.getName(),Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }
        }
    };
}
