package com.karol.vimos.vimosapp.core.chat_preferences.add.hobby;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.core.name.AddNameContract;
import com.karol.vimos.vimosapp.utils.Constants;

/**
 * Created by Vimos on 17/05/17.
 */

public class AddHobbyInteractor implements AddHobbyContract.Interactor {

    private AddHobbyContract.OnHobbyDatabaseListener mOnHobbyDatabaseListener;

    public AddHobbyInteractor(AddHobbyContract.OnHobbyDatabaseListener onHobbyDatabaseListener) {
        this.mOnHobbyDatabaseListener = onHobbyDatabaseListener;
    }


    @Override
    public void addHobbyToDatabase(final Context context, FirebaseUser firebaseUser, final String name) {

        final String userId = firebaseUser.getUid();

        addHobbyToUserDatabase(userId, name);


    }

    private void addHobbyToUserDatabase(String uid, String name) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_PROFESSION)
                .setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mOnHobbyDatabaseListener.onSuccess("Hobby added successfully to user database");
                } else {
                    mOnHobbyDatabaseListener.onFailure("Hobby failed added to user database");
                }

            }
        });
    }


}
