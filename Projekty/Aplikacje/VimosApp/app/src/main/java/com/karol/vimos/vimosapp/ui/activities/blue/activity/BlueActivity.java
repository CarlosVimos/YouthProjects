package com.karol.vimos.vimosapp.ui.activities.blue.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.connection.ConnectionContract;
import com.karol.vimos.vimosapp.core.connection.ConnectionPresenter;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.ui.activities.SelectNavigationActivity;
import com.karol.vimos.vimosapp.utils.BlueLector;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.NetworkConnectionUtil;
import com.karol.vimos.vimosapp.utils.SharedPrefUtil;
import com.karol.vimos.vimosapp.utils.ToastUtil;
import com.karol.vimos.vimosapp.utils.ViewNearestPanelColor;
import com.kongqw.radarscanviewlibrary.RadarScanView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BlueActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener, SensorEventListener, ConnectionContract.View {

    GoogleMap mGoogleMap;
    SupportMapFragment mSupportMapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker; // current marker location
    LocationManager mLocationManager;
    double myLatitude, myLongitude;

    public static Integer mRound;



    private ConnectionPresenter mConnectionPresenter;

    Trace myTrace;

    int distanceInPercentToGreen, distanceInPercentToOrange, distanceInPercentToRed, distanceInPercentToNearest;

    String k, strRadius, strRangeDetection, strObjects, strRadarInfo, strRed, strGreen, strOrange, strInternetInfo;
    String strPerson, strPeople, strAreDetected, strIsDetected, strTrainGoesAway, strTrainIsClosing, strGreenGoesAway, strGreenIsClosing;
    String twoLastNearestDistancesCloseOrAway;

    int distToGreenIfChanged;
    Integer volumeMediaPlayer;

    RadarScanView radarScanView;
    Integer roundEvery50Meters;

    // LogCat:

    int diff;

    private LatLng myLatLng;

    SharedPrefUtil sharedPrefUtil;

    Integer[] arrayRedSempahoreDrawable = new Integer[2];

    String TAG_GREEN_INCREASE_LAST = "G INCREASE -1";
    String TAG_GREEN_INCREASE_B_LAST = "G INCREASE -2";
    String TAG_GREEN_DECREASE_LAST = "G DECREASE -1";
    String TAG_GREEN_DECREASE_B_LAST = "G DECREASE -2";
    String TAG_ORANGE_NEAREST = "NEAREST ORANGE: ";
    String TAG_GREEN_NEAREST = "NEAREST GREEN: ";
    String TAG_ROUND_MIN = "ROUND MIN: ";

    String TAG_NEAREST = "NEAREST_OBJECT";

    String TAG_THE_NEAREST = "THE_NEAREST";

    private Logger LOGGER_GREEN = Logger.getLogger("GREEN_DIST");

    public static final String TAG_GREEN = "GREEN_DIST";
    public static final String TAG_ORANGE = "ORANGE_DIST";
    public static final String TAG_ROUND = "ROUND_MIN";

    boolean isDetected;

    private boolean isBtnVolumeClicked;


//    String distanceIncrease = getResources().getString(R.string.distance_increase),
    //         distanceDecrease = getResources().getString(R.string.distance_decrease);

    private ImageView ivWelcomeGreen, ivWelcomeOrange, ivWelcomeRed, ivNearestGreen,
            ivNearestOrange, ivNearestRed, ivRailSemaphoreRedLeft, ivRailSemaphoreRedRight, ivRedPanel;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    GeoFire myGeoFire, redGeoFire, greenGeoFire, orangeGeoFire;
    GeoQuery myRedGeoQuery, myGreenGeoQuery, myOrangeGeoQuery;

    double myQueryAreaInKm;

    private Map<String, Marker> redMarkers; // cumulates red markers to display current red user position on the map
    private Map<String, Marker> greenMarkers;
    private Map<String, Marker> orangeMarkers;
    private Map<String, Float> distancesToRed; // counts distance from my position to red user position.
    private Map<String, Float> distancesToGreen;
    private Map<String, Float> distancesToOrange;

    private Map<String, Float> redGreenOrangeMarker; // it cumulates all nearest markers to display it in nearestDistnaces..
    // i.e: if in nearest is red, so there is a notification that there is red..

    private Map<String, Float> nearestDistances; // counts distance from my position to all users and select the nearest

    float mDeclination;
    int     myRadiusArea, // my radius detection. user sets this in settins activity
            red_detection, // true (1) or false (0) if blue user detects red user or not
            green_detection,
            orange_detection;

    private Button btnStopDetection, btnMuteUnmute;
    private View

            viewInternetInfo,
            viewWelcomePanel, // welcome panel during onCreate method
            viewDetectedRedPanel, // red panel is visible when red user is detected (is in myRadiusArea)
            viewDetectedGreenPanel,
            viewDetectedOrangePanel,
            viewGreyPanel,
            viewBackInfoPanel,
            viewDetectedNearestPanel; // details of red user in radius (distance, time)

    // to display view nearest object panel in color. vnp0 -> viewNearestPanel0
    private TextView
            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30, vnp32, vnp34,
            vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60, vnp62, vnp64, vnp66, vnp68, vnp70,
            vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90, vnp92, vnp94, vnp96, vnp98;

 int myIntegerSpeed;



    private TextView

            tvInternetInfo;
    private TextView tvOjectsDetection;
    private TextView tvRadiusRange;
    private TextView tvRadiusInfo;
    private TextView tvTitleNearest;
    private TextView tvDistanceToNearest; // show tv if red user is detected in viewDetectedRedPanel
    private TextView tvDistanceToGreen;
    private TextView tvDistanceToOrange;
    private TextView tvDistanceToRed;
    private TextView tvTimeToOrange;
    private TextView tvTimeToRed;
    private TextView tvTimeToGreen;
    private TextView tvTimeToNearest;
    private TextView tvNumberRed;
    private TextView tvNumberGreen;
    private TextView tvNumberOrange; // number orange objects in the circle
            private TextView tvDetailsDistance; // show distance to red user
            private TextView tvDetailsTime; // show time to red user
            private TextView tvMyLocation; // show my current location
            private TextView tvWelcome; // tv in viewWelcomePanel
            private TextView tvMyRadius; // show my radius in meters
            private TextView tvMySpeed; // show my current speed
            private TextView tvMySpeedPlus; // show my current speed after added a correction + 5% to tvMySpeed
            private TextView tvIncrease; // show if user distance increase or decrease (but now it doesn't work)

    DatabaseReference ref_blue,
            ref_red, // reference to database geofire/red -> there are updating red user geofire positions
            ref_green,
            ref_orange,
            ref_radius, // reference to radius
            ref_myName, // reference to name
            ref_redName,
            ref_online,
            ref_connection,
            ref_last_online_time,
            ref_diff_time,
            ref_detection; // show what user detects: red, green, orange


    String myName;
    int true_detection = 1;
    int false_detection = 0;
    int mScore;

    int detect_object; // set number of this variable in onCreate to using this in onLocationChanged

    ArrayList<Float> redDist = new ArrayList<Float>();
    ArrayList<Float> greenDist = new ArrayList<Float>();
    ArrayList<Float> orangeDist = new ArrayList<Float>();
    ArrayList<Integer> twoNearestDistances = new ArrayList<Integer>();
    ArrayList<Integer> roundsFiftyList = new ArrayList<Integer>();
    ArrayList<LatLng> myLatLngList = new ArrayList<LatLng>();

    int round;

    String redKeyString = "red";
    String greenKeyString = "green";
    String orangeKeyString = "orange";
    String whoIsIt, distanceCloseOrGoAwayRed, distanceCloseOrGoAwayGreen, distanceCloseOrGoAwayOrange;

    String goes_away = "goes away";
    String is_closing = "is closing";


    //  Float mySpeedBlue;

//    Location myBlueLocation;


    String mLector; // download from database settings of user lector. If user chose en or pl. It's done in ref_myName
    String lector_pl = "pl"; // it uses to compare lector settings from database
    String lector_en = "en";

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // earlier was balanced battery low. sth like this


    private static final LocationRequest REQUEST_BALANCED = LocationRequest.create()
            .setInterval(100)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    private MediaPlayer detectedObject, plRadiusObjects, plVoiceActivated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

       if (!NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {

           ToastUtil.shortToast(getApplicationContext(), "Brak transferu danych. Włącz internet");

       }

        initViews();
        getStringId();


        arrayRedSempahoreDrawable = new Integer[]{R.drawable.rail_semaphore_red_left, R.drawable.rail_semaphore_red_left};

        isBtnVolumeClicked = false;
       // volumeMediaPlayer = 1;

        btnMuteMedia();

        roundsFiftyList.add(0);
        roundsFiftyList.add(0);
        //plVoiceActivated.start();

        mConnectionPresenter = new ConnectionPresenter(this);


        MyCount counter = new MyCount(10000,1000);
        counter.start();

//        REQUEST.setInterval(16).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        mWakeLock.acquire();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        detectedObject = MediaPlayer.create(this, R.raw.wykryty);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        myTrace = FirebasePerformance.getInstance().newTrace("blue_trace");
        myTrace.start();


        volumeMediaPlayer = 1;


        ref_blue = FirebaseDatabase.getInstance().getReference("geofire/blue");
        ref_red = FirebaseDatabase.getInstance().getReference("geofire/red");
        ref_green = FirebaseDatabase.getInstance().getReference("geofire/green");
        ref_orange = FirebaseDatabase.getInstance().getReference("geofire/orange");

        ref_last_online_time = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS_CONNECTION_STATE).child(Constants.ARG_USER_LAST_TIME_ONLINE).child(firebaseUser.getUid()).child("timestamp");

        //ref_connection = FirebaseDatabase.getInstance().getReference(".info/connected");
        ref_online = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS_CONNECTION_STATE).child(Constants.ARG_BLUE).child(firebaseUser.getUid());


        ref_online.onDisconnect().setValue("0");
        mConnectionPresenter.ConnectionState(getApplicationContext(), ref_connection, ref_online);
      //  ref_connection.addValueEventListener(new ValueEventListener() {
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
//                    ref_online.setValue("0");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                //System.err.println("Listener was cancelled");
//            }
//        });

        ref_diff_time = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS_CONNECTION_STATE).child(Constants.ARG_USER_LAST_TIME_ONLINE).child(firebaseUser.getUid()).child("diff_time");

        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                double offset = snapshot.getValue(Double.class);
                double estimatedServerTimeMs = System.currentTimeMillis() + offset;

                ref_diff_time.setValue(estimatedServerTimeMs);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        radarScanView = (RadarScanView) findViewById(R.id.radarScanView);

        //sharedPrefUtil.getString("PL");
        //Log.i(TAG_ROUND, ("SHARED PREF " + sharedPrefUtil.getString("PL")));



        myGeoFire = new GeoFire(ref_blue);
        redGeoFire = new GeoFire(ref_red);
        greenGeoFire = new GeoFire(ref_green);
        orangeGeoFire = new GeoFire(ref_orange);

        initTvNearestColorPanelInPercent();



        //isInternetAvailable();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        redMarkers = new HashMap<String, Marker>();
        greenMarkers = new HashMap<String, Marker>();
        orangeMarkers = new HashMap<String, Marker>();

        distancesToRed = new HashMap<String, Float>();
        distancesToGreen = new HashMap<String, Float>();
        distancesToOrange = new HashMap<String, Float>();

        nearestDistances = new HashMap<String, Float>();

        redGreenOrangeMarker = new HashMap<String, Float>();

        //   ref_radius = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid()).child(Constants.ARG_RADIUS);

        viewWelcomePanel.setVisibility(View.VISIBLE);
        showGreyPanel();

        //viewDetectedNearestPanel.setVisibility(View.VISIBLE);


        setRadarScanView(radarScanView);
       // radarScanView.startScan();

        // getDetectionFromDatabase();

        final String[] strRadiusInfoArray = {getString(R.string.str_radar_info), getString(R.string.str_radar_info2)};

        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapBlueId);
        mSupportMapFragment.getMapAsync(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_myName = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid());
        //getMyName();
        ref_myName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                myName = user.getName();
                myRadiusArea = user.getRadius();
                mLector = user.getLector();
                mScore = user.getScore();

               // tvWelcome.setText(myName + "\n promień: " + myRadiusArea + " punkty " + mScore);
                tvWelcome.setText(myName + ", " + "Twój radar:");
                tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // it shows particular string in strRadiusInfoArray in every 5 seconds
        tvRadiusInfo.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                tvRadiusInfo.setText(strRadiusInfoArray[i]);
                i++;
                if (i == strRadiusInfoArray.length) {
                    i = 0;
                }
                tvRadiusInfo.postDelayed(this, 5000);

            }
        });


        ref_detection = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid()).child(Constants.ARG_DETECTION);
        ref_detection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                red_detection = user.getRed_detection();
                green_detection = user.getGreen_detection();
                orange_detection = user.getOrange_detection();


                if (red_detection == true_detection && green_detection == true_detection && orange_detection == true_detection) {

                    detect_object = 9;

                    //tvWelcome.setText("Witaj, " + myName + " promień: " + myRadiusArea + " detekcja: " + "RED, GREEN, ORANGE");
                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strRed + ", " + strGreen + ", " + strOrange);
                    //ToastUtil.shortToast(getApplicationContext(), "ALL");

                } else if (red_detection == true_detection && green_detection == true_detection && orange_detection == false_detection) {

                    detect_object = 8;
                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strRed + ", " + strGreen);

                } else if (red_detection == true_detection && green_detection == false_detection && orange_detection == true_detection) {

                    detect_object = 7;

                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strRed + ", " + strOrange);

                } else if (red_detection == true_detection && green_detection == false_detection && orange_detection == false_detection) {

                    detect_object = 6;

                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strRed);

                } else if (red_detection == false_detection && green_detection == true_detection && orange_detection == true_detection) {

                    detect_object = 5;

                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strGreen + ", " + strOrange);

                } else if (red_detection == false_detection && green_detection == true_detection && orange_detection == false_detection) {

                    detect_object = 4;
                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strGreen);

                } else if (red_detection == false_detection && green_detection == false_detection && orange_detection == true_detection) {

                    detect_object = 3;
                    tvWelcome.setText(myName + ", " + strRadarInfo);
                    tvRadiusRange.setText(getString(R.string.tv_radius_range) + " " + myRadiusArea);
                    tvOjectsDetection.setText(strObjects + " " + strOrange);

                }


                //   tvWelcome.setText("Witaj, " + myName + " promień: " + myRadiusArea + " detekcja: " + " RED, GREEN, ORANGE");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if (plVoiceActivated.isPlaying()) {
