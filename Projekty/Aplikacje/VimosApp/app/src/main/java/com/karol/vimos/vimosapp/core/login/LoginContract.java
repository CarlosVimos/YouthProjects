package com.karol.vimos.vimosapp.core.login;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Karol Gruetzmacher on 15/02/17.
 */

public interface LoginContract {
    interface View {
        void onLoginSuccess(String message);

        void onLoginFailure(String message);

        String getEmail();

        String getPassword();


    }

    interface Presenter {
        void login(Activity activity, String email, String password, ProgressDialog progressDialog);
    }

    interface Interactor {
        void performFirebaseLogin(Activity activity, String email, String password);
    }

    interface OnLoginListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
