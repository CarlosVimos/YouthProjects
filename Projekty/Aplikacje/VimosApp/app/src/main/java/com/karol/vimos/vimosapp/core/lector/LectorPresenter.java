package com.karol.vimos.vimosapp.core.lector;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 25/02/17.
 */

public class LectorPresenter implements LectorContract.Presenter, LectorContract.OnLectorDatabaseListener {

    private LectorContract.View mView;
    private LectorInteractor mLectorInteractor;

    public LectorPresenter(LectorContract.View view) {

        this.mView = view;
        mLectorInteractor = new LectorInteractor(this);

    }

    @Override
    public void onSuccess(String message) {

        mView.onLectorSuccess(message);

    }

    @Override
    public void onFailure(String message) {

        mView.onLectorFailure(message);

    }

    @Override
    public void Lector(Context context, FirebaseUser firebaseUser, String lector) {

        mLectorInteractor.addLectorToDatabase(context, firebaseUser, lector);

    }
}
