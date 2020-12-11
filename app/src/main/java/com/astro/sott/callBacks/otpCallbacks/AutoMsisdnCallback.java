package com.astro.sott.callBacks.otpCallbacks;

import com.astro.sott.modelClasses.OtpModel;

import retrofit2.Response;

public interface AutoMsisdnCallback {
    void msisdnReceived(OtpModel smsResponse);
    void msisdnFailure(Response<OtpModel> response);
    void onError(Throwable call);
}
