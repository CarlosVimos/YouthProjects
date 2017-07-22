package com.karol.vimos.vimosapp.core.lector;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 25/02/17.
 */

public interface LectorContract {

    interface View {
        void onLectorSuccess(String message);

        void onLectorFailure(String message);

    }

    interface Presenter {
        void Lector(Context context, FirebaseUser firebaseUser, String lector);
    }

    interface Interactor {
        void addLectorToDatabase(Context context, FirebaseUser firebaseUser, String lector);
    }

    interface OnLectorDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