//
//            plVoiceActivated.stop();
//            plRadiusObjects.start();
//
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }
        }





    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //showBackButtonOptionAlertToUser();
        showBackLayoutOptionToUser();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ToastUtil.shortToast(getApplicationContext(), "onDestroy");

        if (myRedGeoQuery != null) {

            myRedGeoQuery.removeAllListeners();
        }

        ref_online.onDisconnect().setValue("0"); // it works and even work when I come back to SelectNaviActivitiy and minimalize the app and delete it from recent open app.
        myTrace.stop();


    }

    @Override
    protected void onStart() {
        super.onStart();

        ToastUtil.shortToast(getApplicationContext(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        ToastUtil.shortToast(getApplicationContext(), "onResume");
        // this.mWakeLock.release();

        //mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

        ref_online.onDisconnect().setValue("0"); // it doesnt work

        ToastUtil.shortToast(getApplicationContext(), "onStop");
        //onStop is called after onPause

//        if(mGoogleApiClient != null) {
//
            mGoogleApiClient.connect();
//        } else {
//            ToastUtil.shortToast(getApplicationContext(), "Włącz GPS.");
//        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        ref_online.onDisconnect().setValue("0"); // it doesnt work
 //       mGoogleApiClient.disconnect();
        //ref_online.onDisconnect().setValue("0");
        myTrace.stop();
        ToastUtil.shortToast(getApplicationContext(), "onPause");
        // when I press back button onPause is called
//        if(mGoogleApiClient != null) {
//
//            mGoogleApiClient.connect();
//        } else {
//            ToastUtil.shortToast(getApplicationContext(), "Włącz GPS.");
//        }
        //   this.mWakeLock.release();
    }





    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
//        ToastUtil.shortToast(getApplicationContext(), "Connected");
        mLocationRequest.setInterval(1000); //  requeston every 5 seconds (to save capacity of battery) ; setInterval to 1 s when object will be in the radius
        mLocationRequest.setFastestInterval(1000); // because setInterval depepends on another applications too (which use gps) this fastestInterval set min or max interval.
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       // balancedLocationRequest(mLocationRequest);
//
//        if (isDetected == true) {
//            mLocationRequest.setInterval(1000); //  requeston every 5 seconds (to save capacity of battery) ; setInterval to 1 s when object will be in the radius
//            mLocationRequest.setFastestInterval(1000); // because setInterval depepends on another applications too (which use gps) this fastestInterval set min or max interval.
//            //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                //REQUEST.setInterval(1).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        } else if (isDetected == false) {
//
//            mLocationRequest.setInterval(5000); //  requeston every 5 seconds (to save capacity of battery) ; setInterval to 1 s when object will be in the radius
//            mLocationRequest.setFastestInterval(5000); // because setInterval depepends on another applications too (which use gps) this fastestInterval set min or max interval.
//            //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        }

//        if (!isDetected) {
//            mLocationRequest.setInterval(1000);
//            mLocationRequest.setFastestInterval(1000);
//        } else {
//            mLocationRequest.setInterval(5000);
//            mLocationRequest.setFastestInterval(5000);
//        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        isInternetAvailable();


        if (mCurrLocationMarker != null) {

            //    animateMarkerTo(mCurrLocationMarker, location.getLatitude(), location.getLongitude());
            mCurrLocationMarker.remove();
            mGoogleMap.clear();


//            circle.remove();

        }


        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        myLatLng = latLng;


        final Location myBlueLocation = new Location("currBlueLocation");
        myBlueLocation.setLatitude(myLatitude);
        myBlueLocation.setLongitude(myLongitude);

        if (myLatitude != 0 && myLongitude != 0) {

            tvMyLocation.setText("My location: " + "Lat: " + String.valueOf(myLatitude) + " Long: " + String.valueOf(myLongitude));

        }
        final Float mySpeedBlue = location.getSpeed();

        double mySpeed = location.getSpeed() * 3.6;  // from meters to km/h
        String myS = String.format("%.0f", mySpeed);
        double mySpeedPlus = mySpeed * 1.06;     // it adds 6% to mySpeed because there is better accuracy (real speed of a blue is more similar to mySpeedPlus than mySpeed.
        String mySP = String.format("%.0f", mySpeedPlus);
        myIntegerSpeed = (int) Math.round(mySpeedPlus);

        tvMySpeed.setText(myS);
        tvMySpeedPlus.setText(mySP);


        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.flat(true);
        //markerOptions.anchor(0.5f, 0.5f);
        //markerOptions.rotation(90.0f);
          markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker_circle1));
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        myLatLngList.add(latLng);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        if (myLatLngList.size() > 1) {

            //   bearingBetweenLocations(myLatLngList.get(myLatLngList.size()-1), myLatLngList.get(myLatLngList.size()-2));

        }

        if (myLatLngList.size() > 20) {

            myLatLngList.clear();

        }


        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        ref_radius = database1.getReference(Constants.ARG_USERS).child(firebaseUser.getUid());
        //   reference_radius.child(Constants.USERS).child(userId).child("radius");
        ref_radius.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                User user3 = dataSnapshot.getValue(User.class);

                user3.getRadius();

                //   user3.setRadius(user3.getRadius());
                int radius = user3.getRadius();
                myRadiusArea = radius;
                String rad = String.valueOf(radius);
                // radiusTv.setText(rad);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Toast.makeText(getApplicationContext(), "RADIUS: " + String.valueOf(myRadiusArea), Toast.LENGTH_SHORT).show();


        int num;
        String d;

        //  detectGeoFireRed();
        myQueryAreaInKm = myRadiusArea; // meters / 1000 = kilometers
        double km = myQueryAreaInKm / 1000;
        tvMyRadius.setText("\nDetekcja: " + String.valueOf(km) + " kilometrów" + " " + mLector
                + " RED " + red_detection + " GREEN " + green_detection + " ORANGE " + orange_detection);

        btnMuteMedia();

        // long time1 = System.currentTimeMillis();
        if (mLector != null) {

            if (mLector.equals(lector_pl)) {

                if (detect_object == 9) {

                    plRedGeoDetection(myBlueLocation, mySpeedBlue, km);
                    plGreenGeoDetection(myBlueLocation, mySpeedBlue, km);
                    plOrangeGeoDetection(myBlueLocation, mySpeedBlue, km);

                    ivWelcomeGreen.setVisibility(View.VISIBLE);
                    ivWelcomeOrange.setVisibility(View.VISIBLE);
                    ivWelcomeRed.setVisibility(View.VISIBLE);

                    Map.Entry<String, Float> nearestMarker = null;
                    Map.Entry<String, Float> redGreenOrangeString = null;

                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {

//                        if (nearestDistances.size() != 0) {
//
//                            isDetected = true;
//
//                            viewDetectedNearestPanel.setVisibility(View.VISIBLE);
//                            radarScanView.stopScan();
//                            radarScanView.setVisibility(View.INVISIBLE);
//                            hideGreyPanel();
//                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//
//                        } else {
//
//                            isDetected = false;
//                            stopAnimateGreyPanel();
//                            showGreyPanel();
//
//
//                        }

                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                            nearestMarker = entry;

                            Float nearestDist = nearestMarker.getValue();

                            round = Math.round(nearestMarker.getValue());
                            twoNearestDistances.add(round);
                            Log.e("   MIN   ", round + "");
                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                            Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());


                            k = nearestMarker.getKey();

                        }

                    }

                    if (twoNearestDistances.size() > 2) {


                        // if result is > 0 -> green marker goes away from myBluePosition
                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {


                            twoLastNearestDistancesCloseOrAway = goes_away;
                            Log.i(TAG_ROUND, ("round_goesAway " + round));
                            //    round = -2;

                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                            // if result is < 0 -> red marker is closing to myBluePosition

                            twoLastNearestDistancesCloseOrAway = is_closing;
                            Log.i(TAG_ROUND, ("round_isClosing " + round));
                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {

                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?

                            twoLastNearestDistancesCloseOrAway = "zero";
                        }

                    }

                    if (twoNearestDistances.size() > 20) {

                        twoNearestDistances.clear();
                    }

                    Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));

                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

                        //  blueLector.plStop();

                        round = -1;
                        String m = "NOBODY";
                        nearestDistances.clear();
                        tvDistanceToNearest.setText(m);
                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                        viewWelcomePanel.setVisibility(View.VISIBLE);
                        radarScanView.startScan();
                        radarScanView.setVisibility(View.VISIBLE);
                        // stopAnimateGreyPanel();
                        showGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                        isDetected = false;


                    } else {

                        isDetected = true;
                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                        radarScanView.stopScan();
                        radarScanView.setVisibility(View.INVISIBLE);
                        hideGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    }


                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        int s = (int) Math.round(mySpeedPlus);

                        if (v == 1.0) {

                            whoIsIt = "RED";
                            // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                            ivNearestRed.setVisibility(View.VISIBLE);
                            ivNearestGreen.setVisibility(View.INVISIBLE);

                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    //hideGreyPanel();
                                    tvTimeToNearest.setText(strTrainIsClosing);


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea)); // percent to nearest. 90% means that it's very close to you

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
                                            setCameraProperties(mGoogleMap, latLng, 60, 15, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    tvTitleNearest.setText(distanceCloseOrGoAwayRed);

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    // BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);


                                    BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_orange_isClosing " + round));


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strTrainGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

//                                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                    animation1.setDuration(500);
//                                    viewDetectedNearestPanel.setAlpha(1f);
//                                    viewDetectedNearestPanel.startAnimation(animation1);

                                    //animateGreyPanel();

                                    if (distanceInPercentToRed > 95) {

                                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                                    }


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {

                                        distanceInPercentToNearest = round;

                                    }

                                    //  tvTitleNearest.setText(distanceCloseOrGoAwayRed);

                                    Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                                    Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }
                            }

                        } else if (v == 2.0) {

                            whoIsIt = "GREEN";
                            ivNearestRed.setVisibility(View.INVISIBLE);
                            ivNearestGreen.setVisibility(View.VISIBLE);
                            ivNearestOrange.setVisibility(View.INVISIBLE);
                            // tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    viewDetectedGreenPanel.setVisibility(View.VISIBLE);
                                    //hideGreyPanel();

                                    tvTimeToNearest.setText(strGreenIsClosing);

                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 14, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(14).
//                                                    bearing(0).
//                                                    build();

                                            //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(17).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }



                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.green_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.green_400);

                                    //setNearestPanelDrawableInPercent(R.drawable.green_color);
                                    // volumeMediaPlayer = 0;

                                    BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_green_isClosing " + round));

                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strGreenGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                                    // animateGreyPanel();

                                    if (round >= 0) {

//                                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                        animation1.setDuration(500);
//                                        viewDetectedNearestPanel.setAlpha(1f);
//                                        viewDetectedNearestPanel.startAnimation(animation1);

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    if (distanceInPercentToGreen > 95) {

                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                                    }

                                    setNearestPanelColorInPercent(R.color.green_400);

                                    // nearst market get value causes npe i daltego po green kiedy byl po za radius wylaczylo sie do main ativity
                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g goes_away. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    Log.i(TAG_ROUND, ("round_green_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    Log.i(TAG_THE_NEAREST, " g zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }

                            }


                        }
//                    else if (v == 3.0) {
//
//                        whoIsIt = "ORANGE";
//                        ivNearestRed.setVisibility(View.INVISIBLE);
//                        ivNearestGreen.setVisibility(View.INVISIBLE);
//                        ivNearestOrange.setVisibility(View.VISIBLE);
//                        tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
//                        if (twoLastNearestDistancesCloseOrAway != null) {
//
//                            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//                                    distanceInPercentToNearest = round;
//                                }
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
//                                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                Log.i(TAG_ROUND, ("round_orange_isClosing " + round));
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//
//                                    distanceInPercentToNearest = round;
//
//                                }
//
//                                Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
//                                nearestDistances.clear();
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                round = -2;
//                                Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                            }
//                        }
//                    }

                        //whoIsIt = String.valueOf(redGreenOrangeMarker.get(k));

//                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round);



                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        Log.e(" RED GREEN ORANGE KEY ", k + " ");
                        //tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(round) + " m");

                    }
                }

                // if there is nobody in radius (so nearestDistances.size == 0 )

                else if (detect_object == 8) {

                    plRedGeoDetection(myBlueLocation, mySpeedBlue, km);
                    plGreenGeoDetection(myBlueLocation, mySpeedBlue, km);

                    ref_last_online_time.setValue(ServerValue.TIMESTAMP);

                    ivWelcomeGreen.setVisibility(View.VISIBLE);
                    ivWelcomeRed.setVisibility(View.VISIBLE);

//                    Map.Entry<String, Float> nearestMarker = null;
//                    Map.Entry<String, Float> redGreenOrangeString = null;
//
//                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {
//
//                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
//                            nearestMarker = entry;
//
//                            Float nearestDist = nearestMarker.getValue();
//
//                            round = Math.round(nearestMarker.getValue());
//                            roundEvery50Meters = (round / 50) * 50;
//                            twoNearestDistances.add(round);
//                            Log.e("   MIN   ", round + "");
//                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");
//
//                          //  Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());
//
//
//                            k = nearestMarker.getKey();
//
//                        }
//
//                    }
                    findTheNearestObjectWithDistanceAndKey();
//                    if (twoNearestDistances.size() > 2) {
//                        // if result is > 0 -> green marker goes away from myBluePosition
//                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {
//                            twoLastNearestDistancesCloseOrAway = goes_away;
//                            Log.i(TAG_ROUND, ("round_goesAway " + round));
//                            //    round = -2;
//
////                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
////                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
////                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
//                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));
//
//                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
//                            // if result is < 0 -> red marker is closing to myBluePosition
//                            twoLastNearestDistancesCloseOrAway = is_closing;
////                            Log.i(TAG_ROUND, ("round_isClosing " + round));
////                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
////                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
////                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
//                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {
//                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?
//                            twoLastNearestDistancesCloseOrAway = "zero";
//                        }
//
//                    }                              chec
                    checkObjectState(); // check if object is closing, goes away, or zero
                    clear_twoNearestDistances();
//                    if (twoNearestDistances.size() > 20) {
//
//                        twoNearestDistances.clear();
//                    }
//
//                    Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));

//                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)
//
//                        setWhenObjectIsNotDetected();
////                        round = -1;
////                        String m = "NOBODY";
////                        nearestDistances.clear();
////                        tvDistanceToNearest.setText(m);
////                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
////                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
////                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
////                        viewWelcomePanel.setVisibility(View.VISIBLE);
////                        radarScanView.startScan();
////                        radarScanView.setVisibility(View.VISIBLE);
////                       // stopAnimateGreyPanel();
////                        showGreyPanel();
////                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
////
////                        isDetected = false;
////
////                        // check if it will not make exception as list is empty when will detect object try to check two last positions
////                        roundsFiftyList.clear();
//
//                    } else {
//
//                        setWhenObjectIsDetected();
////                        isDetected = true;
////                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
////                        radarScanView.stopScan();
////                        radarScanView.setVisibility(View.INVISIBLE);
////                        hideGreyPanel();
////                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//                    }
                    checkIfObjectIsDetected();

                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        // RED
                        if (v == 1.0) {

//                            whoIsIt = "RED";
//                           // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
//                            ivNearestRed.setVisibility(View.VISIBLE);
//                            ivNearestGreen.setVisibility(View.INVISIBLE);
//
//                            if (twoLastNearestDistancesCloseOrAway != null) {
//
//                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                    //hideGreyPanel();
//                                    tvTimeToNearest.setText(strTrainIsClosing);
//
//
//                                    if (round >= 0) {
//
//                                        int di = round;
//                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea)); // percent to nearest. 90% means that it's very close to you
//
//                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
//                                            setCameraProperties(mGoogleMap, latLng, 60, 15, 0);
//
//                                        }
//
//
//                                        if (distanceInPercentToNearest > 70) {
//
//                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
////                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
////                                            CameraPosition cameraPosition = new CameraPosition.Builder().
////                                                    tilt(60).
////                                                    bearing(0).
////                                                    build();
////
////                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                                        }
//
//                                    } else {
//                                        distanceInPercentToNearest = round;
//                                    }
//
//                                    tvTitleNearest.setText(distanceCloseOrGoAwayRed);
//
////                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
////                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
////                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
////                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
////                                            vnp92, vnp94, vnp96, vnp98);
//                                 //   setNearestPanelColorInPercent(R.color.red_400);
//                                    setNearestPanelColorForRed();
//                                    Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
//                                             "roundEvery50Meters: " + roundEvery50Meters + " " + " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//
//
//                                   // BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
//                                    //BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
//
//                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                    Log.i(TAG_ROUND, ("round_orange_isClosing " + round));
//
//
//                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                    tvTimeToNearest.setText(strTrainGoesAway);
//                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//
////                                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
////                                    animation1.setDuration(500);
////                                    viewDetectedNearestPanel.setAlpha(1f);
////                                    viewDetectedNearestPanel.startAnimation(animation1);
//
//                                    //animateGreyPanel();
//
//                                    if (distanceInPercentToRed > 95) {
//
//                                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
//                                    }
//
//
//                                    if (round >= 0) {
//
//                                        int di = round;
//                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                    } else {
//
//                                        distanceInPercentToNearest = round;
//
//                                    }
//
//                                  //  tvTitleNearest.setText(distanceCloseOrGoAwayRed);
//
//                                    Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
//                                             "roundEvery50Meters: " + roundEvery50Meters + " " + " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
////                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
////                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
////                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
////                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
////                                            vnp92, vnp94, vnp96, vnp98);
//                                    setNearestPanelColorInPercent(R.color.red_400);
//
//                                    Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
//                                    nearestDistances.clear();
//
//
//                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                    round = -2;
//                                   // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
//                                    Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
//                                            "roundEvery50Meters: " + roundEvery50Meters + " " + " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                                }
//                            }
                            setRedDetected(latLng);

                            // GREEN
                        } else if (v == 2.0) {

//                            Integer rnd = round;
//                            Integer rnd2 = round;
//
//                            whoIsIt = "GREEN";
//                            ivNearestRed.setVisibility(View.INVISIBLE);
//                            ivNearestGreen.setVisibility(View.VISIBLE);
//                            ivNearestOrange.setVisibility(View.INVISIBLE);
//                           // tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
//                            if (twoLastNearestDistancesCloseOrAway != null) {
//
//                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                    viewDetectedGreenPanel.setVisibility(View.VISIBLE);
//                                    //hideGreyPanel();
//
//                                    tvTimeToNearest.setText(strGreenIsClosing);
//
//                                    if (round >= 0) {
//
//                                        int di = round;
//
//                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
//
//                                            setCameraProperties(mGoogleMap, latLng, 60, 14, 0);
//                                           // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
////                                            CameraPosition cameraPosition = new CameraPosition.Builder().
////                                                    target(latLng).
////                                                    tilt(60).
////                                                    zoom(14).
////                                                    bearing(0).
////                                                    build();
//
//                                          //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                                        }
//
//
//                                        if (distanceInPercentToNearest > 70) {
//
//                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
//                                           // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
////                                            CameraPosition cameraPosition = new CameraPosition.Builder().
////                                                    target(latLng).
////                                                    tilt(60).
////                                                    zoom(17).
////                                                    bearing(0).
////                                                    build();
////
////                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                                        }
//
//
//
//                                    } else {
//                                        distanceInPercentToNearest = round;
//                                    }
//
////                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.green_400,
////                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
////                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
////                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
////                                            vnp92, vnp94, vnp96, vnp98);
//                                  // setNearestPanelColorInPercent(R.color.green_400);
//                                    setNearestPanelColorForGreen();
//                                    //setNearestPanelDrawableInPercent(R.drawable.green_color);
//                                   // volumeMediaPlayer = 0;
//
//                                    // in Galaxy S7 (android 7.0) sometimes [volume,volume] value was null and app crashed
//
//
////                                    if (round > 80 && round < 120) {
////
////                                        mRound = 100;
////
////                                    }
//
//
//
//                                    if (round > 100) {
//                                        // round every 50 meterss
//
//                                        roundEvery50Meters = (rnd / 50) * 50; // display 50m, 100m, 150m ...
//                                    } else if (round > 0 && round < 100) {
//
//                                        roundEvery50Meters = (rnd2 / 20) * 20; // display 20m, 40m, 60m, 80m
//                                    }
//
//
////                                    if (!roundEvery50Meters.equals(roundsFiftyList.get(roundsFiftyList.size()-1))) {
////                                        roundsFiftyList.add(roundEvery50Meters);
////                                        setPLBlueLector(roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
////
//                                    setGreenLector();
//
//
////                                    if (volumeMediaPlayer != null) {
////                                        BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
////                                    }
//                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                    Log.i(TAG_ROUND, ("round_green_isClosing " + round));
//
//                                    if (twoNearestDistances.size() > 1) {
//                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
//                                    }
//                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
//                                            " roundEvery50Meters: " + roundEvery50Meters + " "
//                                            + "roundsList.(-1): " + roundsFiftyList.get(roundsFiftyList.size()-1)
//                                            + " roundsList.(-2): " + roundsFiftyList.get(roundsFiftyList.size()-2)
//                                            + " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                    tvTimeToNearest.setText(strGreenGoesAway);
//                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//
//
//                                    if (round >= 0) {
//
//                                        int di = round;
//                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                    } else {
//                                        distanceInPercentToNearest = round;
//                                    }
//
//                                    if (distanceInPercentToGreen > 95) {
//
//                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
//                                    }
//
//                                    setNearestPanelColorForGreen();
//
//                                    // nearest market get value causes npe i daltego po green kiedy byl po za radius wylaczylo sie do main ativity
//                                    if (twoNearestDistances.size() > 1) {
//                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
//                                    }
//                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
//                                            " roundEvery50Meters: " + roundEvery50Meters + " "
//                                            + "roundsList.(-1): " + roundsFiftyList.get(roundsFiftyList.size()-1)
//                                            + " roundsList.(-2): " + roundsFiftyList.get(roundsFiftyList.size()-2)
//                                            + " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                    nearestDistances.clear();
//
//
//                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                    round = -2;
//                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
//                                            " roundEvery50Meters: " + roundEvery50Meters + " "
//                                            + "roundsList.(-1): " + roundsFiftyList.get(roundsFiftyList.size()-1)
//                                            + " roundsList.(-2): " + roundsFiftyList.get(roundsFiftyList.size()-2)
//                                            + " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                                }
//
//                            }
                            setGreenDetected();

                        }

                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        //tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(roundEvery50Meters) + " m");

                    }
                }

                // if there is nobody in radius (so nearestDistances.size == 0 )

