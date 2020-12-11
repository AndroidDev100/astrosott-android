package com.astro.sott.baseModel;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.astro.sott.R;
import com.astro.sott.activities.login.ui.StartSessionLogin;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.utils.response.base.Response;

public class Watchlist {

    public void listWatchlist(final String assetId, final Context context, final MutableLiveData<CommonResponse> commonMutableLiveData) {

        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(context);
        ksServices.compareWatchlist((status, errorCode, result) -> {
            if (status) {
               /* commonResponse.setPersonalLists(result.results.getObjects());

                commonMutableLiveData.postValue(commonResponse);*/

                checkAssetAdded(result, commonMutableLiveData, assetId, commonResponse);
            } else {
                if (errorCode.equals(AppLevelConstants.KS_EXPIRE)) {
                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
//                                    PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                            listWatchlist(assetId, context, commonMutableLiveData);
                        } else {
                            commonResponse.setStatus(false);
                            commonResponse.setIsAssetAdded(0);
                            if (result.error != null) {
                                commonResponse.setMessage(result.error.getMessage());
                            } else {
                                commonResponse.setMessage(context.getResources().getString(R.string.something_went_wrong));
                            }

                            commonMutableLiveData.postValue(commonResponse);
                        }
                    });
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setIsAssetAdded(0);
                    if (result.error != null) {
                        commonResponse.setMessage(result.error.getMessage());
                    } else {
                        commonResponse.setMessage(context.getResources().getString(R.string.something_went_wrong));
                    }

                    commonMutableLiveData.postValue(commonResponse);
                }

            }
        });

    }




    public void checkWatchlist(final String assetId, final Context context, final MutableLiveData<CommonResponse> commonMutableLiveData) {

        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(context);
        ksServices.compareWatchlist((status, errorCode, result) -> {
            if (status) {
                checkAssetAdded(result, commonMutableLiveData, assetId, commonResponse);
            } else {
                if (errorCode.equals(AppLevelConstants.KS_EXPIRE)) {
                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
//                                    PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                            checkWatchlist(assetId, context, commonMutableLiveData);
                        } else {
                            commonResponse.setStatus(false);
                            commonResponse.setIsAssetAdded(0);
                            if (result.error != null) {
                                commonResponse.setMessage(result.error.getMessage());
                            } else {
                                commonResponse.setMessage(context.getResources().getString(R.string.something_went_wrong));
                            }

                            commonMutableLiveData.postValue(commonResponse);
                        }
                    });
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setIsAssetAdded(0);
                    if (result.error != null) {
                        commonResponse.setMessage(result.error.getMessage());
                    } else {
                        commonResponse.setMessage(context.getResources().getString(R.string.something_went_wrong));
                    }

                    commonMutableLiveData.postValue(commonResponse);
                }

            }
        });

    }

    private void checkAssetAdded(Response<ListResponse<PersonalList>> result,
                                 MutableLiveData<CommonResponse> stringMutableLiveData,
                                 String assetId, CommonResponse commonResponse) {
        String results = "";
            if (result.results.getTotalCount() > 0) {
                for (int i = 0; i < result.results.getObjects().size(); i++) {
                    String ksqlassetid = result.results.getObjects().get(i).getKsql();
                    Log.d("dssdsdsdsdsdsds", ksqlassetid + "--->>" + assetId);

                    if (ksqlassetid.equals(assetId)) {
                        PrintLogging.printLog(this.getClass(), "", "compareCondition" + ksqlassetid + "-->.." + assetId);
                        results = result.results.getObjects().get(i).getId().toString();
                    }
                }
            }
            if (results.equals("")) {
                commonResponse.setStatus(false);
                commonResponse.setPersonalLists(result.results.getObjects());
                commonResponse.setIsAssetAdded(0);
                stringMutableLiveData.postValue(commonResponse);
            } else {
                commonResponse.setStatus(true);
                commonResponse.setIsAssetAdded(1);
                commonResponse.setAssetID(results);
                stringMutableLiveData.postValue(commonResponse);
            }
    }


    public void addToWatchList(final String id, String titleName, Context context, final MutableLiveData<CommonResponse> stringMutableLiveData,int playlistidtype) {
        KsServices ksServices = new KsServices(context);
        final CommonResponse commonResponse = new CommonResponse();
        callWatchListApi(id, titleName, context, stringMutableLiveData, commonResponse, ksServices,playlistidtype);

    }

    private void callWatchListApi(final String id, final String titleName, final Context context,
                                  final MutableLiveData<CommonResponse> stringMutableLiveData,
                                  final CommonResponse commonResponse, final KsServices ksServices,int playlistidtype) {

        ksServices.addToWatchlist(playlistidtype,id, titleName, (ids, errorCode, message) -> {
            if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status, apiType, list) -> {
                    if (status) {
//                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                        callWatchListApi(id, titleName, context, stringMutableLiveData, commonResponse, ksServices,playlistidtype);
                    }
                });
            } else {
                checkWatchlistAddedCondition(ids, errorCode, stringMutableLiveData, commonResponse, new ErrorCallBack().ErrorMessage(errorCode,message));

            }

        });

    }

    private void checkWatchlistAddedCondition(final String id, String errorCode,
                                              MutableLiveData<CommonResponse> stringMutableLiveData,
                                              CommonResponse commonResponse, String message) {

        if (id.equals("")) {
            commonResponse.setStatus(false);
            commonResponse.setErrorCode(errorCode);
            commonResponse.setMessage(message);
        } else {
            commonResponse.setStatus(true);
            commonResponse.setErrorCode("");
            commonResponse.setMessage("");
            commonResponse.setAssetID(id);
        }

        stringMutableLiveData.postValue(commonResponse);
    }
}
