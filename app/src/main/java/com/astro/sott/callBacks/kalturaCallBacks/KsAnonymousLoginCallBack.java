package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.LoginSession;
import com.kaltura.client.utils.response.base.Response;

public interface KsAnonymousLoginCallBack {

    void success(boolean sucess, Response<LoginSession> result);
    void failure(boolean failure, Response<LoginSession> result);
}
