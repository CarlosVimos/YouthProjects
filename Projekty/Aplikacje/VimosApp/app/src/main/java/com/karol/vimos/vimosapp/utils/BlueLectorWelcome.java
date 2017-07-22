package com.karol.vimos.vimosapp.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Vimos on 03/06/17.
 */

public class BlueLectorWelcome {

    // it informs about detection details when BlueActivity is opened.
    public static void plSpeakDetectionInfo(Context context, int radius, int objectsToDetect) {

        // green = 1, red = 2, green_red = 3
        int green, red, green_red;
        green = 1;
        red = 2;
        green_red = 3;

        MediaPlayer pl_1000, pl_900, pl_800, pl_700, pl_600, pl_500, pl_400, pl_300, pl_200,
                    pl_100, plGreen, plRed, plOrange, plRedGreen;

        if (radius == 1000) {

            // play pl_1000

            if (objectsToDetect == green) {

            }
            else if (objectsToDetect == red) {

            }
            else if (objectsToDetect == green_red) {

                // wait until pl_1000 will finish and start plRedGreem

            }

        }


    }


}
