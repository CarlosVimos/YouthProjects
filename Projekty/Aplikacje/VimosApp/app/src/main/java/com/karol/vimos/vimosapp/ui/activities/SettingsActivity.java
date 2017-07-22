package com.karol.vimos.vimosapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.ui.fragments.NameFragment;
import com.karol.vimos.vimosapp.ui.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_settings,
                SettingsFragment.newInstance(),
                SettingsFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
