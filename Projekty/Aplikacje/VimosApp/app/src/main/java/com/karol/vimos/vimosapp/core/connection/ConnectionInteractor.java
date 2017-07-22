package com.karol.vimos.vimosapp.core.connection;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Vimos on 26/05/17.
 */

public class ConnectionInteractor implements ConnectionContract.Interactor {

    private ConnectionContract.OnConnectionDatabaseListener mOnConnectionDatabaseListener;

    public ConnectionInteractor(ConnectionContract.OnConnectionDatabaseListener onConnectionDatabaseListener) {

        this.mOnConnectionDatabaseListener = onConnectionDatabaseListener;

    }

    @Override
    public void checkConnectionState(Context context, DatabaseReference databaseReference, final DatabaseReference ref_online) {

        // ref_online -> select which activity you wanna connectionState: i.e: BlueActivity: blueUser so: reference to /blue.

        databaseReference = FirebaseDatabase.getInstance().getReference(".info/connected");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);

                if (connected) {

                    ref_online.setValue("1");

                }
                else {

                    ref_online.onDisconnect().setValue("0");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
