package com.karol.vimos.vimosapp.ui.activities.orange_activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.ui.activities.green_activity.GreenActivity;
import com.karol.vimos.vimosapp.ui.fragments.GreenFragment;
import com.karol.vimos.vimosapp.ui.fragments.OrangeFragment;

public class OrangeActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orange);

        init();

    }

    private void init() {

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_orangeMap,
                OrangeFragment.newInstance(),
                OrangeFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
