package com.karol.vimos.vimosapp.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.karol.vimos.vimosapp.R;

/**
 * Created by Vimos on 19/03/17.
 */

public class ViewNearestPanelColor extends AppCompatActivity {




    public static void SetColorPercentPanel(Context context, int percent, int greenColorId, TextView tv0, TextView tv2, TextView tv4, TextView tv6, TextView tv8,
                                            TextView tv10, TextView tv12, TextView tv14, TextView tv16, TextView tv18, TextView tv20, TextView tv22, TextView tv24,
                                            TextView tv26, TextView tv28, TextView tv30, TextView tv32, TextView tv34, TextView tv36, TextView tv38, TextView tv40,
                                            TextView tv42, TextView tv44, TextView tv46, TextView tv48, TextView tv50, TextView tv52, TextView tv54,
                                            TextView tv56, TextView tv58, TextView tv60, TextView tv62, TextView tv64, TextView tv66, TextView tv68,
                                            TextView tv70, TextView tv72, TextView tv74, TextView tv76, TextView tv78, TextView tv80, TextView tv82,
                                            TextView tv84, TextView tv86, TextView tv88, TextView tv90, TextView tv92, TextView tv94, TextView tv96,
                                            TextView tv98) {

        TextView[] tvID = new TextView[] {tv0, tv2, tv4, tv6, tv8, tv10, tv12, tv14, tv16, tv18, tv20, tv22, tv24, tv26, tv28, tv30,
                tv32, tv34, tv36, tv38, tv40, tv42, tv44, tv46, tv48, tv50, tv52, tv54, tv56, tv58, tv60, tv62, tv64, tv66, tv68, tv70,
                tv72, tv74, tv76, tv78, tv80, tv82, tv84, tv86, tv88, tv90, tv92, tv94, tv96, tv98};



        if (percent >= 0 && percent < 2) {

            tv0.setVisibility(View.VISIBLE);
            tv0.setBackgroundColor(ContextCompat.getColor(context, greenColorId));




            for (int i = 0; i < tvID.length; i++) {


                if (i != 0) {
                    tvID[i].setVisibility(View.INVISIBLE);
                }

            }
        }
        else if (percent >= 2 && percent < 4) {

            tv2.setVisibility(View.VISIBLE);
            tv2.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 1) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }

