package com.karol.vimos.vimosapp.core.remove.user;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 17/02/17.
 */

public interface RemoveUserContract {
    interface View {
        void onRemoveUserSuccess(String message);

        void onRemoveUserFailure(String message);
    }

    interface Presenter {

        void remove(Activity activity, FirebaseUser firebaseUser);
    }

    interface Interactor {

        void performRemoveUser(Activity activity, FirebaseUser firebaseUser);
    }

    interface OnRemoveUserListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
