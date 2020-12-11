package com.astro.sott.repositories.loginRepository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.activities.login.ui.KalturaLogin;
import com.astro.sott.callBacks.LoginProcessCallBack;
import com.astro.sott.callBacks.otpCallbacks.AutoMsisdnCallback;
import com.astro.sott.callBacks.otpCallbacks.DTVAccountCallback;
import com.astro.sott.callBacks.otpCallbacks.OtpCallback;
import com.astro.sott.modelClasses.DTVContactInfoModel;
import com.astro.sott.networking.retrofit.ApiInterface;
import com.astro.sott.networking.retrofit.RequestConfig;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.otpCallbacks.OtpVerificationCallback;
import com.astro.sott.modelClasses.OtpModel;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.constants.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private static LoginRepository loginRepository;

    private LoginRepository() {

    }

    public static synchronized LoginRepository getInstance() {
        if (loginRepository == null) {
            loginRepository = new LoginRepository();
        }
        return loginRepository;
    }

    public LiveData<OtpModel> getMpin(Context context, String msisdn) {

        MutableLiveData<OtpModel> mutableLiveData = new MutableLiveData<>();

        final KsServices ksServices = new KsServices(context);


        ksServices.getmPin(msisdn, context, new OtpCallback() {
            @Override
            public void smsReceived(OtpModel smsResponse) {
                if (smsResponse != null) {
                    mutableLiveData.postValue(smsResponse);
                } else {
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void smsFailure(Response<OtpModel> response) {
                OtpModel otpModel = new OtpModel();
                otpModel.setMessage(response.message());
                Log.e("SMS Failure", "True");
                mutableLiveData.postValue(null);
            }

            @Override
            public void smsError(Throwable ex,int errorCode) {
                OtpModel otpModel = new OtpModel();
                otpModel.setMessage(ex.getMessage());
                otpModel.setResponseCode(errorCode);
                mutableLiveData.postValue(otpModel);
            }
        });

        return mutableLiveData;
    }


    public LiveData<CommonResponse> registerUser(Context context, String userName) {

        MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();

        CommonResponse commonResponse = new CommonResponse();

        KsServices ksServices = new KsServices(context);
        ksServices.signUp(userName, context.getResources().getString(R.string.default_password),
                (status, message, error) -> {
                    try {
                        commonResponse.setStatus(status);
                        if (error.getCode() != null)
                            commonResponse.setErrorCode(error.getCode());
                        commonResponse.setMessage(message);
                        mutableLiveData.postValue(commonResponse);
                    } catch (Exception e) {
                        commonResponse.setMessage(e.getMessage());
                        mutableLiveData.postValue(commonResponse);
                    }
                });

        return mutableLiveData;
    }


    public LiveData<CommonResponse> loginUser(Context context, String userName, boolean viaRegistration) {
        MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KalturaLogin kalturaLogin = new KalturaLogin(context);
        kalturaLogin.callKalturaUserLogin(userName, context.getResources().getString(R.string.default_password), viaRegistration, response -> {
            try {
                mutableLiveData.postValue(response);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
                mutableLiveData.postValue(response);
            }

        });


        return mutableLiveData;
    }


    public LiveData<OtpModel> verifyPin(Context context, String msisdn, String otp, String txnId, Boolean isUserVerified) {
        MutableLiveData<OtpModel> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        ksServices.verifyPin(context, msisdn, otp, txnId, isUserVerified, new OtpVerificationCallback() {
            @Override
            public void otpVerified(OtpModel otpModel) {
                otpModel.setResponseCode(200);
                mutableLiveData.postValue(otpModel);
            }

            @Override
            public void otpUnauthorized(Response<OtpModel> response) {
                OtpModel otpModel = new OtpModel();
                otpModel.setMessage(context.getResources().getString(R.string.incorrect_pin));
                otpModel.setResponseCode(response.code());
                mutableLiveData.postValue(otpModel);
            }

            @Override
            public void onError(Throwable t) {
                OtpModel otpModel = new OtpModel();
                otpModel.setMessage(t.getMessage());
                mutableLiveData.postValue(otpModel);

            }
        });
        return mutableLiveData;
    }

    public LiveData<OtpModel> getMsidn(Context context) {
        MutableLiveData<OtpModel> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        ksServices.getMsisdn(new AutoMsisdnCallback() {
            @Override
            public void msisdnReceived(OtpModel smsResponse) {
                smsResponse.setResponseCode(200);
                mutableLiveData.postValue(smsResponse);
            }

            @Override
            public void msisdnFailure(Response<OtpModel> response) {
                OtpModel otpModel = new OtpModel();
                otpModel.setMessage(response.message());
                otpModel.setResponseCode(response.code());
                mutableLiveData.postValue(otpModel);
            }

            @Override
            public void onError(Throwable throwable) {
                OtpModel otpModel = new OtpModel();
                otpModel.setMessage(throwable.getMessage());
                otpModel.setResponseCode(-1);
                mutableLiveData.postValue(otpModel);
            }
        });
        return mutableLiveData;
    }

    public LiveData<DTVContactInfoModel> getDtvAccountDetail(Context context, String dtvAccountNumber) {
        MutableLiveData<DTVContactInfoModel> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        ksServices.getDtvAccountDetails(dtvAccountNumber, new DTVAccountCallback() {
            @Override
            public void dtvSuccess(DTVContactInfoModel dtvResponse) {
                if (dtvResponse != null) {
                    mutableLiveData.postValue(dtvResponse);
                } else {
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void dtvFailure(Response<DTVContactInfoModel> response) {
                DTVContactInfoModel dtvAccountCallback = new DTVContactInfoModel();
                dtvAccountCallback.setResultDesc(response.message());
                Log.e("SMS Failure", "True");
                mutableLiveData.postValue(dtvAccountCallback);
            }

            @Override
            public void dtvError(Throwable ex) {
                DTVContactInfoModel dtvAccountCallback = new DTVContactInfoModel();
                dtvAccountCallback.setResultDesc(ex.getMessage());
                Log.e("SMS Failure", "True");
                mutableLiveData.postValue(dtvAccountCallback);
            }
        });
        return mutableLiveData;
    }

    public LiveData<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response> getConnectionDetails(String phoneNumber) {
        MutableLiveData<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response> mutableLiveData = new MutableLiveData<>();


        Random random = new Random();
        String randomNumber = String.valueOf(Math.round(random.nextFloat() * Math.pow(10, 12)));
        String traceId = "KAL".concat(randomNumber);
        Log.d("traceID", traceId);

        // ApiInterface endpoint = RequestConfig.getClient(BuildConfig.GET_CONNECTION_DETAIL_BASE_URL).create(ApiInterface.class);
        ApiInterface endpoint = RequestConfig.getDTVClient(AppConstants.CERT_CONNECTION_DETAILS).create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("traceId", traceId);
       // jsonObject.addProperty("status", "C");
        jsonObject.addProperty("requesteduser", AppLevelConstants.REQUESTED_USER);
        jsonObject.addProperty("mobileno", phoneNumber);
        jsonObject.addProperty("channel", AppLevelConstants.CHANNEL);


        Call<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response> call = endpoint.getConnectionDetails(jsonObject);

        call.enqueue(new Callback<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response>() {
            @Override
            public void onResponse(Call<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response> call, Response<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response!=null && response.body() != null) {

                            mutableLiveData.postValue(response.body());
                        } else {
                            mutableLiveData.postValue(null);
                        }
                    }
                } else {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        com.astro.sott.activities.SelectAccount.SelectAccountModel.Response error = new Gson().fromJson(errorBody.charStream(), com.astro.sott.activities.SelectAccount.SelectAccountModel.Response.class);
                        //  Log.e("errorResponseCode", error.getData().);

                        mutableLiveData.postValue(null);

                        // Toast.makeText(context,error.getDebugMessage(),Toast.LENGTH_LONG).show();
                    }
                    //  dtvAccountCallback.dtvFailure(response);
                }
            }

            @Override
            public void onFailure(Call<com.astro.sott.activities.SelectAccount.SelectAccountModel.Response> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
//        call.enqueue(new Callback<com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response>() {
//            @Override
//            public void onResponse(Call<com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response> call, retrofit2.Response<com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response> response) {
//                Log.e("Response Code", String.valueOf(response.code()));
//
//                if (response.isSuccessful()) {
//                    if(response.code() == 200) {
//
//                        mutableLiveData.postValue(response.body());
//                    }
//                }
//                else {
//                    ResponseBody errorBody = response.errorBody();
//                    if (errorBody != null) {
//                        com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response error = new Gson().fromJson(errorBody.charStream(), com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response.class);
//                      //  Log.e("errorResponseCode", error.getData().);
//
//                        mutableLiveData.postValue(error);
//
//                        // Toast.makeText(context,error.getDebugMessage(),Toast.LENGTH_LONG).show();
//                    }
//                  //  dtvAccountCallback.dtvFailure(response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response> call, Throwable t) {
//              //  dtvAccountCallback.dtvError(t);
//            }
//        });
        return mutableLiveData;
    }

    public LiveData<CommonResponse> addToken(Context context,boolean viaRegistration) {
        MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KalturaLogin kalturaLogin = new KalturaLogin(context);
            kalturaLogin.callAddToken(viaRegistration, new LoginProcessCallBack() {
        @Override
        public void response(CommonResponse response) {
            try {
                mutableLiveData.postValue(response);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
                mutableLiveData.postValue(response);
            }

        }
    }, new CommonResponse());
            return mutableLiveData;
    }

    public void checkUserType(Context context) {

        KsServices ksServices = new KsServices(context);
        ksServices.checkUserType(context);

    }
}
