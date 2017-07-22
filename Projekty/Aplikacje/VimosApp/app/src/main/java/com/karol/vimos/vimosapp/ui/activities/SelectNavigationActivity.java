package com.karol.vimos.vimosapp.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.karol.vimos.vimosapp.MyApplication;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.logout.LogoutContract;
import com.karol.vimos.vimosapp.core.logout.LogoutPresenter;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.ui.activities.blue.activity.BlueActivity;
import com.karol.vimos.vimosapp.ui.activities.green_activity.GreenActivity;
import com.karol.vimos.vimosapp.ui.activities.orange_activity.OrangeActivity;
import com.karol.vimos.vimosapp.ui.activities.red_activity.RedActivity;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.NetworkConnectionUtil;
import com.karol.vimos.vimosapp.utils.ToastUtil;
import com.skyfishjy.library.RippleBackground;

import java.util.concurrent.CountDownLatch;

public class SelectNavigationActivity extends AppCompatActivity {

    private static final String TAG = "SelectNaviAct";


    DatabaseReference ref_red, ref_database, ref_database2;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    GeoFire redGeoFire;
    GeoQuery myRedGeoQuery;
    FloatingActionButton floatingActionButton;

    private Trace mTrace;
    private CountDownLatch mNumStartupTasks = new CountDownLatch(2);

    private String STARTUP_TRACE_NAME = "startup_trace";
    private String REQUESTS_COUNTER_NAME = "requests sent";
    private String FILE_SIZE_COUNTER_NAME = "file size";

    RippleBackground rippleBackground;
    int size = 100;
    int myRadius, red_detection, green_detection, orange_detection;
    int true_detection = 1;
    int false_detection = 0;

    TextView tvRadius, tvObjectsDetection, tvInternetInfo;
    String strInternetInfo;
    String red_string,
           green_string,
           orange_string;

    View viewInternetInfo;
    View viewInfoLayout;




    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectNavigationActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, SelectNavigationActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_navigation);


        tvRadius = (TextView) findViewById(R.id.tv_radius_select_id);
        tvObjectsDetection = (TextView) findViewById(R.id.tv_objects_det_id);
        tvInternetInfo = (TextView) findViewById(R.id.tv_internet_info_id);
        viewInternetInfo = findViewById(R.id.rel_layout_internet_info_id);
        viewInfoLayout = findViewById(R.id.rel_layout_navi_id);


        // begin tracing app startup tasks.
        mTrace = FirebasePerformance.getInstance().newTrace(STARTUP_TRACE_NAME);
        Log.d(TAG, "Starting trace");

                red_string = getString(R.string.red_string);
                green_string = getString(R.string.green_string);
                orange_string = getString(R.string.orange_string);
                strInternetInfo = getString(R.string.internet_info);


//        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatButtonId);
//        floatingActionButton.setSize(FloatingActionButton.SIZE_NORMAL);
//        floatingActionButton.setBackgroundColor(Color.RED);



        ref_red = FirebaseDatabase.getInstance().getReference("geofire/red");
        redGeoFire = new GeoFire(ref_red);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //redGeoFire.removeLocation(firebaseUser.getUid());

        rippleBackground = (RippleBackground) findViewById(R.id.contentNavi);

        rippleBackground.startRippleAnimation();

       // ToastUtil.shortToast(this, "onCreate in SelectNaviAct");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_database = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid());
        ref_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                myRadius = user.getRadius();

                    tvRadius.setText(String.valueOf(myRadius) + " " + "m");
                    setTvObjectsDetection();
                red_detection = user.getRed_detection();
                green_detection = user.getGreen_detection();
                orange_detection = user.getOrange_detection();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        isInternetAvailable();

        ref_database2 = database.getReference().child(Constants.ARG_USERS).child(firebaseUser.getUid()).child(Constants.ARG_DETECTION);
        ref_database2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);


                red_detection = user.getRed_detection();
                green_detection = user.getGreen_detection();
                orange_detection = user.getOrange_detection();

                setTvObjectsDetection();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Wait for app startup tasks to complete asynchronously and stop the trace.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mNumStartupTasks.await();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Unable to wait for startup task completion.");
                } finally {
                    Log.d(TAG, "Stopping trace");
                    mTrace.stop();
                    SelectNavigationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SelectNavigationActivity.this, "Trace completed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();



    }

    @Override
    public void onBackPressed() {

       // showBackButtonOptionAlertToUser();


        super.onBackPressed();


    }

    @Override
    protected void onResume() {
        super.onResume();

        ToastUtil.shortToast(this, "onResume in SelectNaviAct");
       redGeoFire.removeLocation(firebaseUser.getUid());
       rippleBackground.startRippleAnimation();

        isInternetAvailable();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ToastUtil.shortToast(this, "onDestr in SelectNaviAct");
        isInternetAvailable();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                rippleBackground.stopRippleAnimation();
            }
        }, 500);


    }

    @Override
    protected void onStop() {
        super.onStop();

        isInternetAvailable();
        ToastUtil.shortToast(this, "onStop in SelectNaviAct");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                rippleBackground.stopRippleAnimation();
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();

        isInternetAvailable();
        ToastUtil.shortToast(this, "onPause in SelectNaviAct");

        final  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                rippleBackground.stopRippleAnimation();
            }
        }, 500);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ToastUtil.shortToast(this, "onStart in SelectNaviAct");
    }

    private void showBackButtonOptionAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you wanna close your app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //startActivity(callGPSSettingIntent);
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                       // finish();
                        System.exit(1);

                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void settingsOnClick(View view) {
        SettingsActivity.startActivity(this);

        mNumStartupTasks.countDown();
    }

    public void toBlueOnClick(View view) {
        startActivity(new Intent(SelectNavigationActivity.this, BlueActivity.class));
    }

    public void toRedOnClick(View view) {
        RedActivity.startActivity(this);
    }

    public void toGreenOnClick(View view) {
        GreenActivity.startActivity(this);
    }

    public void toOrangeOnClick(View view) {
        OrangeActivity.startActivity(this);
    }

    public void setTvObjectsDetection() {

        if (red_detection == true_detection && green_detection == true_detection && orange_detection == true_detection) {

            tvObjectsDetection.setText(red_string + ", " + green_string + ", " + orange_string);

        } else if (red_detection == true_detection && green_detection == true_detection && orange_detection == false_detection) {

            tvObjectsDetection.setText(red_string + ", " + green_string);

        } else if (red_detection == true_detection && green_detection == false_detection && orange_detection == true_detection) {

            tvObjectsDetection.setText(red_string + ", " + orange_string);

        } else if (red_detection == true_detection && green_detection == false_detection && orange_detection == false_detection) {

            tvObjectsDetection.setText(red_string);

        } else if (red_detection == false_detection && green_detection == true_detection && orange_detection == true_detection) {

            tvObjectsDetection.setText(green_string + ", " + orange_string);

        } else if (red_detection == false_detection && green_detection == true_detection && orange_detection == false_detection) {

            tvObjectsDetection.setText(green_string);

        } else if (red_detection == false_detection && green_detection == false_detection && orange_detection == true_detection) {

            tvObjectsDetection.setText(orange_string);

        }


    }

    public void isInternetAvailable() {

        if (!NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {


            ToastUtil.shortToast(getApplicationContext(), "Brak transferu danych. Włącz internet.");
            viewInternetInfo.setVisibility(View.VISIBLE);
            tvInternetInfo.setText(strInternetInfo);

        } else {
            viewInternetInfo.setVisibility(View.INVISIBLE);
        }

    }
}
