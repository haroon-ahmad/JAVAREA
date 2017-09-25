package com.example.haroonahmad.javarea10.HelperClasses;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.haroonahmad.javarea10.Home;
import com.example.haroonahmad.javarea10.LoginSignup.Login;
import com.example.haroonahmad.javarea10.Services.BackgroundExecutionService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Haroon Ahmad on 3/6/2017.
 */

public class javarea extends Application {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("lala", "onAuthStateChanged:signed_in:" + user.getUid());

                    //FirebaseHelper helper=new FirebaseHelper(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //Start BackGround execution Service
                    Intent intent  = new Intent(getApplicationContext(),BackgroundExecutionService.class);
                    startService(intent);

                } else {
                    // User is signed out
                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
