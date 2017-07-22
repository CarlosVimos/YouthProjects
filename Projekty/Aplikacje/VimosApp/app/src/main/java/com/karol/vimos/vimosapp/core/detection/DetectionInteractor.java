package com.karol.vimos.vimosapp.core.detection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.utils.Constants;

/**
 * Created by Vimos on 25/02/17.
 */

public class DetectionInteractor implements DetectionContract.Interactor {

    private DetectionContract.OnDetectionDatabaseListener mOnDetectionDatabaseListener;

    public DetectionInteractor(DetectionContract.OnDetectionDatabaseListener onDetectionDatabaseListener) {

        this.mOnDetectionDatabaseListener = onDetectionDatabaseListener;

    }


    private void addRedDetectionToUserDatabase(String uid, int detection) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_DETECTION)
                .child(Constants.ARG_RED_DETECTION)
                .setValue(detection).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

//                if (task.isSuccessful()) {
//                    mOnDetectionDatabaseListener.onSuccess("Detection added successfully to user database");
//                } else {
//                    mOnDetectionDatabaseListener.onFailure("Detection failed added to user database");
//                }

            }
        });
    }

    private void addGreenDetectionToUserDatabase(String uid, int detection) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_DETECTION)
                .child(Constants.ARG_GREEN_DETECTION)
                .setValue(detection).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mOnDetectionDatabaseListener.onSuccess("Detection added successfully to user database");
                } else {
                    mOnDetectionDatabaseListener.onFailure("Detection failed added to user database");
                }

            }
        });
    }

    private void addOrangeDetectionToUserDatabase(String uid, int detection) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_DETECTION)
                .child(Constants.ARG_ORANGE_DETECTION)
                .setValue(detection).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mOnDetectionDatabaseListener.onSuccess("Detection added successfully to user database");
                } else {
                    mOnDetectionDatabaseListener.onFailure("Detection failed added to user database");
                }

            }
        });
    }


    @Override
    public void addRedDetectionToDatabase(Context context, FirebaseUser firebaseUser, int detection) {


        final String userId = firebaseUser.getUid();

        addRedDetectionToUserDatabase(userId, detection);


    }

    @Override
    public void addGreenDetectionToDatabase(Context context, FirebaseUser firebaseUser, int detection) {

        final String userId = firebaseUser.getUid();

        addGreenDetectionToUserDatabase(userId, detection);

    }

    @Override
    public void addOrangeDetectionToDatabase(Context context, FirebaseUser firebaseUser, int detection) {

        final String userId = firebaseUser.getUid();

        addOrangeDetectionToUserDatabase(userId, detection);

    }
}
