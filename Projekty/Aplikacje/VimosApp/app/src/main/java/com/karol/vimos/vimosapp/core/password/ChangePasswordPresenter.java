package com.karol.vimos.vimosapp.core.password;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.core.registration.RegisterContract;

/**
 * Created by Vimos on 17/02/17.
 */

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter, RegisterContract.OnRegistrationListener {

    private ChangePasswordContract.View mView;
    private ChangePasswordInteractor mChangePasswordInteractor;

    public ChangePasswordPresenter(ChangePasswordContract.View changePasswordView) {
        this.mView = changePasswordView;
    }

    @Override
    public void changePassword(Activity activity, FirebaseUser firebaseUser, String password) {

        mChangePasswordInteractor.performChangePassword(activity, firebaseUser, password);

    }


    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mView.onChangePasswordSuccess(firebaseUser);

    }

    @Override
    public void onFailure(String message) {

        mView.onChangePasswordFailure(message);
    }
}
