package com.example.haroonahmad.javarea10.HelperClasses;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.haroonahmad.javarea10.Adapters.DeviceGridViewAdapter;
import com.example.haroonahmad.javarea10.Adapters.RoomGridViewAdapter;
import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 2/28/2017.
 */

public class FirebaseHelper {
    String UserID;
    DatabaseReference RootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyData=RootRef.child("FamilyData");
    DatabaseReference UserRef;
    DatabaseReference DevicesRef;
    DatabaseReference RoomSwitchBoardsRef;
    DatabaseReference RoomsRef;
    DatabaseReference SwitchBoardRef;
    DatabaseReference SwitchBoardDevices;

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<SwitchBoard> switchBoards=new ArrayList<>();
    ArrayList<Device> devices =new ArrayList<>();

    String RoomID;
    ArrayList<String> boardid=new ArrayList<>();
    String MacAddress;
    ArrayList<SwitchBoard> boards2=new ArrayList<>();
    ArrayList<String> deviceids=new ArrayList<>();
    int i;

   public FirebaseHelper(String UserID)
   {
       this.UserID=UserID;
       UserRef=FamilyData.child(UserID);
       DevicesRef=UserRef.child("Devices");
       RoomSwitchBoardsRef=UserRef.child("RoomSwitchBoards");
       RoomsRef=UserRef.child("Rooms");
       SwitchBoardRef=UserRef.child("SwitchBoard");
       SwitchBoardDevices=UserRef.child("SwitchBoardDevices");

       RoomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot child : dataSnapshot.getChildren()) {
                   Room r = child.getValue(Room.class);
                   rooms.add(r);

               }
                LocalDB obj=LocalDB.getInstance();
               obj.setRooms(rooms);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

       SwitchBoardRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot child : dataSnapshot.getChildren()) {
                   SwitchBoard s = child.getValue(SwitchBoard.class);
                   switchBoards.add(s);

               }
               LocalDB obj=LocalDB.getInstance();
               obj.setSwitchBoards(switchBoards);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

       DevicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot child : dataSnapshot.getChildren()) {
                   Device r = child.getValue(Device.class);
                   devices.add(r);

               }
               LocalDB obj=LocalDB.getInstance();
               obj.setDevices(devices);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
       RoomSwitchBoardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               for (DataSnapshot child : dataSnapshot.getChildren()) {


               }


           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
   }



}
