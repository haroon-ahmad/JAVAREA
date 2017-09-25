package com.example.haroonahmad.javarea10;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.haroonahmad.javarea10.Adapters.SwitchBoardsGridadapter;
import com.example.haroonahmad.javarea10.HelperClasses.ImageHolder;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
import com.example.haroonahmad.javarea10.SetupActivity.add_Switchboard;
import com.example.haroonahmad.javarea10.SetupActivity.setup_home;
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
public class SwitchBoards extends Fragment implements AdapterView.OnItemClickListener {

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyRef;
    DatabaseReference BoardsRef;
    DatabaseReference RoomBoardsRef;
    DatabaseReference RoomIDRef;
    ArrayList<SwitchBoard> boards=new ArrayList<>();
    SwitchBoardsGridadapter adapter;
    ArrayList<String > boardids=new ArrayList<>();
    View view;
    GridView grid;
    String RoomID;
    public SwitchBoards() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_switch_boards, container, false);
        boards.clear();
        RoomID = getArguments().getString("RoomID");
        FamilyRef=mRootRef.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        BoardsRef=FamilyRef.child("SwitchBoard");
        RoomBoardsRef=FamilyRef.child("RoomSwitchBoards");
        RoomIDRef=RoomBoardsRef.child(RoomID);
        grid = (GridView) view.findViewById(R.id.RoomSwitchboardGridView);

        Button button=(Button) view.findViewById(R.id.AddNewSwitchBoard);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddSwitchBoard ff=new AddSwitchBoard();
                Bundle args = new Bundle();
                args.putString("RoomID", RoomID);
                ff.setArguments(args);
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.relativefragment, ff, "A");
                transaction.addToBackStack("addA");
                transaction.commit();
            }
        });


        RoomIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    boardids.add(child.getKey());
                }
                RoomIDRef.removeEventListener(this);
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
                grid.setAdapter(adapter);
                BoardsRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        grid.setOnItemClickListener(this);
        return view;
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Bundle args = new Bundle();
        args.putString("RoomID",RoomID );
        args.putString("SwitchBoardID",boards.get(position).getSwitchboardID());
        args.putString("MacAddress",boards.get(position).getMACAddress());

        DeviceFragment ff=new DeviceFragment();
        ff.setArguments(args);
        Home.devicetag = ff.getTag();
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.relativefragment,ff,"A");
        transaction.addToBackStack("addA");
        transaction.commit();


    }

}
