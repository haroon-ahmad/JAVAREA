package com.example.haroonahmad.javarea10;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.haroonahmad.javarea10.Adapters.*;
import com.example.haroonahmad.javarea10.HelperClasses.CommandClass;
import com.example.haroonahmad.javarea10.HelperClasses.FirebaseHelper;
import com.example.haroonahmad.javarea10.HelperClasses.ImageHolder;
import com.example.haroonahmad.javarea10.HelperClasses.LocalDB;
import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener  {


    GridView roomsGrid;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyRef;
    DatabaseReference RoomsRef;
    ArrayList<Room> arr;
    RoomGridViewAdapter adapter;
    ArrayList<String> boardId=new ArrayList<String>();
    ArrayList<String> deviceId=new ArrayList<>();
    View view;
    public final Context con = getContext();
    public RoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_room, container, false);

        FamilyRef=mRootRef.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        RoomsRef=FamilyRef.child("Rooms");
        roomsGrid = (GridView) view.findViewById(R.id.roomGridView);
        arr=new ArrayList<>();
        Log.d("test1",String.valueOf(arr.size()));
        RoomsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    //Log.i("MainActivity", child.getKey());

                    Room r=child.getValue(Room.class);
                    arr.add(r);
                }
                Log.d("test2",String.valueOf(arr.size()));
                adapter = new RoomGridViewAdapter(getActivity().getApplicationContext(), arr);

                roomsGrid.setAdapter(adapter);
                RoomsRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.addroomfloatingbutton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddRoom ff=new AddRoom();
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.relativefragment, ff, "A");
                transaction.addToBackStack("addA");
                transaction.commit();
            }
        });

        roomsGrid.setOnItemClickListener(this);
        roomsGrid.setOnItemLongClickListener(this);
        //CommandClass cc=((Home)getActivity()).getCommand("Mykitchen","Mydevice");
        //Log.d("Command",cc.getMacAddress()+cc.getPortNum());

        // Inflate the layout for this fragment
        return view;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Intent intent= new Intent(this,Room.class);
        //FirebaseHelper help=new FirebaseHelper(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //String mac=help.getMacAddress("Mybedroom","Aalo");
        //Toast.makeText(getActivity().getApplicationContext(),mac , Toast.LENGTH_SHORT).show();

        ImageHolder im= (ImageHolder) view.getTag();
        //;
        Bundle args = new Bundle();
        args.putString("RoomID", arr.get(position).getRoomID());

        SwitchBoards ff=new SwitchBoards();
        ff.setArguments(args);
        Home.devicetag = ff.getTag();
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.relativefragment,ff,"A");
        transaction.addToBackStack("addA");
        transaction.commit();

        // intent.putExtra("roomName", im.textView.getText().toString());
        //startActivity(intent);

    }
    public boolean onItemLongClick (AdapterView<?> parent, View view, final int position, long id){
        PopupMenu pop =  new PopupMenu(getActivity().getApplicationContext(),view);
        pop.getMenuInflater().inflate(R.menu.roommenu,pop.getMenu());

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.deleteRoom){

                    Toast.makeText(getContext(), "Room Should be Deleted", Toast.LENGTH_SHORT).show();
                    final DatabaseReference deleteRoom=RoomsRef.child(arr.get(position).getRoomID());
                    deleteRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                child.getRef().removeValue();
                            }
                            deleteRoom.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });
                    final DatabaseReference RoomBoards=FamilyRef.child("RoomSwitchBoards").child(arr.get(position).getRoomID());

                    RoomBoards.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                boardId.add(child.getKey());
                            }
                            dataSnapshot.getRef().removeValue();
                            RoomBoards.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });
                    final DatabaseReference switchboardDevices=FamilyRef.child("SwitchBoardDevices");
                    switchboardDevices.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                if(boardId.contains(child.getKey()))
                                {
                                    for(DataSnapshot child2: child.getChildren())
                                    {
                                        deviceId.add(child2.getValue().toString());
                                    }
                                    child.getRef().removeValue();
                                }
                            }
                            switchboardDevices.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });
                    final DatabaseReference SwitchBoards=FamilyRef.child("SwitchBoard");
                    SwitchBoards.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                if(boardId.contains(child.getKey()))
                                {
                                    child.getRef().removeValue();
                                }
                            }
                            SwitchBoards.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });
                    final DatabaseReference Devices=FamilyRef.child("Devices");
                    Devices.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                if(deviceId.contains(child.getKey()))
                                {
                                    child.getRef().removeValue();
                                }
                            }
                            Devices.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "onCancelled", databaseError.toException());
                        }
                    });
                }
                else if(item.getItemId()==R.id.configureRoom){

                    ConfigureRoom ff=new ConfigureRoom();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment,ff,"A");
                    transaction.addToBackStack("addA");
                    transaction.commit();

/*                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
                    Toast.makeText(getContext(), "Open Room Edit Fragment", Toast.LENGTH_SHORT).show();
  */              }
                else  if(item.getItemId()==R.id.addRoom){
                    AddRoom ff=new AddRoom();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment,ff,"A");
                    transaction.addToBackStack("addA");
                    transaction.commit();

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

