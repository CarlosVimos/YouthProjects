package com.karol.vimos.vimosapp.core.settings.get.radius;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.models.User;

/**
 * Created by Vimos on 16/02/17.
 */

public interface GetRadiusContract {
    interface View {

        void onGetRadiusSuccess(String message);

        void onGetRadiusFailure(String message);

    }

    interface Presenter {
        void getRadius(Context context, FirebaseUser firebaseUser, int userRadius);
    }

    interface Interactor {
        void getRadiusFromDatabase(Context context, FirebaseUser firebaseUser, int userRadius);
    }

    interface OnGetRadiusDatabaseListener {
        void onSuccess(String message);

       void onGetRadiusFailure(String message);
    }

}
