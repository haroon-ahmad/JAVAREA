package com.example.haroonahmad.javarea10.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class BackgroundExecutionService extends Service {

    private String TAG = "backservice";


    //firebase database refrences
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private DatabaseReference CommandNode=root.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Command");




    String Message=null;
    String MacAddress=null;
    String Portnum=null;
    String State=null;
    String DeviceID=null;
    static  int count=0;

    public BackgroundExecutionService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"created!!");
        Log.d(TAG,count+".");


    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startID){
        Log.d(TAG,count+".");
        CommandNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (count > 0) {
                        final DataSnapshot data=dataSnapshot;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MacAddress = data.child("MacAddress").getValue().toString();
                                Portnum = data.child("Portnum").getValue().toString();
                                State = data.child("State").getValue().toString();
                                DeviceID = data.child("DeviceID").getValue().toString();
                                Intent intent3 = new Intent(Intent.ACTION_SYNC, null, getBaseContext(), BluetoothIntentService.class);
                                intent3.putExtra("MacAddress", MacAddress);
                                intent3.putExtra("Portnum", Portnum);
                                intent3.putExtra("State", State);
                                intent3.putExtra("DeviceID", DeviceID);
                                startService(intent3);
                                Log.d(TAG, MacAddress + Portnum + State);
                            }
                        }).start();

                    }
                    count++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return Service.START_STICKY;
    }
    @Override
    public void onDestroy(){
        count=0;
        Log.d(TAG,"Destroyed");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}