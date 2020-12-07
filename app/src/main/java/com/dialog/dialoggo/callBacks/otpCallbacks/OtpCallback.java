package com.dialog.dialoggo.callBacks.otpCallbacks;

import com.dialog.dialoggo.modelClasses.OtpModel;

import retrofit2.Call;
import retrofit2.Response;

public interface OtpCallback {
    void smsReceived(OtpModel smsResponse);
    void smsFailure(Response<OtpModel> response);
    void smsError(Throwable ex,int errorCode);
}
