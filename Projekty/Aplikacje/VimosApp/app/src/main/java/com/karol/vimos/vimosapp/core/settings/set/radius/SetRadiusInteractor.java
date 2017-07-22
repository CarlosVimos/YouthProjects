package com.karol.vimos.vimosapp.core.settings.set.radius;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.utils.Constants;

/**
 * Created by Vimos on 16/02/17.
 */

public class SetRadiusInteractor implements SetRadiusContract.Interactor {

    private SetRadiusContract.OnSetRadiusDatabaseListener mOnSetRadiusDatabaseListener;

    public SetRadiusInteractor(SetRadiusContract.OnSetRadiusDatabaseListener onSetRadiusDatabaseListener) {
        this.mOnSetRadiusDatabaseListener = onSetRadiusDatabaseListener;
    }

    @Override
    public void setRadiusToDatabase(Context context, FirebaseUser firebaseUser, int radius) {

        final String userId = firebaseUser.getUid();

        addRadiusToDatabase(userId, radius);


    }

    private void addRadiusToDatabase(String uid, int radius) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_RADIUS)
                .setValue(radius).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mOnSetRadiusDatabaseListener.onSuccess("Radius set successfully to user database");
                } else {
                    mOnSetRadiusDatabaseListener.onFailure("Radius failed set to user database");
                }

            }
        });

    }
}
