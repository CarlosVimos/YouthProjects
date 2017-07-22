package com.karol.vimos.vimosapp.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.connection.ConnectionContract;
import com.karol.vimos.vimosapp.core.connection.ConnectionPresenter;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.ui.activities.SelectNavigationActivity;
import com.karol.vimos.vimosapp.ui.activities.UserListingActivity;
import com.karol.vimos.vimosapp.ui.activities.blue.activity.BlueActivity;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.ToastUtil;
import com.skyfishjy.library.RippleBackground;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static com.karol.vimos.vimosapp.ui.activities.blue.activity.BlueActivity.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by Vimos on 20/02/17.
 */

public class RedFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ConnectionContract.View {


    GoogleMap mGoogleMap;
    private MapView mapView;
    SupportMapFragment mSupportMapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location myRedLocation;
    Marker mCurrentLocationMarker;
    LocationManager mLocationManager;
    DatabaseReference ref_red, ref_myName, ref_users_red, ref_online, ref_connection;
    double myLatitude, myLongitude;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    GeoFire redGeoFire;
    GeoQuery myRedGeoQuery;
    int myRedRadiusArea; // my area of red markers
    private Button btnStopDetection;
    private View
            viewWelcomePanel, // welcome panel
            viewRedMarkersPanel, // how many red markers is in my area;
            viewRedDetailsPanel; // my speed, etc. ...

    private TextView tvWelcome, tvRedInYouArea, tvToChatRoom,tvTextSpeed, tvRedSpeed, tvTextInfo;

    private Map<String, String> redMarkers;

    private ConnectionPresenter mConnectionPresenter;

    RippleBackground rippleBackground;
    double redSpeedPlus; // after adding 6% to the getSpeed.
    String strRedSpeed, strInfo1, strInfo2, strInfo3, strInfo4, strInfo5;

    public static RedFragment newInstance() {
        Bundle args = new Bundle();
        RedFragment fragment = new RedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_red, container, false);

        rippleBackground = (RippleBackground) fragmentView.findViewById(R.id.red_ripple_background_id);

        initViews(fragmentView);

        getStringId();

        tvRedInYouArea.setOnClickListener(this);
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRedId);
        mSupportMapFragment.getMapAsync(this);


        return fragmentView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvTextSpeed.setText(getString(R.string.tv_text_speed));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ref_red = FirebaseDatabase.getInstance().getReference("geofire/red");
        redGeoFire = new GeoFire(ref_red);

        mConnectionPresenter = new ConnectionPresenter(this);

        final String[] strInfoArray = {strInfo1, strInfo2, strInfo3, strInfo4};

        // it shows particular string in strInfoArray in every 5 seconds
        tvTextInfo.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                tvTextInfo.setText(strInfoArray[i]);
                i++;
                if (i == strInfoArray.length) {
                    i = 0;
                 //   tvTextInfo.setText(strInfo6 + " " + strRedSpeed + " km/h");
                }
