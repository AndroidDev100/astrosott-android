package com.astro.sott.callBacks.commonCallBacks;

public interface HungamaResponse {
    void onSuccess(String url);
    void onFailureFailure();
    void onError(Throwable ex);
}
