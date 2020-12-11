package com.astro.sott.activities.login.ui;

import android.content.Context;

import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDevice;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDeviceAddCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.LoginCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceHold {
    private final Context context;
    private final KsServices ksServices;
    private LoginCallBack loginCallBack;
    private final List<HouseholdDevice> deviceList;

    public AddDeviceHold(Context applicationContext) {
        context = applicationContext;
        ksServices = new KsServices(applicationContext);
        ksServices.clientSetup();
        deviceList = new ArrayList<>();
    }

    public void callGetHouseHold(LoginCallBack callBack) {
        loginCallBack = callBack;


        ksServices.callGetHouseHold((status, response) -> {

            if (status) {
                callHouseHoldList(ksServices, loginCallBack);
            } else {
                loginCallBack.loginProcess(false, 5, deviceList);
            }
        });

//        ksServices.callGetHouseHold(SharedPrefHelper.getInstance(context), new KsHouseHoldIdCallBack() {
//            @Override
//            public void success(boolean sucess, Response<Household> response) {
//                if (response.isSuccess()) {
//                    if (response.results != null) {
//
//                        callHouseHoldList(ksServices, loginCallBack);
//                    } else {
//                        loginCallBack.loginProcess(false, 5, deviceList);
//                    }
//                } else {
//                    loginCallBack.loginProcess(false, 5, deviceList);
//                }
//
//            }
//
//            @Override
//            public void failure(boolean failure, Response<Household> result) {
//
//            }
//        });
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
}