//                for (int i = 0; i <= strInfoArray.length - 1; i++) {
//
//                    tvTextInfo.setText(strInfoArray[i]);
//
//                }
                tvTextInfo.postDelayed(this, 8000);

            }
        });

        //ref_online = FirebaseDatabase.getInstance().getReference("user_connection_state").child("red").child(firebaseUser.getUid());
        ref_online = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS_CONNECTION_STATE).child(Constants.ARG_RED).child(firebaseUser.getUid());
        //ref_connection = FirebaseDatabase.getInstance().getReference(".info/connected");


        ref_online.onDisconnect().setValue("0"); // when user open Red, and immediately exit the app, it will work with this line of code.
        mConnectionPresenter.ConnectionState(getContext(), ref_connection, ref_online); // when user opren Red and exit the app immediately it won't change to "0".


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


        tvToChatRoom.setOnClickListener(this);
        btnStopDetection.setOnClickListener(this);

        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        redMarkers = new HashMap<String, String>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_myName = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid());

        ref_users_red = FirebaseDatabase.getInstance().getReference("users_in_red_radius");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ref_online.onDisconnect().setValue("0");

        ToastUtil.shortToast(getContext(), "onDestroy");
        firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        redGeoFire.removeLocation(userId);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLocationManager.removeUpdates(this);

        mGoogleApiClient.disconnect();
       // Log.e("   RED_FRAGMENT   ", "on    Destroy");  it displays
    }

    @Override
    public void onStop() {
        super.onStop();

        ToastUtil.shortToast(getContext(), "onStop");
        Log.e("RED_FRAGMENT: ", "ON_STOP");

        firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

//        if (mGoogleApiClient != null) {
//
//            mGoogleApiClient.connect();
//        }
        buildGoogleApiClient();
        redGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));

    }

    @Override
    public void onPause() {
        super.onPause();


        buildGoogleApiClient();

        ToastUtil.shortToast(getContext(), "onPause");
        Log.e("RED_FRAGMENT: ", "ON_PAUSE");
    }


    @Override
    public void onResume() {
        super.onResume();

        ToastUtil.shortToast(getContext(), "onResume");
        Log.e("RED_FRAGMENT: ", "ON_RESUME");
        String userId = firebaseUser.getUid();
//        mGoogleApiClient.connect();
        redGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        rippleBackground.startRippleAnimation();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        if (mCurrentLocationMarker != null) {

            mCurrentLocationMarker.remove();

        }

        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();


        final Location myRedLocation = new Location("currRedLocation");
        myRedLocation.setLatitude(myLatitude);
        myRedLocation.setLongitude(myLongitude);

        final Float mySpeedRed = myRedLocation.getSpeed();
        redSpeedPlus = location.getSpeed() * 3.6 * 1.06;
        strRedSpeed = String.format("%.0f", redSpeedPlus);

        tvRedSpeed.setText(strRedSpeed);

        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.flat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.rotation(90.0f);
        //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrentLocationMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        double kmRedQuery = 0.6; // 0.6 km

        firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null; // not sure if assert is good function for release ver. of app
        final String userId = firebaseUser.getUid(); // sometimes it causes NPE

        redGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));




        myRedGeoQuery = redGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude), kmRedQuery);

        myRedGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!key.equals(userId)) {


                    int redMarkersSize = redMarkers.size();
                    String rSize = String.valueOf(redMarkersSize);
                    tvRedInYouArea.setText(rSize);
                    redMarkers.put(key, userId);
                }

                for (Map.Entry<String, String> entry : redMarkers.entrySet()) {

                    final String markersKey = entry.getKey();



                      //  ref_users_red.child(userId).child(markersKey).child("email").setValue(firebaseUser.getEmail());
                       // ref_users_red.child(userId).child(markersKey).child("uid").setValue(userId);

                        DatabaseReference ref_red_email = FirebaseDatabase.getInstance().getReference().child("users").child(markersKey);
                        ref_red_email.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User userDetails = dataSnapshot.getValue(User.class);
                                String redEmail = userDetails.getEmail();
                                String redUid = userDetails.getUid();
                                String redName = userDetails.getName();
                                String redNick = userDetails.getNick();
                                String redProfession = userDetails.getProfession();

                                ref_users_red.child(userId).child(markersKey).child("email").setValue(redEmail);
                                ref_users_red.child(userId).child(markersKey).child("name").setValue(redName);
                                ref_users_red.child(userId).child(markersKey).child("uid").setValue(redUid);
                                ref_users_red.child(userId).child(markersKey).child("profession").setValue(redProfession);
                                ref_users_red.child(userId).child(markersKey).child("nick").setValue(redNick);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }




            @Override
            public void onKeyExited(String key) {

                if (!key.equals(userId)) {

                    redMarkers.remove(key);
                    int redMarkersSize = redMarkers.size();
                    String rSize = String.valueOf(redMarkersSize);
                    tvRedInYouArea.setText(rSize);

                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(false);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(false);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            showGPSDisabledAlertToUser();
                        }

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

                        mSupportMapFragment.getMapAsync(RedFragment.this);
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
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.tv_user_red_chatroom_id:
                toChatUser();
                break;

            case R.id.btn_red_finish_app_id:
                ref_online.onDisconnect().setValue("0");
               // setDisconnect();
                toMenu();
                break;


        }
    }

    private void toChatUser() {
        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void setDisconnect() {
        ref_online.onDisconnect().setValue("0");
    }

    private void toMenu() {
        SelectNavigationActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void initViews(View fragmentView) {

        btnStopDetection = (Button) fragmentView.findViewById(R.id.btn_red_finish_app_id);
        tvRedInYouArea = (TextView) fragmentView.findViewById(R.id.tv_user_red_detected_id);
        tvTextInfo = (TextView) fragmentView.findViewById(R.id.tv_red_text_info_id);
        tvToChatRoom = (TextView) fragmentView.findViewById(R.id.tv_user_red_chatroom_id);
        tvTextSpeed = (TextView) fragmentView.findViewById(R.id.tv_text_speed_id);
        tvRedSpeed = (TextView) fragmentView.findViewById(R.id.tv_red_speed_id);


    }

    private void getStringId() {

        strInfo1 = getString(R.string.tv_text_r_info1);
        strInfo2 = getString(R.string.tv_text_r_info2);
        strInfo3 = getString(R.string.tv_text_r_info3);
        strInfo4 = getString(R.string.tv_text_r_info4);

    }


    @Override
    public void onConnectionStateSuccess() {

    }

    @Override
    public void onConnectionStateFailure() {

    }
}


