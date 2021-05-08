package com.astro.sott.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.activities.subscription.manager.PaymentItemDetail;
import com.astro.sott.activities.subscription.model.BillPaymentDetails;
import com.astro.sott.activities.subscription.model.BillPaymentModel;
import com.astro.sott.baseModel.PrefrenceBean;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ParentalMapping;
import com.astro.sott.modelClasses.dmsResponse.SubtitleLanguages;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.JsonArray;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.AssetHistory;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LongValue;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class AssetContent {
    static List<PromoPojo> mainValue;
    private static DoubleValue doubleValue;
    private static DoubleValue baseIdValue;
    private static BooleanValue IsdvrEnabled;
    private static BooleanValue purchaseAllowed;

    public static LiveData<String> getUrl(Asset asset, String videoResolution) {
        MutableLiveData<String> assetMutableLiveData = new MutableLiveData<>();
        if (asset.getMediaFiles() != null) {
            String strUrlToPLay = "";
            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                String assetUrl = asset.getMediaFiles().get(i).getType();
                if (videoResolution.equals(AppConstants.HD)) {
                    if (assetUrl.equals(AppConstants.Mobile_Dash_HD)) {
                        strUrlToPLay = asset.getMediaFiles().get(i).getUrl();
                        break;
//                    AppLevelConstants.urlToplay = asset.getMediaFiles().get(i).getUrl();
//                    AppLevelConstants.assetUrl = asset.getMediaFiles().get(i).getUrl();
                    }
                } else {
                    if (assetUrl.equals(AppConstants.Mobile_Dash_SD)) {
                        strUrlToPLay = asset.getMediaFiles().get(i).getUrl();
                        break;
//                    AppLevelConstants.urlToplay = asset.getMediaFiles().get(i).getUrl();
//                    AppLevelConstants.assetUrl = asset.getMediaFiles().get(i).getUrl();
                    }
                }
//                assetMutableLiveData.postValue(AppLevelConstants.urlToplay);
//                assetMutableLiveData.postValue(AppLevelConstants.urlToplay);
            }

            assetMutableLiveData.postValue(strUrlToPLay);

        } else {
            assetMutableLiveData.postValue("");
        }

        return assetMutableLiveData;
    }

    public static LiveData<String> getImageUrl(Asset asset) {
        MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
//        AppLevelConstants.imageUrl = asset.getImages().get(0).getUrl();
//        stringMutableLiveData.postValue(AppLevelConstants.imageUrl);


        stringMutableLiveData.postValue(asset.getImages().get(0).getUrl());
        return stringMutableLiveData;
    }

    public static int getSeriesNumber(Map<String, Value> map) {
        int seriesNumber;
        if (map != null) {
            doubleValue = (DoubleValue) map.get(AppLevelConstants.KEY_SEASON_NUMBER);
        }
        if (doubleValue != null) {
            seriesNumber = doubleValue.getValue().intValue();

        } else {
            seriesNumber = -1;
        }

        return seriesNumber;
    }

    public static String getRefIdStrData(Map<String, MultilingualStringValueArray> map) {
        String ref_id = "";
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.KEY_REF_ID);
        if (crew_list != null)
            crew_value.addAll(crew_list.getObjects());
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
        if (crew_value.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= crew_value.size() - 1; i++) {
                stringBuilder.append(crew_value.get(i).getValue());
            }
            ref_id = stringBuilder.toString();
        }

        return ref_id;
    }


    public static String getSeriesName(Map<String, MultilingualStringValueArray> map) {
        String ref_id = "";
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.SERIES_NAME);
        if (crew_list != null)
            crew_value.addAll(crew_list.getObjects());
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
        if (crew_value.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= crew_value.size() - 1; i++) {
                stringBuilder.append(crew_value.get(i).getValue());
            }
            ref_id = stringBuilder.toString();
        }

        return ref_id;
    }

    public static String getYear(Map<String, Value> metas) {
        DoubleValue doubleValue = null;
        String year = "";
        if (metas != null) {
            doubleValue = (DoubleValue) metas.get(AppLevelConstants.YEAR);
        }
        if (doubleValue != null) {
            year = String.valueOf(doubleValue.getValue());
        }
        return year;
    }

    public static boolean isAdsEnable(Map<String, Value> metas) {
        BooleanValue adsValue = null;
        if (metas != null) {
            adsValue = (BooleanValue) metas.get(AppLevelConstants.NOADS);
        }
        if (adsValue != null) {
            boolean value = adsValue.getValue();
            return value;
        } else {
            return true;
        }
    }

    public static List<Integer> getSeasonNumber(Response<ListResponse<Asset>> result) {
        List<Integer> seasonNumberList = new ArrayList<>();
        if (result.results != null) {
            if (result.results.getObjects() != null) {
                for (int i = 0; i < result.results.getObjects().size(); i++) {
                    Map<String, Value> map = result.results.getObjects().get(i).getMetas();

                    Set keys = map.keySet();
                    Iterator itr = keys.iterator();

                    String key;
                    while (itr.hasNext()) {
                        key = (String) itr.next();
                        if (key.equalsIgnoreCase(AppLevelConstants.KEY_SEASON_NUMBER)) {
                            DoubleValue doubleValue = (DoubleValue) map.get(key);
                            seasonNumberList.add(doubleValue.getValue().intValue());
                            PrintLogging.printLog("AssetContent", "", "metavaluess--" + key + " - " + doubleValue.getValue());
                        }
                    }

                }

            }
        }
        PrintLogging.printLog("AssetContent", "", "seasonNumerLis" + seasonNumberList.size());

        return seasonNumberList;
    }

    public static String getSeriesId(Map<String, Value> map) {
        String seriesID = "";
        List<MultilingualStringValue> seriesList = new ArrayList<>();
        StringValue series_list = (StringValue) map.get(AppLevelConstants.KEY_SERIES_ID);
        if (series_list != null) {
            seriesID = series_list.getValue();
        }

        return seriesID;

    }

    public static String getParentRefIdData(Map<String, MultilingualStringValueArray> map) {
        String ref_id = "";
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.KEY_PARENTREF_ID);
        if (crew_list != null)
            crew_value.addAll(crew_list.getObjects());
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
        if (crew_value.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= crew_value.size() - 1; i++) {
                stringBuilder.append(crew_value.get(i).getValue());
            }
            ref_id = stringBuilder.toString();
        }

        return ref_id;
    }

    public static String getTileVideoURL(Asset asset, String videoResolution) {
        String mediaUrl = "";

        String strUrlToPLay = "";
        for (int i = 0; i < asset.getMediaFiles().size(); i++) {
            String assetUrl = asset.getMediaFiles().get(i).getType();
            if (videoResolution.equals(AppConstants.HD)) {
                if (assetUrl.equals(AppConstants.Mobile_Dash_HD)) {
                    mediaUrl = asset.getMediaFiles().get(i).getUrl();
                    break;
//                    AppLevelConstants.urlToplay = asset.getMediaFiles().get(i).getUrl();
//                    AppLevelConstants.assetUrl = asset.getMediaFiles().get(i).getUrl();
                }
            } else {
                if (assetUrl.equals(AppConstants.Mobile_Dash_SD)) {
                    mediaUrl = asset.getMediaFiles().get(i).getUrl();
                    break;
//                    AppLevelConstants.urlToplay = asset.getMediaFiles().get(i).getUrl();
//                    AppLevelConstants.assetUrl = asset.getMediaFiles().get(i).getUrl();
                }
            }
//                assetMutableLiveData.postValue(AppLevelConstants.urlToplay);
//                assetMutableLiveData.postValue(AppLevelConstants.urlToplay);
        }
        return mediaUrl;
    }

    public static String getLiveGenredata(Map<String, MultilingualStringValueArray> map) {
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_GENRE);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }


        StringBuilderHolder.getInstance().clear();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            StringBuilderHolder.getInstance().append("Genre='" + genre_values.get(i).getValue() + "' ");
        }

        String value = StringBuilderHolder.getInstance().getText().toString();
        if (value.length() > 0) {
            value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
        } else {
            value = "";
        }
        return value;
    }

    public static LiveData<String> getCastActorsData
            (Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String cast;
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_CAST_ACTOR);

        if (cast_list != null)
            cast_value.addAll(cast_list.getObjects());
