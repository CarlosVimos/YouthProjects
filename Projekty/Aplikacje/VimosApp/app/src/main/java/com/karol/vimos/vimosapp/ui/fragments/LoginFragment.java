package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.ui.activities.ResetPasswordActivity;
import com.karol.vimos.vimosapp.ui.activities.RegisterActivity;
import com.karol.vimos.vimosapp.ui.activities.SelectNavigationActivity;
import com.karol.vimos.vimosapp.core.login.LoginContract;
import com.karol.vimos.vimosapp.core.login.LoginPresenter;
import com.karol.vimos.vimosapp.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Vimos on 15/02/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, LoginContract.View {


    private LoginPresenter mLoginPresenter;

    private EditText mETxtEmail, mETxtPassword;
    private Button mBtnLogin, mBtnRegister, mBtnResetPassword;

    private ImageView ivLogo;
    private View layoutView;

    Drawable drLogo;



    private ProgressDialog mProgressDialog;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        bindViews(fragmentView);

        return fragmentView;
    }

    private void bindViews(View view) {
        mETxtEmail = (EditText) view.findViewById(R.id.edit_text_email_id);
        mETxtPassword = (EditText) view.findViewById(R.id.edit_text_password);
        mBtnLogin = (Button) view.findViewById(R.id.button_login);
        mBtnRegister = (Button) view.findViewById(R.id.button_register);
        mBtnResetPassword = (Button) view.findViewById(R.id.button_reset_password);

        layoutView = view.findViewById(R.id.layout_fragment_login_id);
      //  Glide.with(getContext()).load(R.drawable.new_blue_bck).into(layoutView);
       // Glide.with(layoutView).load(R.drawable.new_blue_bck);
//        Glide.with(getContext()).load(R.drawable.new_blue_bck).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//
//
//
//            }
//        });
//
//
//        Picasso.with(getContext()).load(R.drawable.new_blue_bck).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                layoutView.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        });
//
//
//
//
        ivLogo = (ImageView) view.findViewById(R.id.iv_login_logo_id);
        Glide.with(getContext()).load(R.drawable.logo_vimos_with_text_white).into(ivLogo);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {

        mLoginPresenter = new LoginPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnResetPassword.setOnClickListener(this);


        //setDummyCredentials();
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_login:
                onLogin(view);
                break;
            case R.id.button_register:
                onRegister(view);
                break;
            case R.id.button_reset_password:
                //sendResetPassword();
                toResetPasswordActivity();
                break;
        }

    }

    private void sendResetPassword() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        ToastUtil.shortToast(getActivity(), mETxtEmail.getText().toString());
        if (mETxtEmail.getText().toString() != null) {
            auth.sendPasswordResetEmail(mETxtEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("SendResetPassword", "Email sent.");
                            }
                        }
                    });

        }

    }

    private void toResetPasswordActivity() {

        ResetPasswordActivity.startIntent(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }


    private void onLogin(View view) {

        String emailId = mETxtEmail.getText().toString();
        String password = mETxtPassword.getText().toString();
//        String emailId = "kar@kar.com";
//        String password = "karkar";

            mLoginPresenter.login(getActivity(), emailId, password, mProgressDialog);

    }

    private void onRegister(View view) {
        RegisterActivity.startActivity(getActivity());
    }

    @Override
    public void onLoginSuccess(String message) {

        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT).show();
//        toastUtil.shortToast("Logged in seccess!");
        SelectNavigationActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onLoginFailure(String message) {

        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public String getEmail() {
        return mETxtEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return mETxtPassword.getText().toString();
    }
}
