package com.karol.vimos.vimosapp.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.karol.vimos.vimosapp.utils.Constants;
import com.skyfishjy.library.RippleBackground;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static com.karol.vimos.vimosapp.ui.activities.blue.activity.BlueActivity.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by Vimos on 26/02/17.
 */

public class GreenFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ConnectionContract.View {

    private GoogleMap mGoogleMap;
    private MapView mapView;
    private SupportMapFragment mSupportMapFragment;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location myGreenLocation;
    private Marker mCurrentLocationMarker;
    private LocationManager mLocationManager;
    private DatabaseReference ref_green, ref_myName, ref_users_green, ref_online, ref_connection;
    private double myLatitude, myLongitude;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GeoFire greenGeoFire;
    private GeoQuery myGreenGeoQuery;
    private int myGreenRadiusArea; // my area of red markers
    private Button btnStopDetection;
    private View
            viewWelcomePanel, // welcome panel
            viewMarkersPanel, // how many green markers is in my area;
            viewDetailsPanel; // my speed, etc. ...

    private TextView tvWelcome, tvGreenInYouArea, tvToChatRoom, tvTextInfo;
    String strInfo1, strInfo2, strInfo3, strInfo4;

    RippleBackground rippleBackground;

    private ConnectionPresenter mConnectionPresenter;

    private Map<String, String> greenMarkers;

    public static GreenFragment newInstance() {
        Bundle args = new Bundle();
        GreenFragment fragment = new GreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_green, container, false);

        tvGreenInYouArea = (TextView) fragmentView.findViewById(R.id.tv_user_green_detected_id);
        tvToChatRoom = (TextView) fragmentView.findViewById(R.id.tv_user_green_chatroom_id);
        tvTextInfo = (TextView) fragmentView.findViewById(R.id.tv_green_text_info_id);

        rippleBackground = (RippleBackground) fragmentView.findViewById(R.id.content);
//        tvGreenInYouArea.setOnClickListener(this);
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapGreenId);
        mSupportMapFragment.getMapAsync(this);


        strInfo1 = getString(R.string.tv_text_g_info1);
        strInfo2 = getString(R.string.tv_text_g_info2);
        strInfo3 = getString(R.string.tv_text_g_info3);
        strInfo4 = getString(R.string.tv_text_g_info4);


        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ref_green = FirebaseDatabase.getInstance().getReference("geofire/green");
        greenGeoFire = new GeoFire(ref_green);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        tvToChatRoom.setOnClickListener(this);
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        greenMarkers = new HashMap<String, String>();

        mConnectionPresenter = new ConnectionPresenter(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_myName = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid());

        final String[] strInfoArray = {strInfo1, strInfo2, strInfo3, strInfo4};

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

        ref_users_green = FirebaseDatabase.getInstance().getReference("users_in_green_radius");

        ref_online = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS_CONNECTION_STATE).child(Constants.ARG_GREEN).child(firebaseUser.getUid());


        ref_online.onDisconnect().setValue("0");
        mConnectionPresenter.ConnectionState(getContext(), ref_connection, ref_online);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }
        }



    }

    @Override
    public void onStop() {
        super.onStop();

        firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        ref_online.onDisconnect().setValue("0");
        mGoogleApiClient.connect();
        greenGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ref_online.onDisconnect().setValue("0");
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {

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

       // rippleBackground.startRippleAnimation();


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


        final Location myLocation = new Location("currGreenLocation");
        myLocation.setLatitude(myLatitude);
        myLocation.setLongitude(myLongitude);

        final Float mySpeedGreen = myLocation.getSpeed();

        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.flat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.rotation(90.0f);
        //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrentLocationMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        double kmGreenQuery = 0.6; // 0.6 km

        firebaseUser = firebaseAuth.getCurrentUser();
        final String userId = firebaseUser.getUid();

        greenGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));




        myGreenGeoQuery = greenGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude), kmGreenQuery);

        myGreenGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!key.equals(userId)) {


                    int greenMarkersSize = greenMarkers.size();
                    String rSize = String.valueOf(greenMarkersSize);
                    tvGreenInYouArea.setText(rSize);
                    greenMarkers.put(key, userId);
                }

                for (Map.Entry<String, String> entry : greenMarkers.entrySet()) {

                    final String markersKey = entry.getKey();



                    //  ref_users_red.child(userId).child(markersKey).child("email").setValue(firebaseUser.getEmail());
                    // ref_users_red.child(userId).child(markersKey).child("uid").setValue(userId);

                    DatabaseReference ref_green_email = FirebaseDatabase.getInstance().getReference().child("users").child(markersKey);
                    ref_green_email.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User userDetails = dataSnapshot.getValue(User.class);
                            String greenEmail = userDetails.getEmail();
                            String greenUid = userDetails.getUid();
                            ref_users_green.child(userId).child(markersKey).child("email").setValue(greenEmail);
                            ref_users_green.child(userId).child(markersKey).child("uid").setValue(greenUid);

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

                    greenMarkers.remove(key);
                    int redMarkersSize = greenMarkers.size();
                    String rSize = String.valueOf(redMarkersSize);
                    tvGreenInYouArea.setText(rSize);

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

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

                        mSupportMapFragment.getMapAsync(GreenFragment.this);
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
    public void onConnectionStateSuccess() {

    }

    @Override
    public void onConnectionStateFailure() {

    }
}
