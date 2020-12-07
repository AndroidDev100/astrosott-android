package com.dialog.dialoggo.callBacks.otpCallbacks;

import com.dialog.dialoggo.modelClasses.DTVContactInfoModel;
import com.dialog.dialoggo.modelClasses.OtpModel;

import retrofit2.Response;

public interface DTVAccountCallback {
    void dtvSuccess(DTVContactInfoModel dtvResponse);
    void dtvFailure(Response<DTVContactInfoModel> response);
    void dtvError(Throwable ex);
}
