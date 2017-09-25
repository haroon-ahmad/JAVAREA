package com.example.haroonahmad.javarea10.SetupActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.haroonahmad.javarea10.Adapters.SwitchBoardsGridadapter;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
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
public class allswitchboards extends Fragment {

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyRef;
    DatabaseReference BoardsRef;
    DatabaseReference RoomBoardsRef;
    DatabaseReference RoomIDRef;
    ArrayList<SwitchBoard> boards=new ArrayList<>();
    SwitchBoardsGridadapter adapter;
    ArrayList<String > boardids=new ArrayList<>();
    View view;
    public allswitchboards() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_allswitchboards, container, false);
        final String RoomID = getArguments().getString("RoomID");
        Button button=(Button) view.findViewById(R.id.addnewboard);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                add_Switchboard ff=new add_Switchboard();
                Bundle args = new Bundle();
                args.putString("RoomID", RoomID);
                args.putString("Flag","notMain");
                ff.setArguments(args);
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.setupfragmentlayout,ff,"A");
                transaction.addToBackStack("addA");
                transaction.commit();
            }
        });
        Button button2 =(Button) view.findViewById(R.id.SwitchboardFinish);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(getArguments().getString("Flag").equals("main"))
                {
                    RoomFragment ff=new RoomFragment();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.relativefragment,ff,"A");
                    transaction.addToBackStack("addA");
                    transaction.commit();
                }
                else {
                    setup_home ff = new setup_home();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.setupfragmentlayout, ff, "A");
                    transaction.addToBackStack("addA");
                    transaction.commit();
                }
            }
        });
        FamilyRef=mRootRef.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        BoardsRef=FamilyRef.child("SwitchBoard");
        RoomBoardsRef=FamilyRef.child("RoomSwitchBoards");
        RoomIDRef=RoomBoardsRef.child(RoomID);

        RoomIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    boardids.add(child.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        BoardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if(boardids.contains(child.getKey()))
                    {
                        SwitchBoard b=child.getValue(SwitchBoard.class);
                        boards.add(b);

                    }
                }
                adapter = new SwitchBoardsGridadapter(getActivity().getApplicationContext(), boards,RoomID);
                GridView grid = (GridView) view.findViewById(R.id.SwitchboardGridView);
                grid.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
