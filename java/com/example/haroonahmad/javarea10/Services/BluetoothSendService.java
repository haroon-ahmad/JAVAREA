package com.example.haroonahmad.javarea10.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.Vector;


/**
 * Created by Haroon Ahmad on 2/24/2017.
 */

public class BluetoothSendService extends Service {
    private static String TAG = "bluetoothservice";

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private static OutputStream outStream = null;

    private  static String address=null;

    // SPP UUID service

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"created!!");


    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startID){


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

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
        sendData("8,1");

        return Service.START_STICKY;
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
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
    public static void sendData(String message)
    {
        byte[] msgBuffer = message.getBytes();
        //Toast.makeText(this, msgBuffer.toString(), Toast.LENGTH_LONG).show();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
            Log.d(TAG, "...in try: ");
        } catch (IOException e)
        {

            Log.d(TAG, "in catch:  ");
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
            Log.d(TAG,"ERROR!!!");

        }
    }

}
