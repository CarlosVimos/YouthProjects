package com.karol.vimos.vimosapp.core.settings.set.radius;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 16/02/17.
 */

public class SetRadiusPresenter implements SetRadiusContract.Presenter, SetRadiusContract.OnSetRadiusDatabaseListener {

    private SetRadiusContract.View mView;
    private SetRadiusInteractor mSetRadiusInteractor;

    public SetRadiusPresenter(SetRadiusContract.View view) {
        this.mView = view;
        mSetRadiusInteractor = new SetRadiusInteractor(this);
    }

    @Override
    public void onSuccess(String message) {
        mView.onSetRadiusSuccess(message);

    }

    @Override
    public void onFailure(String message) {
        mView.onSetRadiusFailure(message);

    }

    @Override
    public void setRadius(Context context, FirebaseUser firebaseUser, int radius) {

        mSetRadiusInteractor.setRadiusToDatabase(context, firebaseUser, radius);

    }
}
