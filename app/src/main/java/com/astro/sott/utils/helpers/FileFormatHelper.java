package com.astro.sott.utils.helpers;

import android.content.Context;

import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

public class FileFormatHelper {
    public static String getDashSD(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getDashSD();
    }
    public static String getDashHD(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getDashHD();
    }
    public static String getHLSSD(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getHlsSD();
    }
    public static String getHLSHD(Context context)
    {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return responseDmsModel.getParams().getFilesFormat().getHlsHD();
    }
}
