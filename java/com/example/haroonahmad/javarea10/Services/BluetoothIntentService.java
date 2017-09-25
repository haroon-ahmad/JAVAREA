package com.example.haroonahmad.javarea10.Services;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Haroon Ahmad on 2/28/2017.
 */

public class BluetoothIntentService extends IntentService {
    private static final String TAG = "BluetoothIntentService";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private static OutputStream outStream = null;

    private  static String address=null;
    String PortNum=null;
    String State=null;
    String DeviceID=null;
    // SPP UUID service
    private static  DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    static DatabaseReference DeviceRef=root.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Devices");

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothIntentService() {
            super(BluetoothIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DeviceRef.keepSynced(true);
         address=intent.getStringExtra("MacAddress");
         PortNum=intent.getStringExtra("Portnum");
         State=intent.getStringExtra("State");
        DeviceID=intent.getStringExtra("DeviceID");
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device= btAdapter.getRemoteDevice(address);



        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            Log.d(TAG, "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                Log.d(TAG, "In onResume() and output stream creation failed:" + e.getMessage() + ".");
            }
        } catch (IOException e) {
            try {
                btSocket.close();
                Log.d(TAG, "Closing socket in OnResume() Exception");
            } catch (IOException e2) {

                Log.d("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        //checkBTState();
       if(sendData(PortNum+","+State))
       {
           if(State.equals("1")) {
               DeviceRef.child(DeviceID).child("state").setValue(true);
           }
           else
           {
               DeviceRef.child(DeviceID).child("state").setValue(false);
           }
       }
        else
       {
           Log.d(TAG,"in else");
           Toast.makeText(getApplicationContext(),"Could not turn off device",Toast.LENGTH_LONG).show();
       }
        this.stopSelf();

    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice bluetoothDevice) throws IOException {

        try {
            Method m = bluetoothDevice.getClass().getMethod(
                    "createRfcommSocket", new Class[] { int.class });
            btSocket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return btSocket;
    }
    public static boolean sendData(String message)
    {
        byte[] msgBuffer = message.getBytes();
        //Toast.makeText(this, msgBuffer.toString(), Toast.LENGTH_LONG).show();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);

            Log.d(TAG, "...written: ");
            return true;
        }
        catch (Exception e)
        {
            Log.d(TAG, "in catch:  ");
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
            Log.d(TAG,"ERROR!!!");

            return false;

        }
    }

    @Override
    public void onDestroy(){
        try{
            btSocket.close();
            outStream.close();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Could not close");
        }
    }
}