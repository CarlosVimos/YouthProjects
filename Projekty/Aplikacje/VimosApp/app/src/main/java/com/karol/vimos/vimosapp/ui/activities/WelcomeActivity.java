package com.karol.vimos.vimosapp.ui.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.karol.vimos.vimosapp.R;

public class WelcomeActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_MS = 2000;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                // check if user is already logged in or not
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // if logged in redirect the user to user listing activity
                    SelectNavigationActivity.startActivity(WelcomeActivity.this);
                } else {
                    // otherwise redirect the user to login activity
                    LoginActivity.startIntent(WelcomeActivity.this);
                }
                finish();
            }
        };

        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);

    }
}
