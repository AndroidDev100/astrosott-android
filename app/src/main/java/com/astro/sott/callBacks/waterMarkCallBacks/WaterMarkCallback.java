package com.astro.sott.callBacks.waterMarkCallBacks;

import com.astro.sott.modelClasses.WaterMark.WaterMarkModel;

public interface WaterMarkCallback {
    void onSuccess(WaterMarkModel waterMarkModel);
    void onError(Throwable t);
}
