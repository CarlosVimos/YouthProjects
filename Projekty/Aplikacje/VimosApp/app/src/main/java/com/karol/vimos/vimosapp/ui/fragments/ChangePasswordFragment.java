package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.password.ChangePasswordContract;
import com.karol.vimos.vimosapp.core.password.ChangePasswordPresenter;
import com.karol.vimos.vimosapp.ui.activities.LoginActivity;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 17/02/17.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, ChangePasswordContract.View {

    private ChangePasswordPresenter mChangePasswordPresenter;

    private EditText mETnewPassword;
    private Button mBtnSetPassword;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    String newPassword;

    private ProgressDialog mProgressDialog;

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_change_password, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {

        mETnewPassword = (EditText) view.findViewById(R.id.et_newPasswordId);
        mBtnSetPassword = (Button) view.findViewById(R.id.btn_changePasswordId);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mChangePasswordPresenter = new ChangePasswordPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnSetPassword.setOnClickListener(this);
    }

    private void onSetNewPassword() {

        newPassword = mETnewPassword.getText().toString().trim();


      //  mChangePasswordPresenter.changePassword(getActivity(), firebaseUser, newPassword);
        if (firebaseUser != null && !mETnewPassword.getText().toString().trim().equals("")) {
            if (mETnewPassword.getText().toString().trim().length() < 6) {
                mETnewPassword.setError("Password too short, enter minimum 6 characters");
            } else {
                firebaseUser.updatePassword(mETnewPassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    ToastUtil.shortToast(getContext(), "update password...");
                                    //LoginActivity.startIntent(getContext());
                                    LoginActivity.startIntent(getActivity(),
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    //    mOnChangePasswordListener.onSuccess("Update password completed.");
                                } else {
                                    //  mOnChangePasswordListener.onFailure("Failed to update passowrd");\
                                    ToastUtil.shortToast(getContext(), "failed to update password...");
                                    ToastUtil.shortToast(getContext(), "Please Log out and Log in again");
                                    // sometimes firebase causes error that the login time is too long and there is needed re-login and change password.
                                }


                            }
                        });
            }
        }
        else if  (mETnewPassword.getText().toString().trim().equals("")) {
            mETnewPassword.setError("Enter password");
            // mProgressDialog.show();
        }
    }


    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_changePasswordId:
                onSetNewPassword();
                break;
        }


    }

    @Override
    public void onChangePasswordSuccess(FirebaseUser message) {
        mProgressDialog.setMessage("Password changed.");
        Toast.makeText(getActivity(), "Changed!", Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onChangePasswordFailure(String message) {
        mProgressDialog.dismiss();
        mProgressDialog.setMessage("Change password: ERROR");
        Log.e("CHANGE_PASSWORD_USER", "onChangePasswordUserFailure: " + message);
        Toast.makeText(getActivity(), "Change password. Please Log out AND Log in and try again.+\n" + message, Toast.LENGTH_LONG).show();


    }
}
