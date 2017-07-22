package com.karol.vimos.vimosapp.core.SoundService.LectorCar;

import android.content.Context;
import android.media.MediaPlayer;

import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.lector.LectorContract;

/**
 * Created by Vimos on 11/07/17.
 */

public class LectorCarInteractor implements LectorCarContract.Interactor, MediaPlayer.OnErrorListener {


    public LectorCarInteractor(LectorCarPresenter lectorCarPresenter) {
    }

    @Override
    public void playLectorIva(Context context, Integer distanceToTheNearest, Integer Speed, Integer Volume) {




    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public static void plSpeakDistanceToTheNearest(Context context, int distanceToTheNearest, int speed, Integer volume) {

        MediaPlayer pl_900, pl_800, pl_700, pl_600, pl_500, pl_400, pl_300, pl_200,
                pl_40, pl_20, pl_10, pl_100, pl_60, pl_80, pl_close, pl_slowdown;



        pl_900 = MediaPlayer.create(context, R.raw.pl_900);
        pl_800 = MediaPlayer.create(context, R.raw.pl_800);
        pl_700 = MediaPlayer.create(context, R.raw.pl_700);
        pl_600 = MediaPlayer.create(context, R.raw.pl_600);
        pl_500 = MediaPlayer.create(context, R.raw.pl_500);
        pl_400 = MediaPlayer.create(context, R.raw.pl_400);
        pl_300 = MediaPlayer.create(context, R.raw.pl_300);
        pl_200 = MediaPlayer.create(context, R.raw.pl_200);
        pl_100 = MediaPlayer.create(context, R.raw.pl_100);
        pl_80 = MediaPlayer.create(context, R.raw.pl_80);
        pl_60 = MediaPlayer.create(context, R.raw.pl_60);
        pl_40 = MediaPlayer.create(context, R.raw.pl_40);
        pl_20 = MediaPlayer.create(context, R.raw.pl_20);
        pl_10 = MediaPlayer.create(context, R.raw.pl_10);
        pl_close = MediaPlayer.create(context, R.raw.pl_cl);
        pl_slowdown = MediaPlayer.create(context, R.raw.pl_slowdown);


        if (distanceToTheNearest > 880 && distanceToTheNearest < 920) {


            if (pl_900.isPlaying()) {
                pl_900.stop();
                pl_900.reset();

            }
            //  pl_40.release();
            pl_900.start();
            pl_900.setLooping(false);

        }
        else if (distanceToTheNearest > 780 && distanceToTheNearest < 820) {


            pl_800.start();
            pl_800.setLooping(false);

        }
        else if (distanceToTheNearest > 680 && distanceToTheNearest < 720) {


            pl_700.start();
            pl_700.setLooping(false);

        }
        else if (distanceToTheNearest > 580 && distanceToTheNearest < 620) {


            pl_600.start();
            pl_600.setLooping(false);

        }
        else if (distanceToTheNearest > 480 && distanceToTheNearest < 520) {


            pl_500.start();
            pl_500.setLooping(false);
        }
        else if (distanceToTheNearest > 380 && distanceToTheNearest < 420) {

            //  pl_400.release();
            pl_400.start();
            pl_400.setLooping(false);

        }
        else if (distanceToTheNearest > 280 && distanceToTheNearest < 320) {

            //  pl_300.release();
            pl_300.start();
            pl_300.setLooping(false);

        }
        else if (distanceToTheNearest > 180 && distanceToTheNearest < 220) {

            //pl_200.release();
            pl_200.start();
            pl_200.setLooping(false);

        }
        // it depends.. if a blue is driving fast, so maybe there will be no seeing such small value? so maybe
        // better is to do: d > 100 && d < 140 ??
        // I do so many if because I would like to call the speaker only once.

        else if (distanceToTheNearest >= 40 && distanceToTheNearest <= 160) {



            if (pl_slowdown.isPlaying()) {
                pl_slowdown.stop();
                pl_slowdown.reset();
            }

            pl_close.start();
            pl_close.setLooping(false);

            if (speed > 60) {

                // zwolnij

                if (pl_close.isPlaying()) {
                    pl_close.stop();
                    pl_close.reset();
                }
                // pl_close.stop();
                pl_slowdown.start();



            }



        }
//        else if ((distanceToTheNearest >= 100 && distanceToTheNearest <= 102) || (distanceToTheNearest > 102 && distanceToTheNearest <= 104)
//                || (distanceToTheNearest > 104 && distanceToTheNearest <= 106) || (distanceToTheNearest > 106 && distanceToTheNearest <= 112)
//                || (distanceToTheNearest > 112 && distanceToTheNearest <= 122)) {
//
//
//            if (pl_100.isPlaying()) {
//                pl_100.stop();
//                pl_100.reset();
//
//            }
//            //pl_100.release();
//            pl_100.start();
//            pl_100.setLooping(false);
//         //  pl_100.start();
//
//        }
////        else if (distanceToTheNearest == 95) {
////
////            // play 90 meters
////
////        }
//        else if ((distanceToTheNearest >= 80 && distanceToTheNearest <= 82) || (distanceToTheNearest > 82 && distanceToTheNearest <= 84)
//                || (distanceToTheNearest > 84 && distanceToTheNearest <= 86)) {
//
//
//
//            if (pl_80.isPlaying()) {
//                pl_80.stop();
//                pl_80.reset();
//
//            }
//          //  pl_40.release();
//            pl_80.start();
//            pl_80.setLooping(false);
//
//          //  pl_80.start();
//
//        }
////        else if (distanceToTheNearest == 75) {
////
////            // play 70
////
////        }
//        else if ((distanceToTheNearest >= 60 && distanceToTheNearest <= 62) || (distanceToTheNearest > 62 && distanceToTheNearest <= 64)
//                || (distanceToTheNearest > 64 && distanceToTheNearest <= 66)) {
//
//           // pl_60.release();
//            pl_40.start();
//            pl_40.setLooping(false);
//
//        }
////        else if (distanceToTheNearest == 55) {
////
////            // play 50
////
////        }
//        else if ((distanceToTheNearest >= 40 && distanceToTheNearest <= 42) || (distanceToTheNearest > 42 && distanceToTheNearest <= 44)
//                || (distanceToTheNearest > 44 && distanceToTheNearest <= 46)) {
//
//        //    pl_40.release();
//            pl_10.start();
//            pl_10.setLooping(false);
//
//        }
////        else if (distanceToTheNearest == 35) {
////
////            // play 30
////
////        }
////        else if ((distanceToTheNearest >= 20 && distanceToTheNearest <= 22) || (distanceToTheNearest > 22 && distanceToTheNearest <= 24)
////                || (distanceToTheNearest > 24 && distanceToTheNearest <= 26)) {
////
////         //   pl_20.release();
////            pl_20.start();
////            pl_20.setLooping(false);
////
////        }
//        else if (distanceToTheNearest > 10 && distanceToTheNearest < 16) {
//
//      //      pl_10.release();
//            pl_10.start();
//            pl_10.setLooping(false);
//
//        }
        else if (distanceToTheNearest == -1) {

            pl_900.stop();
            pl_800.stop();
            pl_700.stop();
            pl_600.stop();
            pl_500.stop();
            pl_400.stop();
            pl_300.stop();
            pl_200.stop();
            pl_100.stop();
            pl_80.stop();
            pl_60.stop();
            pl_40.stop();
            pl_20.stop();
            pl_10.stop();
            pl_close.stop();
            pl_slowdown.stop();

        }

        MediaPlayer [] listMediaPlayer;
        listMediaPlayer = new MediaPlayer[] {pl_10, pl_20, pl_40, pl_60, pl_80, pl_100, pl_200, pl_300, pl_400, pl_500,
                pl_600, pl_700, pl_800, pl_900, pl_close, pl_slowdown};

        for (MediaPlayer aListMediaPlayer : listMediaPlayer) {


            aListMediaPlayer.setVolume(volume, volume);


        }




    }

    public static void plMute(Context context) {

        MediaPlayer pl_900, pl_800, pl_700, pl_600, pl_500, pl_400, pl_300, pl_200,
                pl_40, pl_20, pl_10, pl_100, pl_60, pl_80, pl_close, pl_slowdown, train_alert, train_alert1;


        pl_900 = MediaPlayer.create(context, R.raw.pl_900);
        pl_800 = MediaPlayer.create(context, R.raw.pl_800);
        pl_700 = MediaPlayer.create(context, R.raw.pl_700);
        pl_600 = MediaPlayer.create(context, R.raw.pl_600);
        pl_500 = MediaPlayer.create(context, R.raw.pl_500);
        pl_400 = MediaPlayer.create(context, R.raw.pl_400);
        pl_300 = MediaPlayer.create(context, R.raw.pl_300);
        pl_200 = MediaPlayer.create(context, R.raw.pl_200);
        pl_100 = MediaPlayer.create(context, R.raw.pl_100);
        pl_80 = MediaPlayer.create(context, R.raw.pl_80);
        pl_60 = MediaPlayer.create(context, R.raw.pl_60);
        pl_40 = MediaPlayer.create(context, R.raw.pl_40);
        pl_20 = MediaPlayer.create(context, R.raw.pl_20);
        pl_10 = MediaPlayer.create(context, R.raw.pl_10);
        pl_close = MediaPlayer.create(context, R.raw.pl_cl);
        pl_slowdown = MediaPlayer.create(context, R.raw.pl_slowdown);
        train_alert = MediaPlayer.create(context, R.raw.train);
        train_alert1 = MediaPlayer.create(context, R.raw.train1);


        MediaPlayer [] listMediaPlayer;
        listMediaPlayer = new MediaPlayer[] {pl_10, pl_20, pl_40, pl_60, pl_80, pl_100, pl_200, pl_300, pl_400, pl_500,
                pl_600, pl_700, pl_800, pl_900, pl_close, pl_slowdown, train_alert, train_alert1};

        for (MediaPlayer aListMediaPlayer : listMediaPlayer) {

            aListMediaPlayer.setVolume(0, 0);

        }



    }

    public static void plUnmute(Context context) {


        MediaPlayer pl_900, pl_800, pl_700, pl_600, pl_500, pl_400, pl_300, pl_200,
                pl_40, pl_20, pl_10, pl_100, pl_60, pl_80, pl_close, pl_slowdown, train_alert, train_alert1;


        pl_900 = MediaPlayer.create(context, R.raw.pl_900);
        pl_800 = MediaPlayer.create(context, R.raw.pl_800);
        pl_700 = MediaPlayer.create(context, R.raw.pl_700);
        pl_600 = MediaPlayer.create(context, R.raw.pl_600);
        pl_500 = MediaPlayer.create(context, R.raw.pl_500);
        pl_400 = MediaPlayer.create(context, R.raw.pl_400);
        pl_300 = MediaPlayer.create(context, R.raw.pl_300);
        pl_200 = MediaPlayer.create(context, R.raw.pl_200);
        pl_100 = MediaPlayer.create(context, R.raw.pl_100);
        pl_80 = MediaPlayer.create(context, R.raw.pl_80);
        pl_60 = MediaPlayer.create(context, R.raw.pl_60);
        pl_40 = MediaPlayer.create(context, R.raw.pl_40);
        pl_20 = MediaPlayer.create(context, R.raw.pl_20);
        pl_10 = MediaPlayer.create(context, R.raw.pl_10);
        pl_close = MediaPlayer.create(context, R.raw.pl_cl);
        pl_slowdown = MediaPlayer.create(context, R.raw.pl_slowdown);
        train_alert = MediaPlayer.create(context, R.raw.train);
        train_alert1 = MediaPlayer.create(context, R.raw.train1);


        MediaPlayer [] listMediaPlayer;
        listMediaPlayer = new MediaPlayer[] {pl_10, pl_20, pl_40, pl_60, pl_80, pl_100, pl_200, pl_300, pl_400, pl_500,
                pl_600, pl_700, pl_800, pl_900, pl_close, pl_slowdown, train_alert, train_alert1};

        for (MediaPlayer aListMediaPlayer : listMediaPlayer) {

            aListMediaPlayer.setVolume(1, 1);

        }


    }


}
