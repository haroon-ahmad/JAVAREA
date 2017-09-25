package com.example.haroonahmad.javarea10;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haroonahmad.javarea10.HelperClasses.MyBroadcastReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends Activity {

    private static final String TAG = "bluetooth1";


    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private static OutputStream outStream = null;

    long tStart = 0;
    double cost = 0;
    double elapsedSeconds=0;
    // SPP UUID service

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
   // private static String address= "20:16:05:16:96:72";
    private static String address= "98:D3:31:FB:15:E3";

    TextView mTotalBill;
    TextView mTime;
    TextView mCost;
    TextView mTax;

    Button startbtn, stopbtn, refreshbtn;


    String data = "";
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    boolean isRunning=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshbtn =(Button)findViewById(R.id.button5);

        mTotalBill = (TextView) findViewById(R.id.totalbilltxt);
        mTime = (TextView) findViewById(R.id.textView13);
        mCost = (TextView) findViewById(R.id.costtxt);
        mTax = (TextView) findViewById(R.id.taxtext);
        mTax.setText("16%");

//        angle=(Button) findViewById(R.id.angles);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        startbtn = (Button)findViewById(R.id.startstop);
        stopbtn = (Button)findViewById(R.id.stopp);

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("7");
                beginListenForData();
            }
        });
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sendData("6,1");
                    tStart = System.currentTimeMillis();

                    isRunning=true;


            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                mTime.setText(String.valueOf(elapsedSeconds));
                isRunning=false;
                sendData("7");
                beginListenForData();
                sendData("6,0");


            }
        });

    }


    public void startAlert() {
        EditText text = (EditText) findViewById(R.id.time);
        int i = Integer.parseInt(text.getText().toString());
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
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

    @Override
    public void onResume()
    {
        super.onResume();
        checkBTState();
        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
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
                mmInputStream= btSocket.getInputStream();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
            }
        } catch (IOException e) {
            try {
                btSocket.close();
                Log.d(TAG, "Closing socket in OnResume() Exception");
            } catch (IOException e2) {

                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");




    }



    private void checkBTState() {
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(),"Restart Application",Toast.LENGTH_LONG).show();
    }

    private void sendData(String message)
    {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e)
        {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        try{
            btSocket.close();
            outStream.close();
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(),"Could not close",Toast.LENGTH_LONG).show();
        }
    }
    void beginListenForData()
    {

        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        //Log.d(TAG, " "+bytesAvailable);

                        if(bytesAvailable > 0)
                        {
                            stopWorker=true;
                            Log.d(TAG, "Received BT data");
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                Log.d(TAG, "In Loop.");
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    Log.d(TAG, "Found Delimiter.");

                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {

                                        if(data.charAt(0)=='~') {
                                            //data.equals(data.substring(data.indexOf('~'),data.indexOf('#')));
                                            data = (data.substring(1, data.length()-2));
                                            cost = Double.parseDouble(data) * (Double.parseDouble(mTime.getText().toString())/3600);
                                            mCost.setText("Rs. " + Double.toString(cost).substring(0,5));
                                            cost = 0.16 * cost + cost;
                                            mTotalBill.setText("Rs. " + Double.toString(cost).substring(0,5));
                                        }
                                        stopWorker=true;
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }



}