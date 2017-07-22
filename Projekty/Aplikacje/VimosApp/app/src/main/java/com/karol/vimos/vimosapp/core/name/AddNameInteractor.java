package com.karol.vimos.vimosapp.core.name;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.core.nick.AddNickContract;
import com.karol.vimos.vimosapp.utils.Constants;

/**
 * Created by Vimos on 16/02/17.
 */

public class AddNameInteractor implements AddNameContract.Interactor {

    private AddNameContract.OnNameDatabaseListener mOnNameDatabaseListener;

    public AddNameInteractor(AddNameContract.OnNameDatabaseListener onNameDatabaseListener) {
        this.mOnNameDatabaseListener = onNameDatabaseListener;
    }


    @Override
    public void addNameToDatabase(final Context context, FirebaseUser firebaseUser, final String name) {

        final String userId = firebaseUser.getUid();

        addNameToUserDatabase(userId, name);


    }

    private void addNameToUserDatabase(String uid, String name) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_NAME)
                .setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mOnNameDatabaseListener.onSuccess("Name added successfully to user database");
                } else {
                    mOnNameDatabaseListener.onFailure("Name failed added to user database");
                }

            }
        });
    }


}
