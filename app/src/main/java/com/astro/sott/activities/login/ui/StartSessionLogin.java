package com.astro.sott.activities.login.ui;

import android.content.Context;

import com.astro.sott.callBacks.kalturaCallBacks.KsAnonymousLoginCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsAppTokenCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDevice;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDeviceAddCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsLoginCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsStartSessionCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.LoginCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.AppToken;
import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LoginResponse;
import com.kaltura.client.types.LoginSession;
import com.kaltura.client.types.SessionInfo;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public class StartSessionLogin {

    private final Context context;
    private final KsServices ksServices;
    private LoginCallBack loginCallBack;
    private List<HouseholdDevice> deviceList;

    public StartSessionLogin(Context applicationContext) {
        context = applicationContext;
        ksServices = new KsServices(applicationContext);
        ksServices.clientSetup();
    }

    public void callUserLogin(String userName, String password, LoginCallBack callBack) {
        loginCallBack = callBack;
        ksServices.login(userName, password, new KsLoginCallBack() {
            @Override
            public void success(boolean success, String message, Response<LoginResponse> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        KsPreferenceKey.getInstance(context).setUserLoginKs(response.results.getLoginSession().getKs());
                        callAddToken(ksServices, loginCallBack);
                    } else {
                        loginCallBack.loginProcess(false, 1, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(false, 1, deviceList);
                }
            }

            @Override
            public void failure(boolean failure, String message, Response<LoginResponse> response) {

            }
        });
    }

    private void callAddToken(final KsServices ksServices, final LoginCallBack loginCallBack) {
        ksServices.addToken(KsPreferenceKey.getInstance(context).getStartSessionKs(), new KsAppTokenCallBack() {
            @Override
            public void success(boolean sucess, Response<AppToken> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        KsPreferenceKey.getInstance(context).setTokenId(response.results.getId() + "");
                        KsPreferenceKey.getInstance(context).setToken(response.results.getToken() + "");
                        anonymousLogin(ksServices);
                    } else {
                        loginCallBack.loginProcess(false, 2, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(false, 2, deviceList);
                }

            }

            @Override
            public void failure(boolean failure, Response<AppToken> result) {

            }
        });

    }

    private void anonymousLogin(final KsServices ksServices) {
        ksServices.callanonymousLogin(SharedPrefHelper.getInstance(context), new KsAnonymousLoginCallBack() {
            @Override
            public void success(boolean success, Response<LoginSession> response) {

                if (response.isSuccess()) {
                    if (response.results != null) {
                        callStartSession(ksServices);
                    } else {
                        loginCallBack.loginProcess(false, 3, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(false, 3, deviceList);
                }


            }

            @Override
            public void failure(boolean failure, Response<LoginSession> result) {

            }
        });
    }

    private void callStartSession(final KsServices ksServices) {
        ksServices.callStartSession(SharedPrefHelper.getInstance(context), new KsStartSessionCallBack() {
            @Override
            public void success(boolean success, Response<SessionInfo> response) {

                if (response.isSuccess()) {
                    if (response.results != null) {
                        KsPreferenceKey.getInstance(context).setStartSessionKs(response.results.getKs());
                        callGetHouseHold();
                    } else {
                        loginCallBack.loginProcess(false, 4, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(false, 4, deviceList);
                }
            }
            @Override
            public void failure(boolean failure, Response<SessionInfo> result) {

            }
        });
    }

    private void callGetHouseHold() {
        ksServices.callGetHouseHold((status, response) -> {
            if (status) {
                callHouseHoldList(ksServices, loginCallBack);
            } else {
                loginCallBack.loginProcess(false, 5, deviceList);
            }
        });
    }

    private void callHouseHoldList(final KsServices ksServices, final LoginCallBack callBack) {
        loginCallBack = callBack;
        ksServices.callHouseHoldList(SharedPrefHelper.getInstance(context), new KsHouseHoldDevice() {
            @Override
            public void success(boolean sucess, final Response<ListResponse<HouseholdDevice>> response) {
                String deviceLimit = KsPreferenceKey.getInstance(context).getHouseHoldDeviceLimit();
                PrintLogging.printLog(this.getClass(), "", "sizeOflist" + response.isSuccess() + "-->>" + deviceLimit);
                if (response.isSuccess()) {
                    if (response.results != null) {

                        // String deviceLimit = KsPreferenceKey.getInstances(context).getHouseHoldDeviceLimit();
                        PrintLogging.printLog(this.getClass(), "", "sizeOflist" + response.results.getTotalCount() + "-->>" + deviceLimit);
                        int size = response.results.getObjects().size();
                        if (deviceLimit.equals(String.valueOf(size))) {
                            loginCallBack.loginProcess(true, 6, response.results.getObjects());
                        } else {
                            // loginCallBack.loginProcess(true,6,response.results.getObjects());
                            callAddHouseHoldDevice(ksServices);
                        }
                    } else {
                        loginCallBack.loginProcess(false, 6, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(false, 6, deviceList);
                }


            }

            @Override
            public void failure(boolean failure, Response<ListResponse<HouseholdDevice>> result) {
                loginCallBack.loginProcess(false, 6, deviceList);
            }
        });

    }


    private void callAddHouseHoldDevice(final KsServices ksServices) {

        ksServices.addHouseHoldDevice(SharedPrefHelper.getInstance(context), new KsHouseHoldDeviceAddCallBack() {
            @Override
            public void success(boolean sucess, Response<HouseholdDevice> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        loginCallBack.loginProcess(true, 7, deviceList);
                    } else {
                        loginCallBack.loginProcess(sucess, 7, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(sucess, 7, deviceList);
                }

                //callGetHouseHold(ksServices);
                //callDeleteHouseHold(ksServices);

            }

            @Override
            public void failure(boolean failure, String errorCode, String message, Response<HouseholdDevice> result) {

                loginCallBack.loginProcess(result.isSuccess(), 7, deviceList);
            }
        });

    }

    public void callDeviceList(final KsServices ksServices, final LoginCallBack callBack) {
        loginCallBack = callBack;
        ksServices.callHouseHoldList(SharedPrefHelper.getInstance(context), new KsHouseHoldDevice() {
            @Override
            public void success(boolean sucess, final Response<ListResponse<HouseholdDevice>> response) {
                PrintLogging.printLog(this.getClass(), "", "sizeOflist" + response.isSuccess());
                if (response.isSuccess()) {
                    if (response.results != null) {
                        PrintLogging.printLog(this.getClass(), "", "sizeOflist" + response.results.getTotalCount());
                        loginCallBack.loginProcess(true, 6, response.results.getObjects());
                    } else {
                        loginCallBack.loginProcess(false, 6, deviceList);
                    }
                } else {
                    loginCallBack.loginProcess(false, 6, deviceList);
                }


            }

            @Override
            public void failure(boolean failure, Response<ListResponse<HouseholdDevice>> result) {
                loginCallBack.loginProcess(false, 6, deviceList);
            }
        });

    }

}
