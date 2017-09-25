package com.example.haroonahmad.javarea10.LoginSignup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haroonahmad.javarea10.R;
import com.example.haroonahmad.javarea10.SetupActivity.Setup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText username;
    EditText mPasswordField;
    EditText phoneNo;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference FamilyUsersRef=mRootRef.child("FamilyUsers");
    DatabaseReference FamilyUserAccounts=mRootRef.child("FamilyUserAccounts");
    DatabaseReference FamilyDataRef=mRootRef.child("FamilyData");
    String email;
    String pass;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.username);
        mPasswordField = (EditText) findViewById(R.id.passwordedittext);
        phoneNo = (EditText) findViewById(R.id.phoneEditText);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("lala", "onAuthStateChanged:signed_in:" + user.getUid());
                    FamilyUsersRef.child(user.getUid()).child("Email").setValue(username.getText().toString());
                    FamilyUsersRef.child(user.getUid()).child("Password").setValue(mPasswordField.getText().toString());
                    FamilyUsersRef.child(user.getUid()).child("Phone").setValue(phoneNo.getText().toString());
                    
                } else {
                    // User is signed out
                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
    }

    public void signup(View v)
    {
        createAccount(username.getText().toString(), mPasswordField.getText().toString());
    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup.this,"Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            Intent intent= new Intent(Signup.this,Setup.class);
                            startActivity(intent);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = username.getText().toString();
        if (TextUtils.isEmpty(email)) {
            username.setError("Required.");
            valid = false;
        } else {
            username.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

