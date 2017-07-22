package com.karol.vimos.vimosapp.core.users.get.all;

import android.text.TextUtils;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vimos on 15/02/17.
 */

public class GetUsersInteractor implements GetUsersContract.Interactor {
    private static final String TAG = "GetUsersInteractor";


    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;

    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }


    @Override
    public void getAllUsersFromFirebase() {

        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final String userId = firebaseUser.getUid();
        //child("users_in_red_radius").child(userId)

        FirebaseDatabase.getInstance().getReference().child("users_in_red_radius").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatUsersFromFirebase() {
        /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CHAT_ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();
                List<User> users=new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    dataSnapshotChild.getRef().
                    Chat chat=dataSnapshotChild.getValue(Chat.class);
                    if(chat.)4
                    if(!TextUtils.equals(user.uid,FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}