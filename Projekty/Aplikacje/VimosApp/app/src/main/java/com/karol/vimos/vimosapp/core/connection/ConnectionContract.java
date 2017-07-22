package com.karol.vimos.vimosapp.core.connection;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Vimos on 26/05/17.
 */

public interface ConnectionContract {

    interface View {

       void onConnectionStateSuccess();

       void onConnectionStateFailure();

    }

    interface Presenter {

        void ConnectionState(Context context, DatabaseReference databaseReference, DatabaseReference ref_online);

    }

    interface Interactor {

        void checkConnectionState(Context context, DatabaseReference databaseReference, DatabaseReference ref_online);

    }

    interface OnConnectionDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
