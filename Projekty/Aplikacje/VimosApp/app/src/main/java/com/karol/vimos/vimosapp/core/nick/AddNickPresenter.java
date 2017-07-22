package com.karol.vimos.vimosapp.core.nick;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.utils.SensitiveWords;
import com.karol.vimos.vimosapp.utils.ToastUtil;

import java.util.Arrays;

/**
 * Created by Vimos on 15/02/17.
 */

public class AddNickPresenter implements AddNickContract.Presenter, AddNickContract.OnNickDatabaseListener {

    private AddNickContract.View mView;
    private AddNickInteractor mAddNickInteractor;

    public AddNickPresenter(AddNickContract.View view) {
        this.mView = view;
        mAddNickInteractor = new AddNickInteractor(this);
    }

    @Override
    public void onSuccess(String message) {
        mView.onAddNickSuccess(message);

    }

    @Override
    public void onFailure(String message) {
        mView.onAddNickFailure(message);
    }

    @Override
    public void addNick(Context context, FirebaseUser firebaseUser, String nick) {

        String[] sensitiveWords = {"kurwa", "chuj", "jebac", "jebak", "pizda", "dupa", "pierdole", "spierdalaj", "napierdalaj" };
        String nickInLowerCase = nick.toLowerCase();
        int check = 2;

        for (String sensitiveWord : sensitiveWords) {

            if (!nickInLowerCase.contains(sensitiveWord)) {

                check = 1; // nick is correct and does not include any inappropriate word

            } else {

                ToastUtil.shortToast(context, "Brzydkie słowo.");
                check = 0;
                break;

            }

        }

        if (check == 1) {

            mAddNickInteractor.addNickToDatabase(context, firebaseUser, nick);


        }

       // if (nick.contains())




    }

}
