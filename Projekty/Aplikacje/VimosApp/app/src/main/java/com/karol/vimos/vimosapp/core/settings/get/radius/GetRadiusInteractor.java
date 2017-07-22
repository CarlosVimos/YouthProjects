package com.karol.vimos.vimosapp.core.settings.get.radius;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.utils.Constants;

/**
 * Created by Vimos on 16/02/17.
 */

public class GetRadiusInteractor implements GetRadiusContract.Interactor {

    private GetRadiusContract.OnGetRadiusDatabaseListener mOnGetRadiusDatabaseListener;

    public GetRadiusInteractor(GetRadiusContract.OnGetRadiusDatabaseListener onGetRadiusDatabaseListener) {
        this.mOnGetRadiusDatabaseListener = onGetRadiusDatabaseListener;
    }



    @Override
    public void getRadiusFromDatabase(Context context, FirebaseUser firebaseUser, final int userRadius) {

        String userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               int radiusFromDB;

                User user = dataSnapshot.getValue(User.class);
                radiusFromDB = user.getRadius();
               // radius = radiusFromDB;
                // user1.setRadius(radius);
              //  userRadius = radiusFromDB;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                mOnGetRadiusDatabaseListener.onGetRadiusFailure(databaseError.getMessage());

            }
        });


    }
}
