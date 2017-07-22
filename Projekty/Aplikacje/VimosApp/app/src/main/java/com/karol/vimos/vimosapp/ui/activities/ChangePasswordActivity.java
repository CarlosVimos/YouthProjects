package com.karol.vimos.vimosapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.ui.fragments.ChangePasswordFragment;
import com.karol.vimos.vimosapp.ui.fragments.RegisterFragment;

public class ChangePasswordActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
    }

    private void init() {

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_change_password,
                ChangePasswordFragment.newInstance(),
                ChangePasswordFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

}
