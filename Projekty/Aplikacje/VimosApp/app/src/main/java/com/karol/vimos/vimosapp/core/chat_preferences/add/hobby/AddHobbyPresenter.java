package com.karol.vimos.vimosapp.core.chat_preferences.add.hobby;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.core.name.AddNameContract;
import com.karol.vimos.vimosapp.core.name.AddNameInteractor;

/**
 * Created by Vimos on 17/05/17.
 */

public class AddHobbyPresenter implements AddHobbyContract.Presenter, AddHobbyContract.OnHobbyDatabaseListener {

    private AddHobbyContract.View mView;
    private AddHobbyInteractor mAddHobbyInteractor;

    public AddHobbyPresenter(AddHobbyContract.View view) {
        this.mView = view;
        mAddHobbyInteractor = new AddHobbyInteractor(this);
    }

    @Override
    public void onSuccess(String message) {
        mView.onAddHobbySuccess(message);

    }

    @Override
    public void onFailure(String message) {
        mView.onAddHobbyFailure(message);
    }

    @Override
    public void addHobby(Context context, FirebaseUser firebaseUser, String name) {

        mAddHobbyInteractor.addHobbyToDatabase(context, firebaseUser, name);

    }
}