package com.karol.vimos.vimosapp.core.name;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.core.nick.AddNickContract;
import com.karol.vimos.vimosapp.core.nick.AddNickInteractor;

/**
 * Created by Vimos on 16/02/17.
 */

public class AddNamePresenter implements AddNameContract.Presenter, AddNameContract.OnNameDatabaseListener {

    private AddNameContract.View mView;
    private AddNameInteractor mAddNameInteractor;

    public AddNamePresenter(AddNameContract.View view) {
        this.mView = view;
        mAddNameInteractor = new AddNameInteractor(this);
    }

    @Override
    public void onSuccess(String message) {
        mView.onAddNameSuccess(message);

    }

    @Override
    public void onFailure(String message) {
        mView.onAddNameFailure(message);
    }

    @Override
    public void addName(Context context, FirebaseUser firebaseUser, String name) {

        mAddNameInteractor.addNameToDatabase(context, firebaseUser, name);

    }
}
