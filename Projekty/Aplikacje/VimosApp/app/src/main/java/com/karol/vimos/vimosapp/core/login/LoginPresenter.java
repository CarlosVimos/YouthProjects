package com.karol.vimos.vimosapp.core.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 15/02/17.
 */

public class LoginPresenter implements LoginContract.Presenter, LoginContract.OnLoginListener {
    private LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;


    public LoginPresenter(LoginContract.View loginView) {
        this.mLoginView = loginView;
        mLoginInteractor = new LoginInteractor(this);
    }

    @Override
    public void login(Activity activity, String email, String password, ProgressDialog progressDialog) {

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(activity, R.string.login_email_error_empty_input, Toast.LENGTH_SHORT).show();
            return;

        }
        else if (TextUtils.isEmpty(password)) {

            Toast.makeText(activity, R.string.login_pass_error_empty_input, Toast.LENGTH_SHORT).show();
            return;

        }
        else if (!email.contains("@")) {

            Toast.makeText(activity, R.string.login_email_error_invalid_input, Toast.LENGTH_SHORT).show();

        }


        else {

            mLoginInteractor.performFirebaseLogin(activity, email, password);
            progressDialog.show();

        }


    }

    @Override
    public void onSuccess(String message) {
        mLoginView.onLoginSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mLoginView.onLoginFailure(message);
    }


    private void checkInputText(Activity activity, String email, String password) {

    }

}