//            for (MultilingualStringValue value : cast_list.getObjects()) {
//                cast_value.add(value);
//            }

        PrintLogging.printLog("AssetContent", "", "castValuessss" + cast_value.size());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue()).append(", ");

        }
        if (stringBuilder.length() > 0) {
            cast = stringBuilder.toString();
            cast = cast.substring(0, cast.length() - 2);
            connection.postValue(cast + "");
        } else {
            connection.postValue("");
        }

        return connection;
    }

    public static String getProgramGenre(Map<String, MultilingualStringValueArray> map) {
        String genre;
        PrintLogging.printLog("AssetContent", "", map + "fdsf");
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_GENRE);
        if (genre_list != null)
            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }


        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(", ");
        }
        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            genre = genre.substring(0, genre.length() - 2);

        } else {
            genre = "";
        }

        return genre;
    }

    public static String getParentalRating(Map<String, MultilingualStringValueArray> map) {
        String rating;
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray rating_list = map.get(AppLevelConstants.PARENTAL_RATING);
        if (rating_list != null)
            cast_value.addAll(rating_list.getObjects());
//            for (MultilingualStringValue value : rating_list.getObjects()) {
//                cast_value.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue()).append(", ");

        }

        if (stringBuilder.length() > 0) {
            rating = stringBuilder.toString();
            rating = rating.substring(0, rating.length() - 2);
        } else {
            rating = "";
        }

        return rating;
    }

    public static String getProviderContentTier(Map<String, MultilingualStringValueArray> map) {
        String rating;
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray rating_list = map.get(AppLevelConstants.ProviderContentTier);
        if (rating_list != null)
            cast_value.addAll(rating_list.getObjects());
//            for (MultilingualStringValue value : rating_list.getObjects()) {
//                cast_value.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue()).append(", ");

        }

        if (stringBuilder.length() > 0) {
            rating = stringBuilder.toString();
            rating = rating.substring(0, rating.length() - 2);
        } else {
            rating = "";
        }

        return rating;
    }


    public static boolean getBillingId(Map<String, MultilingualStringValueArray> map) {
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray rating_list = map.get(AppLevelConstants.BILLING_ID);
        if (rating_list != null) {
            cast_value.addAll(rating_list.getObjects());
            if (cast_value.size() > 0 && cast_value.get(0).getValue() != null) {
                String cast = cast_value.get(0).getValue().trim();
                if (!cast.equalsIgnoreCase("")) {
                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }


    }

    public static int getParentalRatingForChecks
            (Map<String, MultilingualStringValueArray> map, Context applicationContext) {
        String rating;
        int priorityRestrictionLevel = -1;
        String mappingKey = "";
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray rating_list = map.get(AppLevelConstants.PARENTAL_RATING);
        if (rating_list != null)
            cast_value.addAll(rating_list.getObjects());
//            for (MultilingualStringValue value : rating_list.getObjects()) {
//                cast_value.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue());

        }

        if (stringBuilder.length() > 0) {
            rating = stringBuilder.toString();
        } else {
            rating = "";
        }
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(applicationContext);
        ArrayList<ParentalLevels> parentalLevels = responseDmsModel.getParentalLevels();
        ArrayList<ParentalMapping> parentalMappings = responseDmsModel.getMappingArrayList();
        //Finding Parental Rating From Asset
        for (int i = 0; i < parentalMappings.size(); i++) {
            JsonArray jsonArray = parentalMappings.get(i).getMappingList().getAsJsonArray();
            for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                if (jsonArray.get(i1).getAsString().equalsIgnoreCase(rating)) {
                    mappingKey = parentalMappings.get(i).getKey();
                    break;
                }

            }
        }
        //Finding Priority Level
        if (!mappingKey.equalsIgnoreCase("")) {
            for (int k = 0; k < parentalLevels.size(); k++) {
                if (parentalLevels.get(k).getKey().equalsIgnoreCase(mappingKey)) {
                    priorityRestrictionLevel = parentalLevels.get(k).getValue();

                }
            }
        }

        return priorityRestrictionLevel;
    }

    public static boolean getAssetKey(Map<String, MultilingualStringValueArray> map, String
            userSelectedParentalRating, Context applicationContext) {
        String rating;
        boolean mappingKey = false;
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray rating_list = map.get(AppLevelConstants.PARENTAL_RATING);
        if (rating_list != null)
            cast_value.addAll(rating_list.getObjects());
//            for (MultilingualStringValue value : rating_list.getObjects()) {
//                cast_value.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue());

        }

        if (stringBuilder.length() > 0) {
            rating = stringBuilder.toString();
        } else {
            rating = "";
        }
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(applicationContext);
        ArrayList<ParentalLevels> parentalLevels = responseDmsModel.getParentalLevels();
        ArrayList<ParentalMapping> parentalMappings = responseDmsModel.getMappingArrayList();

        if (parentalMappings == null) {
            return true;
        }
        if (!rating.equalsIgnoreCase("") && parentalMappings.size() > 0) {
            //Finding Parental Rating From Asset
            for (int i = 0; i < parentalMappings.size(); i++) {

                if (parentalMappings.get(i).getKey().equalsIgnoreCase(userSelectedParentalRating)) {
                    JsonArray jsonArray = parentalMappings.get(i).getMappingList().getAsJsonArray();
                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        if (jsonArray.get(i1).getAsString().equalsIgnoreCase(rating)) {
                            mappingKey = true;

                            break;
                        }

                    }
                }
                if (mappingKey)
                    break;

            }
        } else {
            mappingKey = true;
        }

        return mappingKey;
    }

    public static int getVideoProgress(Context context, int j, int assetId) {
        int progres = 0;
        try {
            List<AssetHistory> storeList = AppCommonMethods.getContinueWatchingPreferences(context);
            for (int i = 0; i < storeList.size(); i++) {
                if (storeList.get(i).getAssetId() == assetId) {
                    int n = storeList.get(i).getPosition();
                    int v = storeList.get(i).getDuration();
                    progres = n * 100 / v;
                    // progres = (storeList.get(j).getPosition()%3600)/60;
                    PrintLogging.printLog("AssetContent", "", "valuesduration" + progres + "--->>>" + storeList.get(j).getDuration() + "-->>" + storeList.get(j).getPosition() + "--->>" + storeList.get(i).getAssetId() + "---->>" + assetId);
                }
            }
        } catch (Exception e) {

            PrintLogging.printLog("Exception", "", "" + e);
        }
        return progres;
    }

    public static String getTrailorUrl(Asset asset) {

        String trailorURL = "";
        for (int i = 0; i < asset.getMediaFiles().size(); i++) {
            String assetUrl = asset.getMediaFiles().get(i).getType();
            if (assetUrl.equals(AppLevelConstants.HLS)) {
//                AppLevelConstants.trailorURL = asset.getMediaFiles().get(i).getUrl();

                trailorURL = asset.getMediaFiles().get(i).getUrl();
                break;
            }
        }
//        return AppLevelConstants.trailorURL;

        return trailorURL;
    }

    public static String getGenredata(List<String> genreList) {
        String genre;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genreList.size() - 1; i++) {
            stringBuilder.append("Genre='" + genreList.get(i) + "'").append(" ");
        }
        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            PrintLogging.printLog("", "genrefromAssetIs" + genre);
            genre = genre.substring(0, genre.length() - 1);

        } else {
            genre = "";
        }

        return genre;
    }


    public static LiveData<String> getGenredata
            (Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String genre;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_GENRE);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(", ");
        }

        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            genre = genre.substring(0, genre.length() - 2);

        } else {
            genre = "";
        }
        connection.postValue(genre + "");
        return connection;
    }


    public static String getGenredataString(Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String genre;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_GENRE);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(",");
        }

        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            genre = genre.substring(0, genre.length() - 2);

        } else {
            genre = "";
        }
        return genre;
    }

    public static String getKeyworddata(Map<String, MultilingualStringValueArray> map) {
        String keyword;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_KEYWORD);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(", ");
        }

        if (stringBuilder.length() > 0) {
            keyword = stringBuilder.toString();
            keyword = keyword.substring(0, keyword.length() - 2);

        } else {
            keyword = "";
        }
        return keyword;
    }

    public static LiveData<String> getSubGenredata
            (Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String genre;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_SUB_GENRE);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(", ");
        }

        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            genre = genre.substring(0, genre.length() - 2);

        } else {
            genre = "";
        }
        connection.postValue(genre + "");
        return connection;
    }


    public static String getSubGenredataString(Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String genre;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_SUB_GENRE);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(",");
        }

        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            genre = genre.substring(0, genre.length() - 2);

        } else {
            genre = "";
        }

        return genre;
    }


    public static LiveData<String> getRefIdData
            (Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String ref_id;
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.KEY_REF_ID);
        if (crew_list != null)
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
            crew_value.addAll(crew_list.getObjects());
        if (crew_value.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= crew_value.size() - 1; i++) {
                stringBuilder.append(crew_value.get(i).getValue());
            }
            ref_id = stringBuilder.toString();
            PrintLogging.printLog("AssetContent", "", "valuesssInRef" + ref_id);
            connection.postValue(ref_id + "");
        } else {
            connection.postValue("10000012");
        }

        return connection;
    }

    public static int getSpecificSeason(Map<String, Value> map) {
        int seasonValue = 0;
        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase(AppLevelConstants.KEY_SEASON_NUMBER)) {
                DoubleValue doubleValue = (DoubleValue) map.get(key);
                seasonValue = doubleValue.getValue().intValue();

                PrintLogging.printLog("", "", "SeasonnumberofAsset" + key + " - " + doubleValue.getValue());
            }
        }
        return seasonValue;
    }

    public static int getSpecificEpisode(Map<String, Value> map) {
        int episodeValue = 0;
        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("EpisodeNumber")) {
                DoubleValue doubleValue = (DoubleValue) map.get(key);
                episodeValue = doubleValue.getValue().intValue();
                PrintLogging.printLog("", "", "EpisodenumberofParticularAsset" + key + " - " + doubleValue.getValue());
            }
        }

        return episodeValue;
    }

    public static LiveData<String> getCastData(Map<String, MultilingualStringValueArray> map) {

        final MutableLiveData<String> connection = new MutableLiveData<>();
        String cast = "";
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_CAST_ACTOR);
        if (cast_list != null)
