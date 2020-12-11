package com.astro.sott.callBacks.commonCallBacks;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;

public interface CatchupCallBack {
    void catchupCallback(String url, RailCommonData commonData,String programName);
}
