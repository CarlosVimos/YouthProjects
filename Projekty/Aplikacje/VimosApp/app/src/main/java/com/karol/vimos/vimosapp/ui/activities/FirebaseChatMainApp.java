package com.karol.vimos.vimosapp.ui.activities;

import android.app.Application;

/**
 * Created by Vimos on 21/02/17.
 */

public class FirebaseChatMainApp extends Application {

    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        FirebaseChatMainApp.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
