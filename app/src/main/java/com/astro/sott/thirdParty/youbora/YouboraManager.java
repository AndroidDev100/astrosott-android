package com.astro.sott.thirdParty.youbora;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.BuildConfig;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;
import com.kaltura.playkit.PKPluginConfigs;
import com.kaltura.playkit.plugins.youbora.YouboraPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.kaltura.playkit.plugins.youbora.pluginconfig.YouboraConfig.KEY_CONTENT_METADATA_CAST;
import static com.kaltura.playkit.plugins.youbora.pluginconfig.YouboraConfig.KEY_CONTENT_METADATA_DIRECTOR;
import static com.kaltura.playkit.plugins.youbora.pluginconfig.YouboraConfig.KEY_CONTENT_METADATA_OWNER;
import static com.kaltura.playkit.plugins.youbora.pluginconfig.YouboraConfig.KEY_CONTENT_METADATA_YEAR;
import static com.npaw.youbora.lib6.plugin.Options.KEY_ACCOUNT_CODE;
import static com.npaw.youbora.lib6.plugin.Options.KEY_APP_NAME;
import static com.npaw.youbora.lib6.plugin.Options.KEY_APP_RELEASE_VERSION;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_GENRE;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_ID;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_IS_LIVE;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_LANGUAGE;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_METADATA;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_PROGRAM;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CONTENT_TITLE;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CUSTOM_DIMENSION_1;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CUSTOM_DIMENSION_2;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CUSTOM_DIMENSION_3;
import static com.npaw.youbora.lib6.plugin.Options.KEY_CUSTOM_DIMENSION_4;
import static com.npaw.youbora.lib6.plugin.Options.KEY_USERNAME;




public class YouboraManager {
    private static boolean islive;
    private static DoubleValue metaDataYear;
    private static String typeDescriptin;
    private static String catchupValue = "";
    private static Long duration;
    private static String cast;
    private static String crew;
    private static String genre;

    //    private static Map<String, MultilingualStringValueArray> map;
    private static String strUrlToPlay = "";

    public static PKPluginConfigs createYouboraPlugin(Context context, Asset asset, String deviceid) {

//        map = asset.getTags();
        String liveAssetName = "";
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = asset.getTags().get(AppLevelConstants.KEY_GENRE);
        if (genre_list != null)
            genre_values.addAll(genre_list.getObjects());

        if (genre_values.size() > 0) {
            genre = genre_values.get(0).getValue();



            List<MultilingualStringValue> cast_value = new ArrayList<>();
            MultilingualStringValueArray cast_list = asset.getTags().get(AppLevelConstants.KEY_MAINCAST);
            if (cast_list != null)
                cast_value.addAll(cast_list.getObjects());
            if (cast_value.size() > 0) {
                cast = cast_value.get(0).getValue();
            }

            List<MultilingualStringValue> crew_value = new ArrayList<>();
            MultilingualStringValueArray crew_list = asset.getTags().get(AppLevelConstants.KEY_DIRECTOR);
            if (crew_list != null)
                crew_value.addAll(crew_list.getObjects());
            if (crew_value.size() > 0) {
                crew = crew_value.get(0).getValue();
            }

            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                String assetUrl = asset.getMediaFiles().get(i).getType();
                if (assetUrl.equals(AppLevelConstants.HLS)) {
                    strUrlToPlay = asset.getMediaFiles().get(i).getUrl();
                    duration = asset.getMediaFiles().get(i).getDuration();
                }

            }

            Map<String, Value> map = asset.getMetas();

            Set keys = map.keySet();
            Iterator itr = keys.iterator();

            String key;
            while (itr.hasNext()) {
                key = (String) itr.next();
                if (key.equalsIgnoreCase("Year")) {
                    metaDataYear = (DoubleValue) map.get(key);

                }
            }
        }

        //Initialize PKPluginConfigs object.
        PKPluginConfigs pluginConfigs = new PKPluginConfigs();
        //Initialize Json object that will hold all the configurations for the plugin.

        //Youbora config json. Main config goes here.

        Bundle optBundle = new Bundle();

