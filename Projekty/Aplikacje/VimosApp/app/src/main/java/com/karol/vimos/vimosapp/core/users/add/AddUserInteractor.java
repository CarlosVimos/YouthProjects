package com.karol.vimos.vimosapp.core.users.add;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.SharedPrefUtil;

/**
 * Created by Vimos on 15/02/17.
 */

public class AddUserInteractor implements AddUserContract.Interactor {
    private AddUserContract.OnUserDatabaseListener mOnUserDatabaseListener;

    public AddUserInteractor(AddUserContract.OnUserDatabaseListener onUserDatabaseListener) {
        this.mOnUserDatabaseListener = onUserDatabaseListener;
    }

    @Override
    public void addUserToDatabase(final Context context, FirebaseUser firebaseUser) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        User user = new User(firebaseUser.getUid(),
                firebaseUser.getEmail(),
                new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN));
        database.child(Constants.ARG_USERS)
                .child(firebaseUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mOnUserDatabaseListener.onSuccess(context.getString(R.string.user_successfully_added));
                        } else {
                            mOnUserDatabaseListener.onFailure(context.getString(R.string.user_unable_to_add));
                        }
                    }
                });

        int radius = 100;
        int score = 100;
        // setting radius settings and adding score after registration process
        database.child(Constants.ARG_USERS).child(firebaseUser.getUid()).child(Constants.ARG_RADIUS).setValue(radius);
        database.child(Constants.ARG_USERS).child(firebaseUser.getUid()).child(Constants.ARG_SCORE).setValue(score);
    }
}
