package com.karol.vimos.vimosapp.core.nick;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 15/02/17.
 */

public interface AddNickContract {
    interface View {
        void onAddNickSuccess(String message);

        void onAddNickFailure(String message);

    }

    interface Presenter {
        void addNick(Context context, FirebaseUser firebaseUser, String nick);
    }

    interface Interactor {
        void addNickToDatabase(Context context, FirebaseUser firebaseUser, String nick);
    }

    interface OnNickDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
