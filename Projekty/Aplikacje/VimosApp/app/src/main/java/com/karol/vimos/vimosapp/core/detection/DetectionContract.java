package com.karol.vimos.vimosapp.core.detection;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 25/02/17.
 */

public interface DetectionContract {

    interface View {
        void onDetectionSuccess(String message);

        void onDetectionFailure(String message);

    }

    interface Presenter {

        void RedDetection(Context context, FirebaseUser firebaseUser, int detection);
        void GreenDetection(Context context, FirebaseUser firebaseUser, int detection);
        void OrangeDetection(Context context, FirebaseUser firebaseUser, int detection);
    }

    interface Interactor {

        void addRedDetectionToDatabase(Context context, FirebaseUser firebaseUser, int detection);
        void addGreenDetectionToDatabase(Context context, FirebaseUser firebaseUser, int detection);
        void addOrangeDetectionToDatabase(Context context, FirebaseUser firebaseUser, int detection);
    }

    interface OnDetectionDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
