package com.example.haroonahmad.javarea10.SetupActivity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.haroonahmad.javarea10.Models.*;
import com.example.haroonahmad.javarea10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Setup extends AppCompatActivity {


    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyDataRef=mRootRef.child("FamilyData");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Intent intent  = new Intent(this,BluetoothSendService.class);
        //startService(intent);

        setup_home ff=new setup_home();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.setupfragmentlayout, ff, "A");
        transaction.addToBackStack("addA");
        transaction.commit();

    }

    public void onNextClick(View v){

        //Get Attributes
        Spinner sp=(Spinner) findViewById(R.id.typeofroom);
        String type=sp.getSelectedItem().toString();
        EditText et=(EditText) findViewById(R.id.RoomName);
        String name=et.getText().toString();
        //Insert int firebase database
        Room room=new Room();
        if(type!=null && name!=null) {
            room.setName(name);
            room.setType(type);
            room.setRoomID(FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rooms").push().getKey());
            FamilyDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rooms").child(room.getRoomID()).setValue(room);

        }

        allswitchboards ff=new allswitchboards();
        //sending room id to the next fragment ie. add_Switchboard
        Bundle args = new Bundle();
        args.putString("RoomID",room.getRoomID() );
        ff.setArguments(args);

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.setupfragmentlayout,ff,"A");
        transaction.addToBackStack("addA");
        transaction.commit();

    }
    public void addswitchboard(View v){
        allswitchboards ff=new allswitchboards();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.setupfragmentlayout,ff,"A");
        transaction.addToBackStack("addA");
        transaction.commit();

    }


    public void addNewDevice(View v){
        addDevices ff=new addDevices();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.setupfragmentlayout,ff,"A");
        transaction.addToBackStack("addA");
        transaction.commit();

    }


}
