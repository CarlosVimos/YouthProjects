package com.karol.vimos.vimosapp.core.connection;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Vimos on 26/05/17.
 */

public class ConnectionPresenter implements ConnectionContract.Presenter, ConnectionContract.OnConnectionDatabaseListener {

    private ConnectionContract.View mView;
    private ConnectionInteractor mConnectionInteractor;

    public ConnectionPresenter(ConnectionContract.View view) {
        this.mView = view;
        mConnectionInteractor = new ConnectionInteractor(this);
    }

    @Override
    public void ConnectionState(Context context, DatabaseReference databaseReference, DatabaseReference ref_online) {

        mConnectionInteractor.checkConnectionState(context, databaseReference, ref_online);

    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onFailure(String message) {

    }
}
