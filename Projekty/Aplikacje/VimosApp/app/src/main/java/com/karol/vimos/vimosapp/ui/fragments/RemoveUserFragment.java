package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.remove.user.RemoveUserContract;
import com.karol.vimos.vimosapp.core.remove.user.RemoveUserPresenter;
import com.karol.vimos.vimosapp.ui.activities.LoginActivity;
import com.karol.vimos.vimosapp.ui.activities.NickActivity;

/**
 * Created by Vimos on 17/02/17.
 */

public class RemoveUserFragment extends Fragment implements View.OnClickListener, RemoveUserContract.View {

    private Button mBtnRemoveUser;
    private RemoveUserPresenter mRemoveUserPresenter;
    private ProgressDialog mProgressDialog;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    public static RemoveUserFragment newInstance() {
        Bundle args = new Bundle();
        RemoveUserFragment fragment = new RemoveUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_remove_user, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {

        mBtnRemoveUser = (Button) view.findViewById(R.id.btn_remove_userId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }

    private void init() {



        mRemoveUserPresenter = new RemoveUserPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnRemoveUser.setOnClickListener(this);

    }

    private void onRemoveUser(View v) {

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        mRemoveUserPresenter.remove(getActivity(), firebaseUser);
        mProgressDialog.show();
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_remove_userId:
                onRemoveUser(v);
                break;
        }

    }

    @Override
    public void onRemoveUserSuccess(String message) {
        mProgressDialog.setMessage("User deleted.");
        Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onRemoveUserFailure(String message) {
        mProgressDialog.dismiss();
        mProgressDialog.setMessage("Remove user: ERROR");
        Log.e("REMOVE_USER", "onRemoveUserFailure: " + message);
        Toast.makeText(getActivity(), "Remove failed. Please Log in again and try delete.+\n" + message, Toast.LENGTH_LONG).show();

    }
}
