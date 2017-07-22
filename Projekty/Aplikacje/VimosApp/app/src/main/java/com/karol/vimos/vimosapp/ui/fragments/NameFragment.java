package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.name.AddNameContract;
import com.karol.vimos.vimosapp.core.name.AddNamePresenter;
import com.karol.vimos.vimosapp.ui.activities.SelectNavigationActivity;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 15/02/17.
 */

public class NameFragment extends Fragment implements View.OnClickListener, AddNameContract.View{

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String name, strAdded1;

    private AddNamePresenter mAddNamePresenter;


    private TextView mTVdisplayName, mTVsetName;
    private EditText mETname;
    private Button mBtnGoToSelect, mBtnSetName;

    private ProgressDialog mProgressDialog;

    public static NameFragment newInstance() {
        Bundle args = new Bundle();
        NameFragment fragment = new NameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_name, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        init();


    }

    private void bindViews(View view) {

        mETname = (EditText) view.findViewById(R.id.et_name_id);
        mBtnSetName = (Button) view.findViewById(R.id.btn_set_name);
        mTVdisplayName = (TextView) view.findViewById(R.id.tv_display_name);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    private void init() {
        mAddNamePresenter = new AddNamePresenter(this);
        mBtnSetName.setOnClickListener(this);
    }

    private void onAddName() {

        name = mETname.getText().toString().trim();

//        if (TextUtils.isEmpty(name)) {
//
//            ToastUtil.shortToast(getContext(), "Please, enter your name");
//
//        }
//
//        mAddNamePresenter.addName(getContext(), firebaseUser, name);
        if (!name.equals("")) {

            if (TextUtils.isEmpty(name)) {

                ToastUtil.shortToast(getContext(), strAdded1);

            }

            mAddNamePresenter.addName(getContext(), firebaseUser, name);
        }
        else {

            ToastUtil.shortToast(getContext(), strAdded1);
        }


    }


    @Override
    public void onClick(View v) {

        int vId = v.getId();

        switch (vId) {
            case R.id.btn_set_name:
                onAddName();
                break;

            case R.id.btn_go_to_name:
               // toNameActivity();


        }


    }

    @Override
    public void onAddNameSuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        SelectNavigationActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onAddNameFailure(String message) {

    }

    private void setString() {

        strAdded1 = getString(R.string.str_added_name);


    }
}
