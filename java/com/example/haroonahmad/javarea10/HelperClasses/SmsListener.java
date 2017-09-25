package com.example.haroonahmad.javarea10.HelperClasses;

/**
 * Created by Haroon Ahmad on 3/9/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import com.example.haroonahmad.javarea05.RoomControl;

/**
 * Created by Haroon Ahmad on 1/11/2017.
 */

public class SmsListener extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        // TODO Auto-generated method stub

        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();

        DatabaseReference aalu = mRootRef.child("FamilyUserAccounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final ArrayList<String>familymembernumbers=new ArrayList<>();
        aalu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    familymembernumbers.add(child.getValue().toString());
                }

                DatabaseReference FamilyRef;
                if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                    Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                    SmsMessage[] msgs = null;
                    String msg_from;
                    if (bundle != null){
                        //---retrieve the SMS message received---
                        try{
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            msgs = new SmsMessage[pdus.length];
                            for(int i=0; i<msgs.length; i++){
                                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                                msg_from = msgs[i].getOriginatingAddress();
                                //Toast.makeText(context,msg_from,Toast.LENGTH_LONG).show();

                                for (int ii=0;ii<familymembernumbers.size();ii++) {
                                    if (PhoneNumberUtils.compare(familymembernumbers.get(ii), msg_from)) {
                                        String msgBody = msgs[i].getMessageBody();
                                        Toast.makeText(context, msgBody, Toast.LENGTH_LONG).show();
                                        String[] params = msgBody.split(",");


                                        if (params.length >= 3) {
                                            FamilyRef = FirebaseDatabase.getInstance().getReference().child("FamilyData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            DatabaseReference commandRef = FamilyRef.child("Command");
                                            commandRef.child("MacAddress").setValue(params[0]);
                                            commandRef.child("Portnum").setValue(params[1]);
                                            int command;
                                            if (params[2].equals("0")) {
                                                command = 1;
                                            } else {
                                                command = 0;
                                            }
                                            commandRef.child("State").setValue(command);
                                            commandRef.child("DeviceID").setValue(params[3]);

                                        }


                                        //params[0]
                                        // RoomControl.sendData(msgBody,context);
                                    }
                                }
                            }
                        }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}