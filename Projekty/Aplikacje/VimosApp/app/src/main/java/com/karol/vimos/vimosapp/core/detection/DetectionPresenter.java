package com.karol.vimos.vimosapp.core.detection;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.core.lector.LectorContract;

/**
 * Created by Vimos on 25/02/17.
 */

public class DetectionPresenter implements DetectionContract.Presenter, DetectionContract.OnDetectionDatabaseListener {

    private DetectionContract.View mView;
    private DetectionInteractor mDetectionInteractor;

    public DetectionPresenter(DetectionContract.View view) {

        this.mView = view;
        mDetectionInteractor = new DetectionInteractor(this);
    }


    @Override
    public void onSuccess(String message) {

//        mView.onDetectionSuccess(message);

    }

    @Override
    public void onFailure(String message) {

    //    mView.onDetectionFailure(message);

    }


    @Override
    public void RedDetection(Context context, FirebaseUser firebaseUser, int detection) {

        mDetectionInteractor.addRedDetectionToDatabase(context, firebaseUser, detection);

    }

    @Override
    public void GreenDetection(Context context, FirebaseUser firebaseUser, int detection) {

        mDetectionInteractor.addGreenDetectionToDatabase(context, firebaseUser, detection);

    }

    @Override
    public void OrangeDetection(Context context, FirebaseUser firebaseUser, int detection) {

        mDetectionInteractor.addOrangeDetectionToDatabase(context, firebaseUser, detection);

    }
}
