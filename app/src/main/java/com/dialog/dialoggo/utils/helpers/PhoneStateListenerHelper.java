package com.dialog.dialoggo.utils.helpers;

import androidx.fragment.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.dialog.dialoggo.callBacks.PhoneListenerCallBack;

public class PhoneStateListenerHelper extends PhoneStateListener {

    private static PhoneStateListener mInstance;
    private static PhoneListenerCallBack phoneListenerCallBack;

    private PhoneStateListenerHelper() {
    }

    public static PhoneStateListener getInstance(Fragment context) {

        phoneListenerCallBack = (PhoneListenerCallBack) context;
        if (mInstance == null)
            mInstance = new PhoneStateListenerHelper();
        return mInstance;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {

        if (state == TelephonyManager.CALL_STATE_RINGING) {
            phoneListenerCallBack.onCallStateRinging();
        } else if (state == TelephonyManager.CALL_STATE_IDLE) {
            phoneListenerCallBack.onCallStateIdle();
        } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
            phoneListenerCallBack.onCallStateRinging();
        }
        super.onCallStateChanged(state, phoneNumber);
    }
}
