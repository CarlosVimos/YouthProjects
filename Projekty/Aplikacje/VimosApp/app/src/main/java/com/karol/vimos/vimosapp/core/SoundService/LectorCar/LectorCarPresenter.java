package com.karol.vimos.vimosapp.core.SoundService.LectorCar;

import android.content.Context;

import com.karol.vimos.vimosapp.core.lector.LectorContract;
import com.karol.vimos.vimosapp.core.lector.LectorInteractor;

/**
 * Created by Vimos on 11/07/17.
 */

public class LectorCarPresenter implements LectorCarContract.Presenter {

    private LectorCarContract.View mView;
    private LectorCarInteractor mLectorCarInteractor;

    public LectorCarPresenter(LectorCarContract.View view) {

        this.mView = view;
        mLectorCarInteractor = new LectorCarInteractor(this);

    }


    @Override
    public void LectorIva(Context context, Integer distanceToTheNearest, Integer Speed, Integer Volume) {




    }
}
