package com.karol.vimos.vimosapp.utils;

import android.content.Context;

/**
 * Created by Vimos on 30/05/17.
 */

public class SensitiveWords {

    public static void checkIfWordIsSensitive(Context context, String word, int check) {

        String[] sensitiveWords = {"kurwa", "chuj", "jebac", "jebak", "pizda", "dupa", "pierdole", "spierdalaj", "napierdalaj" };
        String nickInLowerCase = word.toLowerCase();
        check = 2;

        for (String sensitiveWord : sensitiveWords) {

            if (!nickInLowerCase.contains(sensitiveWord)) {

                check = 1; // nick is correct and does not include any inappropriate word

            } else {

                ToastUtil.shortToast(context, "Brzydkie słowo.");
                check = 0;
                break;

            }

        }


    }


}
