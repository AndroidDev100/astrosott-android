package com.dialog.dialoggo.callBacks.commonCallBacks;

import com.dialog.dialoggo.modelClasses.DTVContactInfoModel;

import retrofit2.Response;

public interface HungamaResponse {
    void onSuccess(String url);
    void onFailureFailure();
    void onError(Throwable ex);
}