//
//                Log.e("SIZE OF nearestDistanc", nearestDistances.size() + "");
//                Log.e("SIZEOF RED_GREEN_ORANGE", redGreenOrangeMarker.size() + "");

                //  ToastUtil.shortToast(getApplicationContext(), String.valueOf(detect_object));

                 else if (detect_object == 7) {

                plRedGeoDetection(myBlueLocation, mySpeedBlue, km);
                plOrangeGeoDetection(myBlueLocation, mySpeedBlue, km);

                ivWelcomeOrange.setVisibility(View.VISIBLE);
                ivWelcomeRed.setVisibility(View.VISIBLE);

                Map.Entry<String, Float> nearestMarker = null;
                Map.Entry<String, Float> redGreenOrangeString = null;

                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {


                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                            nearestMarker = entry;

                            Float nearestDist = nearestMarker.getValue();

                            round = Math.round(nearestMarker.getValue());
                            twoNearestDistances.add(round);
                            Log.e("   MIN   ", round + "");
                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                            Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());


                            k = nearestMarker.getKey();

                        }

                    }

                    if (twoNearestDistances.size() > 2) {


                        // if result is > 0 -> green marker goes away from myBluePosition
                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {


                            twoLastNearestDistancesCloseOrAway = goes_away;
                            Log.i(TAG_ROUND, ("round_goesAway " + round));
                            //    round = -2;

                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                            // if result is < 0 -> red marker is closing to myBluePosition

                            twoLastNearestDistancesCloseOrAway = is_closing;
                            Log.i(TAG_ROUND, ("round_isClosing " + round));
                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {

                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?

                            twoLastNearestDistancesCloseOrAway = "zero";
                        }

                    }

                    if (twoNearestDistances.size() > 20) {

                        twoNearestDistances.clear();
                    }

                    Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));

                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

                        //  blueLector.plStop();

                        round = -1;
                        String m = "NOBODY";
                        nearestDistances.clear();
                        tvDistanceToNearest.setText(m);
                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                        viewWelcomePanel.setVisibility(View.VISIBLE);
                        radarScanView.startScan();
                        radarScanView.setVisibility(View.VISIBLE);
                        // stopAnimateGreyPanel();
                        showGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                        isDetected = false;


                    } else {

                        isDetected = true;
                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                        radarScanView.stopScan();
                        radarScanView.setVisibility(View.INVISIBLE);
                        hideGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    }


                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        int s = (int) Math.round(mySpeedPlus);

                        if (v == 1.0) {

                            whoIsIt = "RED";
                            // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                            ivNearestRed.setVisibility(View.VISIBLE);
                            ivNearestGreen.setVisibility(View.INVISIBLE);

                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    //hideGreyPanel();
                                    tvTimeToNearest.setText(strTrainIsClosing);


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea)); // percent to nearest. 90% means that it's very close to you

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
                                            setCameraProperties(mGoogleMap, latLng, 60, 15, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    tvTitleNearest.setText(distanceCloseOrGoAwayRed);

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    // BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
                                    BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_orange_isClosing " + round));


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strTrainGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

//                                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                    animation1.setDuration(500);
//                                    viewDetectedNearestPanel.setAlpha(1f);
//                                    viewDetectedNearestPanel.startAnimation(animation1);

                                    //animateGreyPanel();

                                    if (distanceInPercentToRed > 95) {

                                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                                    }


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {

                                        distanceInPercentToNearest = round;

                                    }

                                    //  tvTitleNearest.setText(distanceCloseOrGoAwayRed);

                                    Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                                    Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }
                            }

                        } else if (v == 3.0) {

                            // it's needed from green to orange. so only changed whiIsIt. If you want to set ORANGE properly make all function for orange.

                            whoIsIt = "ORANGE";
                            ivNearestRed.setVisibility(View.INVISIBLE);
                            ivNearestGreen.setVisibility(View.INVISIBLE);
                            ivNearestOrange.setVisibility(View.VISIBLE);
                            // tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    viewDetectedGreenPanel.setVisibility(View.VISIBLE);
                                    //hideGreyPanel();

                                    tvTimeToNearest.setText(strGreenIsClosing);

                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 14, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(14).
//                                                    bearing(0).
//                                                    build();

                                            //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(17).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }



                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.green_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.green_400);

                                    //setNearestPanelDrawableInPercent(R.drawable.green_color);
                                    // volumeMediaPlayer = 0;

                                    BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_green_isClosing " + round));

                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strGreenGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                                    // animateGreyPanel();

                                    if (round >= 0) {

//                                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                        animation1.setDuration(500);
//                                        viewDetectedNearestPanel.setAlpha(1f);
//                                        viewDetectedNearestPanel.startAnimation(animation1);

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    if (distanceInPercentToGreen > 95) {

                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                                    }

                                    setNearestPanelColorInPercent(R.color.green_400);

                                    // nearst market get value causes npe i daltego po green kiedy byl po za radius wylaczylo sie do main ativity
                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g goes_away. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    Log.i(TAG_ROUND, ("round_green_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    Log.i(TAG_THE_NEAREST, " g zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }

                            }


                        }
//                    else if (v == 3.0) {
//
//                        whoIsIt = "ORANGE";
//                        ivNearestRed.setVisibility(View.INVISIBLE);
//                        ivNearestGreen.setVisibility(View.INVISIBLE);
//                        ivNearestOrange.setVisibility(View.VISIBLE);
//                        tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
//                        if (twoLastNearestDistancesCloseOrAway != null) {
//
//                            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//                                    distanceInPercentToNearest = round;
//                                }
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
//                                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                Log.i(TAG_ROUND, ("round_orange_isClosing " + round));
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//
//                                    distanceInPercentToNearest = round;
//
//                                }
//
//                                Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
//                                nearestDistances.clear();
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                round = -2;
//                                Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                            }
//                        }
//                    }

                        //whoIsIt = String.valueOf(redGreenOrangeMarker.get(k));

//                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round);



                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        Log.e(" RED GREEN ORANGE KEY ", k + " ");
                        //tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(round) + " m");

                    }
                }
            else if (detect_object == 6) {

                plRedGeoDetection(myBlueLocation, mySpeedBlue, km);
                //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(detect_object));
                ivWelcomeRed.setVisibility(View.VISIBLE);

                Map.Entry<String, Float> nearestMarker = null;
                Map.Entry<String, Float> redGreenOrangeString = null;

                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {

//                        if (nearestDistances.size() != 0) {
//
//                            isDetected = true;
//
//                            viewDetectedNearestPanel.setVisibility(View.VISIBLE);
//                            radarScanView.stopScan();
//                            radarScanView.setVisibility(View.INVISIBLE);
//                            hideGreyPanel();
//                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//
//                        } else {
//
//                            isDetected = false;
//                            stopAnimateGreyPanel();
//                            showGreyPanel();
//
//
//                        }

                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                            nearestMarker = entry;

                            Float nearestDist = nearestMarker.getValue();

                            round = Math.round(nearestMarker.getValue());
                            twoNearestDistances.add(round);
                            Log.e("   MIN   ", round + "");
                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                            Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());


                            k = nearestMarker.getKey();

                        }

                    }

                    if (twoNearestDistances.size() > 2) {


                        // if result is > 0 -> green marker goes away from myBluePosition
                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {


                            twoLastNearestDistancesCloseOrAway = goes_away;
                            Log.i(TAG_ROUND, ("round_goesAway " + round));
                            //    round = -2;

                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                            // if result is < 0 -> red marker is closing to myBluePosition

                            twoLastNearestDistancesCloseOrAway = is_closing;
                            Log.i(TAG_ROUND, ("round_isClosing " + round));
                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {

                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?

                            twoLastNearestDistancesCloseOrAway = "zero";
                        }

                    }

                    if (twoNearestDistances.size() > 20) {

                        twoNearestDistances.clear();
                    }

                    Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));

                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

                        //  blueLector.plStop();

                        round = -1;
                        String m = "NOBODY";
                        nearestDistances.clear();
                        tvDistanceToNearest.setText(m);
                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                        viewWelcomePanel.setVisibility(View.VISIBLE);
                        radarScanView.startScan();
                        radarScanView.setVisibility(View.VISIBLE);
                        // stopAnimateGreyPanel();
                        showGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                        isDetected = false;


                    } else {

                        isDetected = true;
                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                        radarScanView.stopScan();
                        radarScanView.setVisibility(View.INVISIBLE);
                        hideGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    }


                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        int s = (int) Math.round(mySpeedPlus);

                        if (v == 1.0) {

                            whoIsIt = "RED";
                            // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                            ivNearestRed.setVisibility(View.VISIBLE);
                            ivNearestGreen.setVisibility(View.INVISIBLE);

                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    //hideGreyPanel();
                                    tvTimeToNearest.setText(strTrainIsClosing);


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea)); // percent to nearest. 90% means that it's very close to you

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
                                            setCameraProperties(mGoogleMap, latLng, 60, 15, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    tvTitleNearest.setText(distanceCloseOrGoAwayRed);

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    // BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
                                    BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_orange_isClosing " + round));


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strTrainGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

//                                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                    animation1.setDuration(500);
//                                    viewDetectedNearestPanel.setAlpha(1f);
//                                    viewDetectedNearestPanel.startAnimation(animation1);

                                    //animateGreyPanel();

                                    if (distanceInPercentToRed > 95) {

                                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                                    }


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {

                                        distanceInPercentToNearest = round;

                                    }

                                    //  tvTitleNearest.setText(distanceCloseOrGoAwayRed);

                                    Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                                    Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }
                            }

                        }

//                    else if (v == 3.0) {
//
//                        whoIsIt = "ORANGE";
//                        ivNearestRed.setVisibility(View.INVISIBLE);
//                        ivNearestGreen.setVisibility(View.INVISIBLE);
//                        ivNearestOrange.setVisibility(View.VISIBLE);
//                        tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
//                        if (twoLastNearestDistancesCloseOrAway != null) {
//
//                            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//                                    distanceInPercentToNearest = round;
//                                }
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
//                                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                Log.i(TAG_ROUND, ("round_orange_isClosing " + round));
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//
//                                    distanceInPercentToNearest = round;
//
//                                }
//
//                                Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
//                                nearestDistances.clear();
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                round = -2;
//                                Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                            }
//                        }
//                    }

                        //whoIsIt = String.valueOf(redGreenOrangeMarker.get(k));

//                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round);

                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        Log.e(" RED GREEN ORANGE KEY ", k + " ");
                        //tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(round) + " m");

                    }
                }
                else if (detect_object == 5) {

                    plGreenGeoDetection(myBlueLocation, mySpeedBlue, km);
                    plOrangeGeoDetection(myBlueLocation, mySpeedBlue, km);
                    //     ToastUtil.shortToast(getApplicationContext(), String.valueOf(detect_object));

                    ivWelcomeGreen.setVisibility(View.VISIBLE);
                    ivWelcomeOrange.setVisibility(View.VISIBLE);

                    Map.Entry<String, Float> nearestMarker = null;
                    Map.Entry<String, Float> redGreenOrangeString = null;

                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {

//                        if (nearestDistances.size() != 0) {
//
//                            isDetected = true;
//
//                            viewDetectedNearestPanel.setVisibility(View.VISIBLE);
//                            radarScanView.stopScan();
//                            radarScanView.setVisibility(View.INVISIBLE);
//                            hideGreyPanel();
//                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//
//                        } else {
//
//                            isDetected = false;
//                            stopAnimateGreyPanel();
//                            showGreyPanel();
//
//
//                        }

                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                            nearestMarker = entry;

                            Float nearestDist = nearestMarker.getValue();

                            round = Math.round(nearestMarker.getValue());
                            twoNearestDistances.add(round);
                            Log.e("   MIN   ", round + "");
                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                            Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());


                            k = nearestMarker.getKey();

                        }

                    }

                    if (twoNearestDistances.size() > 2) {


                        // if result is > 0 -> green marker goes away from myBluePosition
                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {


                            twoLastNearestDistancesCloseOrAway = goes_away;
                            Log.i(TAG_ROUND, ("round_goesAway " + round));
                            //    round = -2;

                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                            // if result is < 0 -> red marker is closing to myBluePosition

                            twoLastNearestDistancesCloseOrAway = is_closing;
                            Log.i(TAG_ROUND, ("round_isClosing " + round));
                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {

                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?

                            twoLastNearestDistancesCloseOrAway = "zero";
                        }

                    }

                    if (twoNearestDistances.size() > 20) {

                        twoNearestDistances.clear();
                    }

                    Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));

                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

                        //  blueLector.plStop();

                        round = -1;
                        String m = "NOBODY";
                        nearestDistances.clear();
                        tvDistanceToNearest.setText(m);
                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                        viewWelcomePanel.setVisibility(View.VISIBLE);
                        radarScanView.startScan();
                        radarScanView.setVisibility(View.VISIBLE);
                        // stopAnimateGreyPanel();
                        showGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                        isDetected = false;


                    } else {

                        isDetected = true;
                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                        radarScanView.stopScan();
                        radarScanView.setVisibility(View.INVISIBLE);
                        hideGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    }


                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        int s = (int) Math.round(mySpeedPlus);

                        if (v == 3.0) {

                            // for ORANGE change all function
                            whoIsIt = "ORANGE";
                            // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                            ivNearestRed.setVisibility(View.VISIBLE);
                            ivNearestGreen.setVisibility(View.INVISIBLE);

                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    //hideGreyPanel();
                                    tvTimeToNearest.setText(strTrainIsClosing);


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea)); // percent to nearest. 90% means that it's very close to you

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
                                            setCameraProperties(mGoogleMap, latLng, 60, 15, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    tvTitleNearest.setText(distanceCloseOrGoAwayRed);

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    // BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
                                    BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_orange_isClosing " + round));


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strTrainGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

//                                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                    animation1.setDuration(500);
//                                    viewDetectedNearestPanel.setAlpha(1f);
//                                    viewDetectedNearestPanel.startAnimation(animation1);

                                    //animateGreyPanel();

                                    if (distanceInPercentToRed > 95) {

                                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                                    }


                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {

                                        distanceInPercentToNearest = round;

                                    }

                                    //  tvTitleNearest.setText(distanceCloseOrGoAwayRed);

                                    Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.red_400);

                                    Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                                    Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }
                            }

                        } else if (v == 2.0) {

                            whoIsIt = "GREEN";
                            ivNearestRed.setVisibility(View.INVISIBLE);
                            ivNearestGreen.setVisibility(View.VISIBLE);
                            ivNearestOrange.setVisibility(View.INVISIBLE);
                            // tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    viewDetectedGreenPanel.setVisibility(View.VISIBLE);
                                    //hideGreyPanel();

                                    tvTimeToNearest.setText(strGreenIsClosing);

                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 14, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(14).
//                                                    bearing(0).
//                                                    build();

                                            //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(17).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }



                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.green_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.green_400);

                                    //setNearestPanelDrawableInPercent(R.drawable.green_color);
                                    // volumeMediaPlayer = 0;

                                    BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_green_isClosing " + round));

                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strGreenGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                                    // animateGreyPanel();

                                    if (round >= 0) {

//                                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                        animation1.setDuration(500);
//                                        viewDetectedNearestPanel.setAlpha(1f);
//                                        viewDetectedNearestPanel.startAnimation(animation1);

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    if (distanceInPercentToGreen > 95) {

                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                                    }

                                    setNearestPanelColorInPercent(R.color.green_400);

                                    // nearst market get value causes npe i daltego po green kiedy byl po za radius wylaczylo sie do main ativity
                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g goes_away. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    Log.i(TAG_ROUND, ("round_green_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    Log.i(TAG_THE_NEAREST, " g zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }

                            }


                        }
