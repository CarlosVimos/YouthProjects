package com.karol.vimos.vimosapp.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Vimos on 15/02/17.
 */

public class ToastUtil extends Activity {

    public static void shortToast(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }

    public static void longToast(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
