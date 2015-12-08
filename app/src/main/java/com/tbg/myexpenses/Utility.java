package com.tbg.myexpenses;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pavlya on 28/11/2015.
 */
public class Utility {

    public static void hideSoftKeyboard(final Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getDateText(long dateValue) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateValue);
        Date date = calendar.getTime();
        String dateFormat = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return String.valueOf(simpleDateFormat.format(date));
    }

    public static boolean checkNullOrEmptyString(String string) {
        return (string == null || string.length() <= 0);
    }
}
