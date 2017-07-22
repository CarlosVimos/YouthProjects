package com.karol.vimos.vimosapp.core.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;

/**
 * Created by Vimos on 15/02/17.
 */

public class RegisterPresenter implements RegisterContract.Presenter, RegisterContract.OnRegistrationListener {
    private RegisterContract.View mRegisterView;
    private RegisterInteractor mRegisterInteractor;

    public RegisterPresenter(RegisterContract.View registerView) {
        this.mRegisterView = registerView;
        mRegisterInteractor = new RegisterInteractor(this);
    }

    @Override
    public void register(Activity activity, String email, String password, ProgressDialog progressDialog) {

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(activity, R.string.login_email_error_empty_input, Toast.LENGTH_SHORT).show();
           // return;

        }
        else if (TextUtils.isEmpty(password)) {

            Toast.makeText(activity, R.string.login_pass_error_empty_input, Toast.LENGTH_SHORT).show();
          //  return;

        }
        else if (!email.contains("@")) {

            Toast.makeText(activity, R.string.login_email_error_invalid_input, Toast.LENGTH_SHORT).show();

        }


        else {

            mRegisterInteractor.performFirebaseRegistration(activity, email, password);
            progressDialog.show();

        }

    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mRegisterView.onRegistrationSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);
    }
}

