package com.karol.vimos.vimosapp.core.lector;

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

public class LectorInteractor implements LectorContract.Interactor {

    public LectorContract.OnLectorDatabaseListener mOnLectorDatabaseListener;

    public LectorInteractor(LectorContract.OnLectorDatabaseListener onLectorDatabaseListener) {

        this.mOnLectorDatabaseListener = onLectorDatabaseListener;
    }


    @Override
    public void addLectorToDatabase(Context context, FirebaseUser firebaseUser, String lector) {

        final String userId = firebaseUser.getUid();

        addLectorToUserDatabase(userId, lector);

    }

    private void addLectorToUserDatabase(String uid, String lector) {


            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.ARG_USERS)
                    .child(uid)
                    .child(Constants.ARG_LECTOR)
                    .setValue(lector).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        mOnLectorDatabaseListener.onSuccess("Lector added successfully to user database");
                    } else {
                        mOnLectorDatabaseListener.onFailure("Lector failed added to user database");
                    }

                }
            });

        }

}
