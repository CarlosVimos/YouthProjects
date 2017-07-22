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
import com.karol.vimos.vimosapp.core.detection.DetectionContract;
import com.karol.vimos.vimosapp.core.detection.DetectionPresenter;
import com.karol.vimos.vimosapp.core.lector.LectorContract;
import com.karol.vimos.vimosapp.core.lector.LectorPresenter;
import com.karol.vimos.vimosapp.ui.activities.NameActivity;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.nick.AddNickContract;
import com.karol.vimos.vimosapp.core.nick.AddNickPresenter;
import com.karol.vimos.vimosapp.ui.activities.SelectNavigationActivity;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 15/02/17.
 */

public class NickFragment extends Fragment implements View.OnClickListener, AddNickContract.View, DetectionContract.View, LectorContract.View {

   FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String nick, strAdded1;

    private AddNickPresenter mAddNickPresenter;
    private DetectionPresenter mDetectionPresenter;
    private LectorPresenter mLectorPresenter;


    private TextView mTVdisplayNick, mTVsetNick;
    private EditText mETnick;
    private Button mBtnGoToName, mBtnSetNick;

    private ProgressDialog mProgressDialog;

    public static NickFragment newInstance() {
        Bundle args = new Bundle();
        NickFragment fragment = new NickFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_nick, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        init();
        setString();


    }



    private void bindViews(View view) {

        mETnick = (EditText) view.findViewById(R.id.et_nick_id);
        mBtnGoToName = (Button) view.findViewById(R.id.btn_go_to_name);
        mBtnSetNick = (Button) view.findViewById(R.id.btn_set_nick);
        mTVdisplayNick = (TextView) view.findViewById(R.id.tv_display_nick);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    private void init() {
        mAddNickPresenter = new AddNickPresenter(this);
        mDetectionPresenter = new DetectionPresenter(this);
        mLectorPresenter = new LectorPresenter(this);
        mBtnSetNick.setOnClickListener(this);
        mBtnGoToName.setOnClickListener(this);
    }

    private void onAddNick() {

        nick = mETnick.getText().toString().trim();

//        if (TextUtils.isEmpty(nick)) {
//
//            ToastUtil.shortToast(getContext(), "Please, enter your nick");
//
//        }
//
//        mAddNickPresenter.addNick(getContext(), firebaseUser, nick);
        if (!nick.equals(""))
        {

            if (TextUtils.isEmpty(nick))
            {

                ToastUtil.shortToast(getContext(), strAdded1);

            }

            mAddNickPresenter.addNick(getContext(), firebaseUser, nick);

        }
        else
        {

            ToastUtil.shortToast(getContext(), strAdded1);

        }



    }

    // it sets all objects to detection (red, green, orange)
    private void addObjectToDetection() {

        int red_detection = 1;
        int orange_detection = 1;
        int green_detection = 1;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mDetectionPresenter.RedDetection(getContext(), firebaseUser, red_detection);
        mDetectionPresenter.GreenDetection(getContext(), firebaseUser, green_detection);
        mDetectionPresenter.OrangeDetection(getContext(), firebaseUser, orange_detection);

    }

    private void addLector() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mLectorPresenter.Lector(getContext(), firebaseUser, "pl");

    }


    @Override
    public void onAddNickSuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        NameActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onAddNickFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {

        int vId = v.getId();

        switch (vId) {
            case R.id.btn_set_nick:
                onAddNick();
                addObjectToDetection();
                addLector();
                break;

            case R.id.btn_go_to_name:
                toNameActivity();


        }

    }



    @Override
    public void onDetectionSuccess(String message) {

    }

    @Override
    public void onDetectionFailure(String message) {

    }

    @Override
    public void onLectorSuccess(String message) {

    }

    @Override
    public void onLectorFailure(String message) {

    }


    private void toNameActivity() {

        startActivity(new Intent(getActivity(), SelectNavigationActivity.class));

    }

    private void setString() {

        strAdded1 = getString(R.string.str_added_nick);


    }

}
