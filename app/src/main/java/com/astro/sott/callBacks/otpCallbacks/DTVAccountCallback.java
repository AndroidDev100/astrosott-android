package com.astro.sott.callBacks.otpCallbacks;

import com.astro.sott.modelClasses.DTVContactInfoModel;

import retrofit2.Response;

public interface DTVAccountCallback {
    void dtvSuccess(DTVContactInfoModel dtvResponse);
    void dtvFailure(Response<DTVContactInfoModel> response);
    void dtvError(Throwable ex);
}
