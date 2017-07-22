package com.karol.vimos.vimosapp.core.remove.user;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Vimos on 17/02/17.
 */

public class RemoveUserPresenter implements RemoveUserContract.Presenter, RemoveUserContract.OnRemoveUserListener {

    private RemoveUserContract.View mView;
    private RemoveUserInteractor mRemoveUserInteractor;

    public RemoveUserPresenter(RemoveUserContract.View removeUserView) {
        this.mView = removeUserView;
        mRemoveUserInteractor = new RemoveUserInteractor(this);
    }



    @Override
    public void onSuccess(String message) {
        mView.onRemoveUserSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mView.onRemoveUserFailure(message);

    }

    @Override
    public void remove(Activity activity, FirebaseUser firebaseUser) {

        mRemoveUserInteractor.performRemoveUser(activity, firebaseUser);

    }
}
