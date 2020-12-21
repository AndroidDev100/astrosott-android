package com.astro.sott.utils.helpers;

import android.content.Context;
import android.text.TextUtils;

import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

public class MediaTypeConstant {

//    Context context;

//    public MediaTypeConstant(Context base) {
//        super(base);
//        context = base;
//    }

    public static int getMovie(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getMovie());
    }

    public static int getDrama(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);

        return 559;

    }



    public static int getPromo(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        if (responseDmsModel.getParams().getMediaTypes().getPromo() != null)
            return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getPromo());
        else
            return -1;
    }

    public static int getLinear(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getLinear());

    }


    public static int getGenre(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);

        if (TextUtils.isEmpty(responseDmsModel.getParams().getMediaTypes().getGenre())) {
            return 0;
        } else {

            return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getGenre());
        }

    }

    public static int getTrailer(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getTrailer());
    }


    public static int getClip() {
//        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference();
//        return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getClip());

        return -1;

    }



    public static int getProgram(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return Integer.parseInt(responseDmsModel.getParams().getMediaTypes().getProgram());

    }


    public static int getDefaultParentalRule(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);

        if (responseDmsModel.getParams().getParentalDefaultRule() == null || TextUtils.isEmpty(responseDmsModel.getParams().getParentalDefaultRule().getDefaultRule())) {
            return 0;
        } else {
            return Integer.parseInt(responseDmsModel.getParams().getParentalDefaultRule().getDefaultRule());
        }
    }





    public static int getWebSeries(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 559;

    }

    public static int getWebEpisode(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 557;

    }


    public static int getShortFilm(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 596;
    }

    public static int getSpotlightEpisode(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 603;
    }

    public static int getSpotlightSeries(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 604;

    }


    public static int getUGCCreator(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 600;

    }




    public static int getCreatorVideo(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 598;

    }

    public static int getTVShowSeries(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 594;
    }

    public static int getTVEpisode(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 595;
    }

    public static int getSeries(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 604;

    }

    public static int getUGCVideo(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 597;

    }

    public static int getClip(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 602;

    }



    public static int getIFP(Context context) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        return 620;

    }


    public static String getOther(Context context) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MediaTypeConstant.getUGCCreator(context));
        return stringBuilder.toString();
    }
}
