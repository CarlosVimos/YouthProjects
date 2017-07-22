package com.karol.vimos.vimosapp.core.chat_preferences.add.hobby;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 17/05/17.
 */

public interface AddHobbyContract {

    interface View {
        void onAddHobbySuccess(String message);

        void onAddHobbyFailure(String message);

    }

    interface Presenter {
        void addHobby(Context context, FirebaseUser firebaseUser, String name);
    }

    interface Interactor {
        void addHobbyToDatabase(Context context, FirebaseUser firebaseUser, String name);
    }

    interface OnHobbyDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }

}
