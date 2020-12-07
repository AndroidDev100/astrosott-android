package com.dialog.dialoggo.activities.liveChannel.liveChannelManager;

import android.content.Context;
import android.util.Log;

import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.commonCallBacks.CheckLiveProgram;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.SeasonCallBack;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.SpecificAssetCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.ProgramAsset;
import com.kaltura.client.utils.response.base.Response;

public class LiveChannelManager {
    private String clickedAssesId;
    private String clickedStartTime;

    public void getLiveProgram(Context context, Asset asset, final CheckLiveProgram callBack) {
        if(asset!=null) {
            clickedAssesId = asset.getId().toString();
            clickedStartTime = asset.getStartDate().toString();
            KsServices ksServices = new KsServices(context);
            ksServices.callSpecificAsset(((ProgramAsset) asset).getLinearAssetId().toString(), new SpecificAssetCallBack() {
                @Override
                public void getAsset(boolean status, Asset asset) {
                    PrintLogging.printLog(this.getClass(), "", "programAsset" + status);
                    if (status && asset != null) {
                        checkProgramLiveOrNot(context, asset, callBack);
                    }

                }
            });
        }
    }

    private void checkProgramLiveOrNot(Context context, Asset railCommonData, CheckLiveProgram callBack) {
        MediaAsset mediaAsset = (MediaAsset) railCommonData;
        String externalIDs = mediaAsset.getExternalIds();
        PrintLogging.printLog(this.getClass(), "", "externalIdddss" + externalIDs);

        callCurrentProgram(context, externalIDs, railCommonData, callBack);

    }

    private void callCurrentProgram(Context context, String externalIDs, Asset railCommonData, final CheckLiveProgram callBack) {
        final CommonResponse commonResponse = new CommonResponse();
        new KsServices(context).callLiveEPGDayWise(externalIDs, "", "", 1,1, new SeasonCallBack() {
            @Override
            public void result(boolean status, Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", result.isSuccess() + "CurrentProgram" + "-->>" + result.isSuccess());
                if (status) {
                    PrintLogging.printLog(this.getClass(), "", result.isSuccess() + "CurrentProgram" + "-->>" + clickedAssesId + "-->>" + result.results.getObjects().get(0).getId());
                    result.results.getObjects().get(0).getStartDate();
                    commonResponse.setStatus(true);
                    commonResponse.setCurrentProgram(result.results.getObjects().get(0));

                    if (clickedAssesId.equalsIgnoreCase(result.results.getObjects().get(0).getId().toString())) {
                        commonResponse.setLivePrograme(true);
                    } else {
                        PrintLogging.printLog(this.getClass(), "", result.isSuccess() + "CurrentProgram" + "-->>" + clickedStartTime + "-->>" + result.results.getObjects().get(0).getStartDate());
                        commonResponse.setLivePrograme(false);
                        if (Integer.parseInt(clickedStartTime) > Integer.parseInt(result.results.getObjects().get(0).getStartDate().toString())) {
                            PrintLogging.printLog(this.getClass(), "", result.isSuccess() + "CurrentProgram if--" + "-->>" + clickedStartTime + "-->>" + result.results.getObjects().get(0).getStartDate());
                            commonResponse.setProgramTime(2);
                        } else {
                            PrintLogging.printLog(this.getClass(), "", result.isSuccess() + "CurrentProgram  else--" + "-->>" + clickedStartTime + "-->>" + result.results.getObjects().get(0).getStartDate());
                            commonResponse.setProgramTime(1);
                        }
                    }
                    callBack.response(commonResponse);
                } else {
                    commonResponse.setStatus(false);
                    callBack.response(commonResponse);

                }
            }
        });
    }
}
