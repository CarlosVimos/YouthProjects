package com.karol.vimos.vimosapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.red.SetRedPositionContract;
import com.karol.vimos.vimosapp.core.red.SetRedPositionPresenter;
import com.karol.vimos.vimosapp.ui.activities.red_activity.RedActivity;
import com.karol.vimos.vimosapp.ui.adapters.UserListingPagerAdapter;
import com.karol.vimos.vimosapp.utils.ToastUtil;

public class UserListingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    double myLatitude, myLongitude;
    GeoFire redGeo;
    DatabaseReference ref_red;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listing);

        ref_red = FirebaseDatabase.getInstance().getReference("geofire/red");
        redGeo = new GeoFire(ref_red);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        ToastUtil.shortToast(this, "onCreate in Listining");


        bindViews();
        init();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        RedActivity.startActivity(getApplicationContext());
        startActivity(new Intent(UserListingActivity.this, RedActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // ToastUtil.shortToast(this, "onDestroy in Listining");
        Log.e("   Listinig_ACTIVITY   ", "on    Destroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  ToastUtil.shortToast(this, "onResume in Listining");
    }

    @Override
    protected void onStart() {
        super.onStart();
       // ToastUtil.shortToast(this, "onStart in Listining");
    }

    @Override
    protected void onStop() {
        super.onStop();
       // ToastUtil.shortToast(this, "onStop in Listining");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ToastUtil.shortToast(this, "onRestart in Listining");
    }


    private void bindViews() {
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayoutUserListing = (TabLayout) findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        // set the toolbar


        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getSupportFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

    }



}