        if (asset.getType() == MediaTypeConstant.getMovie(context)) {
            typeDescriptin = AppLevelConstants.MOVIE;
            islive = false;
        } else if (asset.getType() == MediaTypeConstant.getDrama(context)) {
            typeDescriptin = AppLevelConstants.WEB_SERIES;
            islive = false;
        } else if (asset.getType() == MediaTypeConstant.getWebEpisode(context)) {
            typeDescriptin = AppLevelConstants.WEB_EPISODE_STRING;
            islive = false;
        } else if (asset.getType() == MediaTypeConstant.getShortFilm(context)) {
            typeDescriptin = AppLevelConstants.SHORT_FILM;
            islive = false;
        } else if (asset.getType() == MediaTypeConstant.getLinear(context)) {
            liveAssetName = asset.getName();
            typeDescriptin = AppLevelConstants.LINEAR;
            duration = 0L;
            islive = true;
            optBundle.putString(KEY_CONTENT_PROGRAM,Constants.programName);
        } else if (asset.getType() == MediaTypeConstant.getTrailer(context)) {
            typeDescriptin = AppLevelConstants.TRAILER;
            islive = false;
        }else if (asset.getType() == MediaTypeConstant.getProgram(context)){
            catchupValue = AppLevelConstants.CATCHUP;
            islive = false;
            optBundle.putString(KEY_CONTENT_PROGRAM,asset.getName());
        }




        optBundle.putString(KEY_ACCOUNT_CODE, AppLevelConstants.ACCOUNT_TYPE);
        optBundle.putString(KEY_APP_RELEASE_VERSION, BuildConfig.VERSION_NAME);

        optBundle.putString(KEY_APP_NAME, context.getPackageName());

        if (KsPreferenceKey.getInstance(context).getUserActive()) {
            optBundle.putString(KEY_USERNAME, KsPreferenceKey.getInstance(context).getUser().getId());
        } else {
            optBundle.putString(KEY_USERNAME, "");
        }


        //Media entry json.
        if (asset.getType() == MediaTypeConstant.getProgram(context)){
            optBundle.putString(KEY_CONTENT_TITLE, Constants.channelName);
        }else{
            optBundle.putString(KEY_CONTENT_TITLE, asset.getName());
        }

//        if (duration!=null) {
//            optBundle.putLong(KEY_CONTENT_DURATION, duration);
//        }else {
//            optBundle.putLong(KEY_CONTENT_DURATION,-1);
//        }
        optBundle.putBoolean(KEY_CONTENT_IS_LIVE, islive);
     //   optBundle.putString(KEY_CONTENT_RESOURCE, strUrlToPlay);

        //Properties Bundle
        optBundle.putLong(KEY_CONTENT_ID, asset.getId());
        optBundle.putString(KEY_CONTENT_GENRE, genre);



        // Content Metadata bundle
        Bundle contentMetaDataBundle = new Bundle();

        contentMetaDataBundle.putString(KEY_CONTENT_LANGUAGE, AppLevelConstants.ENLGISH);
        contentMetaDataBundle.putString(KEY_CONTENT_METADATA_CAST, cast);
        contentMetaDataBundle.putString(KEY_CONTENT_METADATA_DIRECTOR, crew);
        contentMetaDataBundle.putString(KEY_CONTENT_METADATA_OWNER, AppLevelConstants.SELF);
        if (metaDataYear != null) {
            optBundle.putDouble(KEY_CONTENT_METADATA_YEAR, metaDataYear.getValue());
        }
        optBundle.putBundle(KEY_CONTENT_METADATA, contentMetaDataBundle);

        //You can add some extra params here:

        optBundle.putString(KEY_CUSTOM_DIMENSION_1, typeDescriptin);
        optBundle.putString(KEY_CUSTOM_DIMENSION_2, liveAssetName);
        optBundle.putString(KEY_CUSTOM_DIMENSION_3, Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        optBundle.putString(KEY_CUSTOM_DIMENSION_4, catchupValue);


        //Set plugin entry to the plugin configs.
        pluginConfigs.setPluginConfig(YouboraPlugin.factory.getName(), optBundle);

        return pluginConfigs;
    }


}