//                    else if (v == 3.0) {
//
//                        whoIsIt = "ORANGE";
//                        ivNearestRed.setVisibility(View.INVISIBLE);
//                        ivNearestGreen.setVisibility(View.INVISIBLE);
//                        ivNearestOrange.setVisibility(View.VISIBLE);
//                        tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
//                        if (twoLastNearestDistancesCloseOrAway != null) {
//
//                            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//                                    distanceInPercentToNearest = round;
//                                }
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
//                                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                Log.i(TAG_ROUND, ("round_orange_isClosing " + round));
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//
//                                    distanceInPercentToNearest = round;
//
//                                }
//
//                                Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
//                                nearestDistances.clear();
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                round = -2;
//                                Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                            }
//                        }
//                    }

                        //whoIsIt = String.valueOf(redGreenOrangeMarker.get(k));

//                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round);



                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        Log.e(" RED GREEN ORANGE KEY ", k + " ");
                        //tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(round) + " m");

                    }
                }

                else if (detect_object == 4) {

                    plGreenGeoDetection(myBlueLocation, mySpeedBlue, km);
                    //     ToastUtil.shortToast(getApplicationContext(), String.valueOf(detect_object));
                    ivWelcomeGreen.setVisibility(View.VISIBLE);

                    Map.Entry<String, Float> nearestMarker = null;
                    Map.Entry<String, Float> redGreenOrangeString = null;

                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {

//                        if (nearestDistances.size() != 0) {
//
//                            isDetected = true;
//
//                            viewDetectedNearestPanel.setVisibility(View.VISIBLE);
//                            radarScanView.stopScan();
//                            radarScanView.setVisibility(View.INVISIBLE);
//                            hideGreyPanel();
//                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//
//                        } else {
//
//                            isDetected = false;
//                            stopAnimateGreyPanel();
//                            showGreyPanel();
//
//
//                        }

                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                            nearestMarker = entry;

                            Float nearestDist = nearestMarker.getValue();

                            round = Math.round(nearestMarker.getValue());
                            twoNearestDistances.add(round);
                            Log.e("   MIN   ", round + "");
                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                            Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());


                            k = nearestMarker.getKey();

                        }

                    }

                    if (twoNearestDistances.size() > 2) {


                        // if result is > 0 -> green marker goes away from myBluePosition
                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {


                            twoLastNearestDistancesCloseOrAway = goes_away;
                            Log.i(TAG_ROUND, ("round_goesAway " + round));
                            //    round = -2;

                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                            // if result is < 0 -> red marker is closing to myBluePosition

                            twoLastNearestDistancesCloseOrAway = is_closing;
                            Log.i(TAG_ROUND, ("round_isClosing " + round));
                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {

                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?

                            twoLastNearestDistancesCloseOrAway = "zero";
                        }

                    }

                    if (twoNearestDistances.size() > 20) {

                        twoNearestDistances.clear();
                    }

                    Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));

                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

                        //  blueLector.plStop();

                        round = -1;
                        String m = "NOBODY";
                        nearestDistances.clear();
                        tvDistanceToNearest.setText(m);
                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                        viewWelcomePanel.setVisibility(View.VISIBLE);
                        radarScanView.startScan();
                        radarScanView.setVisibility(View.VISIBLE);
                        // stopAnimateGreyPanel();
                        showGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                        isDetected = false;


                    } else {

                        isDetected = true;
                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                        radarScanView.stopScan();
                        radarScanView.setVisibility(View.INVISIBLE);
                        hideGreyPanel();
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    }


                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        int s = (int) Math.round(mySpeedPlus);

                        if (v == 2.0) {

                            whoIsIt = "GREEN";
                            ivNearestRed.setVisibility(View.INVISIBLE);
                            ivNearestGreen.setVisibility(View.VISIBLE);
                            ivNearestOrange.setVisibility(View.INVISIBLE);
                            // tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    viewDetectedGreenPanel.setVisibility(View.VISIBLE);
                                    //hideGreyPanel();

                                    tvTimeToNearest.setText(strGreenIsClosing);

                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                        if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 14, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(14).
//                                                    bearing(0).
//                                                    build();

                                            //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        }


                                        if (distanceInPercentToNearest > 70) {

                                            setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
                                            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(17).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                        }



                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.green_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.green_400);

                                    //setNearestPanelDrawableInPercent(R.drawable.green_color);
                                    // volumeMediaPlayer = 0;

                                    BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_green_isClosing " + round));

                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    tvTimeToNearest.setText(strGreenGoesAway);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                                    // animateGreyPanel();

                                    if (round >= 0) {

//                                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                        animation1.setDuration(500);
//                                        viewDetectedNearestPanel.setAlpha(1f);
//                                        viewDetectedNearestPanel.startAnimation(animation1);

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

                                    if (distanceInPercentToGreen > 95) {

                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                                    }

                                    setNearestPanelColorInPercent(R.color.green_400);

                                    // nearst market get value causes npe i daltego po green kiedy byl po za radius wylaczylo sie do main ativity
                                    if (twoNearestDistances.size() > 1) {
                                        diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                                    }
                                    Log.i(TAG_THE_NEAREST, " g goes_away. round: " + round + " diff: " + diff +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                    Log.i(TAG_ROUND, ("round_green_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    Log.i(TAG_THE_NEAREST, " g zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }

                            }


                        }
//                    else if (v == 3.0) {
//
//                        whoIsIt = "ORANGE";
//                        ivNearestRed.setVisibility(View.INVISIBLE);
//                        ivNearestGreen.setVisibility(View.INVISIBLE);
//                        ivNearestOrange.setVisibility(View.VISIBLE);
//                        tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
//                        if (twoLastNearestDistancesCloseOrAway != null) {
//
//                            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//                                    distanceInPercentToNearest = round;
//                                }
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
//                                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
//                                Log.i(TAG_ROUND, ("round_orange_isClosing " + round));
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {
//
//                                if (round >= 0) {
//
//                                    int di = round;
//                                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));
//
//                                } else {
//
//                                    distanceInPercentToNearest = round;
//
//                                }
//
//                                Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//
//                                ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                        vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                        vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                        vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                        vnp92, vnp94, vnp96, vnp98);
//
//                                Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
//                                nearestDistances.clear();
//
//
//                            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {
//
//                                round = -2;
//                                Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
//                                        " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);
//
//                            }
//                        }
//                    }

                        //whoIsIt = String.valueOf(redGreenOrangeMarker.get(k));

//                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round);



                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        Log.e(" RED GREEN ORANGE KEY ", k + " ");
                        //tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(round) + " m");

                    }
                }
                else if (detect_object == 3) {

                    plOrangeGeoDetection(myBlueLocation, mySpeedBlue, km);
                    //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(detect_object));
                    ivWelcomeOrange.setVisibility(View.VISIBLE);

                    Map.Entry<String, Float> nearestMarker = null;
                    Map.Entry<String, Float> redGreenOrangeString = null;

                    for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {

                        if (nearestDistances.size() != 0) {

                            isDetected = true;

                            viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                            radarScanView.stopScan();
                            radarScanView.setVisibility(View.INVISIBLE);
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));


                        } else {

                            isDetected = false;


                        }

                        if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                            nearestMarker = entry;

                            Float nearestDist = nearestMarker.getValue();

                            round = Math.round(nearestMarker.getValue());
                            twoNearestDistances.add(round);
                            Log.e("   MIN   ", round + "");
                            Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                            Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());

                            if (twoNearestDistances.size() > 2) {


                            }


                            k = nearestMarker.getKey();


                        }

                    }

                    if (twoNearestDistances.size() > 2) {


                        // if result is > 0 -> green marker goes away from myBluePosition
                        if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {


                            twoLastNearestDistancesCloseOrAway = goes_away;
                            Log.i(TAG_ROUND, ("round_goesAway " + round));
                            //    round = -2;

                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                            //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                            // if result is < 0 -> red marker is closing to myBluePosition

//                        Log.i(TAG_GREEN, (TAG_GREEN_DECREASE_LAST + " ENT " + greenDist.get(greenDist.size() - 1)));
//                        Log.i(TAG_GREEN, (TAG_GREEN_DECREASE_B_LAST + " ENT "  + greenDist.get(greenDist.size() - 2)));

                            twoLastNearestDistancesCloseOrAway = is_closing;
                            Log.i(TAG_ROUND, ("round_isClosing " + round));
                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);


                        } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {

                            // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?

                            twoLastNearestDistancesCloseOrAway = "zero";
                        }

                    }

                    if (twoNearestDistances.size() > 20) {

                        twoNearestDistances.clear();
                    }

                    if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

                        //  blueLector.plStop();

                        round = -1;
                        String m = "NOBODY";
                        nearestDistances.clear();
                        tvDistanceToNearest.setText(m);
                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
                        viewDetectedOrangePanel.setVisibility(View.INVISIBLE);
                        viewWelcomePanel.setVisibility(View.VISIBLE);
                        radarScanView.startScan();
                        radarScanView.setVisibility(View.VISIBLE);
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                        isDetected = false;

//                        REQUEST_BALANCED.setInterval(10).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, REQUEST_BALANCED, this);
//                        }

                    }


                    //Float v = (Float)redGreenOrangeMarker.get(k);

                    Float v = redGreenOrangeMarker.get(k); // to find nearest key
                    if (v != null) {

                        int s = (int) Math.round(mySpeedPlus);

                         if (v == 3.0) {

                            whoIsIt = "ORANGE";
                            ivNearestRed.setVisibility(View.INVISIBLE);
                            ivNearestGreen.setVisibility(View.INVISIBLE);
                            ivNearestOrange.setVisibility(View.VISIBLE);
                            tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
                            if (twoLastNearestDistancesCloseOrAway != null) {

                                if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {
                                        distanceInPercentToNearest = round;
                                    }

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.orange_400);

                                    Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                                 //   BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed);
                                    Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                                    Log.i(TAG_ROUND, ("round_orange_isClosing " + round));


                                } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                                    if (round >= 0) {

                                        int di = round;
                                        distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                                    } else {

                                        distanceInPercentToNearest = round;

                                    }

                                    Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.orange_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                                    setNearestPanelColorInPercent(R.color.orange_400);

                                    Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
                                    nearestDistances.clear();


                                } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                                    round = -2;
                                    Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
                                            " is: " + whoIsIt + " v: " + s + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

                                }
                            }
                        }

                        //whoIsIt = String.valueOf(redGreenOrangeMarker.get(k));

//                BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round);
                        Log.e("    ROUND MIN    ", round + "" + " KEY " + k + " WHO " + whoIsIt);
                        Log.e(" RED GREEN ORANGE KEY ", k + " ");
                      //  tvDistanceToNearest.setText("dist: " + String.valueOf(round) + " " + whoIsIt + " " + distanceInPercentToNearest + " %"); // display only the nearest distance from all objects // but doesnt display which this object is
                        tvDistanceToNearest.setText(String.valueOf(round) + " m");
                    }

                }

                // long time2 = System.currentTimeMillis();
                // long time3 = time2 - time1;
                // ten process nie jest zbyt kosztowny. Na początku przejscie przez petle do spełnienia odpowiedniego warunku
                // wynosi 50-60 ms. Później to jest 1 ms. (time3)
            }

        }

    }

//    else if(mLector.equals(lector_en))
//
//    {
//
//        if (detect_object == 9) {
//
//            //enRedGeoDetection(myBlueLocation, mySpeedBlue, km);
//            // enGreen
//            // enOrange
//
//        } else if (detect_object == 8) {
//
//            //  enRedGeoDetection(myBlueLocation, mySpeedBlue, km);
//            //enGreen
//
//        } else if (detect_object == 7) {
//
//            //enRedGeoDetection(myBlueLocation, mySpeedBlue, km);
//            // enOrange
//
//        } else if (detect_object == 6) {
//
//            //enRedGeoDetection(myBlueLocation, mySpeedBlue, km);
//
//
//        } else if (detect_object == 5) {
//
//            // enGreen
//            // enOrange
//
//        } else if (detect_object == 4) {
//
//            // enGreen
//
//        } else if (detect_object == 3) {
//
//            // enOrange
//
//        }
////
//    }



    private void plRedGeoDetection(final Location myBlueLocation, final Float mySpeedBlue, double km) {

        myRedGeoQuery = redGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude),km );

        myRedGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                viewWelcomePanel.setVisibility(View.INVISIBLE);
               // viewDetectedNearestPanel.setVisibility(View.VISIBLE);

               // SlideToAbove();

                viewDetectedRedPanel.setVisibility(View.VISIBLE);
                //viewDetectedRedPanel.setAlpha(0.8f);


             //   viewDetailsDetectedPanel.setVisibility(View.VISIBLE);

                //tvDetected.setText("Detected");
                //    detectedObject.start();

                Marker redMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                        .icon((BitmapDescriptorFactory.fromResource(R.drawable.marker_red))));
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                redMarkers.put(key, redMarker);
                int amountRed = redMarkers.size();
                String rmSize = String.valueOf(amountRed); // size in string of redMarkersSize
                tvNumberRed.setText(rmSize);

