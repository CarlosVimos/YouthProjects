package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.password.ChangePasswordPresenter;
import com.karol.vimos.vimosapp.ui.activities.LoginActivity;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 19/02/17.
 */

public class ChangeEmailFragment extends Fragment implements View.OnClickListener {


    private EditText mETnewEmail;
    private Button mBtnSetEmail;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    String newEmail;

    private ProgressDialog mProgressDialog;

    public static ChangeEmailFragment newInstance() {
        Bundle args = new Bundle();
        ChangeEmailFragment fragment = new ChangeEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_change_email, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {

        mETnewEmail = (EditText) view.findViewById(R.id.et_newEmailId);
        mBtnSetEmail = (Button) view.findViewById(R.id.btn_changeEmailId);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnSetEmail.setOnClickListener(this);
    }

    private void onSetNewEmail() {

        newEmail = mETnewEmail.getText().toString().trim();


        //  mChangePasswordPresenter.changePassword(getActivity(), firebaseUser, newPassword);
        if (firebaseUser != null && !mETnewEmail.getText().toString().trim().equals("")) {

                firebaseUser.updateEmail(mETnewEmail.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    updateEmailInUserDatabase(firebaseUser.getUid(), newEmail);
                                    ToastUtil.shortToast(getContext(), "update email...");
                                    //LoginActivity.startIntent(getContext());
                                    LoginActivity.startIntent(getActivity(),
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    //    mOnChangePasswordListener.onSuccess("Update password completed.");
                                } else {
                                    //  mOnChangePasswordListener.onFailure("Failed to update passowrd");\
                                    ToastUtil.shortToast(getContext(), "failed to update email...");
                                    ToastUtil.shortToast(getContext(), "Please Log out and Log in again");
                                    // sometimes firebase causes error that the login time is too long and there is needed re-login and change password.
                                }


                            }
                        });
            }
            else if
            (mETnewEmail.getText().toString().trim().equals("")) {
            mETnewEmail.setError("Enter email");
            // mProgressDialog.show();
        }
    }

    private void updateEmailInUserDatabase(String uid, String name) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_EMAIL)
                .setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                  //  mOnNameDatabaseListener.onSuccess("Name added successfully to user database");
                } else {

                   // mOnNameDatabaseListener.onFailure("Name failed added to user database");
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_changeEmailId:
                onSetNewEmail();
                break;
        }

    }
}
