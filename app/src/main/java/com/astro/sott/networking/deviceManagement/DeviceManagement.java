package com.astro.sott.networking.deviceManagement;

import android.content.Context;
import android.util.Log;

import com.astro.sott.callBacks.LoginProcessCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDevice;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.R;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDeviceAddCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.UDID;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public class DeviceManagement {
    private final Context context;
    private final KsServices ksServices;

    public DeviceManagement(Context appContext) {
        this.context = appContext;
        ksServices = new KsServices(context);
    }

    public void callGetHouseHold(final CommonResponse commonResponse, final LoginProcessCallBack loginProcessCallBack) {
        Log.e("callGetHouseHold","TRUE");

        ksServices.callGetHouseHold((status, response) -> {
            if (status) {
                callHouseHoldList(commonResponse, loginProcessCallBack);
            } else {
                if (response.getErrorCode() != null) {
                    if (response.getErrorCode().equals("1006")) {
                        callAddHouseHold(commonResponse, loginProcessCallBack);
                    } else {
                        commonResponse.setStatus(false);
                        commonResponse.setMessage(response.getMessage());
                        loginProcessCallBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.getMessage() != null)
                        commonResponse.setMessage(response.getMessage());
                    loginProcessCallBack.response(commonResponse);
                }
            }
        });
    }

    int deviceLimi=0;
    private void callHouseHoldList(final CommonResponse commonResponse, final LoginProcessCallBack loginProcessCallBack) {
        try {
            ksServices.callHouseHoldList(SharedPrefHelper.getInstance(context), new KsHouseHoldDevice() {
                @Override
                public void success(boolean sucess, final Response<ListResponse<HouseholdDevice>> response) {
                    String deviceLimit = KsPreferenceKey.getInstance(context).getHouseHoldDeviceLimit();
                    if (response.isSuccess()) {
                        if (response.results != null) {
                            int size = response.results.getObjects().size();
                            boolean status = checkDeviceAddedOrNot(loginProcessCallBack, response, commonResponse);
                            if (!status) {
                                if (!deviceLimit.equalsIgnoreCase("")){
                                    deviceLimi=Integer.parseInt(deviceLimit);
                                }
                                if (size>=deviceLimi) {
                                    commonResponse.setStatus(false);
                                    commonResponse.setIsDeviceAdded(1);
                                    commonResponse.setMessage("");
                                    loginProcessCallBack.response(commonResponse);
                                } else {
                                    callAddHouseHoldDevice(commonResponse, loginProcessCallBack);
                                }
                            } else {
                                commonResponse.setStatus(true);
                                loginProcessCallBack.response(commonResponse);
                            }

                        } else {
                            commonResponse.setStatus(false);
                            if (response.error != null) {
                                commonResponse.setMessage(response.error.getMessage());
                            }
                            loginProcessCallBack.response(commonResponse);
                        }
                    } else {
                        if (response.error != null) {
                            if (response.error.getCode().equals("1006")) {
                                callAddHouseHold(commonResponse, loginProcessCallBack);
                            } else {
                                commonResponse.setStatus(false);
                                commonResponse.setMessage(response.error.getMessage());
                                loginProcessCallBack.response(commonResponse);
                            }
                        }
                    }
                }

                @Override
                public void failure(boolean failure, Response<ListResponse<HouseholdDevice>> response) {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    loginProcessCallBack.response(commonResponse);
                }
            });
        }catch (Exception ignored){
            commonResponse.setStatus(false);
            if (context!=null){
                commonResponse.setMessage(context.getString(R.string.something_went_wrong));
            }
            loginProcessCallBack.response(commonResponse);
        }


    }

    private boolean checkDeviceAddedOrNot(LoginProcessCallBack loginCallBack,
                                          Response<ListResponse<HouseholdDevice>> response,
                                          CommonResponse commonResponse) {
        boolean deviceAdded = false;
        String deviceUDID = UDID.getDeviceId(context, context.getContentResolver());
        // String deviceUDID= "123457";
        for (int i = 0; i < response.results.getObjects().size(); i++) {
            if (response.results.getObjects().get(i).getUdid().equals(deviceUDID)) {
                deviceAdded = true;
                PrintLogging.printLog(this.getClass(), "", "DeviceAlreadyAdded");
            }
        }
        return deviceAdded;
    }

    private void callAddHouseHoldDevice(final CommonResponse commonResponse, final LoginProcessCallBack loginProcessCallBack) {
        ksServices.addHouseHoldDevice(SharedPrefHelper.getInstance(context), new KsHouseHoldDeviceAddCallBack() {
            @Override
            public void success(boolean sucess, Response<HouseholdDevice> response) {
                if (response.isSuccess()) {
                    if (response.results != null) {
                        Log.e("Device Addded","TRUE");
                        commonResponse.setStatus(true);
                        commonResponse.setMessage("Device Added");
                        callGetHouseHold(commonResponse, loginProcessCallBack);
                    } else {
                        commonResponse.setStatus(false);
                        if (response.error != null) {
                            commonResponse.setMessage(response.error.getMessage());
                        }
                        loginProcessCallBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    loginProcessCallBack.response(commonResponse);
                }

            }

            @Override
            public void failure(boolean failure, String errorCode, String message, Response<HouseholdDevice> response) {
                if (errorCode.equalsIgnoreCase(AppLevelConstants.DEVICE_EXISTS)) {
                    commonResponse.setStatus(true);
                    loginProcessCallBack.response(commonResponse);
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setMessage(response.error.getMessage());
                    loginProcessCallBack.response(commonResponse);
                }

            }
        });

    }


    public void callDeviceList(final String from, final LoginProcessCallBack loginProcessCallBack) {
        final CommonResponse commonResponse = new CommonResponse();
        new KsServices(context).callHouseHoldList(SharedPrefHelper.getInstance(context), new KsHouseHoldDevice() {
            @Override
            public void success(boolean sucess, final Response<ListResponse<HouseholdDevice>> response) {
                PrintLogging.printLog(this.getClass(), "", "sizeOflist" + response.isSuccess());
                String deviceLimit = KsPreferenceKey.getInstance(context).getHouseHoldDeviceLimit();
                if (response.isSuccess()) {
                    if (response.results != null) {
                        int size = response.results.getObjects().size();
                        boolean status = checkDeviceAddedOrNot(loginProcessCallBack, response, commonResponse);
                        if (!status) {
                            if (!deviceLimit.equalsIgnoreCase("")){
                                deviceLimi=Integer.parseInt(deviceLimit);
                            }
                            if (size>=deviceLimi) {
                                commonResponse.setStatus(false);
                                commonResponse.setIsDeviceAdded(100);
                                commonResponse.setMessage("");
                                commonResponse.setDeviceList(response.results.getObjects());
                                loginProcessCallBack.response(commonResponse);
                            } else {

                                if (from.equalsIgnoreCase("")) {
                                    commonResponse.setStatus(true);
                                    commonResponse.setDeviceList(response.results.getObjects());
                                    loginProcessCallBack.response(commonResponse);
                                } else {
                                    callAddHouseHoldDevice(commonResponse, loginProcessCallBack);
                                }

                            }
                        } else {
                            commonResponse.setStatus(true);
                            commonResponse.setDeviceList(response.results.getObjects());
                            loginProcessCallBack.response(commonResponse);
                        }

                    } else {
                        commonResponse.setStatus(false);
                        if (response.error != null) {
                            commonResponse.setMessage(response.error.getMessage());
                        }
                        loginProcessCallBack.response(commonResponse);
                    }
                } else {
                    commonResponse.setStatus(false);
                    if (response.error != null) {
                        commonResponse.setMessage(response.error.getMessage());
                    }
                    loginProcessCallBack.response(commonResponse);
                }


            }

            @Override
            public void failure(boolean failure, Response<ListResponse<HouseholdDevice>> response) {
                PrintLogging.printLog(this.getClass(), "", "ksExpireError" + response.error.getCode() + "--->>" + response.error.getMessage());

                commonResponse.setStatus(false);
                if (response.error != null) {
                    commonResponse.setMessage(response.error.getMessage());
                    commonResponse.setErrorCode(response.error.getCode());
                }
                loginProcessCallBack.response(commonResponse);
            }
        });
    }

    public void callAddHouseHold(final CommonResponse commonResponse, final LoginProcessCallBack loginProcessCallBack) {
        ksServices.addHouseHold((status, message) -> {
            if (status) {
                commonResponse.setStatus(true);
                callGetHouseHold(commonResponse, loginProcessCallBack);
            } else {
                commonResponse.setStatus(false);
                commonResponse.setMessage(message);
                loginProcessCallBack.response(commonResponse);
            }
        });

    }

}
