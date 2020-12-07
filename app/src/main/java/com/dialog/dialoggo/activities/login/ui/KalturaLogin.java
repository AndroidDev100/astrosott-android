package com.dialog.dialoggo.activities.login.ui;

import android.content.Context;
import android.util.Log;

import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.LoginProcessCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsAnonymousLoginCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsAppTokenCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsHouseHoldDevice;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsHouseHoldDeviceAddCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsLoginCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.KsStartSessionCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.LoginCallBack;
import com.dialog.dialoggo.networking.deviceManagement.DeviceManagement;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.SharedPrefHelper;
import com.dialog.dialoggo.utils.helpers.UDID;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.AppToken;
import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LoginResponse;
import com.kaltura.client.types.LoginSession;
import com.kaltura.client.types.SessionInfo;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public class KalturaLogin {
    private final Context context;
    private final KsServices ksServices;
    private  LoginCallBack loginCallBack;
    private List<HouseholdDevice> deviceList;
    private LoginProcessCallBack loginProcessCallBack;

    public KalturaLogin(Context applicationContext) {
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
                        callAddToken(loginCallBack);
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

    public void callAddToken(final LoginCallBack loginCallBack) {
        ksServices.addToken(KsPreferenceKey.getInstance(context).getUserLoginKs(), new KsAppTokenCallBack() {
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
                        Log.w("startSession", response.results.getKs());
                        KsPreferenceKey.getInstance(context).setStartSessionKs(response.results.getKs());
                        callGetHouseHold(ksServices);
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

    private void callGetHouseHold(final KsServices ksServices) {
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
                if (response.isSuccess()) {
                    if (response.results != null) {

                        // String deviceLimit = KsPreferenceKey.getInstances(context).getHouseHoldDeviceLimit();
                        int size = response.results.getObjects().size();
                        if (deviceLimit.equals(String.valueOf(size))) {
                            // loginCallBack.loginProcess(false,6,response.results.getObjects());
                            checkDeviceAddedOrNot(loginCallBack, response);
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

    private void checkDeviceAddedOrNot(LoginCallBack loginCallBack, Response<ListResponse<HouseholdDevice>> response) {
        boolean deviceAdded = false;
        String deviceUDID = UDID.getDeviceId(context, context.getContentResolver());
        for (int i = 0; i < response.results.getObjects().size(); i++) {
            if (response.results.getObjects().get(i).getUdid().equals(deviceUDID)) {
                loginCallBack.loginProcess(true, 6, response.results.getObjects());
                deviceAdded = true;
            }
        }

        if (!deviceAdded) {
            loginCallBack.loginProcess(false, 6, response.results.getObjects());
        }
    }

    private void callAddHouseHoldDevice(final KsServices ksServices) {
            Log.e("KS===>",KsPreferenceKey.getInstance(context).getStartSessionKs());
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
            public void failure(boolean failure, String errorcode, String message, Response<HouseholdDevice> result) {
                loginCallBack.loginProcess(result.isSuccess(), 7, deviceList);
            }
        });

    }

    public void callDeviceList(final KsServices ksServices, final LoginCallBack callBack) {
        loginCallBack = callBack;
        ksServices.callHouseHoldList(SharedPrefHelper.getInstance(context), new KsHouseHoldDevice() {
            @Override
            public void success(boolean sucess, final Response<ListResponse<HouseholdDevice>> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
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

    public void callKalturaUserLogin(String userName, String password, boolean viaRegistration, LoginProcessCallBack callBack) {
        final CommonResponse commonResponse = new CommonResponse();
        loginProcessCallBack = callBack;
        ksServices.login(userName, password, new KsLoginCallBack() {
            @Override
            public void success(boolean success, String message, Response<LoginResponse> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        commonResponse.setStatus(true);
                        KsPreferenceKey.getInstance(context).setUser(response.results.getUser());
                        KsPreferenceKey.getInstance(context).setStartSessionKs(response.results.getLoginSession().getKs());


                       // KsPreferenceKey.getInstance(context).setUserRegistered(isUserRegistered);

                        callAddToken(viaRegistration, loginProcessCallBack, commonResponse);
                    } else {
                        commonResponse.setStatus(false);
                        if (response.error != null) {
                            commonResponse.setErrorCode(response.error.getCode());
                            commonResponse.setMessage(response.error.getMessage());
                        }
                        loginProcessCallBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setErrorCode(response.error.getCode());
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    loginProcessCallBack.response(commonResponse);
                }
            }

            @Override
            public void failure(boolean failure, String message, Response<LoginResponse> response) {
                commonResponse.setStatus(false);
                if (response.error != null) {
                    commonResponse.setErrorCode(response.error.getCode());
                    commonResponse.setMessage(response.error.getMessage());
                }
                loginProcessCallBack.response(commonResponse);
            }
        });
    }

    public void callAddToken(boolean viaRegistration, final LoginProcessCallBack loginCallBack,
                              final CommonResponse commonResponse) {
        ksServices.addToken(KsPreferenceKey.getInstance(context).getStartSessionKs(), new KsAppTokenCallBack() {
            @Override
            public void success(boolean sucess, Response<AppToken> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        commonResponse.setStatus(true);
                        KsPreferenceKey.getInstance(context).setTokenId(response.results.getId() + "");
                        KsPreferenceKey.getInstance(context).setToken(response.results.getToken() + "");
                        PrintLogging.printLog("PrintLogginADDT", "", "" + viaRegistration);
                        if (viaRegistration) {
                            new DeviceManagement(context).callAddHouseHold(commonResponse, loginCallBack);
                        } else {
                            new DeviceManagement(context).callGetHouseHold(commonResponse, loginCallBack);
                        }
                    } else {
                        commonResponse.setStatus(false);
                        if (response.error != null) {
                            commonResponse.setMessage(response.error.getMessage());
                        }
                        loginCallBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    loginCallBack.response(commonResponse);
                }
            }

            @Override
            public void failure(boolean failure, Response<AppToken> response) {
                commonResponse.setStatus(false);
                if (response.error != null) {
                    commonResponse.setMessage(response.error.getMessage());
                }
                loginCallBack.response(commonResponse);
            }
        });

    }


//    public void callKalturaUserSignUp(LoginProcessCallBack callBack) {
//        final CommonResponse commonResponse = new CommonResponse();
//        loginProcessCallBack = callBack;
//        ksServices.login(new KsLoginCallBack() {
//            @Override
//            public void success(boolean success, String message, Response<LoginResponse> response) {
//                if (response.isSuccess()) {
//                    if (response.results != null) {
//                        commonResponse.setStatus(true);
//
//                        KsPreferenceKey.getInstance(context).setUser(response.results.getUser());
//                        KsPreferenceKey.getInstance(context).setStartSessionKs(response.results.getLoginSession().getKs());
//                        callAddToken(ksServices, loginProcessCallBack, commonResponse);
//                    } else {
//                        commonResponse.setStatus(false);
//                        if (response.error != null) {
//                            commonResponse.setMessage(response.error.getMessage());
//                        }
//                        loginProcessCallBack.response(commonResponse);
//
//                    }
//                } else {
//                    commonResponse.setStatus(false);
//                    if (response.error != null) {
//                        commonResponse.setMessage(response.error.getMessage());
//                    }
//                    loginProcessCallBack.response(commonResponse);
//                }
//            }
//
//            @Override
//            public void failure(boolean failure, String message, Response<LoginResponse> response) {
//                commonResponse.setStatus(false);
//                if (response.error != null) {
//                    commonResponse.setMessage(response.error.getMessage());
//                }
//                loginProcessCallBack.response(commonResponse);
//            }
//        });
//    }


}
