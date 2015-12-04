package com.tbg.myexpenses;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Pavlya on 28/11/2015.
 */
public class Utility {

    public static void hideSoftKeyboard(final Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
