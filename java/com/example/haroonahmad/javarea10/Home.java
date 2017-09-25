package com.example.haroonahmad.javarea10;

import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haroonahmad.javarea10.StringProcessing;
import com.example.haroonahmad.javarea10.HelperClasses.CommandClass;
import com.example.haroonahmad.javarea10.HelperClasses.FirebaseHelper;
import com.example.haroonahmad.javarea10.HelperClasses.LocalDB;
import com.example.haroonahmad.javarea10.LoginSignup.Login;
import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;
import com.example.haroonahmad.javarea10.Services.BackgroundExecutionService;
import com.example.haroonahmad.javarea10.Services.BluetoothIntentService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    public static String devicetag="sa";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    ArrayList<Room> arr=new ArrayList<>();
    CommandClass command=new CommandClass();




    //Database refrences
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyRef;
    DatabaseReference RoomsRef;
    DatabaseReference BoardSwicthesRef;
    DatabaseReference DevicesRef;
    DatabaseReference BoardsRef;
    DatabaseReference RoomBoardsRef;

    LocalDB dbInstance = LocalDB.getInstance();
    ArrayList<Room> userRooms=new ArrayList<>();
    ArrayList<Device> userDevices=new ArrayList<>();
    private TextView finalresult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this to set delegate/listener back to this class
        //asyncTask.delegate = this;
        finalresult= (TextView) findViewById(R.id.Answers);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("lala", "onAuthStateChanged:signed_in:" + user.getUid());
                    FamilyRef=mRootRef.child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    RoomsRef=FamilyRef.child("Rooms");
                    DevicesRef=FamilyRef.child("Devices");
                    BoardSwicthesRef=FamilyRef.child("SwitchBoardDevices");
                    BoardsRef=FamilyRef.child("SwitchBoard");
                    RoomBoardsRef=FamilyRef.child("RoomSwitchBoards");
                    //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                } else {
                    // User is signed out
                    Intent in= new Intent(Home.this,Login.class);
                    startActivity(in);
                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*String inputParameter="hello haroon off";
        StringProcessing.Command processedString = obj.extractCommandFromString(inputParameter);
        Log.d("loli: ",processedString.bluetoothID+":"+processedString.portNum);
*/

        //Log.d("aalu", "onDataChange: ");
        //Start BackGround execution Service
        /*Intent intent  = new Intent(getApplicationContext(),BackgroundExecutionService.class);
        startService(intent);*/



        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment ff=new HomeFragment();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        //ImageView iv = (ImageView) findViewById(R.id.imageView);
        //iv.setImageResource(R.drawable.device);



    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void micBtnClick(View v){
        Toast.makeText(this,"Google Api Should run here!!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void addDeviceBtnClick(View v){
        Toast.makeText(this,"Device should be Added!!",Toast.LENGTH_SHORT).show();

    }

    public void doneBtnClick(View v){
        Toast.makeText(this,"SwitchBoard Should be added!!",Toast.LENGTH_SHORT).show();

    }

    public void addNewDeviceBtnClick(View v){
        AddDevice ff=new AddDevice();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.relativefragment, ff, "A");
        transaction.addToBackStack("addA");
        transaction.commit();

    }


    public void searchBoardClick(View v){
        Toast.makeText(this,"Search for bluetooth devices",Toast.LENGTH_SHORT).show();

    }
    public void addSwitchBoardClick(View v){

        AddSwitchBoard ff=new AddSwitchBoard();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.relativefragment, ff, "A");
        transaction.addToBackStack("addA");
        transaction.commit();
        Toast.makeText(this,"Add SwitchBoard Clicked",Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onBackPressed() {
        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.relativefragment);
        FragmentManager fm = getFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

       /* if (f instanceof DevicesFragment){
            Toast.makeText(this,"Pakra gya ullu ka patha",Toast.LENGTH_SHORT).show();
            RoomFragment ff=new RoomFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else if (f instanceof AddRoom2) {
            AddRoom ff=new AddRoom();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else if (f instanceof AddRoom) {
            RoomFragment ff=new RoomFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else if (f instanceof AddDevice) {
            DevicesFragment ff=new DevicesFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else if (f instanceof AddSwitchBoard) {
            ConfigureRoom ff=new ConfigureRoom();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else if (f instanceof ConfigureRoom) {
            RoomFragment ff=new RoomFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else if (f instanceof ConfigureSwitchBoard) {
            ConfigureRoom ff=new ConfigureRoom();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
        }
        else*/
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.action_mic) {
            //Toast.makeText(this,"Google Api should start here!",Toast.LENGTH_SHORT).show();
           Toast.makeText(this,"Google Api Should run here!!",Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
           intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                   RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
           intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
           intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                   getString(R.string.speech_prompt));
           try {
               startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
           } catch (ActivityNotFoundException a) {
               Toast.makeText(getApplicationContext(),
                       getString(R.string.speech_not_supported),
                       Toast.LENGTH_SHORT).show();
           }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment ff=new HomeFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();


//            Toast.makeText(this,"aalu",Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_rooms) {
            RoomFragment ff=new RoomFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.relativefragment, ff, "A");
            transaction.addToBackStack("addA");
            transaction.commit();
