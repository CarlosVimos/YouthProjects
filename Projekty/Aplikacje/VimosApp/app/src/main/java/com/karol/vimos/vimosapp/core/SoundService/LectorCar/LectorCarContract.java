package com.karol.vimos.vimosapp.core.SoundService.LectorCar;

import android.content.Context;

/**
 * Created by Vimos on 11/07/17.
 */

public interface LectorCarContract {

    interface View {

        void onLectorCarSuccess(String message);

        void onLectorCarFailure(String message);


    }

    interface Presenter {

        void LectorIva(Context context, Integer distanceToTheNearest, Integer Speed, Integer Volume);

    }

    interface Interactor {

        void playLectorIva(Context context, Integer distanceToTheNearest, Integer Speed, Integer Volume);

    }



}
