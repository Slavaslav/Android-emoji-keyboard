package com.slava.emojicfc;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

class AndroidUtilities {
    private static final float density;

    static {

        density = App.applicationContext.getResources().getDisplayMetrics().density;

    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static int dp(float value) {
        return (int) Math.ceil(density * value);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            App.applicationHandler.post(runnable);
        } else {
            App.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        App.applicationHandler.removeCallbacks(runnable);
    }
}