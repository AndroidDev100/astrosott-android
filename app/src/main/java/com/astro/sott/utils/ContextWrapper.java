package com.astro.sott.utils;

import android.app.Activity;

public class ContextWrapper {

    private static ContextWrapper contextWrapper;
    private Activity activity;

    public static ContextWrapper getInstance() {
        if (contextWrapper == null)
            contextWrapper = new ContextWrapper();
        return contextWrapper;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
