package com.karol.vimos.vimosapp.core.nick;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.ToastUtil;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Vimos on 15/02/17.
 */

public class AddNickInteractor implements AddNickContract.Interactor {
    private AddNickContract.OnNickDatabaseListener mOnNickDatabaseListener;

    public AddNickInteractor(AddNickContract.OnNickDatabaseListener onNickDatabaseListener) {
        this.mOnNickDatabaseListener = onNickDatabaseListener;
    }

    @Override
    public void addNickToDatabase(final Context context, FirebaseUser firebaseUser, final String nick) {

        final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference(Constants.DB_NICKS);
        final String userId = firebaseUser.getUid();

        // check if nick from editText exists in database DB_NICKS

        database1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(nick)) {

                    // if not exist -> set nick
                    addNickToUserDatabase(userId, nick);
                    addNickToNicksDatabase(nick);
                    ToastUtil.shortToast(context, context.getString(R.string.nick_success));


                } else {

                    // if exists -> display that exists.
                    ToastUtil.shortToast(context, context.getString(R.string.nick_failure));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addNickToUserDatabase(String uid, String nick) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_NICK)
                .setValue(nick).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mOnNickDatabaseListener.onSuccess("Nick added successfully to user database");
                } else {
                    mOnNickDatabaseListener.onFailure("Nick failed added to user database");
                }

            }
        });
    }

    private void addNickToNicksDatabase(String nick) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.DB_NICKS)
                .child(nick)
                .setValue(nick).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mOnNickDatabaseListener.onSuccess("Nick added successfully to nicks database");
                } else {
                    mOnNickDatabaseListener.onFailure("Nick failed added to nicks database");
                }

            }
        });
    }
}
