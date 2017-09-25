package com.example.haroonahmad.javarea10;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.SetupActivity.addRoomDetails;
import com.example.haroonahmad.javarea10.SetupActivity.add_Switchboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRoom2 extends Fragment {

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyDataRef=mRootRef.child("FamilyData");
    String type;
    String name;
    EditText et;
    public AddRoom2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_add_room2, container, false);
        type=getArguments().getString("Type");
        et=(EditText) view.findViewById(R.id.RoomNameEt);

        Button button= (Button) view.findViewById(R.id.SaveRoom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=et.getText().toString();
                Room room=new Room();
                if(type!=null && name!=null) {
                    room.setName(name);
                    room.setType(type);
                    room.setRoomID(FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rooms").push().getKey());
                    FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rooms").child(room.getRoomID()).setValue(room);

                }
                Bundle args=new Bundle();
                args.putString("RoomID",room.getRoomID());
                args.putString("Flag","main");
                add_Switchboard ff=new add_Switchboard();
                ff.setArguments(args);
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.relativefragment,ff,"A");
                transaction.addToBackStack("addA");
                transaction.commit();
            }
        });

       return view;
    }

}