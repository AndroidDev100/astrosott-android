package com.dialog.dialoggo.callBacks.otpCallbacks;

import com.dialog.dialoggo.modelClasses.OtpModel;

import retrofit2.Response;

public interface AutoMsisdnCallback {
    void msisdnReceived(OtpModel smsResponse);
    void msisdnFailure(Response<OtpModel> response);
    void onError(Throwable call);
}
