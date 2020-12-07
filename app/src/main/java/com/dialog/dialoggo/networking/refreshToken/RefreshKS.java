package com.dialog.dialoggo.networking.refreshToken;

import android.content.Context;
import android.util.Log;

import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsAnonymousLoginCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsStartSessionCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.RefreshTokenCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.SharedPrefHelper;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.LoginSession;
import com.kaltura.client.types.SessionInfo;
import com.kaltura.client.utils.response.base.Response;

public class RefreshKS {
    private final Context context;
    private final KsServices ksServices;
    private RefreshTokenCallBack refreshTokenCallBack;

    public RefreshKS(Context appContext) {
        this.context = appContext;
        ksServices = new KsServices(context);
    }

    public void refreshKS(RefreshTokenCallBack callBack) {
        refreshTokenCallBack = callBack;
        final CommonResponse commonResponse = new CommonResponse();
        ksServices.callanonymousLogin(SharedPrefHelper.getInstance(context), new KsAnonymousLoginCallBack() {
            @Override
            public void success(boolean success, Response<LoginSession> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        callStartSession(refreshTokenCallBack, commonResponse);
                    } else {
                        commonResponse.setStatus(false);
                        if (response.error != null) {
                            commonResponse.setMessage(response.error.getMessage());
                        }
                        refreshTokenCallBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    refreshTokenCallBack.response(commonResponse);
                }
            }

            @Override
            public void failure(boolean failure, Response<LoginSession> response) {
                commonResponse.setStatus(false);
                if (response.error != null) {
                    commonResponse.setMessage(response.error.getMessage());
                }
                refreshTokenCallBack.response(commonResponse);
            }
        });
    }

    private void callStartSession(final RefreshTokenCallBack callBack, final CommonResponse commonResponse) {
        ksServices.refreshStartSessionKs(SharedPrefHelper.getInstance(context), new KsStartSessionCallBack() {
            @Override
            public void success(boolean success, Response<SessionInfo> response) {

                if (response.isSuccess()) {
                    if (response.results != null) {
                        Log.w("startSession", response.results.getKs());
                        KsPreferenceKey.getInstance(context).setStartSessionKs(response.results.getKs());
                        commonResponse.setStatus(true);
                        callBack.response(commonResponse);
                    } else {
                        commonResponse.setStatus(false);
                        if (response.error != null) {
                            commonResponse.setMessage(response.error.getMessage());
                        }
                        callBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    callBack.response(commonResponse);
                }

            }

            @Override
            public void failure(boolean failure, Response<SessionInfo> response) {
                commonResponse.setStatus(false);
                if (response.error != null) {
                    commonResponse.setMessage(response.error.getMessage());
                }
                callBack.response(commonResponse);
            }
        });
    }
}
