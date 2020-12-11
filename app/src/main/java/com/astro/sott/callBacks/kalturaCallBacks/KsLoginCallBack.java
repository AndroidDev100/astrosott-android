package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.LoginResponse;
import com.kaltura.client.utils.response.base.Response;

public interface KsLoginCallBack {

    void success(boolean sucess, String message, Response<LoginResponse> response);
    void failure(boolean failure, String message, Response<LoginResponse> response);
}
