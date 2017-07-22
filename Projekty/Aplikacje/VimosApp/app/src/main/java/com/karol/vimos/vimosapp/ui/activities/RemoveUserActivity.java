package com.karol.vimos.vimosapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.ui.fragments.RegisterFragment;
import com.karol.vimos.vimosapp.ui.fragments.RemoveUserFragment;

public class RemoveUserActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RemoveUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        init();
    }

    private void init() {

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_remove_user,
                RemoveUserFragment.newInstance(),
                RemoveUserFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

}
