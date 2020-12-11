package com.astro.sott.callBacks.commonCallBacks;

import com.astro.sott.beanModel.login.CommonResponse;

public interface ChannelCallBack {

    void response(boolean status, CommonResponse response);
}