//            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
            //   SecondFragment secondFragment = SecondFragment.newInstance("aalu1","aalu2");
            //  android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            // manager.beginTransaction().replace(R.id.relativefragment, secondFragment, secondFragment.getTag()).commit();

        } else if (id == R.id.nav_aalu){
            SecondFragment ff=new SecondFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.relativefragment,ff,"aalu");
            transaction.addToBackStack("addA");
            transaction.commit();

        }
        else if (id == R.id.nav_logout){
            mAuth.signOut();
            if(mAuth.getCurrentUser()==null);
            {
                Intent in = new Intent(this, Login.class);
                startActivity(in);
            }

        }
        else if (id == R.id.nav_outsource) {
            Outsource_Fragment ff=new Outsource_Fragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.relativefragment, ff, "A");
            transaction.addToBackStack("addA");
            transaction.commit();
//            manager.beginTransaction().replace(R.id.relativefragment, ff, ff.getTag()).commit();
            //   SecondFragment secondFragment = SecondFragment.newInstance("aalu1","aalu2");
            //  android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            // manager.beginTransaction().replace(R.id.relativefragment, secondFragment, secondFragment.getTag()).commit();

        }
        else if (id == R.id.nav_bill){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {


                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String theInput = result.get(0);
                    Toast.makeText(getApplicationContext(), theInput, Toast.LENGTH_SHORT).show();
                    RoomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Room r = child.getValue(Room.class);
                                userRooms.add(r);

                            }
                            DevicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Device r = child.getValue(Device.class);
                                        userDevices.add(r);
                                    }
                                    StringProcessing obj= StringProcessing.getInstance();

                                    for(Device d : userDevices){
                                        obj.addInDeviceDictionary(d.getName(),"1");


                                    }
                                    for(Room r: userRooms){
                                        obj.addInRoomDictionary(r.getName(),"1");

                                    }
                                    obj.addInRoomDictionary("outdoor","1");
                                    obj.addInDeviceDictionary("yellow","2");

                                    String inputParameter=theInput;
                                    String resp="Ok";
                                    //String inputParameter="library left off";
                                    StringProcessing.Command processedString = obj.extractCommandFromString(inputParameter);
                                    //Toast.makeText(this,"Voice Command by Manaal" , Toast.LENGTH_SHORT).show();

                                    /*if(processedString.portNum=="-99" && processedString.bluetoothID=="-99" && processedString.all && processedString.action!=-99)
                                        resp="\nAll devices in the house " + "and Action: "+ processedString.action;
                                    else if(processedString.portNum=="-99" && processedString.bluetoothID!="-99" && processedString.all)
                                        resp="\nAll devices in Room: " + processedString.bluetoothID + "and Action: "+ processedString.action;
                                    else if(processedString.portNum=="-99" || processedString.action==-99 || processedString.bluetoothID=="-99")
                                    {
                                        resp="\nRephrase Voice Command"; //PUT THIS AS TOAST OR SOMETHING.
                                        Toast.makeText(getApplicationContext(),"Repeat Voice Command",Toast.LENGTH_SHORT).show();
                                    }*/
                                    if(processedString.portNum!="-99" && processedString.action!=-99 && processedString.bluetoothID!="-99"){

                                        //resp = "\nRoom Number: " + processedString.bluetoothID + "  Device Number: " + processedString.portNum + "    Action: " + processedString.action;
                                        Log.d("loli: ",processedString.bluetoothID+":"+processedString.portNum+":"+processedString.action);
                                        Toast.makeText(getApplicationContext(),processedString.bluetoothID+":"+processedString.portNum+":"+processedString.action,Toast.LENGTH_SHORT).show();
                                        getCommand(processedString.bluetoothID,processedString.portNum,processedString.action);

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Please Repeat Voice Command",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    ///////CheckSpeech//////////
                    //AsyncTaskRunner runner=new AsyncTaskRunner();
                    //runner.execute("manaala room one");
                    //runner.execute(theInput);



                }
            }
        }
    }
    public String getRoomID( final String Name)
    {
        DatabaseReference RoomsRef=FirebaseDatabase.getInstance().getReference().child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rooms");

        RoomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.child("name").equals(Name))
                    {
                        command.setRoomID(child.getValue().toString());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return command.getRoomID();
    }
    public CommandClass getCommand(final String RoomName, final String DeviceName,final int State) //Edit this to include action ON/OFF too
    {

        //Log.d("zain6: ","lol");
        final String TAG="Speech";
        RoomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.child("name").getValue().toString().toLowerCase().equals(RoomName.toLowerCase()) || child.child("name").getValue().toString().toLowerCase().contains(RoomName.toLowerCase()))
                    {

                        Log.d(TAG,child.getKey());

                        command.setRoomID(child.getKey());

                    }

                }

                RoomBoardsRef.child(command.getRoomID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            command.getBoardmap().put(child.getKey(),child.getValue().toString());

                        }
                       // RoomBoardsRef.removeEventListener(this);
                        DevicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if(child.child("name").getValue().toString().toLowerCase().equals(DeviceName.toLowerCase())|| child.child("name").getValue().toString().toLowerCase().contains(DeviceName.toLowerCase()))
                                    {
                                        Log.d(TAG,"in Device ref");
                                        command.setDeviceID(child.getKey());
                                        command.setPortNum(child.child("portNum").getValue().toString());
                                        Log.d("zain1: ",child.getKey());
                                    }

                                }   
                                for (Map.Entry<String, String> entry : command.getBoardmap().entrySet()) {
                                    String key = entry.getKey();
                                    final String value = entry.getValue();


                                    BoardSwicthesRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d("zain","asfas");
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                if( child.getValue().toString().equals(command.getDeviceID()) || child.getValue().toString().contains(command.getDeviceID()))
                                                {
                                                    Log.d(TAG,"in boardswitches");
                                                    command.setMacAddress(value);
                                                    DatabaseReference commandRef=FamilyRef.child("Command");
                                                    commandRef.child("MacAddress").setValue(command.getMacAddress());
                                                    commandRef.child("Portnum").setValue(command.getPortNum());
                                                    commandRef.child("State").setValue(State);
                                                    Log.d(TAG,command.getMacAddress()+command.getPortNum()+State);
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return command;

    }
    public void speak(View v)
    {
        Toast.makeText(getApplicationContext(),"aalu",Toast.LENGTH_LONG).show();
    }

}