//            for (MultilingualStringValue value : cast_list.getObjects()) {
//                cast_value.add(value);
//            }
            cast_value.addAll(cast_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue()).append(", ");

        }

        if (stringBuilder.length() > 0) {
            cast = stringBuilder.toString();
            cast = cast.substring(0, cast.length() - 2);
        }

        connection.postValue(cast + "");
        return connection;
    }

    public static String getActor(Map<String, MultilingualStringValueArray> map) {

        String cast = "";
        List<MultilingualStringValue> cast_value = new ArrayList<>();
        MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_CAST_ACTOR);
        if (cast_list != null)
//            for (MultilingualStringValue value : cast_list.getObjects()) {
//                cast_value.add(value);
//            }
            cast_value.addAll(cast_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cast_value.size() - 1; i++) {

            stringBuilder.append(cast_value.get(i).getValue()).append(", ");

        }

        if (stringBuilder.length() > 0) {
            cast = stringBuilder.toString();
            cast = cast.substring(0, cast.length() - 2);
        }

        return cast;
    }

    public static LiveData<String> getLanguageData
            (Map<String, MultilingualStringValueArray> map) {

        final MutableLiveData<String> connection = new MutableLiveData<>();
        String language = "";
        List<MultilingualStringValue> language_value = new ArrayList<>();
        MultilingualStringValueArray language_list = map.get(AppLevelConstants.KEY_LANGUAGE);
        if (language_list != null)
//            for (MultilingualStringValue value : language_list.getObjects()) {
//                language_value.add(value);
//            }
            language_value.addAll(language_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= language_value.size() - 1; i++) {

            stringBuilder.append(language_value.get(i).getValue()).append(", ");

        }

        if (stringBuilder.length() > 0) {
            language = stringBuilder.toString();
            language = language.substring(0, language.length() - 2);
        }


        connection.postValue(language + "");
        return connection;
    }

    public static String getLanguageDataString
            (Map<String, MultilingualStringValueArray> map, Context context) {

        String language = "";
        String lang = "";
        List<MultilingualStringValue> language_value = new ArrayList<>();
        MultilingualStringValueArray language_list = map.get(AppLevelConstants.KEY_LANGUAGE);
        if (language_list != null)
//            for (MultilingualStringValue value : language_list.getObjects()) {
//                language_value.add(value);
//            }
            language_value.addAll(language_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        ArrayList<AudioLanguages> audioLanguageList = responseDmsModel.getAudioLanguageList();

        for (int i = 0; i <= language_value.size() - 1; i++) {
            lang = language_value.get(i).getValue();
            for (int j = 0; j < audioLanguageList.size(); j++) {
                if (lang.equalsIgnoreCase(audioLanguageList.get(j).getKey())) {
                    stringBuilder.append(audioLanguageList.get(j).getValue()).append(",");
                }
            }
        }

        if (stringBuilder.length() > 0) {
            language = stringBuilder.toString();
            language = language.substring(0, language.length() - 2);
        }


        return language;
    }


    public static String getParentRefId(Map<String, MultilingualStringValueArray> map) {

        String language = "";
        List<MultilingualStringValue> refId_value = new ArrayList<>();
        MultilingualStringValueArray refId_list = map.get(AppLevelConstants.KEY_PARENT_REF_ID);
        if (refId_list != null)
//            for (MultilingualStringValue value : language_list.getObjects()) {
//                language_value.add(value);
//            }
            refId_value.addAll(refId_list.getObjects());
        if (refId_value.get(0) != null)
            language = refId_value.get(0).getValue();


        return language;
    }

    public static LiveData<String> getSubTitleLanguageData
            (Map<String, MultilingualStringValueArray> map) {

        final MutableLiveData<String> connection = new MutableLiveData<>();
        String language = "";
        List<MultilingualStringValue> language_value = new ArrayList<>();
        MultilingualStringValueArray language_list = map.get(AppLevelConstants.KEY_SUBTITLE_LANGUAGE);
        if (language_list != null)
//            for (MultilingualStringValue value : language_list.getObjects()) {
//                language_value.add(value);
//            }
            language_value.addAll(language_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= language_value.size() - 1; i++) {

            stringBuilder.append(language_value.get(i).getValue()).append(", ");

        }

        if (stringBuilder.length() > 0) {
            language = stringBuilder.toString();
            language = language.substring(0, language.length() - 2);
        }


        connection.postValue(language + "");
        return connection;
    }

    public static String getSubTitleLanguageDataString(Map<String, MultilingualStringValueArray> map, Context context) {

        String language = "";
        String lang = "";
        List<MultilingualStringValue> language_value = new ArrayList<>();
        MultilingualStringValueArray language_list = map.get(AppLevelConstants.KEY_SUBTITLE_LANGUAGE);
        if (language_list != null)
//            for (MultilingualStringValue value : language_list.getObjects()) {
//                language_value.add(value);
//            }
            language_value.addAll(language_list.getObjects());
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        ArrayList<SubtitleLanguages> subtitleLanguages = responseDmsModel.getSubtitleLanguageList();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= language_value.size() - 1; i++) {
            lang = language_value.get(i).getValue();
            for (int j = 0; j < subtitleLanguages.size(); j++) {
                if (lang.equalsIgnoreCase(subtitleLanguages.get(j).getKey())) {
                    stringBuilder.append(subtitleLanguages.get(j).getValue()).append(",");
                }
            }

        }

        if (stringBuilder.length() > 0) {
            language = stringBuilder.toString();
            language = language.substring(0, language.length() - 2);
        }


        return language;
    }

    public static LiveData<String> getCrewData(Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        String crew;
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.KEY_DIRECTOR);
        if (crew_list != null)
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
            crew_value.addAll(crew_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= crew_value.size() - 1; i++) {
            stringBuilder.append(crew_value.get(i).getValue()).append(", ");
        }

        if (stringBuilder.length() > 0) {
            crew = stringBuilder.toString();
            crew = crew.substring(0, crew.length() - 2);
        } else {
            crew = "";
        }
        connection.postValue(crew + "");
        return connection;
    }

    public static String getProducer(Map<String, MultilingualStringValueArray> map) {
        String crew;
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.KEY_PRODUCER);
        if (crew_list != null)
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
            crew_value.addAll(crew_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= crew_value.size() - 1; i++) {
            stringBuilder.append(crew_value.get(i).getValue()).append(", ");
        }

        if (stringBuilder.length() > 0) {
            crew = stringBuilder.toString();
            crew = crew.substring(0, crew.length() - 2);
        } else {
            crew = "";
        }

        return crew;
    }

    public static String getDirector(Map<String, MultilingualStringValueArray> map) {
        String crew;
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.KEY_DIRECTOR);
        if (crew_list != null)
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
            crew_value.addAll(crew_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= crew_value.size() - 1; i++) {
            stringBuilder.append(crew_value.get(i).getValue()).append(", ");
        }

        if (stringBuilder.length() > 0) {
            crew = stringBuilder.toString();
            crew = crew.substring(0, crew.length() - 2);
        } else {
            crew = "";
        }

        return crew;
    }

    public static String getProvider(Map<String, MultilingualStringValueArray> map) {
        String provider;
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.PROVIDER);
        if (crew_list != null)
