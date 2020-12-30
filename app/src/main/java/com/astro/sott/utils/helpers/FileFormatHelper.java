package com.astro.sott.utils.helpers;

import android.content.Context;

import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

public class FileFormatHelper {
    public static String getHss_playready(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getSS();
    }
    public static String getDash_playready(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getDASH();
    }
    public static String getDash_widevine(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getDASHWV();
    }
    public static String getHls_fairplay(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getHLS();
    }
}
