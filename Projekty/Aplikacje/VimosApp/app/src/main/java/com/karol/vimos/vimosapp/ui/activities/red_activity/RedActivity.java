package com.karol.vimos.vimosapp.ui.activities.red_activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.ui.fragments.RedFragment;
import com.karol.vimos.vimosapp.utils.ToastUtil;

public class RedActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    DatabaseReference ref_red, ref_myName, ref_users_red, ref_online, ref_connection;



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RedActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);

        init();
        ToastUtil.shortToast(this, "onCreate in Activity");

        ref_online = FirebaseDatabase.getInstance().getReference("red_online");
        ref_connection = FirebaseDatabase.getInstance().getReference(".info/connected");

//        ref_online.onDisconnect().setValue("0");
//        ref_connection.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//
//                    //ref_online.setValue("1");
//                    //System.out.println("connected");
//                    ref_online.setValue("1");
//                } else {
//                    //System.out.println("not connected");
//                    //ref_online.setValue("0");
//                    ref_online.onDisconnect().setValue("0");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                //System.err.println("Listener was cancelled");
//            }
//        });





    }



    @Override
    protected void onResume() {
        super.onResume();

        ToastUtil.shortToast(this, "onResume in Activity");
    }

    @Override
    protected void onPause() {
        super.onPause();

        ToastUtil.shortToast(this, "onPause in Activity");
    }

    @Override
    protected void onStop() {
        super.onStop();

      //  ToastUtil.shortToast(this, "onStop in Activity");
    }

    @Override
    protected void onStart() {
        super.onStart();

        ToastUtil.shortToast(this, "onStart in Activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

      //  ref_online.onDisconnect().setValue("0");
        ToastUtil.shortToast(this, "onDestroy in Activity");
       // Log.e("   RED_ACTIVITY   ", "on    Destroy"); - it displays
    }






    private void init() {

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_redMap,
                RedFragment.newInstance(),
                RedFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }



}