//            for (MultilingualStringValue value : crew_list.getObjects()) {
//                crew_value.add(value);
//            }
            crew_value.addAll(crew_list.getObjects());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= crew_value.size() - 1; i++) {
            stringBuilder.append(crew_value.get(i).getValue()).append(",");
        }

        if (stringBuilder.length() > 0) {
            provider = stringBuilder.toString();
            provider = provider.substring(0, provider.length() - 2);
        } else {
            provider = "";
        }

        return provider;
    }

    public static int getVideoPosition(Context context, int j, int assetId) {
        int progres = 0;
        try {
            List<AssetHistory> storeList = AppCommonMethods.getContinueWatchingPreferences(context);
            for (int i = 0; i < storeList.size(); i++) {
                if (storeList.get(i).getAssetId() == assetId) {
                    progres = storeList.get(j).getPosition();
                    PrintLogging.printLog("AssetContent", "", "valuesduration" + progres + "--->>>" + storeList.get(j).getPosition());
                }
            }

        } catch (Exception e) {


            PrintLogging.printLog("Exception", "", "" + e);
        }
        return progres;
    }

    public static String getWatchListGenre(Map<String, MultilingualStringValueArray> map) {
        String genre;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_GENRE);
        if (genre_list != null)
//            for (MultilingualStringValue value : genre_list.getObjects()) {
//                genre_values.add(value);
//            }
            genre_values.addAll(genre_list.getObjects());

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            stringBuilder.append(genre_values.get(i).getValue()).append(" \u2022 ");
        }
        if (stringBuilder.length() > 0) {
            genre = stringBuilder.toString();
            genre = genre.substring(0, genre.length() - 2);

        } else {
            genre = "";
        }

        return genre;
    }

    public static String getAssetIds(Context context, List<AssetHistory> selectedList) {
        List<Long> continueWatchingIds = new ArrayList<>();
        String value;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < selectedList.size(); i++) {

            if (selectedList.get(i).getPosition() == 0 || selectedList.get(i).getFinishedWatching()) {

            } else {
                continueWatchingIds.add(selectedList.get(i).getAssetId());
                stringBuilder.append(selectedList.get(i).getAssetId()).append(",");
            }

        }

        if (stringBuilder.length() > 0) {
            value = stringBuilder.toString();
            value = value.substring(0, value.length() - 1);
            AppCommonMethods.setContinueWatchingIDsPreferences(continueWatchingIds, context);

        } else {
            value = "";
        }

        return value;
    }

    public static String getSelectedPrefrences(ArrayList<PrefrenceBean> selectedList) {
        String value;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < selectedList.size(); i++) {
            if (selectedList.get(i).getChecked()) {
                stringBuilder.append(selectedList.get(i).getName()).append(",");
            }

        }

        if (stringBuilder.length() > 0) {
            value = stringBuilder.toString();
            value = value.substring(0, value.length() - 1);

        } else {
            value = "";
        }

        return value;
    }

    public static String getUserPreference(Map<String, StringValue> map) {
        String genre;
        StringValue genre_list = map.get(AppLevelConstants.KEY_CONTENT_PREFRENCES);
        if (genre_list != null) {
            genre = genre_list.getValue();
            PrintLogging.printLog("", "", "valuesFrmContent" + genre_list.getValue());
        } else {
            genre = null;
        }

        return genre;
    }

    public static LiveData<String> getVideoResolution
            (Map<String, MultilingualStringValueArray> map) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        if (map.containsKey(AppLevelConstants.KEY_VIDEO_RESOLUTION)) {
            MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_VIDEO_RESOLUTION);
            if (cast_list != null && cast_list.getObjects() != null && cast_list.getObjects().size() == 0) {
                connection.postValue(AppConstants.SD);
            } else {
                connection.postValue(cast_list.getObjects().get(0).getValue());
            }
        } else {
            connection.postValue(AppConstants.SD);
        }
        return connection;
    }

    public static String getVideoResol(Map<String, MultilingualStringValueArray> map) {
        String resolution;
        final MutableLiveData<String> connection = new MutableLiveData<>();
        if (map.containsKey(AppLevelConstants.KEY_VIDEO_RESOLUTION)) {
            MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_VIDEO_RESOLUTION);
            if (cast_list != null && cast_list.getObjects() != null && cast_list.getObjects().size() == 0) {
                resolution = AppConstants.SD;
                // connection.postValue(AppConstants.SD);
            } else {
                resolution = cast_list.getObjects().get(0).getValue();
                connection.postValue(cast_list.getObjects().get(0).getValue());
            }
        } else {
            resolution = AppConstants.SD;
            // connection.postValue(AppConstants.SD);
        }
        return resolution;
    }

    public static String getUserPreferenceForParental(Map<String, StringValue> map) {
        String genre = null;
        StringValue genre_list = map.get(AppLevelConstants.KEY_CONTENT_PREFRENCES_F_PARENTAL);
        if (genre_list != null) {
            genre = genre_list.getValue();
        }
        return genre;
    }

    public static void getUserTypeForDialogAndNonDialogUser
            (Map<String, StringValue> map, Context context) {
        String userType = "";

        if (KsPreferenceKey.getInstance(context).getUserType().equalsIgnoreCase("")) {
            StringValue userOriginList = map.get(AppLevelConstants.KEY_USER_TYPE);
            if (userOriginList != null) {
                userType = userOriginList.getValue();
            }

            if (userType != "") {
                KsPreferenceKey.getInstance(context).setUserType(userType);
            } else {
                KsPreferenceKey.getInstance(context).setUserType(AppLevelConstants.NON_DIALOG);
            }

        }


    }

    public static String getURL(Asset asset) {
        String assetURL = "";
        if (asset.getMediaFiles() != null) {
            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                String assetUrl = asset.getMediaFiles().get(i).getType();
                if (assetUrl.equals(AppConstants.HD)) {
                    assetURL = asset.getMediaFiles().get(i).getUrl();
                }

            }
        } else {
            assetURL = "";
        }
        Log.e("ASSET CONTENT URL", "Hello=" + assetURL);
        return assetURL;
    }

    public static List<PromoPojo> getMediaInformation(Map<String, Value> map) {
        String valuePromo = "";
        mainValue = new ArrayList<>();
        PromoPojo promoPojo = new PromoPojo();


        mainValue.clear();


        if (map.containsKey(AppLevelConstants.KEY_EXTERNAL_URL) && ((MultilingualStringValue) map.get(AppLevelConstants.KEY_EXTERNAL_URL)).getValue() != null && !((MultilingualStringValue) map.get(AppLevelConstants.KEY_EXTERNAL_URL)).getValue().equals("")) {
            // PromoPojo promoPojo = new PromoPojo();
            MultilingualStringValue value = (MultilingualStringValue) map.get(AppLevelConstants.KEY_EXTERNAL_URL);
            if (value.getValue() != null && !value.getValue().equals("")) {

                promoPojo.setKeyName(AppLevelConstants.KEY_EXTERNAL_URL);
                promoPojo.setKeyValue(value.getValue());

                mainValue.add(promoPojo);


            }
        } else if (map.containsKey(AppLevelConstants.KEY_PROMO_MEDIA_ID) && ((DoubleValue) map.get(AppLevelConstants.KEY_PROMO_MEDIA_ID)).getValue() != null && !((DoubleValue) map.get(AppLevelConstants.KEY_PROMO_MEDIA_ID)).getValue().equals("")) {
            // PromoPojo promoPojo = new PromoPojo();
            DoubleValue doubleValue = (DoubleValue) map.get(AppLevelConstants.KEY_PROMO_MEDIA_ID);
            if (doubleValue.getValue() != null && !doubleValue.getValue().equals("")) {
                int valueIs = doubleValue.getValue().intValue();

                promoPojo.setKeyName(AppLevelConstants.KEY_PROMO_MEDIA_ID);
                promoPojo.setKeyValue(String.valueOf(valueIs));

                mainValue.add(promoPojo);


            }


        } else if (map.containsKey(AppLevelConstants.KEY_PROGRAM_ID) && ((MultilingualStringValue) map.get(AppLevelConstants.KEY_PROGRAM_ID)).getValue() != null && !((MultilingualStringValue) map.get(AppLevelConstants.KEY_PROGRAM_ID)).getValue().equals("")) {
            // PromoPojo promoPojo = new PromoPojo();
            MultilingualStringValue value = (MultilingualStringValue) map.get(AppLevelConstants.KEY_PROGRAM_ID);
            if (value.getValue() != null && !value.getValue().equals("")) {

                promoPojo.setKeyName(AppLevelConstants.KEY_PROGRAM_ID);
                promoPojo.setKeyValue(value.getValue());

                mainValue.add(promoPojo);


            }

        } else if (map.containsKey(AppLevelConstants.KEY_BASE_ID) && ((DoubleValue) map.get(AppLevelConstants.KEY_BASE_ID)).getValue() != null && !((DoubleValue) map.get(AppLevelConstants.KEY_BASE_ID)).getValue().equals("")) {
            // PromoPojo promoPojo = new PromoPojo();
            DoubleValue doubleValue = (DoubleValue) map.get(AppLevelConstants.KEY_BASE_ID);
            if (doubleValue.getValue() != null && !doubleValue.getValue().equals("")) {
                int valueIs = doubleValue.getValue().intValue();

                promoPojo.setKeyName(AppLevelConstants.KEY_BASE_ID);
                promoPojo.setKeyValue(String.valueOf(valueIs));

                mainValue.add(promoPojo);


            }


        }

//
//
//        }


        return mainValue;
    }

    public static String getDtvAccountDetail(Map<String, StringValue> map) {

        String account = "";
        StringValue dtv_list = map.get(AppLevelConstants.DTV_ACCOUNT);
        // StringValue mbb_list = map.get(AppLevelConstants.MBB_ACCOUNT);
        if (dtv_list != null) {
            account = dtv_list.getValue();
            PrintLogging.printLog("", "", "valuesFromdtvList" + dtv_list.getValue());
        } else {
            account = null;
        }

        return account;

    }

    public static String getMBBAccountDetail(Map<String, StringValue> map) {

        String account = "";
        StringValue mbb_list = map.get(AppLevelConstants.MBB_ACCOUNT);
        if (mbb_list != null) {
            account = mbb_list.getValue();
            PrintLogging.printLog("", "", "valuesFromdtvList" + mbb_list.getValue());
        } else {
            account = null;
        }

        return account;

    }

    public static LiveData<List<BillPaymentModel>> getMbbAccountDetails(Context context) {
        final MutableLiveData<List<BillPaymentModel>> mutableLiveData = new MutableLiveData<>();
        List<BillPaymentModel> billPaymentModels = new ArrayList<>();
        List<BillPaymentDetails> billPaymentDetails = new ArrayList<>();
        billPaymentDetails.clear();
        billPaymentModels.clear();
        BillPaymentDetails billModel = new BillPaymentDetails();
        BillPaymentModel paymentModel = new BillPaymentModel();
        String mbbAccount = "";

        if (KsPreferenceKey.getInstance(context).getUser().getUsername() != null) {
            billModel.setAccountType(AppLevelConstants.MOBILE);
            billModel.setAccountNumber(KsPreferenceKey.getInstance(context).getUser().getUsername());
            billPaymentDetails.add(billModel);
        }

        if (billPaymentDetails.size() > 0 && billPaymentDetails != null) {
            paymentModel.setHeaderTitle(AppLevelConstants.ADD_TO_BILL);
            paymentModel.setBillPaymentDetails(billPaymentDetails);
            billPaymentModels.add(paymentModel);
        } else {
            billPaymentModels = null;
        }
        mutableLiveData.postValue(billPaymentModels);
        return mutableLiveData;
    }

    public static List<BillPaymentModel> getMBBDTVAccountDetail(Map<String, StringValue> map) {
        List<BillPaymentModel> billPaymentModels = new ArrayList<>();
        List<BillPaymentDetails> billPaymentDetails = new ArrayList<>();
        billPaymentDetails.clear();
        billPaymentModels.clear();
        BillPaymentDetails billModel = null;
        BillPaymentModel paymentModel = new BillPaymentModel();
        String mbbAccount = "";
        String dtvAccount = "";
        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;


        while (itr.hasNext()) {
            billModel = new BillPaymentDetails();
            key = (String) itr.next();
//            if (key.equalsIgnoreCase("DTV")){
//                StringValue dtv_list = map.get(key);
//                if (dtv_list!=null){
//                    dtvAccount=dtv_list.getValue();
//                    billModel.setAccountType(AppLevelConstants.DIALOG_TV);
//                    billModel.setAccountNumber(dtvAccount);
//                    billPaymentDetails.add(billModel);
//                }
//            }

            if (key.equalsIgnoreCase("MBB")) {
                StringValue mbb_list = map.get(key);
                if (mbb_list != null) {
                    mbbAccount = mbb_list.getValue();
                    billModel.setAccountType(AppLevelConstants.MOBILE);
                    billModel.setAccountNumber(mbbAccount);
                    billPaymentDetails.add(billModel);

                }
            }
        }


        if (billPaymentDetails.size() > 0 && billPaymentDetails != null) {
            paymentModel.setHeaderTitle(AppLevelConstants.ADD_TO_BILL);
            paymentModel.setBillPaymentDetails(billPaymentDetails);
            billPaymentModels.add(paymentModel);
        } else {
            billPaymentModels = null;
        }
        return billPaymentModels;

    }

    public static boolean getTSTVData(Map<String, Value> dvrMap) {
        boolean dvrEnabled = false;
        if (dvrMap != null) {
            IsdvrEnabled = (BooleanValue) dvrMap.get(AppLevelConstants.KEY_DVR_ENABLED);
        }

        if (IsdvrEnabled != null) {
            dvrEnabled = IsdvrEnabled.getValue();
        }

        return dvrEnabled;
    }

    public static boolean isPurchaseAllowed(Map<String, Value> metas, Asset object, Activity
            activity) {
        boolean isPurchaseAllowed = false;

        if (object.getType() == MediaTypeConstant.getLinear(activity) || object.getType() == MediaTypeConstant.getProgram(activity)) {
            if (metas != null) {
                purchaseAllowed = (BooleanValue) metas.get(AppLevelConstants.KEY_PURCHASE_ALLOWED);
            }

            if (purchaseAllowed != null) {
                isPurchaseAllowed = purchaseAllowed.getValue();
            } else {
                isPurchaseAllowed = false;
            }
        } else {
            isPurchaseAllowed = true;
        }


        return isPurchaseAllowed;
    }

    public static String getBaseId(Map<String, Value> metas) {
        String baseId = "";

        if (metas != null) {
            baseIdValue = (DoubleValue) metas.get(AppLevelConstants.BASE_ID);

            if (baseIdValue != null) {
                baseId = String.valueOf(baseIdValue.getValue().intValue());

            } else {
                baseId = "";
            }
        }
        return baseId;
    }

    public static void saveIdAndReasonCode(List<Asset> metas) {
        MultilingualStringValue adapterData;
        DoubleValue Id;
        List<PaymentItemDetail> paymentItemDetails = new ArrayList<>();

        if (metas != null) {
            for (int i = 0; i < metas.size(); i++) {
                PaymentItemDetail paymentItemDetail = new PaymentItemDetail();
                adapterData = (MultilingualStringValue) metas.get(i).getMetas().get(AppLevelConstants.REASEN_CODE);
                Id = (DoubleValue) metas.get(i).getMetas().get(AppLevelConstants.BASE_ID);
                if (adapterData != null && Id != null) {
                    paymentItemDetail.setPackageId(String.valueOf(Id.getValue().intValue()));
                    paymentItemDetail.setAdapterId(adapterData.getValue());
                    paymentItemDetails.add(paymentItemDetail);

                }
            }
            AllChannelManager.getInstance().setPaymentItemDetails(paymentItemDetails);
        }


    }

    public static boolean getHungamaTag(Map<String, MultilingualStringValueArray> map) {
        boolean isHungamaTag;

        if (map.containsKey(AppLevelConstants.KEY_PROVIDER)) {
            MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_PROVIDER);
            if (cast_list != null) {
                if (cast_list.getObjects().get(0).getValue().equalsIgnoreCase("Hungama")) {
                    isHungamaTag = true;
                } else {
                    isHungamaTag = false;
                }

                // connection.postValue(AppConstants.SD);
            } else {
                isHungamaTag = false;

            }
        } else {
            isHungamaTag = false;
            // connection.postValue(AppConstants.SD);
        }
        return isHungamaTag;
    }

    public static String getProviderExternalContentId
            (Map<String, MultilingualStringValueArray> map) {
        String externalId = "";
        if (map.containsKey(AppLevelConstants.KEY_PROVIDER_EXTERNAL_CONTENT_ID)) {
            MultilingualStringValueArray cast_list = map.get(AppLevelConstants.KEY_PROVIDER_EXTERNAL_CONTENT_ID);
            if (cast_list != null) {
                externalId = cast_list.getObjects().get(0).getValue();

                // connection.postValue(AppConstants.SD);
            } else {
                externalId = "";

            }
        } else {
            externalId = "";
            // connection.postValue(AppConstants.SD);
        }

        return externalId;
    }

    public static long getTimeDifference(String startTime, String endTime) {
        long diffSeconds1 = 0;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(startTime);
            d2 = format.parse(endTime);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            diffSeconds1 = diff / 1000;


        } catch (Exception e) {

        }
        return diffSeconds1;
    }

    public static List<String> getRibbon(Map<String, MultilingualStringValueArray> map) {
        List<String> ribbonList = new ArrayList<>();
        List<MultilingualStringValue> crew_value = new ArrayList<>();
        MultilingualStringValueArray crew_list = map.get(AppLevelConstants.RIBBON);
        if (crew_list != null)
            crew_value.addAll(crew_list.getObjects());
        for (int i = 0; i <= crew_value.size() - 1; i++) {
            if (crew_value.get(i) != null) {
                try {
                    String[] splitString = crew_value.get(i).getValue().split("\\|");
                    String label = splitString[0];
                    String startTimeSplit = splitString[1];
                    String endTimeSplit = splitString[2];
                    String[] startTimeArray = startTimeSplit.split("=");
                    String[] endTimeArray = endTimeSplit.split("=");
                    String startTime = startTimeArray[1];
                    String endTime = endTimeArray[1];
                    boolean isVisible = AppCommonMethods.compareStartEndTime(startTime, endTime);
                    if (isVisible) {
                        ribbonList.add(label);
                    }
                } catch (Exception e) {

                }


            }

            // stringBuilder.append(crew_value.get(i).getValue()).append(", ");
        }
        return ribbonList;

    }

    public static boolean plabackControl(Map<String, Value> metas) {
        StringValue startValue, endValue;
        String startDate, endDate;
        startValue = (StringValue) metas.get(AppLevelConstants.PLAYBACK_START_DATE);
        endValue = (StringValue) metas.get(AppLevelConstants.PLAYBACK_END_DATE);

        if (startValue != null && endValue != null) {
            startDate = startValue.getValue();
            endDate = endValue.getValue();
            if (startDate != null && endDate != null && !startDate.equalsIgnoreCase("") && !endDate.equalsIgnoreCase("")) {
                return AppCommonMethods.comparePlaybackStartEndTime(startDate, endDate);
            } else {
                return true;
            }

        } else {
            return true;
        }


    }

    public static String getPlayBackDate(Map<String, Value> metas) {
        StringValue playbackStart;
        String date = "";
        Date inputDate;
        playbackStart = (StringValue) metas.get(AppLevelConstants.PLAYBACK_START_DATE);

        if (playbackStart != null) {
            date = playbackStart.getValue();
            if (date != null && !date.equalsIgnoreCase("")) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd");
                inputFormat.setTimeZone(TimeZone.getDefault());
                try {
                    inputDate = inputFormat.parse(date);
                    date = outputFormat.format(inputDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return date;

    }

    public static boolean checkComingSoon(Map<String, Value> metas) {
        StringValue startValue;
        String startDate;
        startValue = (StringValue) metas.get(AppLevelConstants.PLAYBACK_START_DATE);

        if (startValue != null) {
            startDate = startValue.getValue();
            if (startDate != null && !startDate.equalsIgnoreCase("")) {
                Date currentDate = new Date();
                Date startDateTime = new Date();


                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                inputFormat.setTimeZone(TimeZone.getDefault());
                String currentTime = inputFormat.format(today);
                try {
                    currentDate = inputFormat.parse(currentTime);
                    startDateTime = inputFormat.parse(startDate);

                    if (startDateTime.after(currentDate)) {
                        return true;
                    } else {
                        return false;

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }

        } else {
            return false;
        }


    }

    public static boolean isSponsored(Map<String, Value> metas) {
        BooleanValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (BooleanValue) metas.get(AppLevelConstants.IS_SPONSORED);
            if (sponsoredValue != null) {
                return sponsoredValue.getValue();
            } else {
                return false;
            }

        } else {
            return false;
        }


    }

    public static boolean isLiveEvent(Map<String, Value> metas) {
        BooleanValue liveEventValue;
        if (metas != null) {
            liveEventValue = (BooleanValue) metas.get(AppLevelConstants.IS_LIVE_EVENT);
            if (liveEventValue != null) {
                return liveEventValue.getValue();
            } else {
                return false;
            }

        } else {
            return false;
        }


    }


    public static long getIntroStart(Map<String, Value> metas) {
        long introStart = 0;
        DoubleValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (DoubleValue) metas.get(AppLevelConstants.INTRO_START);
            if (sponsoredValue != null && sponsoredValue.getValue() != null) {
                introStart = sponsoredValue.getValue().longValue();
                return introStart;
            } else {
                return introStart;
            }

        } else {
            return introStart;
        }

    }


    public static long getCreditStart(Map<String, Value> metas) {
        long introStart = 0;
        DoubleValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (DoubleValue) metas.get(AppLevelConstants.CREDIT_START);
            if (sponsoredValue != null && sponsoredValue.getValue() != null) {
                introStart = sponsoredValue.getValue().longValue();
                return introStart;
            } else {
                return introStart;
            }

        } else {
            return introStart;
        }

    }

    public static long getRecapStart(Map<String, Value> metas) {
        long recap = 0;
        DoubleValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (DoubleValue) metas.get(AppLevelConstants.RECAP_START);
            if (sponsoredValue != null && sponsoredValue.getValue() != null) {
                recap = sponsoredValue.getValue().longValue();
                return recap;
            } else {
                return recap;
            }

        } else {
            return recap;
        }

    }


    public static long getCreditEnd(Map<String, Value> metas) {
        long introStart = 0;
        DoubleValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (DoubleValue) metas.get(AppLevelConstants.CREDIT_END);
            if (sponsoredValue != null && sponsoredValue.getValue() != null) {
                introStart = sponsoredValue.getValue().longValue();
                return introStart;
            } else {
                return introStart;
            }

        } else {
            return introStart;
        }


    }

    public static long getRecapEnd(Map<String, Value> metas) {
        long introStart = 0;
        DoubleValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (DoubleValue) metas.get(AppLevelConstants.RECAP_END);
            if (sponsoredValue != null && sponsoredValue.getValue() != null) {
                introStart = sponsoredValue.getValue().longValue();
                return introStart;
            } else {
                return introStart;
            }

        } else {
            return introStart;
        }

    }


    public static long getIntroEnd(Map<String, Value> metas) {
        long introStart = 0;
        DoubleValue sponsoredValue;
        if (metas != null) {
            sponsoredValue = (DoubleValue) metas.get(AppLevelConstants.INTRO_END);
            if (sponsoredValue != null && sponsoredValue.getValue() != null) {
                introStart = sponsoredValue.getValue().longValue();
                return introStart;
            } else {
                return introStart;
            }

        } else {
            return introStart;
        }

    }

}
