package com.example.haroonahmad.javarea10.SetupActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.haroonahmad.javarea10.Adapters.RoomGridViewAdapter;
import com.example.haroonahmad.javarea10.Home;
import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.R;
import com.example.haroonahmad.javarea10.RoomFragment;
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
public class setup_home extends Fragment {

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyRef;
    DatabaseReference RoomsRef;
    ArrayList<Room> arr=new ArrayList<>();
    RoomGridViewAdapter adapter;
    View view;

    public setup_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_setup_home, container, false);
        Button button=(Button) view.findViewById(R.id.setupfinish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getActivity(),Home.class);
                startActivity(in);
            }
        });
        Button button2=(Button) view.findViewById(R.id.setupaddroom);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoomDetails ff=new addRoomDetails();
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.setupfragmentlayout,ff,"A");
                transaction.addToBackStack("addA");
                transaction.commit();
            }
        });
        FamilyRef=mRootRef.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        RoomsRef=FamilyRef.child("Rooms");
        RoomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    //Log.i("MainActivity", child.getKey());
                    Room r=child.getValue(Room.class);
                    arr.add(r);
                }
                adapter = new RoomGridViewAdapter(getActivity().getApplicationContext(), arr);
                GridView grid = (GridView) view.findViewById(R.id.SetupRoomGrid);
                grid.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
