package com.astro.sott.callBacks.otpCallbacks;

import com.astro.sott.modelClasses.OtpModel;

import retrofit2.Response;

public interface OtpCallback {
    void smsReceived(OtpModel smsResponse);
    void smsFailure(Response<OtpModel> response);
    void smsError(Throwable ex,int errorCode);
}
