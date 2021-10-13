package com.robertory.packinglot;


import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.firebase.auth.FirebaseAuth.*;

public  class FirebaseUtil {
    private static final int RC_SIGN_IN =123 ;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    private static FirebaseAuth mFirebaseAuth;
    private static AuthStateListener mAuthListener;
    private  static Activity caller;

    public static ArrayList<Authors> mDeals;

    //create a private constructor to avoid this class being instantiated
    private FirebaseUtil(){}


    public static void openFbReference(String ref, final Activity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDeals = new ArrayList<Authors>();
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
        mFirebaseAuth= FirebaseAuth.getInstance();
        caller=callerActivity;
        mAuthListener=new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                 FirebaseUtil.signIn();

                Toast.makeText(callerActivity.getBaseContext(),"Welcome back",Toast.LENGTH_SHORT).show();

            }
        };



    }

    private static void signIn() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    public static void attachListener()
    {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener()
    {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

}
