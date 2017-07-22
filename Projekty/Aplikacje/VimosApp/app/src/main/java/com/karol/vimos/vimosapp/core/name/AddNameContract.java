package com.karol.vimos.vimosapp.core.name;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 16/02/17.
 */

public interface AddNameContract {

    interface View {
        void onAddNameSuccess(String message);

        void onAddNameFailure(String message);

    }

    interface Presenter {
        void addName(Context context, FirebaseUser firebaseUser, String name);
    }

    interface Interactor {
        void addNameToDatabase(Context context, FirebaseUser firebaseUser, String name);
    }

    interface OnNameDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
