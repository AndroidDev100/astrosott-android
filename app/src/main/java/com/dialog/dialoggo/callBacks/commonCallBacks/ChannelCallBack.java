package com.dialog.dialoggo.callBacks.commonCallBacks;

import com.dialog.dialoggo.beanModel.login.CommonResponse;

public interface ChannelCallBack {

    void response(boolean status, CommonResponse response);
}