//                if (amountRed == 1) {
//
//                    tvNumberRed.setText(rmSize + " is detected");
//                }
//                else if (amountRed > 1) {
//
//                    tvNumberRed.setText(rmSize + " are detected");
//                }

                // count distance from my current location to another in the circle

                Location itemLocation = new Location("itemRedLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   KEY ENTERED   distance to       " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");

                Float distanceToRed = myBlueLocation.distanceTo(itemLocation); // distance to every red

                distancesToRed.put(key, distanceToRed); // put here every distance


                Map.Entry<String, Float> nearestRedMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToRed.entrySet()) {
                    if (nearestRedMarker == null || nearestRedMarker.getValue() > entry.getValue()) {
                        nearestRedMarker = entry;


                        final String keyOfNearestRed = nearestRedMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestRed = nearestRedMarker.getValue(); // distance to the nearest red marker


                        int d = Math.round(distanceToNearestRed);
                        distanceInPercentToRed = ((d * 100)/myRadiusArea);


                        redDist.add(distanceToNearestRed); // add every nearest red distance and compare two last values in arraylist (redDist)


                        if (redDist.size() > 2) {


                            // if result is > 0 -> red marker goes away from myBluePosition
                            if (((redDist.get(redDist.size() - 1)) - (redDist.get(redDist.size() - 2))) > 0) {

                                Log.e(" RED INCREASE ", (redDist.get(redDist.size() - 1)) + " RED INCREASE ");

                                //tvNumberRed.setText(rmSize + " goes away");

                                AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                                animation1.setDuration(500);
                                viewDetectedRedPanel.setAlpha(1f);
                                viewDetectedRedPanel.startAnimation(animation1);
                                //tvTimeToGreen.setText(rmSize);
                                if (redMarkers.size() == 1) {

                                    if (distanceInPercentToRed > 95) {
                                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                                    }
                                }
                                else if (redMarkers.size() == 0) {
                                    viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                                }


                            }
                            else if (((redDist.get(redDist.size() - 1)) - (redDist.get(redDist.size() - 2))) < 0) {
                                // if result is < 0 -> red marker is closing to myBluePosition
                                Log.e(" RED DECREASE ", (redDist.get(redDist.size() - 1)) + " RED DECREASE ");

//
//                                ivRedPanel.post(new Runnable() {
//                                    int i = 0;
//                                    @Override
//                                    public void run() {
//                                        ivRedPanel.setBackgroundResource(arrayRedSempahoreDrawable[i]);
//                                        i++;
//                                        if (i == arrayRedSempahoreDrawable.length) {
//                                            i = 0;
//                                        }
//                                        ivRedPanel.postDelayed(this, 1000);
//
//                                    }
//                                });


                                //tvNumberRed.setText(rmSize + " is closing");
                                //tvTimeToGreen.setText(rmSize);

                            }

                        }

                        if (redDist.size() > 20) {

                            redDist.clear();
                        }
                      //  Float lastPosition = redDist.get(redDist.size()); // check size of this array
                        // tvIncrease.setText(String.valueOf(distanceToNearest));

//                                System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
//
                               Log.e(" SIZE OF redDist ", redDist.size() + "");

                          //      ToastUtil.shortToast(getApplicationContext(), String.valueOf(redDist.size()));

//             }
                        nearestDistances.put(key, distanceToNearestRed);
                        redGreenOrangeMarker.put(key, 1f); // assign "red" to key of the nearest value

                      //  BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), distanceToNearestRed);

                        final int timeToNearestRed = Math.round(distanceToNearestRed / mySpeedBlue);
                        final Float t = distanceToNearestRed / mySpeedBlue;

                        String myD = String.format("%.0f", distanceToNearestRed);
                        String myT = String.format("%.2f", t);

                       // tvDistanceToRed.setText(myD + " m");

                        if (t.isNaN()) {

                          //  tvTimeToRed.setText("0");

                        } else if (t.isInfinite()) {

                           // tvTimeToRed.setText("Too much");
                        }
                        else {
                           // tvTimeToRed.setText(myT + " s");
                        }




                        //  tvDetailsTime.setText(myT + " s");
                        // tvDetailsTime.setText(String.valueOf(t) + " s");



                    } else {

                     //   tvIncrease.setText("Objekt się oddala");
                    }
                }


            }

            @Override
            public void onKeyExited(String key) {

                Marker redMarker = redMarkers.get(key);
                if (redMarker != null) {
                    redMarker.remove();
                    redMarkers.remove(key);
                }



                distancesToRed.remove(key); // delete user from distances
                nearestDistances.remove(key);
                redGreenOrangeMarker.remove(key);

//                if (nearestDistances.size() == 0) {
//
//                    tvDistanceToNearest.setText(" EMPTY ");
//
//                }
           //     nearestDistances.clear();

                //redDist.clear();

                int amountRed = redMarkers.size(); // size of RedMarkers after key exited.
                String rmSize = String.valueOf(amountRed);

                if (amountRed > 0 ) {

                    tvNumberRed.setText(rmSize);
                   // tvTimeToGreen.setText(amountRedMarkers);
//                    if (amountRed == 1) {
//
//                        tvNumberRed.setText(rmSize + " is detected");
//                    }
//                    else if (amountRed > 1) {
//
//                        tvNumberRed.setText(rmSize + " are detected");
//                    }

                } else {

                    tvNumberRed.setText(rmSize);
                    viewDetectedRedPanel.setVisibility(View.INVISIBLE);
//                    viewDetectedRedPanel.animate()
//                            .translationY(0)
//                            .alpha(0.0f)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    viewDetectedRedPanel.setVisibility(View.GONE);
//                                }
//                            });
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                Marker redMarker = redMarkers.get(key);
                if (redMarker != null) {
                    redMarker.remove();
                    redMarkers.remove(key);
                }

                int amountRed = redMarkers.size(); // size of RedMarkers after key exited.
                String rmSize = String.valueOf(amountRed);

                tvNumberRed.setText(rmSize);

//                if (amountRed == 1) {
//
//                    tvNumberRed.setText(rmSize + " is detected");
//                }
//                else if (amountRed > 1) {
//
//                    tvNumberRed.setText(rmSize + " are detected");
//                }

                distancesToRed.remove(key); // when red is moved, previous key is deleted.
                nearestDistances.remove(key); // when red is moved, previous key is deleted
                redGreenOrangeMarker.remove(key);
                //    nearestDistances.remove(key);

                // I create a new current location of a red user and put into distances.

                Location itemLocation = new Location("itemLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   after KEY MOVED   distance to       " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");
                Float distance = myBlueLocation.distanceTo(itemLocation);

                distancesToRed.put(key, distance);



                Map.Entry<String, Float> nearestRedMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToRed.entrySet()) {
                    if (nearestRedMarker == null || nearestRedMarker.getValue() > entry.getValue()) {
                        nearestRedMarker = entry;

                      //  tvIncrease.setText("Objekt się zbliża");
                        final String keyOfNearestRed = nearestRedMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestRed = nearestRedMarker.getValue(); // distance to the nearest red marker

                        redDist.add(distanceToNearestRed);

                        if (redDist.size() > 2) {

                            if (((redDist.get(redDist.size() - 1)) - (redDist.get(redDist.size() - 2))) > 0) {

                                Log.e(" RED INCREASE ", (redDist.get(redDist.size() - 1)) + " RED INCREASE ");
                               // tvNumberRed.setText(" goes away");

                                AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                                animation1.setDuration(500);
                                viewDetectedRedPanel.setAlpha(1f);
                                viewDetectedRedPanel.startAnimation(animation1);
                                //tvTimeToGreen.setText(amountRedMarkers);

                            } else if (((redDist.get(redDist.size() - 1)) - (redDist.get(redDist.size() - 2))) < 0) {
                                Log.e(" RED DECREASE ", (redDist.get(redDist.size() - 1)) + " RED DECREASE ");
                                //tvNumberRed.setText(" is closing");
                                //tvTimeToGreen.setText(amountRedMarkers);

                            }
                        }

                        if (redDist.size() > 20) {

                            redDist.clear();

                        }
                        //  Float lastPosition = redDist.get(redDist.size()); // check size of this array
                        // tvIncrease.setText(String.valueOf(distanceToNearest));

//                                System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
//
                        Log.e(" SIZE OF redDist ", redDist.size() + "");

                       // ToastUtil.shortToast(getApplicationContext(), String.valueOf(redDist.size()));

                        nearestDistances.put(key, distanceToNearestRed);
                        redGreenOrangeMarker.put(key, 1f);

                        final int timeToNearestRed = Math.round(distanceToNearestRed / mySpeedBlue);
                        final Float t = distanceToNearestRed / mySpeedBlue;

                        String myD = String.format("%.0f", distanceToNearestRed);
                        String myT = String.format("%.2f", t);

                     //   tvDistanceToRed.setText(myD + " m");

                        if (t.isNaN()) {

                       //     tvTimeToRed.setText("0");

                        } else if (t.isInfinite()) {

                         //   tvTimeToRed.setText("Too much");
                        }
                        else {
                           // tvTimeToRed.setText(myT + " s");
                        }


                        // tvTimeToRed.setText(myT + " s");
                        //  tvDetailsTime.setText(String.valueOf(t) + " s");

//                        tvDetailsDistance.setText(String.valueOf(distanceToNearestRed + " m"));
//                        tvDetailsTime.setText(String.valueOf(timeToNearestRed + " s"));

                    }
                    else {
                       // tvIncrease.setText("Obiekt się oddala");
                    }
                }


            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });



    }

    private void plGreenGeoDetection(final Location myBlueLocation, final Float mySpeedBlue, double km) {

        myGreenGeoQuery = greenGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude),km );

        myGreenGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                viewWelcomePanel.setVisibility(View.INVISIBLE);
                viewDetectedGreenPanel.setVisibility(View.VISIBLE);
             //   viewDetectedNearestPanel.setVisibility(View.VISIBLE);
               // viewDetailsDetectedPanel.setVisibility(View.VISIBLE);

              //  tvDetectedGreen.setText("Detected Green");
                //    detectedObject.start();

                Marker greenMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                       // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        .icon((BitmapDescriptorFactory.fromResource(R.drawable.marker_green))));
                greenMarkers.put(key, greenMarker);
                int amountGreen = greenMarkers.size();
                String gmSize = String.valueOf(amountGreen); // size in string of redMarkersSize

                // count distance from my current location to another in the circle

                Location itemLocation = new Location("itemGreenLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   KEY ENTERED   distance to GREEN " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");

                Float distanceToGreen = myBlueLocation.distanceTo(itemLocation); // distance to every green

                distancesToGreen.put(key, distanceToGreen); // put here every green distance

                Map.Entry<String, Float> nearestGreenMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToGreen.entrySet()) {
                    if (nearestGreenMarker == null || nearestGreenMarker.getValue() > entry.getValue()) {
                        nearestGreenMarker = entry;


                        final String keyOfNearestGreen = nearestGreenMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestGreen = nearestGreenMarker.getValue(); // distance to the nearest red marker

                        distToGreenIfChanged = Math.round(distanceToNearestGreen);

                        //Log.i(TAG_GREEN, (TAG_GREEN_NEAREST + distanceToNearestGreen));
                        double s = mySpeedBlue * 3.6 * 1.06;
                        int v = (int) Math.round(s);
                        Log.i(TAG_GREEN, (TAG_GREEN_NEAREST + nearestGreenMarker.getValue() + " distToGreenIfChanged " + distToGreenIfChanged + " my speed: " + v + " key: " + nearestGreenMarker.getKey()));

                        long timeToNearestOrange = Math.round(distanceToNearestGreen / mySpeedBlue);

                        long minute = TimeUnit.SECONDS.toMinutes(timeToNearestOrange) - (TimeUnit.SECONDS.toHours(timeToNearestOrange) * 60);
                        long second = TimeUnit.SECONDS.toSeconds(timeToNearestOrange) - (TimeUnit.SECONDS.toMinutes(timeToNearestOrange) * 60);

                        nearestDistances.put(key, distanceToNearestGreen);
                        redGreenOrangeMarker.put(key, 2f);

                        tvNumberGreen.setText(gmSize);


//                        if (amountGreen == 1) {
//
//                            tvDistanceToGreen.setText(strPerson);
//                            tvTimeToGreen.setText(strIsDetected);
//                        }
//                        else if (amountGreen > 1) {
//
//                            tvDistanceToGreen.setText(strPeople);
//                            tvTimeToGreen.setText(strAreDetected);
//                        }


                        greenDist.add(distanceToNearestGreen); // add every nearest green distance and compare two last values in arraylist (redDist)

                        int d = Math.round(distanceToNearestGreen);
                        distanceInPercentToGreen = ((d * 100)/myRadiusArea);

                        if (greenDist.size() > 2) {



                            // if result is > 0 -> green marker goes away from myBluePosition
                            if (((greenDist.get(greenDist.size() - 1)) - (greenDist.get(greenDist.size() - 3))) > 0) {

                                Log.e(" GREEN INCREASE -1 ", (greenDist.get(greenDist.size() - 1)) + " GREEN INCREASE -1 ");
                                Log.e(" GREEN INCREASE -2 ", (greenDist.get(greenDist.size() - 2)) + " GREEN INCREASE -2 ");

                                Log.i(TAG_GREEN, (TAG_GREEN_INCREASE_LAST + " ENT "  + greenDist.get(greenDist.size() - 1)));
                                Log.i(TAG_GREEN, (TAG_GREEN_INCREASE_B_LAST + " ENT "  + greenDist.get(greenDist.size() - 2)));

                                distanceCloseOrGoAwayGreen = " goes away";
                               // tvNumberGreen.setText(gmSize + distanceCloseOrGoAwayGreen + " " + String.valueOf(distanceInPercentToGreen) + " %");
                              //  tvNumberGreen.setText(gmSize + " x");

                                if (greenMarkers.size() == 1) {

                                    if (distanceInPercentToGreen > 95) {
                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                                    }
                                }
                                else if (greenDist.size() == 0) {

                                    viewDetectedGreenPanel.setVisibility(View.INVISIBLE);

                                }


                            } else if (((greenDist.get(greenDist.size() - 1)) - (greenDist.get(greenDist.size() - 3))) < 0) {
                                // if result is < 0 -> red marker is closing to myBluePosition
                                Log.e(" GREEN DECREASE -1 ", (greenDist.get(greenDist.size() - 1)) + " GREEN DECREASE -1 ");
                                Log.e(" GREEN DECREASE -2 ", (greenDist.get(greenDist.size() - 2)) + " GREEN DECREASE -2 ");

                                Log.i(TAG_GREEN, (TAG_GREEN_DECREASE_LAST + " ENT " + greenDist.get(greenDist.size() - 1)));
                                Log.i(TAG_GREEN, (TAG_GREEN_DECREASE_B_LAST + " ENT "  + greenDist.get(greenDist.size() - 2)));

//                                nearestDistances.put(key, distanceToNearestGreen);
//                                redGreenOrangeMarker.put(key, 2f);

                                distanceCloseOrGoAwayGreen= " is closing";
                               // tvNumberGreen.setText(gmSize + distanceCloseOrGoAwayGreen + " " + String.valueOf(distanceInPercentToGreen) + " %");

                             //   tvNumberGreen.setText(gmSize + " x");

                            }

                        }

                        if (greenDist.size() > 20) {

                            greenDist.clear();

                            // print greenDist in LogCat
                        }

                     //   tvIncrease.setText("the nearest green: " + String.valueOf(distanceToNearestGreen));
                     //   BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), distanceToNearestGreen);

                        final int timeToNearestRed = Math.round(distanceToNearestGreen / mySpeedBlue);
                        final Float t = distanceToNearestGreen / mySpeedBlue;

                        String myD = String.format("%.0f", distanceToNearestGreen);
                        String myT = String.format("%.2f", t);

                       // tvDistanceToGreen.setText(myD + " m");

                        if (t.isNaN()) {

                        //    tvTimeToGreen.setText("0");

                        } else if (t.isInfinite()) {

                           // tvTimeToGreen.setText("Too much");
                        }
                        else {
                           // tvTimeToGreen.setText(myT + " s");
                           // tvTimeToGreen.setText(String.valueOf(minute) + " m " + String.valueOf(second) +  " s");
                        }

                        //  tvDetailsTime.setText(myT + " s");
                        // tvDetailsTime.setText(String.valueOf(t) + " s");

                    } else {

                     //   tvIncrease.setText("Objekt się oddala");
                    }
                }


            }

            @Override
            public void onKeyExited(String key) {

                Marker greenMarker = greenMarkers.get(key);
                if (greenMarker != null) {
                    greenMarker.remove();
                    greenMarkers.remove(key);
                }

             //tr   ToastUtil.shortToast(getApplicationContext(), " KEY EXITED ... ");
//
//                distancesToGreen.remove(key); // delete user from distances
//                nearestDistances.remove(key);
//                redGreenOrangeMarker.remove(key);

                if (greenMarkers.size() > 0) {

                    greenMarkers.remove(key);

                }

                if (greenMarkers.size() == 1) {

                    if (distanceInPercentToGreen > 90) {
                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                    }
                }


             //   nearestDistances.clear();

                int amountGreen = greenMarkers.size();
                String gmSize = String.valueOf(amountGreen); // size in string of redMarkersSize
                //tvNumberGreen.setText(gmSize);

                tvNumberGreen.setText(gmSize);
                if (amountGreen == 0) {

//                    tvDistanceToGreen.setText(strPerson);
//                    tvTimeToGreen.setText(strIsDetected);
//                }
//                else if (amountGreen > 1) {
//
//                    tvDistanceToGreen.setText(strPeople);
//                    tvTimeToGreen.setText(strAreDetected);
//                }
//                else {
                    viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                Marker greenMarker = greenMarkers.get(key);
                if (greenMarker != null) {
                    greenMarker.remove();
                    greenMarkers.remove(key);
                }

                int amountGreen = greenMarkers.size();
                String gmSize = String.valueOf(amountGreen); // size in string of redMarkersSize
                tvNumberGreen.setText(gmSize);


//                if (amountGreen == 1) {
//
//                    tvDistanceToGreen.setText(strPerson);
//                    tvTimeToGreen.setText(strIsDetected);
//                }
//                else if (amountGreen > 1) {
//
//                    tvDistanceToGreen.setText(strPeople);
//                    tvTimeToGreen.setText(strAreDetected);
//                }

                // if there is nobody as a green object, for instance: when key isn't exited from db, but is out of my radius it should be invisible
                // now when green is out of my radius it still shows in blue activity (but values not change)
                if (distancesToGreen.size() == 0) {

                    viewDetectedGreenPanel.setVisibility(View.INVISIBLE);

                }


//
                distancesToGreen.remove(key); // when red is moved, previous key is deleted.
                nearestDistances.remove(key); // when red is moved, previous key is deleted.
                redGreenOrangeMarker.remove(key);

                // I create a new current location of a red user and put into distances.

                Location itemLocation = new Location("itemGreenLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   after KEY MOVED   distance to GREEN " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");
                Float distance = myBlueLocation.distanceTo(itemLocation);
                double dis = (double) distance;

                distancesToGreen.put(key, distance);

                // I'd like to use it when distance to nearest green is bigger than my radius area, delete this key, and after this, somehow when it's possible set panel invisible
//                if (dis > myQueryAreaInKm) {
//
//                    distancesToGreen.remove(key); // when red is moved, previous key is deleted.
//                    nearestDistances.remove(key); // when red is moved, previous key is deleted.
//                    redGreenOrangeMarker.remove(key);
//
//                }



                Map.Entry<String, Float> nearestGreenMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToGreen.entrySet()) {
                    if (nearestGreenMarker == null || nearestGreenMarker.getValue() > entry.getValue()) {
                        nearestGreenMarker = entry;

                       // tvIncrease.setText("Obiekt się zbliża");
                        final String keyOfNearestGreen = nearestGreenMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestGreen = nearestGreenMarker.getValue(); // distance to the nearest red marker

                        nearestDistances.put(key, distanceToNearestGreen);
                        redGreenOrangeMarker.put(key, 2f);

                        greenDist.add(distanceToNearestGreen); // add every nearest red distance and compare two last values in arraylist (redDist)

                        Log.i(TAG_GREEN, (TAG_GREEN_NEAREST + distanceToNearestGreen));

                        if (greenDist.size() > 2) {


                            // if result is > 0 -> red marker goes away from myBluePosition
                            if (((greenDist.get(greenDist.size() - 1)) - (greenDist.get(greenDist.size() - 2))) > 0) {

                                Log.e(" GREEN INCREASE M -1 ", (greenDist.get(greenDist.size() - 1)) + " GREEN INCREASE -1 ");
                                Log.e(" GREEN INCREASE M -2 ", (greenDist.get(greenDist.size() - 2)) + " GREEN INCREASE -2 ");

                                Log.i(TAG_GREEN, (TAG_GREEN_INCREASE_LAST + greenDist.get(greenDist.size() - 1)));
                                Log.i(TAG_GREEN, (TAG_GREEN_INCREASE_B_LAST + greenDist.get(greenDist.size() - 2)));

                                distanceCloseOrGoAwayGreen = " goes away";
                               // tvNumberGreen.setText(distanceCloseOrGoAwayGreen);
                             //   tvNumberGreen.setText(gmSize + " x");
                                if (greenMarkers.size() == 1) {

                                    if (distanceInPercentToGreen > 90) {
                                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                                    }
                                }


//                                distancesToGreen.remove(key); // key is deleted.
//                                nearestDistances.remove(key); // key is deleted.
//                                redGreenOrangeMarker.remove(key); // key is deleted

                                // when green is out of my radius there panel is no setting as invisible
                               // viewDetectedGreenPanel.setVisibility(View.INVISIBLE);

//                                if (distancesToGreen.size() == 0) {
//
//                                    viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
//
//                                }

                            } else if (((greenDist.get(greenDist.size() - 1)) - (greenDist.get(greenDist.size() - 2))) < 0) {
                                // if result is < 0 -> red marker is closing to myBluePosition
                                Log.e(" GREEN DECR M -1 ", (greenDist.get(greenDist.size() - 1)) + " GREEN DECR -1 ");
                                Log.e(" GREEN DECR M -2 ", (greenDist.get(greenDist.size() - 2)) + " GREEN DECR -2 ");

                                Log.i(TAG_GREEN, (TAG_GREEN_DECREASE_LAST + greenDist.get(greenDist.size() - 1)));
                                Log.i(TAG_GREEN, (TAG_GREEN_DECREASE_B_LAST + greenDist.get(greenDist.size() - 2)));

//                                nearestDistances.put(key, distanceToNearestGreen);
//                                redGreenOrangeMarker.put(key, 2f);

                                distanceCloseOrGoAwayGreen= " is closing";
                               // tvNumberGreen.setText(distanceCloseOrGoAwayGreen);
                              //  tvNumberGreen.setText(gmSize + " x");

                            }

                        }

                        if (greenDist.size() > 20) {

                            greenDist.clear();
                        }

                        final int timeToNearestGreen = Math.round(distanceToNearestGreen / mySpeedBlue);
                        final Float t = distanceToNearestGreen / mySpeedBlue;

                      //  tvIncrease.setText("the nearest green: " + String.valueOf(distanceToNearestGreen));

                        String myD = String.format("%.0f", distanceToNearestGreen);
                        String myT = String.format("%.2f", t);

                     //   tvDistanceToGreen.setText(myD + " m");


                        if (t.isNaN()) {

                          //  tvTimeToGreen.setText("0");

                        } else if (t.isInfinite()) {

                          //  tvTimeToGreen.setText("Too much");
                        }
                        else {
                         //   tvTimeToGreen.setText(myT + " s");
                        }


                        //   tvDetailsTime.setText(myT + " s");
                        //  tvDetailsTime.setText(String.valueOf(t) + " s");

//                        tvDetailsDistance.setText(String.valueOf(distanceToNearestRed + " m"));
//                        tvDetailsTime.setText(String.valueOf(timeToNearestRed + " s"));

                    }
                    else {
                      //  tvIncrease.setText("Obiekt się oddala");
                    }
                }


            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    private void plOrangeGeoDetection(final Location myBlueLocation, final Float mySpeedBlue, double km) {

        myOrangeGeoQuery = orangeGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude),km );

        myOrangeGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                viewWelcomePanel.setVisibility(View.INVISIBLE);
                viewDetectedOrangePanel.setVisibility(View.VISIBLE);
                //viewDetectedNearestPanel.setVisibility(View.VISIBLE);
                // viewDetailsDetectedPanel.setVisibility(View.VISIBLE);

             //   tvDetectedOrange.setText("Detected Orange");
                //    detectedObject.start();

                Marker orangeMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                       // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        .icon((BitmapDescriptorFactory.fromResource(R.drawable.marker_orange))));
                orangeMarkers.put(key, orangeMarker);
                int orangeMarkersSize = orangeMarkers.size();
                String gmSize = String.valueOf(orangeMarkersSize); // size in string of orangeMarkersSize
                tvNumberOrange.setText(gmSize);

                // count distance from my current location to another in the circle

                Location itemLocation = new Location("itemOrangeLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   KEY ENTERED   distance to ORANGE " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");

                Float distanceToOrange = myBlueLocation.distanceTo(itemLocation); // distance to every red

                distancesToOrange.put(key, distanceToOrange); // put here every distance

                Map.Entry<String, Float> nearestOrangeMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToOrange.entrySet()) {
                    if (nearestOrangeMarker == null || nearestOrangeMarker.getValue() > entry.getValue()) {
                        nearestOrangeMarker = entry;

                      //  tvIncrease.setText("Objekt się zbliża");
                        final String keyOfNearestOrange = nearestOrangeMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestOrange = nearestOrangeMarker.getValue(); // distance to the nearest red marker

                        int d = Math.round(distanceToNearestOrange);
                        distanceInPercentToOrange = ((d * 100)/myRadiusArea);

                   //     tvDetectedOrange.setText("Detected " + gmSize + " the nearest: " + String.valueOf(distanceToNearestOrange));

                        nearestDistances.put(key, distanceToNearestOrange); // put the nearest dist to the map of nearestDistances. Next it will choose which distance from all objects is the nearest one.
                        redGreenOrangeMarker.put(key, 3f); // means that the key of the nearest belongs to the orange object
                        // with it I can recognize which object is the nearest one.

                        orangeDist.add(distanceToNearestOrange); // add every nearest red distance and compare two last values in arraylist (redDist)

                        double s = mySpeedBlue * 3.6 * 1.06;

                        Log.i(TAG_ORANGE, (TAG_ORANGE_NEAREST + nearestOrangeMarker.getValue() + " my speed: " + s + " key: " + nearestOrangeMarker.getKey()));

                        if (orangeDist.size() > 2) {


                            // if result is > 0 -> red marker goes away from myBluePosition
                            if (((orangeDist.get(orangeDist.size() - 1)) - (orangeDist.get(orangeDist.size() - 3))) > 0) {

                                Log.e(" ORANGE INCREASE ", (orangeDist.get(orangeDist.size() - 1)) + " ORANGE INCREASE ");
                                distanceCloseOrGoAwayOrange = " goes away";
                                tvNumberOrange.setText(gmSize + distanceCloseOrGoAwayOrange + " " + String.valueOf(distanceInPercentToOrange) + " %");

//                                if (distanceInPercentToOrange >= 40) {
//
//                                    // hm.. to nie powoduje zadnych zmian... a moze powoduje ale jak jest ciagle onKeyEntered to odswieza
//                                    // sie na nowo wszystko i nie widac po prostu tych zmian?
//                                    distancesToOrange.remove(key); // delete user from distances
//                                    nearestDistances.remove(key);
//                                    redGreenOrangeMarker.remove(key);
//                                    orangeDist.remove(orangeDist.size() - 1);

 //                               }

                                if (orangeDist.size() == 0) {

                                    viewDetectedOrangePanel.setVisibility(View.INVISIBLE);
                                }


                            } else if (((orangeDist.get(orangeDist.size() - 1)) - (orangeDist.get(orangeDist.size() - 3))) < 0) {

//                                nearestDistances.put(key, distanceToNearestOrange);
//                                redGreenOrangeMarker.put(key, 3f);
                                // if result is < 0 -> red marker is closing to myBluePosition
                                Log.e(" ORANGE DECREASE ", (orangeDist.get(orangeDist.size() - 1)) + " ORANGE DECREASE ");
                                distanceCloseOrGoAwayOrange = " is closing";
                                tvNumberOrange.setText(gmSize + distanceCloseOrGoAwayOrange + " " + String.valueOf(distanceInPercentToOrange) + " %");

                            }

                        }

                        if (orangeDist.size() > 20) {

                            orangeDist.clear();
                        }

                        //   BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), distanceToNearestGreen);

                        long timeToNearestOrange = Math.round(distanceToNearestOrange / mySpeedBlue);
                        final Float t = distanceToNearestOrange / mySpeedBlue;

                        long minute = TimeUnit.SECONDS.toMinutes(timeToNearestOrange) - (TimeUnit.SECONDS.toHours(timeToNearestOrange)* 60);
                        long second = TimeUnit.SECONDS.toSeconds(timeToNearestOrange) - (TimeUnit.SECONDS.toMinutes(timeToNearestOrange) *60);

                        String myD = String.format("%.0f", distanceToNearestOrange);
                        String myT = String.format("%.2f", t);

                         tvDistanceToOrange.setText(myD + " m");

                        if (t.isNaN()) {

                            tvTimeToOrange.setText("0");

                        } else if (t.isInfinite()) {

                            tvTimeToOrange.setText("Too much");
                        }
                        else {
                            int ok = 5;
                            //tvTimeToOrange.setText(myT + " s");
                            tvTimeToOrange.setText(" M " + String.valueOf(minute) + " S " + String.valueOf(second));
                        }




                        //  tvDetailsTime.setText(myT + " s");
                        // tvDetailsTime.setText(String.valueOf(t) + " s");



                    } else {

                        //   tvIncrease.setText("Objekt się oddala");
                    }
                }


            }

            @Override
            public void onKeyExited(String key) {

                Marker orangeMarker = orangeMarkers.get(key);
                if (orangeMarker != null) {
                    orangeMarker.remove();
                    orangeMarkers.remove(key);
                }


                // nie dziala to wcale.. dlaczego? kiedy sa poza promieniem nie znikaja, ani nie maleje size... . co w zwiazku z tym?
                distancesToOrange.remove(key); // delete user from distances
                nearestDistances.remove(key);
                redGreenOrangeMarker.remove(key);

                if (orangeMarkers.size() > 0) {

                    orangeMarkers.remove(key);

                }

             //    nearestDistances.clear();

                int amountOrange = orangeMarkers.size(); // size of OrangeMarkers after key exited.
                String amountOrangeMarkers = String.valueOf(amountOrange);

                if (amountOrange > 0 ) {

                    tvNumberOrange.setText(amountOrangeMarkers);

                } else {

                    tvNumberOrange.setText("0");
                    //     viewDetailsDetectedPanel.setVisibility(View.INVISIBLE);
                    viewDetectedOrangePanel.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                Marker orangeMarker = orangeMarkers.get(key);
                if (orangeMarker != null) {
                    orangeMarker.remove();
                    orangeMarkers.remove(key);
                }

//                distancesToOrange.remove(key); // when red is moved, previous key is deleted.
//                nearestDistances.remove(key); // when red is moved, previous key is deleted
//                redGreenOrangeMarker.remove(key);

                // I create a new current location of a red user and put into distances.

                Location itemLocation = new Location("itemOrangeLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   after KEY MOVED   distance to ORANGE " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");
                Float distance = myBlueLocation.distanceTo(itemLocation);

                distancesToOrange.put(key, distance);



                Map.Entry<String, Float> nearestOrangeMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToOrange.entrySet()) {
                    if (nearestOrangeMarker == null || nearestOrangeMarker.getValue() > entry.getValue()) {
                        nearestOrangeMarker = entry;

                        // tvIncrease.setText("Obiekt się zbliża");
                        final String keyOfNearestOrange = nearestOrangeMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestOrange = nearestOrangeMarker.getValue(); // distance to the nearest red marker

                        nearestDistances.put(key, distanceToNearestOrange);
                        redGreenOrangeMarker.put(key, 3f);
                      //  tvDetectedOrange.setText(" the nearest: " + String.valueOf(distanceToNearestOrange));

                        orangeDist.add(distanceToNearestOrange); // add every nearest red distance and compare two last values in arraylist (redDist)


                        if (orangeDist.size() > 2) {
//
//
//                            // if result is > 0 -> red marker goes away from myBluePosition
//                            if (((orangeDist.get(orangeDist.size() - 1)) - (orangeDist.get(orangeDist.size() - 2))) > 0) {
//
//                                Log.e(" ORANGE INCREASE ", (orangeDist.get(orangeDist.size() - 1)) + " ORANGE INCREASE ");
//                                tvNumberOrange.setText(" goes away");
//
//                            } else if (((orangeDist.get(orangeDist.size() - 1)) - (orangeDist.get(orangeDist.size() - 2))) < 0) {
//                                // if result is < 0 -> red marker is closing to myBluePosition
//                                Log.e(" ORANGE DECREASE ", (orangeDist.get(orangeDist.size() - 1)) + " ORANGE DECREASE ");
//                                tvNumberOrange.setText(" is closing");
//
//                            }
                        // if result is > 0 -> red marker goes away from myBluePosition
                        if (((orangeDist.get(orangeDist.size() - 1)) - (orangeDist.get(orangeDist.size() - 2))) > 0) {

                            Log.e(" ORANGE INCREASE ", (orangeDist.get(orangeDist.size() - 1)) + " ORANGE INCREASE ");
                            distanceCloseOrGoAwayOrange = " goes away";
                            tvNumberOrange.setText(distanceCloseOrGoAwayOrange);

                        } else if (((orangeDist.get(orangeDist.size() - 1)) - (orangeDist.get(orangeDist.size() - 2))) < 0) {
//                            nearestDistances.put(key, distanceToNearestOrange);
//                            redGreenOrangeMarker.put(key, 3f);
                            // if result is < 0 -> red marker is closing to myBluePosition
                            Log.e(" ORANGE DECREASE ", (orangeDist.get(orangeDist.size() - 1)) + " ORANGE DECREASE ");
                            distanceCloseOrGoAwayOrange = " is closing";
                            tvNumberOrange.setText(distanceCloseOrGoAwayOrange);

                        }

                        }

                        if (orangeDist.size() > 20) {

                            orangeDist.clear();
                        }

                        final int timeToNearestOrange = Math.round(distanceToNearestOrange / mySpeedBlue);
                        final Float t = distanceToNearestOrange / mySpeedBlue;

                        String myD = String.format("%.0f", distanceToNearestOrange);
                        String myT = String.format("%.2f", t);

                          tvDistanceToOrange.setText(myD + " m");

                        if (t.isNaN()) {

                            tvTimeToOrange.setText("0");

                        } else if (t.isInfinite()) {

                            tvTimeToOrange.setText("Too much");
                        }
                        else {
                            tvTimeToOrange.setText(myT + " s");
                        }



                        //   tvDetailsTime.setText(myT + " s");
                        //  tvDetailsTime.setText(String.valueOf(t) + " s");

//                        tvDetailsDistance.setText(String.valueOf(distanceToNearestRed + " m"));
//                        tvDetailsTime.setText(String.valueOf(timeToNearestRed + " s"));

                    }
                    else {
                        //  tvIncrease.setText("Obiekt się oddala");
                    }
                }


            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    private void enRedGeoDetection(final Location myBlueLocation, final Float mySpeedBlue, double km) {

        myRedGeoQuery = redGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude),km );

        myRedGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                viewWelcomePanel.setVisibility(View.INVISIBLE);
                viewDetectedRedPanel.setVisibility(View.VISIBLE);


                //tvDetected.setText("Detected");
                //    detectedObject.start();

                Marker redMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                redMarkers.put(key, redMarker);
                int redMarkersSize = redMarkers.size();
                String rmSize = String.valueOf(redMarkersSize); // size in string of redMarkersSize
                tvNumberRed.setText(rmSize);

                // count distance from my current location to another in the circle

                Location itemLocation = new Location("itemLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   KEY ENTERED   distance to       " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");

                Float distanceToRed = myBlueLocation.distanceTo(itemLocation); // distance to every red

                distancesToRed.put(key, distanceToRed); // put here every distance

                Map.Entry<String, Float> nearestRedMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToRed.entrySet()) {
                    if (nearestRedMarker == null || nearestRedMarker.getValue() > entry.getValue()) {
                        nearestRedMarker = entry;

                        //tvIncrease.setText("Objekt się zbliża");
                        final String keyOfNearestRed = nearestRedMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestRed = nearestRedMarker.getValue(); // distance to the nearest red marker

                        nearestDistances.put(key, distanceToNearestRed);


                        for (Map.Entry<String, Float> entry1 : nearestDistances.entrySet()) {

//                            if ((distanceToNearestRed - entry1.getValue()) > 0 ) {
//
//                                Log.e("  DISTANCE INCREASE    ",  "+++++++++++++++++");
//                                tvIncrease.setText("Objekt się zbliża");
//
//                            } else {
//                                tvIncrease.setText("Objekt się oddala");
//                            }

                            Float e =   entry1.getValue();
                            Log.e("  nearest_ DISTANCES   ", e + "");

                        }



                        BlueLector.enSpeakDistanceToTheNearest(getApplicationContext(), distanceToNearestRed);

                        final int timeToNearestRed = Math.round(distanceToNearestRed / mySpeedBlue);
                        final Float t = distanceToNearestRed / mySpeedBlue;

                        String myD = String.format("%.0f", distanceToNearestRed);
                        String myT = String.format("%.2f", t);

                        tvDistanceToRed.setText(myD + " m");

                        if (t.isNaN()) {

                            tvTimeToRed.setText("0");

                        } else if (t.isInfinite()) {

                            tvTimeToRed.setText("Too much");
                        }
                        else {
                            tvTimeToRed.setText(myT + " s");
                        }




                        //  tvDetailsTime.setText(myT + " s");
                        // tvDetailsTime.setText(String.valueOf(t) + " s");



                    } else {

                        // tvIncrease.setText("Obiekt się oddala");
                    }
                }


            }

            @Override
            public void onKeyExited(String key) {

                Marker redMarker = redMarkers.get(key);
                if (redMarker != null) {
                    redMarker.remove();
                    redMarkers.remove(key);
                }



                distancesToRed.remove(key); // delete user from distanes
                nearestDistances.remove(key);

                int amountRed = redMarkers.size(); // size of RedMarkers after key exited.
                String amountRedMarkers = String.valueOf(amountRed);

                if (amountRed > 0 ) {

                    tvNumberRed.setText(amountRedMarkers);

                } else {

                    tvNumberRed.setText("0");
                    viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                Marker redMarker = redMarkers.get(key);
                if (redMarker != null) {
                    redMarker.remove();
                    redMarkers.remove(key);
                }

                distancesToRed.remove(key); // when red is moved, previous key is deleted.
                nearestDistances.remove(key);
                redGreenOrangeMarker.remove(key);

                // I create new current location of a red user and put into distances.

                Location itemLocation = new Location("itemLocation");
                itemLocation.setLatitude(location.latitude);
                itemLocation.setLongitude(location.longitude);
                Log.e("   after KEY MOVED   distance to       " + key + " is", myBlueLocation.distanceTo(itemLocation) + "");
                Float distance = myBlueLocation.distanceTo(itemLocation);

                distancesToRed.put(key, distance);



                Map.Entry<String, Float> nearestRedMarker = null;
                // look for the nearest red
                for (Map.Entry<String, Float> entry : distancesToRed.entrySet()) {
                    if (nearestRedMarker == null || nearestRedMarker.getValue() > entry.getValue()) {
                        nearestRedMarker = entry;

                        //  tvIncrease.setText("Obiekt się zbliża");
                        final String keyOfNearestRed = nearestRedMarker.getKey(); // key of the nearest red marker
                        final Float distanceToNearestRed = nearestRedMarker.getValue(); // distance to the nearest red marker

                        nearestDistances.put(key, nearestRedMarker.getValue());



                        final int timeToNearestRed = Math.round(distanceToNearestRed / mySpeedBlue);
                        final Float t = distanceToNearestRed / mySpeedBlue;

                        String myD = String.format("%.0f", distanceToNearestRed);
                        String myT = String.format("%.2f", t);

                        tvDistanceToRed.setText(myD + " m");
                        //  tvTimeToRed.setText(myT + " s");

                        if (t.isNaN()) {

                            tvTimeToRed.setText("0");

                        } else if (t.isInfinite()) {

                            tvTimeToRed.setText("Too much");
                        }
                        else {
                            tvTimeToRed.setText(myT + " s");
                        }
                        //  tvDetailsTime.setText(String.valueOf(t) + " s");

//                        tvDetailsDistance.setText(String.valueOf(distanceToNearestRed + " m"));
//                        tvDetailsTime.setText(String.valueOf(timeToNearestRed + " s"));



                    }
                    else {
                        //  tvIncrease.setText("Obiekt się oddala");
                    }
                }





            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }



    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap=googleMap;
        // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        //set this in another method
        // clear map after 10 s (but in onMarReady it doesn't work.) - it's needed because sometimes
        // there are markers left after some users..

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                mGoogleMap.clear();
//                Toast.makeText(getApplicationContext(), "CLEAR MAP AFTER 10 s", Toast.LENGTH_SHORT).show();
//
//            }
//        }, 10000);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(false);
            }
        }
        else
        {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(false);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {

                        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        {
                            showGPSDisabledAlertToUser();
                        }

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        ToastUtil.shortToast(getApplicationContext(), String.valueOf(brng));

        return brng;
    }

    @Override
    public void onConnectionStateSuccess() {

    }

    @Override
    public void onConnectionStateFailure() {

    }

    public void btnToMenuFromBlue(View view) {

        startActivity(new Intent(BlueActivity.this, SelectNavigationActivity.class));
        finish();

    }

    public void btnBackToBlue(View view) {

        viewBackInfoPanel.setVisibility(View.INVISIBLE);

    }

    public void btnExitApp(View view) {

        finish();

    }

    public void btnMinimalizeBlue(View view) {

        minimalizeApp();
        viewBackInfoPanel.setVisibility(View.INVISIBLE);

    }

    public void btnMuteMedia() {

        btnMuteUnmute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.btn_mute_media_blue_id) {

                    isBtnVolumeClicked = !isBtnVolumeClicked;
                    // v.setBackgroundResource(isBtnVolumeClicked ? R.drawable.rectangle_red : R.drawable.rect_green);

                    if (isBtnVolumeClicked) {

                        v.setBackgroundResource(R.drawable.ic_volume_off_white_48dp);
                        volumeMediaPlayer = 0;

                    } else {

                        v.setBackgroundResource(R.drawable.ic_volume_up_white_48dp);
                        volumeMediaPlayer = 1;

                    }

                }

            }
        });



    }


