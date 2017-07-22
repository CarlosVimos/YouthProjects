package com.karol.vimos.vimosapp.core.registration;

import android.app.Activity;
import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 15/02/17.
 */

public interface RegisterContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void register(Activity activity, String email, String password, ProgressDialog progressDialog);
    }

    interface Interactor {
        void performFirebaseRegistration(Activity activity, String email, String password);
    }

    interface OnRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);

        void onFailure(String message);
    }
}
