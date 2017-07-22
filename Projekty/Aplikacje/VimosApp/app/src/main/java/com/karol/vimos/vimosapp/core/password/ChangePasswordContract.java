package com.karol.vimos.vimosapp.core.password;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 17/02/17.
 */

// changes user password

public interface ChangePasswordContract {
    interface View {

        void onChangePasswordSuccess(FirebaseUser message);

        void onChangePasswordFailure(String message);

    }

    interface Presenter {

        void changePassword(Activity activity, FirebaseUser firebaseUser, String password);

    }

    interface Interactor {

        void performChangePassword(Activity activity, FirebaseUser firebaseUser, String password);
    }

    interface OnChangePasswordListener {

        void onSuccess(String message);

        void onFailure(String message);
    }
}