        else if (percent >= 4 && percent < 6) {

            tv4.setVisibility(View.VISIBLE);
            tv4.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 2) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 6 && percent < 8) {

            tv6.setVisibility(View.VISIBLE);
            tv6.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 3) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 8 && percent < 10) {

            tv8.setVisibility(View.VISIBLE);
            tv8.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 4) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 10 && percent < 12) {

            tv10.setVisibility(View.VISIBLE);
            tv10.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 5) {

                    tvID[i].setVisibility(View.INVISIBLE);

                }
            }
        }
        else if (percent >= 12 && percent < 14) {

            tv12.setVisibility(View.VISIBLE);
            tv12.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 6) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 14 && percent < 16) {

            tv14.setVisibility(View.VISIBLE);
            tv14.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 7) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 16 && percent < 20) {

            tv16.setVisibility(View.VISIBLE);
            tv16.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 8) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 18 && percent < 20) {

            tv18.setVisibility(View.VISIBLE);
            tv18.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 9) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 20 && percent < 22) {

            tv20.setVisibility(View.VISIBLE);
            tv20.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 10) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 22 && percent < 24) {

            tv22.setVisibility(View.VISIBLE);
            tv22.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 11) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 24 && percent < 26) {

            tv24.setVisibility(View.VISIBLE);
            tv24.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 12) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 26 && percent < 28) {

            tv26.setVisibility(View.VISIBLE);
            tv26.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 13) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 28 && percent < 30) {

            tv28.setVisibility(View.VISIBLE);
            tv28.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 14) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 30 && percent < 32) {

            tv30.setVisibility(View.VISIBLE);
            tv30.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 15) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 32 && percent < 34) {

            tv32.setVisibility(View.VISIBLE);
            tv32.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 16) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 34 && percent < 36) {

            tv34.setVisibility(View.VISIBLE);
            tv34.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 17) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 36 && percent < 38) {

            tv36.setVisibility(View.VISIBLE);
            tv36.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 18) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 38 && percent < 40) {

            tv38.setVisibility(View.VISIBLE);
            tv38.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 19) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 40 && percent < 42) {

            tv40.setVisibility(View.VISIBLE);
            tv40.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 20) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 42 && percent < 44) {

            tv42.setVisibility(View.VISIBLE);
            tv42.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 21) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 44 && percent < 46) {

            tv44.setVisibility(View.VISIBLE);
            tv44.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 22) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 46 && percent < 48) {

            tv46.setVisibility(View.VISIBLE);
            tv46.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 23) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 48 && percent < 50) {

            tv48.setVisibility(View.VISIBLE);
            tv48.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 24) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 50 && percent < 52) {

            tv50.setVisibility(View.VISIBLE);
            tv50.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 25) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 52 && percent < 54) {

            tv52.setVisibility(View.VISIBLE);
            tv52.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 26) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 54 && percent < 56) {

            tv54.setVisibility(View.VISIBLE);
            tv54.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 27) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 56 && percent < 58) {

            tv56.setVisibility(View.VISIBLE);
            tv56.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 28) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 58 && percent < 60) {

            tv58.setVisibility(View.VISIBLE);
            tv58.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 29) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 60 && percent < 62) {

            tv60.setVisibility(View.VISIBLE);
            tv60.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 30) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 62 && percent < 64) {

            tv62.setVisibility(View.VISIBLE);
            tv62.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 31) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 64 && percent < 66) {

            tv64.setVisibility(View.VISIBLE);
            tv64.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 32) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }else if (percent >= 66 && percent < 68) {

            tv66.setVisibility(View.VISIBLE);
            tv66.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 33) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 68 && percent < 70) {

            tv68.setVisibility(View.VISIBLE);
            tv68.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 34) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 70 && percent < 72) {

            tv70.setVisibility(View.VISIBLE);
            tv70.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 35) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 72 && percent < 74) {

            tv72.setVisibility(View.VISIBLE);
            tv72.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 36) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 74 && percent < 76) {

            tv74.setVisibility(View.VISIBLE);
            tv74.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 37) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 76 && percent < 78) {

            tv76.setVisibility(View.VISIBLE);
            tv76.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 38) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 78 && percent < 80) {

            tv78.setVisibility(View.VISIBLE);
            tv78.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 39) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 80 && percent < 82) {

            tv80.setVisibility(View.VISIBLE);
            tv80.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 40) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 82 && percent < 84) {

            tv82.setVisibility(View.VISIBLE);
            tv82.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 41) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 84 && percent < 86) {

            tv84.setVisibility(View.VISIBLE);
            tv84.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 42) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 86 && percent < 88) {

            tv86.setVisibility(View.VISIBLE);
            tv86.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 43) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 88 && percent < 90) {

            tv88.setVisibility(View.VISIBLE);
            tv88.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 44) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 90 && percent < 92) {

            tv90.setVisibility(View.VISIBLE);
            tv90.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 45) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 92 && percent < 94) {

            tv92.setVisibility(View.VISIBLE);
            tv92.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 46) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 94 && percent < 96) {

            tv94.setVisibility(View.VISIBLE);
            tv94.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 47) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 96 && percent < 98) {

            tv96.setVisibility(View.VISIBLE);
            tv96.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 48) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 98 && percent <= 100) {

            tv98.setVisibility(View.VISIBLE);
            tv98.setBackgroundColor(ContextCompat.getColor(context, greenColorId));

            for (int i = 0; i < tvID.length; i++) {

                if (i != 49) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }



    }




    public static void SetDrawablePercentPanel(Context context, int percent, Drawable greenColorId, TextView tv0, TextView tv2, TextView tv4, TextView tv6, TextView tv8,
                                               TextView tv10, TextView tv12, TextView tv14, TextView tv16, TextView tv18, TextView tv20, TextView tv22, TextView tv24,
                                               TextView tv26, TextView tv28, TextView tv30, TextView tv32, TextView tv34, TextView tv36, TextView tv38, TextView tv40,
                                               TextView tv42, TextView tv44, TextView tv46, TextView tv48, TextView tv50, TextView tv52, TextView tv54,
                                               TextView tv56, TextView tv58, TextView tv60, TextView tv62, TextView tv64, TextView tv66, TextView tv68,
                                               TextView tv70, TextView tv72, TextView tv74, TextView tv76, TextView tv78, TextView tv80, TextView tv82,
                                               TextView tv84, TextView tv86, TextView tv88, TextView tv90, TextView tv92, TextView tv94, TextView tv96,
                                               TextView tv98) {

        TextView[] tvID = new TextView[] {tv0, tv2, tv4, tv6, tv8, tv10, tv12, tv14, tv16, tv18, tv20, tv22, tv24, tv26, tv28, tv30,
                tv32, tv34, tv36, tv38, tv40, tv42, tv44, tv46, tv48, tv50, tv52, tv54, tv56, tv58, tv60, tv62, tv64, tv66, tv68, tv70,
                tv72, tv74, tv76, tv78, tv80, tv82, tv84, tv86, tv88, tv90, tv92, tv94, tv96, tv98};



        if (percent >= 0 && percent < 2) {

            tv0.setVisibility(View.VISIBLE);
            //tv0.setBackgroundColor(ContextCompat.getColor(context, greenColorId));
            tv0.setBackground(greenColorId);




            for (int i = 0; i < tvID.length; i++) {


                if (i != 0) {
                    tvID[i].setVisibility(View.INVISIBLE);
                }

            }
        }
        else if (percent >= 2 && percent < 4) {

            tv2.setVisibility(View.VISIBLE);
            tv2.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 1) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }

        else if (percent >= 4 && percent < 6) {

            tv4.setVisibility(View.VISIBLE);
            tv4.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 2) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 6 && percent < 8) {

            tv6.setVisibility(View.VISIBLE);
            tv6.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 3) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 8 && percent < 10) {

            tv8.setVisibility(View.VISIBLE);
            tv8.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 4) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 10 && percent < 12) {

            tv10.setVisibility(View.VISIBLE);
            tv10.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 5) {

                    tvID[i].setVisibility(View.INVISIBLE);

                }
            }
        }
        else if (percent >= 12 && percent < 14) {

            tv12.setVisibility(View.VISIBLE);
            tv12.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 6) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 14 && percent < 16) {

            tv14.setVisibility(View.VISIBLE);
            tv14.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 7) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 16 && percent < 20) {

            tv16.setVisibility(View.VISIBLE);
            tv16.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 8) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 18 && percent < 20) {

            tv18.setVisibility(View.VISIBLE);
            tv18.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 9) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 20 && percent < 22) {

            tv20.setVisibility(View.VISIBLE);
            tv20.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 10) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 22 && percent < 24) {

            tv22.setVisibility(View.VISIBLE);
            tv22.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 11) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 24 && percent < 26) {

            tv24.setVisibility(View.VISIBLE);
            tv24.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 12) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 26 && percent < 28) {

            tv26.setVisibility(View.VISIBLE);
            tv26.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 13) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 28 && percent < 30) {

            tv28.setVisibility(View.VISIBLE);
            tv28.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 14) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 30 && percent < 32) {

            tv30.setVisibility(View.VISIBLE);
            tv30.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 15) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 32 && percent < 34) {

            tv32.setVisibility(View.VISIBLE);
            tv32.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 16) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 34 && percent < 36) {

            tv34.setVisibility(View.VISIBLE);
            tv34.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 17) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 36 && percent < 38) {

            tv36.setVisibility(View.VISIBLE);
            tv36.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 18) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 38 && percent < 40) {

            tv38.setVisibility(View.VISIBLE);
            tv38.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 19) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 40 && percent < 42) {

            tv40.setVisibility(View.VISIBLE);
            tv40.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 20) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 42 && percent < 44) {

            tv42.setVisibility(View.VISIBLE);
            tv42.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 21) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 44 && percent < 46) {

            tv44.setVisibility(View.VISIBLE);
            tv44.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 22) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 46 && percent < 48) {

            tv46.setVisibility(View.VISIBLE);
            tv46.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 23) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 48 && percent < 50) {

            tv48.setVisibility(View.VISIBLE);
            tv48.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 24) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 50 && percent < 52) {

            tv50.setVisibility(View.VISIBLE);
            tv50.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 25) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 52 && percent < 54) {

            tv52.setVisibility(View.VISIBLE);
            tv52.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 26) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 54 && percent < 56) {

            tv54.setVisibility(View.VISIBLE);
            tv54.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 27) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 56 && percent < 58) {

            tv56.setVisibility(View.VISIBLE);
            tv56.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 28) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 58 && percent < 60) {

            tv58.setVisibility(View.VISIBLE);
            tv58.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 29) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 60 && percent < 62) {

            tv60.setVisibility(View.VISIBLE);
            tv60.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 30) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 62 && percent < 64) {

            tv62.setVisibility(View.VISIBLE);
            tv62.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 31) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 64 && percent < 66) {

            tv64.setVisibility(View.VISIBLE);
            tv64.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 32) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }else if (percent >= 66 && percent < 68) {

            tv66.setVisibility(View.VISIBLE);
            tv66.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 33) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 68 && percent < 70) {

            tv68.setVisibility(View.VISIBLE);
            tv68.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 34) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 70 && percent < 72) {

            tv70.setVisibility(View.VISIBLE);
            tv70.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 35) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 72 && percent < 74) {

            tv72.setVisibility(View.VISIBLE);
            tv72.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 36) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 74 && percent < 76) {

            tv74.setVisibility(View.VISIBLE);
            tv74.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 37) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 76 && percent < 78) {

            tv76.setVisibility(View.VISIBLE);
            tv76.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 38) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 78 && percent < 80) {

            tv78.setVisibility(View.VISIBLE);
            tv78.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 39) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 80 && percent < 82) {

            tv80.setVisibility(View.VISIBLE);
            tv80.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 40) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 82 && percent < 84) {

            tv82.setVisibility(View.VISIBLE);
            tv82.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 41) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 84 && percent < 86) {

            tv84.setVisibility(View.VISIBLE);
            tv84.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 42) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 86 && percent < 88) {

            tv86.setVisibility(View.VISIBLE);
            tv86.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 43) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 88 && percent < 90) {

            tv88.setVisibility(View.VISIBLE);
            tv88.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 44) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 90 && percent < 92) {

            tv90.setVisibility(View.VISIBLE);
            tv90.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 45) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 92 && percent < 94) {

            tv92.setVisibility(View.VISIBLE);
            tv92.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 46) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 94 && percent < 96) {

            tv94.setVisibility(View.VISIBLE);
            tv94.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 47) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 96 && percent < 98) {

            tv96.setVisibility(View.VISIBLE);
            tv96.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 48) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (percent >= 98 && percent <= 100) {

            tv98.setVisibility(View.VISIBLE);
            tv98.setBackground(greenColorId);

            for (int i = 0; i < tvID.length; i++) {

                if (i != 49) {

                    tvID[i].setVisibility(View.INVISIBLE);
                }
            }
        }




    }







}
