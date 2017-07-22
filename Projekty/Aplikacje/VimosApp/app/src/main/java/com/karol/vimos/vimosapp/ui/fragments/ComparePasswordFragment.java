package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.password.ChangePasswordContract;
import com.karol.vimos.vimosapp.core.password.ChangePasswordPresenter;

/**
 * Created by Vimos on 18/02/17.
 */

public class ComparePasswordFragment extends Fragment implements View.OnClickListener {

    private EditText mETcurrentPassword;
    private Button mBtnComparePassword;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    String currentPassword;

    private ProgressDialog mProgressDialog;

    public static ComparePasswordFragment newInstance() {
        Bundle args = new Bundle();
        ComparePasswordFragment fragment = new ComparePasswordFragment();
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

        mETcurrentPassword = (EditText) view.findViewById(R.id.et_currentPasswordId);
        mBtnComparePassword = (Button) view.findViewById(R.id.btn_comparePasswordId);
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

        mBtnComparePassword.setOnClickListener(this);
    }

    private void onComparePassword() {



    }


    @Override
    public void onClick(View v) {

    }
}
