package com.astro.sott.networking.retrofit;


import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.activities.SelectAccount.SelectAccountModel.Response;
import com.astro.sott.modelClasses.DTVContactInfoModel;
import com.astro.sott.modelClasses.OtpModel;
import com.astro.sott.modelClasses.TokenModel;
import com.astro.sott.modelClasses.playbackContext.PlaybackContextResponse;
import com.astro.sott.utils.constants.AppConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("configurations/action/serveByDevice")
    Call<ResponseDmsModel> getDMS(@Body JsonObject apiVersion);


    @POST("asset/action/getPlaybackContext")
    Call<PlaybackContextResponse> getPlaybackContext(@Body JsonObject apiVersion);

    @POST("asset/action/getPlaybackContext")
    Call<PlaybackContextResponse> getAdContext(@Body JsonObject apiVersion);


    @GET(AppConstants.SMS_API_END_POINT)
    Call<OtpModel> getmPin(@Query("number") String msisdn);

    @POST(AppConstants.SMS_API_END_POINT)
    Call<OtpModel> sendOTP(@Body JsonObject msisdn);

    @POST(AppConstants.VERIFY_OTP)
    Call<OtpModel> verifyPin(@Body JsonObject verifyOtpPayload);

    @POST(AppConstants.DTV_ACC_NUM)
    Call<DTVContactInfoModel> getDTVContactInfo(@Body JsonObject jsonObject);

    @POST(AppConstants.CONNETION_DETAILS)
    Call<Response> getConnectionDetails(@Body JsonObject jsonObject);

    @GET("cd_service_metasea2_dialogviu_stream_url_api.php?")
    Call<JsonElement> getHungama(@Query("content_id") String content_id, @Query("user_id") String user_id, @Header("PRODUCT") String product, @Header("APP-KEY") String app_key, @Header("deviceId") String deviceId);


    @GET("QA/getAPIToken_QA")
    Call<TokenModel> getToken();

    @GET(".")
    Call<OtpModel> getMsisdn();
}
