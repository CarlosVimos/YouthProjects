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
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static com.karol.vimos.vimosapp.ui.activities.blue.activity.BlueActivity.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by Vimos on 26/02/17.
 */

public class OrangeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mGoogleMap;
    private MapView mapView;
    private SupportMapFragment mSupportMapFragment;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location myGreenLocation;
    private Marker mCurrentLocationMarker;
    private LocationManager mLocationManager;
    private DatabaseReference ref_orange, ref_myName, ref_users_orange;
    private double myLatitude, myLongitude;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GeoFire orangeGeoFire;
    private GeoQuery myOrangeGeoQuery;
    private int myOrangeRadiusArea; // my area of red markers
    private Button btnStopDetection;
    private View
            viewWelcomePanel, // welcome panel
            viewMarkersPanel, // how many green markers is in my area;
            viewDetailsPanel; // my speed, etc. ...

    private TextView tvWelcome, tvOrangeInYouArea, tvToChatRoom;

    private Map<String, String> orangeMarkers;

    public static OrangeFragment newInstance() {
        Bundle args = new Bundle();
        OrangeFragment fragment = new OrangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_orange, container, false);

        tvOrangeInYouArea = (TextView) fragmentView.findViewById(R.id.tv_user_orange_detected_id);
//        tvGreenInYouArea.setOnClickListener(this);
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapOrangeId);
        mSupportMapFragment.getMapAsync(this);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ref_orange = FirebaseDatabase.getInstance().getReference("geofire/orange");
        orangeGeoFire = new GeoFire(ref_orange);

        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        orangeMarkers = new HashMap<String, String>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_myName = database.getReference(Constants.ARG_USERS).child(firebaseUser.getUid());

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
        mGoogleApiClient.connect();
        orangeGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

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


        final Location myLocation = new Location("currOrangeLocation");
        myLocation.setLatitude(myLatitude);
        myLocation.setLongitude(myLongitude);

        final Float mySpeedOrange = myLocation.getSpeed();

        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.flat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.rotation(90.0f);
        //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mCurrentLocationMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        double kmOrangeQuery = 0.6; // 0.6 km

        firebaseUser = firebaseAuth.getCurrentUser();
        final String userId = firebaseUser.getUid();

        orangeGeoFire.setLocation(userId, new GeoLocation(myLatitude, myLongitude));




        myOrangeGeoQuery = orangeGeoFire.queryAtLocation(new GeoLocation(myLatitude, myLongitude), kmOrangeQuery);

        myOrangeGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!key.equals(userId)) {


                    int orangeMarkersSize = orangeMarkers.size();
                    String rSize = String.valueOf(orangeMarkersSize);
                    tvOrangeInYouArea.setText(rSize);
                    orangeMarkers.put(key, userId);
                }

                for (Map.Entry<String, String> entry : orangeMarkers.entrySet()) {

                    final String markersKey = entry.getKey();



                    //  ref_users_red.child(userId).child(markersKey).child("email").setValue(firebaseUser.getEmail());
                    // ref_users_red.child(userId).child(markersKey).child("uid").setValue(userId);

                    DatabaseReference ref_green_email = FirebaseDatabase.getInstance().getReference().child("users").child(markersKey);
                    ref_green_email.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User userDetails = dataSnapshot.getValue(User.class);
                            String orangeEmail = userDetails.getEmail();
                            String orangeUid = userDetails.getUid();
                            ref_users_orange.child(userId).child(markersKey).child("email").setValue(orangeEmail);
                            ref_users_orange.child(userId).child(markersKey).child("uid").setValue(orangeUid);

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

                    orangeMarkers.remove(key);
                    int orangeMarkersSize = orangeMarkers.size();
                    String rSize = String.valueOf(orangeMarkersSize);
                    tvOrangeInYouArea.setText(rSize);

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

                        mSupportMapFragment.getMapAsync(OrangeFragment.this);
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

}
