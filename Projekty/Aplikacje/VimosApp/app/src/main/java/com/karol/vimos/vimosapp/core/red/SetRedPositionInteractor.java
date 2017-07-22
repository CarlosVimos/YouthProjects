package com.karol.vimos.vimosapp.core.red;

import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vimos on 21/02/17.
 */

public class SetRedPositionInteractor implements SetRedPositionContract.Interactor, LocationListener {

   double myLatitude, myLongitude;
    GeoFire redGeo;
    DatabaseReference ref_red;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private SetRedPositionContract.OnSetRedPositionListener mOnSetRedPositionListener;

    public SetRedPositionInteractor(SetRedPositionContract.OnSetRedPositionListener onSetRedPositionListener) {
        this.mOnSetRedPositionListener = onSetRedPositionListener;
    }


    @Override
    public void onLocationChanged(Location location) {

        ref_red = FirebaseDatabase.getInstance().getReference("geofire/red");
        redGeo = new GeoFire(ref_red);

        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();



    }

    @Override
    public void setRedPositionToDatabase(String uid) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        uid = firebaseUser.getUid();

        redGeo.setLocation(uid, new GeoLocation(myLatitude, myLongitude));

    }


}
