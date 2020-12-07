package com.dialog.dialoggo.callBacks.otpCallbacks;

import com.dialog.dialoggo.modelClasses.OtpModel;

import retrofit2.Call;
import retrofit2.Response;

public interface OtpVerificationCallback {
    void otpVerified(OtpModel otpModel);
    void otpUnauthorized(Response<OtpModel> response);
    void onError(Throwable t);
}
