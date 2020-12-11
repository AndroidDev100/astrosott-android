package com.astro.sott.callBacks.otpCallbacks;

import com.astro.sott.modelClasses.OtpModel;

import retrofit2.Response;

public interface OtpVerificationCallback {
    void otpVerified(OtpModel otpModel);
    void otpUnauthorized(Response<OtpModel> response);
    void onError(Throwable t);
}
