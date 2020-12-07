package com.dialog.dialoggo.utils.helpers;

import android.util.Log;

public class PrintLogging {
    public static void printLog(Class mClass,String _message, String _value) {
        Log.w(mClass.getSimpleName(),_value);
    }
    public static void printLog(String mClass,String _message, String _value) {
        Log.w(mClass,_value);
    }

    public static void printLog(String _message, String _value) {
      Log.w("printValues", _value);
    }
}