//    public void SlideToAbove() {
//        Animation slide = null;
//        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);
//
//       // slide.setDuration(400);
//        slide.setFillAfter(true);
//        slide.setFillEnabled(true);
//        viewDetectedRedPanel.setVisibility(View.VISIBLE);
//        viewDetectedRedPanel.startAnimation(slide);
//
//        slide.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                viewDetectedRedPanel.clearAnimation();
//
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        viewDetectedRedPanel.getWidth(), viewDetectedRedPanel.getHeight());
//                 lp.setMargins(0, 10, 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                viewDetectedRedPanel.setLayoutParams(lp);
//
//            }
//
//        });
//
//    }

//    public void SlideToDown() {
//        Animation slide = null;
//        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);
//
//        slide.setDuration(400);
//        slide.setFillAfter(true);
//        slide.setFillEnabled(true);
//        viewDetectedRedPanel.startAnimation(slide);
//
//        slide.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                viewDetectedRedPanel.clearAnimation();
//
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        viewDetectedRedPanel.getWidth(), viewDetectedRedPanel.getHeight());
//                lp.setMargins(0, viewDetectedRedPanel.getWidth(), 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                viewDetectedRedPanel.setLayoutParams(lp);
//
//            }
//
//        });
//
//    }

    public class MyCount extends CountDownTimer {

        int check = -3;
        int a;
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {







        }

        @Override
        public void onFinish() {



        }
    }

    private void initTvNearestColorPanelInPercent() {

        vnp0 = (TextView) findViewById(R.id.view_nearest_percent_0_id);
        vnp2 = (TextView) findViewById(R.id.view_nearest_percent_2_id);
        vnp4 = (TextView) findViewById(R.id.view_nearest_percent_4_id);
        vnp6 = (TextView) findViewById(R.id.view_nearest_percent_6_id);
        vnp8 = (TextView) findViewById(R.id.view_nearest_percent_8_id);
        vnp10 = (TextView) findViewById(R.id.view_nearest_percent_10_id);
        vnp12 = (TextView) findViewById(R.id.view_nearest_percent_12_id);
        vnp14 = (TextView) findViewById(R.id.view_nearest_percent_14_id);
        vnp16 = (TextView) findViewById(R.id.view_nearest_percent_16_id);
        vnp18 = (TextView) findViewById(R.id.view_nearest_percent_18_id);
        vnp20 = (TextView) findViewById(R.id.view_nearest_percent_20_id);
        vnp22 = (TextView) findViewById(R.id.view_nearest_percent_22_id);
        vnp24 = (TextView) findViewById(R.id.view_nearest_percent_24_id);
        vnp26 = (TextView) findViewById(R.id.view_nearest_percent_26_id);
        vnp28 = (TextView) findViewById(R.id.view_nearest_percent_28_id);
        vnp30 = (TextView) findViewById(R.id.view_nearest_percent_30_id);
        vnp32 = (TextView) findViewById(R.id.view_nearest_percent_32_id);
        vnp34 = (TextView) findViewById(R.id.view_nearest_percent_34_id);
        vnp36 = (TextView) findViewById(R.id.view_nearest_percent_36_id);
        vnp38 = (TextView) findViewById(R.id.view_nearest_percent_38_id);
        vnp40 = (TextView) findViewById(R.id.view_nearest_percent_40_id);
        vnp42 = (TextView) findViewById(R.id.view_nearest_percent_42_id);
        vnp44 = (TextView) findViewById(R.id.view_nearest_percent_44_id);
        vnp46 = (TextView) findViewById(R.id.view_nearest_percent_46_id);
        vnp48 = (TextView) findViewById(R.id.view_nearest_percent_48_id);
        vnp50 = (TextView) findViewById(R.id.view_nearest_percent_50_id);
        vnp52 = (TextView) findViewById(R.id.view_nearest_percent_52_id);
        vnp54 = (TextView) findViewById(R.id.view_nearest_percent_54_id);
        vnp56 = (TextView) findViewById(R.id.view_nearest_percent_56_id);
        vnp58 = (TextView) findViewById(R.id.view_nearest_percent_58_id);
        vnp60 = (TextView) findViewById(R.id.view_nearest_percent_60_id);
        vnp62 = (TextView) findViewById(R.id.view_nearest_percent_62_id);
        vnp64 = (TextView) findViewById(R.id.view_nearest_percent_64_id);
        vnp66 = (TextView) findViewById(R.id.view_nearest_percent_66_id);
        vnp68 = (TextView) findViewById(R.id.view_nearest_percent_68_id);
        vnp70 = (TextView) findViewById(R.id.view_nearest_percent_70_id);
        vnp72 = (TextView) findViewById(R.id.view_nearest_percent_72_id);
        vnp74 = (TextView) findViewById(R.id.view_nearest_percent_74_id);
        vnp76 = (TextView) findViewById(R.id.view_nearest_percent_76_id);
        vnp78 = (TextView) findViewById(R.id.view_nearest_percent_78_id);
        vnp80 = (TextView) findViewById(R.id.view_nearest_percent_80_id);
        vnp82 = (TextView) findViewById(R.id.view_nearest_percent_82_id);
        vnp84 = (TextView) findViewById(R.id.view_nearest_percent_84_id);
        vnp86 = (TextView) findViewById(R.id.view_nearest_percent_86_id);
        vnp88 = (TextView) findViewById(R.id.view_nearest_percent_88_id);
        vnp90 = (TextView) findViewById(R.id.view_nearest_percent_90_id);
        vnp92 = (TextView) findViewById(R.id.view_nearest_percent_92_id);
        vnp94 = (TextView) findViewById(R.id.view_nearest_percent_94_id);
        vnp96 = (TextView) findViewById(R.id.view_nearest_percent_96_id);
        vnp98 = (TextView) findViewById(R.id.view_nearest_percent_98_id);


    }

    private void setRadarScanView(RadarScanView radarScanView) {

        int greenColor = 0xff669966;
        int whiteColor = 0xffffffff;

//        radarScanView
//                // 设置雷达扫描一圈时间
//                .setRadarScanTime(2000)
//                // 设置雷达背景颜色
//                .setRadarBackgroundColor(Color.WHITE)
//                // 设置雷达背景圆圈数量
//                .setRadarBackgroundLinesNumber(4)
//                // 设置雷达背景圆圈宽度
//                .setRadarBackgroundLinesWidth(0.01f)
//                // 设置雷达背景圆圈颜色
//                .setRadarBackgroundLinesColor(Color.GRAY)
//                // 设置雷达扫描颜色
//                .setRadarScanColor(0xFFAAAAAA)
//                // 设置雷达扫描透明度
//                .setRadarScanAlpha(0xAA);
        radarScanView
                // 设置雷达扫描一圈时间
                .setRadarScanTime(2000)
                // 设置雷达背景颜色
                .setRadarBackgroundColor(R.color.blue_400)
                // 设置雷达背景圆圈数量
                .setRadarBackgroundLinesNumber(4)
                // 设置雷达背景圆圈宽度
                .setRadarBackgroundLinesWidth(0.01f)
                // 设置雷达背景圆圈颜色
                .setRadarBackgroundLinesColor(Color.GRAY)
                // 设置雷达扫描颜色
                .setRadarScanColor(greenColor)
               // .setRadarScanColor(0xffffffff)
               // .setRadarScanColors(whiteColor, greenColor)
                // 设置雷达扫描透明度
                .setRadarScanAlpha(0xAA);
    }

    private void highLocationRequest(LocationRequest mLocationRequest) {
        //LocationRequest mLocationRequest = new LocationRequest();
        //mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void balancedLocationRequest(LocationRequest mLocationRequest) {
       // LocationRequest mLocationRequest = new LocationRequest();
       // mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void setNearestPanelColorInPercent(int color) {

        ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, color,
                vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
                vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
                vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
                vnp92, vnp94, vnp96, vnp98);


    }

    private void setNearestPanelDrawableInPercent(Drawable color) {

        ViewNearestPanelColor.SetDrawablePercentPanel(getApplicationContext(), distanceInPercentToNearest, color,
                vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
                vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
                vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
                vnp92, vnp94, vnp96, vnp98);


    }

    private void setNearestPanelColorForGreen() {

        setNearestPanelColorInPercent(R.color.green_400);

    }

    private void setNearestPanelColorForRed() {

        setNearestPanelColorInPercent(R.color.red_400);

    }

    private void setPLBlueLector(int round, int myIntegerSpeed, Integer volumeMediaPlayer) {

        if (volumeMediaPlayer != null) {
            BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
        }

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

                        mSupportMapFragment.getMapAsync(BlueActivity.this);
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

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }

            return true;
        }
    }

    private void showBackButtonOptionAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you wanna stop detection of obiects?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //startActivity(callGPSSettingIntent);
                        ref_online.onDisconnect().setValue("0"); // it doesnt work
                        startActivity(new Intent(BlueActivity.this, SelectNavigationActivity.class));

                        mSupportMapFragment.getMapAsync(BlueActivity.this);
                        finish();
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

    private void showBackLayoutOptionToUser() {

        viewBackInfoPanel.setVisibility(View.VISIBLE);

    }

    private void minimalizeApp() {

        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_HOME);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
        moveTaskToBack(true);

    }

    private void showGreyPanel() {

        viewGreyPanel.setVisibility(View.VISIBLE);

    }

    private void animateGreyPanel() {

        viewGreyPanel.setAlpha(1f);
        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(1000);
        viewGreyPanel.setAlpha(1f);
        viewGreyPanel.startAnimation(animation1);
    }

    private void stopAnimateGreyPanel() {

        viewGreyPanel.setAlpha(1.0f);
        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 1.0f);
        animation1.setDuration(0);
        viewGreyPanel.setAlpha(1f);
        viewGreyPanel.startAnimation(animation1);

    }

    private void hideGreyPanel() {

        viewGreyPanel.setVisibility(View.INVISIBLE);

    }

    public void isInternetAvailable() {

        if (!NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {


           // ToastUtil.shortToast(getApplicationContext(), "Brak transferu danych. Włącz internet.");
            viewWelcomePanel.setVisibility(View.INVISIBLE);
            viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
            viewInternetInfo.setVisibility(View.VISIBLE);
            tvInternetInfo.setText(strInternetInfo);

        } else {
            viewInternetInfo.setVisibility(View.INVISIBLE);

        }

    }

    private void initViews() {

        btnMuteUnmute = (Button) findViewById(R.id.btn_mute_media_blue_id);

        tvInternetInfo = (TextView) findViewById(R.id.tv_internet_info_blue_id);
        tvRadiusInfo = (TextView) findViewById(R.id.tv_radius_info_id);
        tvOjectsDetection = (TextView) findViewById(R.id.tv_objects_detection_blue_id);
        tvRadiusRange = (TextView) findViewById(R.id.tv_radius_range_blue_id);
        tvTitleNearest = (TextView) findViewById(R.id.tv_details_title_nearest_id);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome_blue_id);
        tvNumberRed = (TextView) findViewById(R.id.tv_details_red_number_id);
        tvNumberGreen = (TextView) findViewById(R.id.tv_details_green_number_id);
        tvNumberOrange = (TextView) findViewById(R.id.tv_details_orange_number_id);
        tvDistanceToRed = (TextView) findViewById(R.id.tv_details_red_distance_id);
        tvDistanceToGreen = (TextView) findViewById(R.id.tv_details_green_distance_id);
        tvDistanceToOrange = (TextView) findViewById(R.id.tv_details_orange_distance_id);
        tvDistanceToNearest = (TextView) findViewById(R.id.tv_details_distance_nearest_id);
        tvTimeToRed = (TextView) findViewById(R.id.tv_details_red_time_id);
        tvTimeToGreen = (TextView) findViewById(R.id.tv_details_green_time_id);
        tvTimeToOrange = (TextView) findViewById(R.id.tv_details_orange_time_id);
        tvTimeToNearest = (TextView) findViewById(R.id.tv_details_time_nearest_id);
        tvMyLocation = (TextView) findViewById(R.id.tv_my_blue_location_id);
        tvMySpeed = (TextView) findViewById(R.id.tv_blue_speed_id);
        tvMySpeedPlus = (TextView) findViewById(R.id.tv_blue_speed_plus_id);
        tvMyRadius = (TextView) findViewById(R.id.tv_my_radius_id);
        tvIncrease = (TextView) findViewById(R.id.tv_nearest_distance_increase_id);

        viewWelcomePanel = findViewById(R.id.rel_layout_welcome_panel_blue_maps_id);
        viewDetectedRedPanel = findViewById(R.id.rel_layout_detection_red_id);
        viewDetectedGreenPanel = findViewById(R.id.rel_layout_detection_green_id);
        viewDetectedOrangePanel = findViewById(R.id.rel_layout_detection_orange_id);
        viewDetectedNearestPanel = findViewById(R.id.rel_layout_detection_nearest_id);
        viewGreyPanel = findViewById(R.id.rel_layout_grey_id);
        viewInternetInfo = findViewById(R.id.rel_layout_internet_info_blue_id);
        viewBackInfoPanel = findViewById(R.id.rel_layout_exit_blue_id);


        ivWelcomeGreen = (ImageView) findViewById(R.id.iv_welcome_green);
        ivWelcomeOrange = (ImageView) findViewById(R.id.iv_welcome_orange);
        ivWelcomeRed = (ImageView) findViewById(R.id.iv_welcome_red);
        ivNearestGreen = (ImageView) findViewById(R.id.iv_nearest_green_id);
        ivNearestOrange = (ImageView) findViewById(R.id.iv_nearest_orange_id);
        ivNearestRed = (ImageView) findViewById(R.id.iv_nearest_red_id);
        ivRedPanel = (ImageView) findViewById(R.id.iv_red_panel_id);

        plRadiusObjects = MediaPlayer.create(getApplicationContext(), R.raw.pl_radius_objects);
        plVoiceActivated = MediaPlayer.create(getApplicationContext(), R.raw.pl_voice_activated);

    }

    private void getStringId() {


        strRed = getString(R.string.red_string);
        strGreen = getString(R.string.green_string);
        strOrange = getString(R.string.orange_string);
        strObjects = getString(R.string.tv_objects);
        strRadarInfo = getString(R.string.tv_radar_info);
        strInternetInfo = getString(R.string.internet_info);
        strTrainGoesAway = getString(R.string.tv_train_goes_away);
        strTrainIsClosing = getString(R.string.tv_train_is_closing);
        strGreenGoesAway = getString(R.string.tv_person_goes_away);
        strGreenIsClosing = getString(R.string.tv_person_is_closing);
        strIsDetected = getString(R.string.tv_isDetected);
        strAreDetected = getString(R.string.tv_areDetected);
        strPerson = getString(R.string.tv_person);
        strPeople = getString(R.string.tv_people);

    }

    private void setCameraProperties(GoogleMap mGoogleMap, LatLng latLng, float fTilt, float fZoom, float fBearing) {

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(latLng).
                tilt(fTilt).
                zoom(fZoom).
                bearing(fBearing).
                build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void setGreenLector() {

        if (roundsFiftyList.size() > 0) {
            if (!roundEvery50Meters.equals(roundsFiftyList.get(roundsFiftyList.size() - 1))) {
                roundsFiftyList.add(roundEvery50Meters);
                setPLBlueLector(roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
            }
        } else if (roundsFiftyList.size() == 0) {
            roundsFiftyList.add(0);
            roundsFiftyList.add(roundEvery50Meters);
           // setPLBlueLector(roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
        }

    }

    private void setRedLector() {

        if (!roundEvery50Meters.equals(roundsFiftyList.get(roundsFiftyList.size()-1))) {
            roundsFiftyList.add(roundEvery50Meters);
            BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
        }
    }

    private void setRedDetected(LatLng latLng){


        whoIsIt = "RED";
        // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
        ivNearestRed.setVisibility(View.VISIBLE);
        ivNearestGreen.setVisibility(View.INVISIBLE);

        if (twoLastNearestDistancesCloseOrAway != null) {

            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                //hideGreyPanel();
                tvTimeToNearest.setText(strTrainIsClosing);


                if (round >= 0) {

                    int di = round;
                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea)); // percent to nearest. 90% means that it's very close to you

                    if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {
                        setCameraProperties(mGoogleMap, latLng, 60, 15, 0);

                    }


                    if (distanceInPercentToNearest > 70) {

                        setCameraProperties(mGoogleMap, latLng, 60, 17, 0);
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    tilt(60).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }

                } else {
                    distanceInPercentToNearest = round;
                }

                tvTitleNearest.setText(distanceCloseOrGoAwayRed);

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                //   setNearestPanelColorInPercent(R.color.red_400);
                setNearestPanelColorForRed();
                Log.i(TAG_THE_NEAREST, " o is_closing. round: " + round +
                        "roundEvery50Meters: " + roundEvery50Meters + " " + " is: " + whoIsIt + " v: " + myIntegerSpeed + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                //BlueLector.plSpeakDistanceToTheNearestRed(getApplicationContext(), roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
                setRedLector();

                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                Log.i(TAG_ROUND, ("round_orange_isClosing " + round));


            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                tvTimeToNearest.setText(strTrainGoesAway);
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

//                                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
//                                    animation1.setDuration(500);
//                                    viewDetectedNearestPanel.setAlpha(1f);
//                                    viewDetectedNearestPanel.startAnimation(animation1);

                //animateGreyPanel();

                if (distanceInPercentToRed > 95) {

                    viewDetectedRedPanel.setVisibility(View.INVISIBLE);
                }


                if (round >= 0) {

                    int di = round;
                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                } else {

                    distanceInPercentToNearest = round;

                }

                //  tvTitleNearest.setText(distanceCloseOrGoAwayRed);

                Log.i(TAG_THE_NEAREST, " o goes_away. round: " + round +
                        "roundEvery50Meters: " + roundEvery50Meters + " " + " is: " + whoIsIt + " v: " + myIntegerSpeed + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.red_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                setNearestPanelColorForRed();

                Log.i(TAG_ROUND, ("round_orange_goesAway " + round));
                nearestDistances.clear();


            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                round = -2;
                // tvTitleNearest.setText(distanceCloseOrGoAwayRed);
                Log.i(TAG_THE_NEAREST, " o zero. round: " + round +
                        "roundEvery50Meters: " + roundEvery50Meters + " " + " is: " + whoIsIt + " v: " + myIntegerSpeed + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

            }
        }

    }

    private void setGreenDetected() {

        Integer rnd = round;
        Integer rnd2 = round;

        whoIsIt = "GREEN";
        ivNearestRed.setVisibility(View.INVISIBLE);
        ivNearestGreen.setVisibility(View.VISIBLE);
        ivNearestOrange.setVisibility(View.INVISIBLE);
        // tvTitleNearest.setText(twoLastNearestDistancesCloseOrAway);
        if (twoLastNearestDistancesCloseOrAway != null) {

            if (twoLastNearestDistancesCloseOrAway.equals(is_closing)) {

                viewDetectedGreenPanel.setVisibility(View.VISIBLE);
                //hideGreyPanel();

                tvTimeToNearest.setText(strGreenIsClosing);

                if (round >= 0) {

                    int di = round;

                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                    if (distanceInPercentToNearest > 40 && distanceInPercentToNearest < 60) {

                        setCameraProperties(mGoogleMap, myLatLng, 60, 14, 0);
                        // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(14).
//                                                    bearing(0).
//                                                    build();

                        //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }


                    if (distanceInPercentToNearest > 70) {

                        setCameraProperties(mGoogleMap, myLatLng, 60, 17, 0);
                        // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//                                            CameraPosition cameraPosition = new CameraPosition.Builder().
//                                                    target(latLng).
//                                                    tilt(60).
//                                                    zoom(17).
//                                                    bearing(0).
//                                                    build();
//
//                                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }



                } else {
                    distanceInPercentToNearest = round;
                }

//                                    ViewNearestPanelColor.SetColorPercentPanel(getApplicationContext(), distanceInPercentToNearest, R.color.green_400,
//                                            vnp0, vnp2, vnp4, vnp6, vnp8, vnp10, vnp12, vnp14, vnp16, vnp18, vnp20, vnp22, vnp24, vnp26, vnp28, vnp30,
//                                            vnp32, vnp34, vnp36, vnp38, vnp40, vnp42, vnp44, vnp46, vnp48, vnp50, vnp52, vnp54, vnp56, vnp58, vnp60,
//                                            vnp62, vnp64, vnp66, vnp68, vnp70, vnp72, vnp74, vnp76, vnp78, vnp80, vnp82, vnp84, vnp86, vnp88, vnp90,
//                                            vnp92, vnp94, vnp96, vnp98);
                // setNearestPanelColorInPercent(R.color.green_400);
                setNearestPanelColorForGreen();
                //setNearestPanelDrawableInPercent(R.drawable.green_color);
                // volumeMediaPlayer = 0;

                // in Galaxy S7 (android 7.0) sometimes [volume,volume] value was null and app crashed


//                                    if (round > 80 && round < 120) {
//
//                                        mRound = 100;
//
//                                    }



                if (round > 100) {
                    // round every 50 meterss

                    roundEvery50Meters = (rnd / 50) * 50; // display 50m, 100m, 150m ...
                } else if (round > 0 && round < 100) {

                    roundEvery50Meters = (rnd2 / 20) * 20; // display 20m, 40m, 60m, 80m
                }


//                                    if (!roundEvery50Meters.equals(roundsFiftyList.get(roundsFiftyList.size()-1))) {
//                                        roundsFiftyList.add(roundEvery50Meters);
//                                        setPLBlueLector(roundEvery50Meters, myIntegerSpeed, volumeMediaPlayer);
//
                setGreenLector();


//                                    if (volumeMediaPlayer != null) {
//                                        BlueLector.plSpeakDistanceToTheNearest(getApplicationContext(), round, myIntegerSpeed, volumeMediaPlayer);
//                                    }
                Log.i(TAG_GREEN, (TAG_ROUND_MIN + round));
                Log.i(TAG_ROUND, ("round_green_isClosing " + round));

                if (twoNearestDistances.size() > 1) {
                    diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                }
                Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                        " roundEvery50Meters: " + roundEvery50Meters + " "
                        + "roundsList.(-1): " + roundsFiftyList.get(roundsFiftyList.size()-1)
                        + " roundsList.(-2): " + roundsFiftyList.get(roundsFiftyList.size()-2)
                        + " is: " + whoIsIt + " v: " + myIntegerSpeed + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


            } else if (twoLastNearestDistancesCloseOrAway.equals(goes_away)) {

                tvTimeToNearest.setText(strGreenGoesAway);
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                if (round >= 0) {

                    int di = round;
                    distanceInPercentToNearest = (100 - ((di * 100) / myRadiusArea));

                } else {
                    distanceInPercentToNearest = round;
                }

                if (distanceInPercentToGreen > 95) {

                    viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
                }

                setNearestPanelColorForGreen();

                // nearest market get value causes npe i daltego po green kiedy byl po za radius wylaczylo sie do main ativity
                if (twoNearestDistances.size() > 1) {
                    diff = ((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 2)));
                }
                Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                        " roundEvery50Meters: " + roundEvery50Meters + " "
                       // + "roundsList.(-1): " + roundsFiftyList.get(roundsFiftyList.size()-1)
                        //+ " roundsList.(-2): " + roundsFiftyList.get(roundsFiftyList.size()-2)
                        + " is: " + whoIsIt + " v: " + myIntegerSpeed + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);


                nearestDistances.clear();


            } else if (twoLastNearestDistancesCloseOrAway.equals("zero")) {

                round = -2;
                Log.i(TAG_THE_NEAREST, " g is_closing. round: " + round + " diff: " + diff +
                        " roundEvery50Meters: " + roundEvery50Meters + " "
                        + "roundsList.(-1): " + roundsFiftyList.get(roundsFiftyList.size()-1)
                        + " roundsList.(-2): " + roundsFiftyList.get(roundsFiftyList.size()-2)
                        + " is: " + whoIsIt + " v: " + myIntegerSpeed + " p: " + distanceInPercentToNearest + "%" + " r: " + myRadiusArea);

            }

        }

    }

    private void setWhenObjectIsDetected() {

        isDetected = true;
        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
        radarScanView.stopScan();
        radarScanView.setVisibility(View.INVISIBLE);
        hideGreyPanel();
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

    }

    private void setWhenObjectIsNotDetected() {

        isDetected = false;
        round = -1;
        String m = "NOBODY";
        nearestDistances.clear();
        tvDistanceToNearest.setText(m);
        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
        viewWelcomePanel.setVisibility(View.VISIBLE);
        radarScanView.startScan();
        radarScanView.setVisibility(View.VISIBLE);
        // stopAnimateGreyPanel();
        showGreyPanel();
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        // check if it will not make exception as list is empty when will detect object try to check two last positions
        roundsFiftyList.clear();
    }

    private void findTheNearestObjectWithDistanceAndKey() {

        Map.Entry<String, Float> nearestMarker = null;
        Map.Entry<String, Float> redGreenOrangeString = null;

        for (Map.Entry<String, Float> entry : nearestDistances.entrySet()) {

            if (nearestMarker == null || nearestMarker.getValue() > entry.getValue()) {
                nearestMarker = entry;

                Float nearestDist = nearestMarker.getValue();

                round = Math.round(nearestMarker.getValue());
          //      roundEvery50Meters = (round / 50) * 50;
                twoNearestDistances.add(round);
                Log.e("   MIN   ", round + "");
                Log.e("    MIN_KEY   ", nearestMarker.getKey() + "");

                //  Log.i(TAG_NEAREST, ("narestMarker.getValue " + nearestMarker.getValue()) + " .getKey " + nearestMarker.getKey());


                k = nearestMarker.getKey();

            }

        }

    }

    private void checkObjectState() {

        if (twoNearestDistances.size() > 2) {
            // if result is > 0 -> green marker goes away from myBluePosition
            if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) > 0) {
                twoLastNearestDistancesCloseOrAway = goes_away;
                Log.i(TAG_ROUND, ("round_goesAway " + round));
                //    round = -2;

//                            Log.i(TAG_NEAREST, ("GOES AWAY -1:  " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
//                            Log.i(TAG_NEAREST, ("GOES AWAY -2:  " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
//                            Log.i(TAG_NEAREST, ("GOES AWAY -3:  " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
                //   ToastUtil.shortToast(getApplicationContext(), String.valueOf(twoNearestDistances.get(twoNearestDistances.size() - 1)));

            } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) < 0) {
                // if result is < 0 -> red marker is closing to myBluePosition
                twoLastNearestDistancesCloseOrAway = is_closing;
//                            Log.i(TAG_ROUND, ("round_isClosing " + round));
//                            Log.i(TAG_NEAREST, ("is closing -1: " + twoNearestDistances.get(twoNearestDistances.size() - 1)) + "  min: " + round + "  " + k);
//                            Log.i(TAG_NEAREST, ("is closing -2: " + twoNearestDistances.get(twoNearestDistances.size() - 2)) + "  min: " + round + "  " + k);
//                            Log.i(TAG_NEAREST, ("is closing -3: " + twoNearestDistances.get(twoNearestDistances.size() - 3)) + "  min: " + round + "  " + k);
            } else if (((twoNearestDistances.get(twoNearestDistances.size() - 1)) - (twoNearestDistances.get(twoNearestDistances.size() - 3))) == 0) {
                // np kiedy stoje, to jest rowne zero... to moze oznaczyc to jako postoj w sensie np... round == -2?
                twoLastNearestDistancesCloseOrAway = "zero";
            }

        }

    }

    private void checkIfObjectIsDetected() {

        if (nearestDistances.size() == 0) { // if there is nobody in radius (so nobody in nearestDistances)

            setWhenObjectIsNotDetected();
//                        round = -1;
//                        String m = "NOBODY";
//                        nearestDistances.clear();
//                        tvDistanceToNearest.setText(m);
//                        viewDetectedNearestPanel.setVisibility(View.INVISIBLE);
//                        viewDetectedGreenPanel.setVisibility(View.INVISIBLE);
//                        viewDetectedRedPanel.setVisibility(View.INVISIBLE);
//                        viewWelcomePanel.setVisibility(View.VISIBLE);
//                        radarScanView.startScan();
//                        radarScanView.setVisibility(View.VISIBLE);
//                       // stopAnimateGreyPanel();
//                        showGreyPanel();
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//
//                        isDetected = false;
//
//                        // check if it will not make exception as list is empty when will detect object try to check two last positions
//                        roundsFiftyList.clear();

        } else {

            setWhenObjectIsDetected();
//                        isDetected = true;
//                        viewDetectedNearestPanel.setVisibility(View.VISIBLE);
//                        radarScanView.stopScan();
//                        radarScanView.setVisibility(View.INVISIBLE);
//                        hideGreyPanel();
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }


    }

    private void clear_twoNearestDistances() {

        if (twoNearestDistances.size() > 20) {

            twoNearestDistances.clear();
        }

        Log.i(TAG_NEAREST, ("nearestDistances.size: " + nearestDistances.size()));
    }


}
