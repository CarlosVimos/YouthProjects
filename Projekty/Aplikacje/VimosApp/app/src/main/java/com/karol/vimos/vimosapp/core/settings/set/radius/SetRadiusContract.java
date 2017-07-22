package com.karol.vimos.vimosapp.core.settings.set.radius;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 16/02/17.
 */

public interface SetRadiusContract {
    interface View {

        void onSetRadiusSuccess(String message);

        void onSetRadiusFailure(String message);

    }

    interface Presenter {
        void setRadius(Context context, FirebaseUser firebaseUser, int radius);
    }

    interface Interactor {
        void setRadiusToDatabase(Context context, FirebaseUser firebaseUser, int radius);
    }

    interface OnSetRadiusDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }


}
