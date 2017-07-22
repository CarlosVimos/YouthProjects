package com.karol.vimos.vimosapp.core.settings.get.radius;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.models.User;

/**
 * Created by Vimos on 16/02/17.
 */

public class GetRadiusPresenter implements GetRadiusContract.Presenter, GetRadiusContract.OnGetRadiusDatabaseListener {


    private GetRadiusContract.View mView;
    private GetRadiusInteractor mGetRadiusInteractor;

    public GetRadiusPresenter(GetRadiusContract.View view) {
        this.mView = view;
        mGetRadiusInteractor = new GetRadiusInteractor(this);
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onGetRadiusFailure(String message) {

    }

    @Override
    public void getRadius(Context context, FirebaseUser firebaseUser, int userRadius) {

        mGetRadiusInteractor.getRadiusFromDatabase(context, firebaseUser, userRadius);



    }
}
