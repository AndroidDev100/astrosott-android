package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.APIException;

public interface SignUpCallBack {
    void signupStatus(boolean status, String message, APIException error);
}
