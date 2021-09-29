package com.astro.sott.utils.commonMethods;

import static com.astro.sott.activities.myPlans.adapter.MyPlanAdapter.getDateCurrentTimeZone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.astro.sott.BuildConfig;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.search.constants.SearchFilterEnum;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.PrefrenceBean;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.kalturaCallBacks.DMSCallBack;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.modelClasses.dmsResponse.SubtitleLanguages;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.player.ui.DTPlayer;
import com.astro.sott.usermanagment.modelClasses.getContact.SocialLoginTypesItem;
import com.astro.sott.usermanagment.modelClasses.getDevice.AccountDeviceDetailsItem;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrefConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.ksPreferenceKey.SubCategoriesPrefs;
import com.astro.sott.utils.userInfo.UserInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.astro.sott.R;
import com.astro.sott.utils.constants.AppConstants;
import com.clevertap.android.sdk.CleverTapAPI;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.enums.RailCardType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.AssetHistory;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LongValue;
import com.kaltura.client.types.MediaImage;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Subscription;
import com.kaltura.client.types.SubscriptionEntitlement;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppCommonMethods {
    private static String progDuration;
    public static boolean isAdsEnable = true;
    private static Long _time;
    public static boolean isTablet = false;


//    public static String convertProgramTime(String time) {
//
//        Date date = new Date(Long.parseLong(time) * 1000L);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd=HH:mm a");
//        String dateTimeValue = simpleDateFormat.format(date);
////        PrintLogging.printLog(context.getClass(), "StartDateTIme", dateTimeValue + "");
//        return dateTimeValue;
//    }

    public static String getCurrentDateTimeStamp(int type) {
        String formattedDate;
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        if (type == 1) {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            formattedDate = df.format(today);
            PrintLogging.printLog("AppCommonMethods", "", "printDatedate" + formattedDate + "-->>");
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            formattedDate = df.format(tomorrow);
            PrintLogging.printLog("AppCommonMethods", "", "printDatedate" + formattedDate + "-->>");

        }

        return getTimeStamp(formattedDate, type);
    }


    public static void getLanguage() {


    }

    public static void setCrashlyticsUserId(Activity activity) {
        if (UserInfo.getInstance(activity).getCpCustomerId() != null && !UserInfo.getInstance(activity).getCpCustomerId().equalsIgnoreCase(""))
            FirebaseCrashlytics.getInstance().setUserId(UserInfo.getInstance(activity).getCpCustomerId());

    }

    public static String getCarrier(Context activity) {
        String simOperatorName = "";
        TelephonyManager telemamanger = (TelephonyManager)
                activity.getSystemService(Context.TELEPHONY_SERVICE);

        simOperatorName = telemamanger.getNetworkOperatorName();
        return simOperatorName;
    }

    public static void setCleverTap(Activity context) {

        try {
            CleverTapAPI clevertapDefaultInstance =
                    CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver()));
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", UserInfo.getInstance(context).getFirstName());
            profileUpdate.put("Identity", UserInfo.getInstance(context).getCpCustomerId());
            profileUpdate.put("Email", UserInfo.getInstance(context).getEmail());
            profileUpdate.put("Phone", UserInfo.getInstance(context).getMobileNumber());
            profileUpdate.put("App Language", "English");
            clevertapDefaultInstance.onUserLogin(profileUpdate, AppCommonMethods.getDeviceId(context.getContentResolver()));
        } catch (Exception e) {

        }


    }

    public static void onUserRegister(Activity context) {
        try {
            CleverTapAPI clevertapDefaultInstance =
                    CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver()));
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("MSG-email", true);
            clevertapDefaultInstance.pushProfile(profileUpdate);
        } catch (Exception e) {

        }


    }

    public static void namePushCleverTap(Activity context, String name) {

        try {
            CleverTapAPI clevertapDefaultInstance =
                    CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver()));
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", name);
            profileUpdate.put("Identity", UserInfo.getInstance(context).getCpCustomerId());
            clevertapDefaultInstance.pushProfile(profileUpdate);
        } catch (Exception e) {

        }


    }

    public static void emailPushCleverTap(Activity context, String email) {

        try {
            CleverTapAPI clevertapDefaultInstance =
                    CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver()));
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Email", email);
            profileUpdate.put("Identity", UserInfo.getInstance(context).getCpCustomerId());
            clevertapDefaultInstance.pushProfile(profileUpdate);
        } catch (Exception e) {

        }


    }

    public static void mobilePushCleverTap(Activity context, String phone) {

        try {
            CleverTapAPI clevertapDefaultInstance =
                    CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver()));
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Phone", phone);
            profileUpdate.put("Identity", UserInfo.getInstance(context).getCpCustomerId());
            clevertapDefaultInstance.pushProfile(profileUpdate);
        } catch (Exception e) {

        }


    }

    public static void removeUserPrerences(Context context) {
        UserInfo.getInstance(context).setUserName("");
        UserInfo.getInstance(context).setMaxis(false);
        UserInfo.getInstance(context).setVip(false);
        UserInfo.getInstance(context).setHouseHoldError(false);
        UserInfo.getInstance(context).setCpCustomerId("");
        UserInfo.getInstance(context).setLastName("");
        UserInfo.getInstance(context).setEmail("");
        UserInfo.getInstance(context).setMobileNumber("");
        UserInfo.getInstance(context).setSocialLogin(false);
        UserInfo.getInstance(context).setAlternateUserName("");
        UserInfo.getInstance(context).setAccessToken("");
        UserInfo.getInstance(context).setRefreshToken("");
        UserInfo.getInstance(context).setExternalSessionToken("");
        UserInfo.getInstance(context).setActive(false);
        new KsPreferenceKey(context).setStartSessionKs("");

    }

    public static String getAssetType(Integer assetType, Context context) {
        String type = "";
        if (assetType == MediaTypeConstant.getMovie(context)) {
            type = "Movie";
        } else if (assetType == MediaTypeConstant.getWebEpisode(context)) {
            type = "Episode";
        } else if (assetType == MediaTypeConstant.getWebSeries(context)) {
            type = "Series";
        } else if (assetType == MediaTypeConstant.getHighlight(context)) {
            type = "Highlight";
        } else if (assetType == MediaTypeConstant.getTrailer(context)) {
            type = "Trailer";
        } else if (assetType == MediaTypeConstant.getLinear(context)) {
            type = "Linear";
        } else if (assetType == MediaTypeConstant.getProgram(context)) {
            type = "Program";
        }

        return type;
    }

    public static void updateLanguage(String language, Context context) {
        try {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Resources res = context.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
            new KsPreferenceKey(context).setAppLangName(language);
        } catch (Exception exc) {

        }
    }

    public static String getEndTime(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaaa", Locale.US);
            sdf.setTimeZone(tz);
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public static String getFirebaseDate(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
            sdf.setTimeZone(tz);
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public static int getEpisodeNumber(Map<String, Value> metas) {
        int episodeNumber = -1;
        try {
            Set keys = metas.keySet();
            Iterator itr = keys.iterator();

            String key;
            while (itr.hasNext()) {
                key = (String) itr.next();
                if (key.equalsIgnoreCase("EpisodeNumber")) {
                    DoubleValue doubleValue = (DoubleValue) metas.get(key);
                    PrintLogging.printLog("", "metavaluess--" + key + " - " + doubleValue.getValue());
                    episodeNumber = doubleValue.getValue().intValue();
                }
            }
        } catch (Exception ignored) {

        }
        return episodeNumber;
    }

    public static String setTime(Asset asset, int type) {
        String programTime = "";
        Long _time;
        try {
            if (type == 1) {
                _time = asset.getStartDate();
            } else {
                _time = asset.getEndDate();
            }

            Date date = new Date(_time * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd=hh:mma", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String dateTimeValue = simpleDateFormat.format(date);
            String _value[] = dateTimeValue.split("=");
            programTime = _value[1];

        } catch (Exception e) {

            PrintLogging.printLog("Exception", "", "" + e);
        }
        return programTime;
    }

    public static String getAssetHistory(Context context) {
        String assetHistoryDays = "";
        ResponseDmsModel responseDmsModel = callpreference(context);
        if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getAssetHistoryDays() != null && responseDmsModel.getParams().getAssetHistoryDays().getDays() != null && !responseDmsModel.getParams().getAssetHistoryDays().getDays().equalsIgnoreCase("")) {
            assetHistoryDays = responseDmsModel.getParams().getAssetHistoryDays().getDays();
        }
        return assetHistoryDays;
    }

    public static Calendar getDate(Asset asset) {
        String programTime = "";
        String month = "";
        String dd = "";
        String year = "";
        Calendar c = null;
        StringBuilder stringBuilder = new StringBuilder();
        Long _time;
        try {

            _time = asset.getStartDate();
            Date date = new Date(_time * 1000L);
            c = Calendar.getInstance();
            c.setTime(date);
//
//           month = checkDigit(c.get(Calendar.MONTH));
//           dd =
//           (c.get(Calendar.DATE));
//           year = checkDigit(c.get(Calendar.YEAR));

//         stringBuilder.append(month).append("_");
//         stringBuilder.append(dd).append("_");
//         stringBuilder.append(year);
//         programTime = stringBuilder.toString();

        } catch (Exception e) {

            PrintLogging.printLog("Exception", "", "" + e);
        }
        return c;
    }

    public static String getCurrentTimeStamp() {

        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }

    public static Long getCurrentTimeStampLong() {

        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong;
    }


    public static String getNextDateTimeStamp(int type, int dateIndex) {
        String formattedDate;
        int nextDay = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, dateIndex);
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, nextDay);
        Date tomorrow = calendar.getTime();

        if (type == 1) {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(today);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(tomorrow);
        }

        calendar.clear();
        return getTimeStamp(formattedDate, type);
    }

    public static String getDateFromTimeStamp(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());

            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public static String getDateCleverTap(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DDTHH:MM:SSZ", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());

            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    private static String getTimeStamp(String todayDate, int type) {
        /*Calendar currnetDateTime = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss a");
        String  currentTime = df.format(currnetDateTime.getTime());
        Log.w("currentTime-->>",currentTime);*/
        long timestamp = 0;
        String startTime = " 00:00:00 AM";
        String dateStr;
        if (type == 1) {
            dateStr = todayDate + startTime;
        } else {
            dateStr = todayDate + startTime;
        }

        DateFormat readFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.US);
        DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = null;
        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        if (date != null) {
            long output = date.getTime() / 1000L;
            String str = Long.toString(output);
            timestamp = Long.parseLong(str);
            PrintLogging.printLog("AppCommonMethods", "", "printDatedate" + formattedDate + "-->>" + timestamp);
            System.out.println(formattedDate);
        }


        return String.valueOf(timestamp);
    }

    public static String getProgramDurtion(String endTime, String stTime) {
        try {


            String programTimeArr[] = endTime.split(":");
            String currentTimeArr[] = stTime.split(":");

            String progTimeHour = programTimeArr[0];
            String progTimeMins = programTimeArr[1];
            String arr[] = progTimeMins.split(" ");
            String progTimeMin = arr[0];

            String currTimeHour = currentTimeArr[0];
            String currTimeMins = currentTimeArr[1];
            String arr2[] = currTimeMins.split(" ");
            String currTimeMin = arr2[0];


            if (Integer.parseInt(progTimeHour) < Integer.parseInt(currTimeHour)) {
                if (Integer.parseInt(progTimeHour) == 00) {
                    progTimeHour = "24";
                } else {
                    progTimeHour = String.valueOf(24 + Integer.parseInt(progTimeHour));
                }

                if (Integer.parseInt(progTimeMin) == 00) {
                    progTimeMin = "00";
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date date2 = simpleDateFormat.parse(progTimeHour + ":" + progTimeMin);
                Date date1 = simpleDateFormat.parse(currTimeHour + ":" + currTimeMin);

                long difference = date2.getTime() - date1.getTime();

                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

                if (min > 0) {
                    // hours = hours - 1;
                }

                hours = (hours < 0 ? 0 : hours);


                if (hours == 0) {
                    if (min == 0) {
                        progDuration = String.valueOf(min) + " min";
                    } else if (min == 1) {
                        progDuration = String.valueOf(min) + " min";
                    } else {
                        progDuration = String.valueOf(min) + " mins";
                    }
                } else if (hours == 1) {
                    if (min == 0) {
                        progDuration = String.valueOf(hours) + " hr";
                    } else if (min == 1) {
                        progDuration = String.valueOf(hours) + " hr" + " : " + String.valueOf(min) + " min";
                    } else {
                        progDuration = String.valueOf(hours) + " hr" + " : " + String.valueOf(min) + " mins";
                    }

                } else {
                    if (min == 0) {
                        progDuration = String.valueOf(hours) + " hr";
                    } else if (min == 1) {
                        progDuration = String.valueOf(hours) + " hrs" + " : " + String.valueOf(min) + " min";
                    } else {
                        progDuration = String.valueOf(hours) + " hrs" + " : " + String.valueOf(min) + " mins";
                    }

                }

            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date date2 = simpleDateFormat.parse(progTimeHour + ":" + progTimeMin);
                Date date1 = simpleDateFormat.parse(currTimeHour + ":" + currTimeMin);

                long difference = date2.getTime() - date1.getTime();
                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                hours = (hours < 0 ? -hours : hours);

                if (hours == 0) {
                    if (min == 0) {
                        progDuration = String.valueOf(min) + " min";
                    } else if (min == 1) {
                        progDuration = String.valueOf(min) + " min";
                    } else {
                        progDuration = String.valueOf(min) + " mins";
                    }
                } else if (hours == 1) {
                    if (min == 0) {
                        progDuration = String.valueOf(hours) + " hr";
                    } else if (min == 1) {
                        progDuration = String.valueOf(hours) + " hr" + " : " + String.valueOf(min) + " min";
                    } else {
                        progDuration = String.valueOf(hours) + " hr" + " : " + String.valueOf(min) + " mins";
                    }

                } else {
                    if (min == 0) {
                        progDuration = String.valueOf(hours) + " hr";
                    } else if (min == 1) {
                        progDuration = String.valueOf(hours) + " hrs" + " : " + String.valueOf(min) + " min";
                    } else {
                        progDuration = String.valueOf(hours) + " hrs" + " : " + String.valueOf(min) + " mins";
                    }

                }

            }


        } catch (Exception e) {
            // Log.e("erroroccured", e.toString());
        }
        return progDuration;
    }


    public static String getProgramTime(Asset asset, int type) {
        String programTime = "";
        Long _time;
        try {
            if (type == 1) {
                _time = asset.getStartDate();
            } else {
                _time = asset.getEndDate();
            }

            Date date = new Date(_time * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd=HH:mm a", Locale.US);
            String dateTimeValue = simpleDateFormat.format(date);
            String _value[] = dateTimeValue.split("=");
            programTime = _value[1];

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
        return programTime;
    }

    public static String getURLDuration(Asset asset) {
        int HLSPOs = 0;
        if (asset.getMediaFiles() != null && asset.getMediaFiles().size() > 0) {
            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                if (asset.getMediaFiles().get(i).getType().equals("Dash_widevine")) {
                    HLSPOs = i;
                }
            }
            long totalSecs = asset.getMediaFiles().get(HLSPOs).getDuration();
            long hours = (totalSecs / 3600);
            long mins = (totalSecs / 60) % 60;
            long secs = totalSecs % 60;
            String minsString = (mins == 0)
                    ? "00"
                    : ((mins < 10)
                    ? "0" + mins
                    : "" + mins);
            String secsString = (secs == 0)
                    ? "00"
                    : ((secs < 10)
                    ? "" + secs + " sec"
                    : "" + secs + " sec");
            if (hours > 0)
                return hours + " hr " + minsString + " min ";
            else if (mins > 0)
                return mins + " min";
            else return secsString;
        }
        return "";
    }

    public static String getDuration(Asset asset) {
        int HLSPOs = 0;
        if (asset.getMediaFiles() != null && asset.getMediaFiles().size() > 0) {
            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                if (asset.getMediaFiles().get(i).getType().equals("Dash_widevine")) {
                    HLSPOs = i;
                }
            }
            long totalSecs = asset.getMediaFiles().get(HLSPOs).getDuration();
            return totalSecs + "";
        }
        return "";
    }


    public static long getDurationFromUrl(Asset asset) {
        int HLSPOs = 0;
        long totalSecs = 0;
        if (asset.getMediaFiles() != null && asset.getMediaFiles().size() > 0) {
            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                if (asset.getMediaFiles().get(i).getType().equals("Dash_widevine")) {
                    HLSPOs = i;
                }
            }
            totalSecs = asset.getMediaFiles().get(HLSPOs).getDuration();
            return totalSecs;
        }
        return totalSecs;
    }

    public static String getPlayerUrl(Asset asset) {
        String url = "";
        if (asset.getMediaFiles() != null && asset.getMediaFiles().size() > 0) {
            for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                if (asset.getMediaFiles().get(i).getType().equals("Dash_widevine")) {
                    url = asset.getMediaFiles().get(i).getUrl();
                    break;
                }
            }
            return url;
        }
        return url;
    }


    static Uri dynamicLinkUri;

    public static void openShareDialog(final Activity activity, final Asset asset, Context context, String subMediaType) {


        try {
            String uri = createURI(asset, activity);
            String fallBackUrl = createFallBackUrl(asset, activity);
            Log.w("urivalue-->>", asset.getName() + "  " + uri);
            Log.w("urivalue-->>", asset.getName() + "  " + Uri.parse(uri));
/*
            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(uri))
                    .setDomainUriPrefix("https://stagingsott.page.link")
                    // Open links with this app on Android
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.astro.stagingsott")
                            .setMinimumVersion(1)
                            .build())
                    // Open links with com.example.ios on iOS
                    .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                    .buildDynamicLink();
*/


            //  Uri dynamicLinkUri = dynamicLink.getUri();


            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    //.setDomainUriPrefix("https://stagingsott.page.link/")
                    .setLink(Uri.parse(uri))
                    .setDomainUriPrefix(AppConstants.FIREBASE_DPLNK_PREFIX)
                    //.setLink(Uri.parse(uri))
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(AppConstants.FIREBASE_ANDROID_PACKAGE).setFallbackUrl(Uri.parse(fallBackUrl))
                            .build())
                    .setIosParameters(new DynamicLink.IosParameters.Builder(AppConstants.FIREBASE_IOS_PACKAGE).build())
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle(asset.getName())
                                    .setDescription(asset.getDescription())
                                    .setImageUrl(Uri.parse(AppCommonMethods.getSharingImage(activity, asset.getImages(), asset.getType())))
                                    .build())
                    .setNavigationInfoParameters(new DynamicLink.NavigationInfoParameters.Builder().setForcedRedirectEnabled(true)
                            .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener(activity, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {

                                dynamicLinkUri = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Log.w("dynamicUrl", dynamicLinkUri.toString() + flowchartLink);
                                try {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dynamicLinkUri != null) {
                                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                                sharingIntent.setType("text/plain");
                                                sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + asset.getName() + " " + activity.getResources().getString(R.string.on_Dialog) + "\n" + asset.getDescription() + "\n" + dynamicLinkUri.toString());
                                                // sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + asset.getName() + " " + activity.getResources().getString(R.string.on_Dialog) + "\n" + "https://stagingsott.page.link/?link="+dynamicLinkUri.toString()+"&apn=com.astro.stagingsott");

                                                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));

                                            }

                                        }
                                    });
                                } catch (Exception ignored) {

                                }


                            } else {
                                // Log.w("dynamicUrl",dynamicLinkUri.toString());
                            }
                        }
                    });

            shortLinkTask.toString();
            Log.w("urivalue-->>", asset.getName() + "  " + shortLinkTask.toString());

        } catch (Exception ignored) {

        }
    }

    private static String createFallBackUrl(Asset asset, Activity activity) {
        String uri = "";
        try {
            String assetId = asset.getId() + "";
            String assetType = asset.getType() + "";
            uri = Uri.parse(AppConstants.FIREBASE_DPLNK_FALLBACK_URL)
                    .buildUpon()
                    .appendQueryParameter("id", assetId)
                    .appendQueryParameter("mediaType", assetType)
                    .build().toString();

        } catch (Exception ignored) {
            uri = "";
        }

        return uri;
    }

    private static String createURI(Asset asset, Activity activity) {
        String uri = "";
        try {
            String assetId = asset.getId() + "";
            String assetType = asset.getType() + "";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority(AppConstants.FIREBASE_DPLNK_URL)
                    .appendPath("data")
                    .appendQueryParameter("id", assetId)
                    .appendQueryParameter("mediaType", assetType)
                    .appendQueryParameter("image", AppCommonMethods.getSharingImage(activity, asset.getImages(), asset.getType()))
                    .appendQueryParameter("name", asset.getName())
                    .appendQueryParameter("sd", asset.getDescription())
                    .appendQueryParameter("apn", AppConstants.FIREBASE_ANDROID_PACKAGE);
            uri = builder.build().toString();

        } catch (Exception ignored) {
            uri = "";
        }

        return uri;
    }


    private static String getSharingImage(Context context, List<MediaImage> mediaImage, Integer type) {
        String imageURL = "";
        for (int i = 0; i < mediaImage.size(); i++) {
            Log.w("imagevideo-->", mediaImage.get(i).getRatio() + "  " + type);
            if (type == MediaTypeConstant.getMovie(context)) {
                if (mediaImage.get(i).getRatio().equals("9x16")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.portrait_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.portrait_image_height) + AppLevelConstants.QUALITY;
                } else if (mediaImage.get(i).getRatio().equals("16x9")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                }

            } else {
                if (mediaImage.get(i).getRatio().equals("16x9")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                } else if (mediaImage.get(i).getRatio().equals("1:1")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.QUALITY;

                }

            }
        }

        return imageURL;
    }

    public static String getDeviceName(Context context) {
        String deviceName;
        if (TextUtils.isEmpty(Settings.System.getString(context.getContentResolver(), AppLevelConstants.DEVICE_NAME))) {
            deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
//            deviceName= android.os.Build.MODEL;
        } else {
            deviceName = Settings.System.getString(context.getContentResolver(), AppLevelConstants.DEVICE_NAME);
//            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//            deviceName = myDevice.getName();
//            Log.d("tfgh",deviceName+"");
//            deviceName = android.os.Build.MODEL;

        }
        return deviceName;
    }

    public static String getDeviceId(ContentResolver contentResolver) {
//        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//        String deviceName = myDevice.getName();
//        return deviceName;
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
    }

    public static String getFileIdOfAssest(Asset asset) {
        String fileId = "";

        for (int i = 0; i < asset.getMediaFiles().size(); i++) {
            String assetUrl = asset.getMediaFiles().get(i).getType();
            if (assetUrl.equals(AppLevelConstants.DASH_WIDEVINE)) {
                fileId = asset.getMediaFiles().get(i).getId().toString();
                PrintLogging.printLog("AppCommonMethods HD", "", "playerurl>>>" + fileId);
            }

        }
        return fileId;
    }


    public static Uri getImageURI(int resId, ImageView imageView) {
        Resources resources = imageView.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
    }

    public static String getDuration(List<Response<ListResponse<Asset>>> list, int position, int j, int k) {
        long totalSecs = list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getDuration();
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;
        String minsString = (mins == 0)
                ? "00"
                : ((mins < 10)
                ? "0" + mins
                : "" + mins);
        String secsString = (secs == 0)
                ? "00"
                : ((secs < 10)
                ? "" + secs + " sec"
                : "" + secs + " sec");
        if (hours > 0)
            return hours + ":" + minsString + ":" + secsString;
        else if (mins > 0)
            return mins + " min";
        else return secsString;
    }

    public static void getImageList(Context context, String tileType, int position, int j, int k, List<Response<ListResponse<Asset>>> list, AssetCommonImages assetCommonImages, List<AssetCommonImages> imagesList) {
        if (context instanceof HomeActivity) {
            Log.w("imageType-->>", tileType);
        }
        switch (tileType) {
            case AppLevelConstants.TYPE2:

                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.circle_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.circle_image_height) + AppLevelConstants.QUALITY;
                        PrintLogging.printLog("AppCommonMethods", "", "imageCondition" + image_url + AppLevelConstants.WIDTH + context.getResources().getDimension(R.dimen.circle_image_width) + AppLevelConstants.HEIGHT + context.getResources().getDimension(R.dimen.circle_image_height) + AppLevelConstants.QUALITY);
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }
                }
                break;
            case AppLevelConstants.TYPE3:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.portrait_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.portrait_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
            case AppLevelConstants.TYPE4:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("1:1")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
            case AppLevelConstants.TYPE5:

                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPELDS:

                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        } else if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2x3")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPEPOSTER:
                Log.w("ImageRatio-->>", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2x3")) {
                            Log.w("ImageRatio-->>in", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in", final_url);
                            Log.w("ImageUrl-->>in", image_url);
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPEPR2:
                Log.w("ImageRatio-->>", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2x3")) {
                            Log.w("ImageRatio-->>in", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in", final_url);
                            Log.w("ImageUrl-->>in", image_url);
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;
            case AppLevelConstants.TYPE6:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.portrait_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.portrait_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    } else if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
            case AppLevelConstants.TYPE7:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("1:1")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
        }

    }

    public static void getCategoryImageList(Context context, String tileType, int position, int j, int k, List<Response<ListResponse<Asset>>> list, AssetCommonImages assetCommonImages, List<AssetCommonImages> imagesList, VIUChannel channelList) {
        Log.w("imageType-->>", tileType + " ----  " + channelList.getKalturaOTTImageType());
        switch (tileType) {
            case AppLevelConstants.TYPE2:

                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.circle_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.circle_image_height) + AppLevelConstants.QUALITY;
                        PrintLogging.printLog("AppCommonMethods", "", "imageCondition" + image_url + AppLevelConstants.WIDTH + context.getResources().getDimension(R.dimen.circle_image_width) + AppLevelConstants.HEIGHT + context.getResources().getDimension(R.dimen.circle_image_height) + AppLevelConstants.QUALITY);
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }
                }
                break;
            case AppLevelConstants.TYPE3:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.portrait_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.portrait_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
            case AppLevelConstants.TYPE4:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("1:1")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
            case AppLevelConstants.TYPE5:

                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (channelList != null && channelList.getKalturaOTTImageType() != null && !channelList.getKalturaOTTImageType().equalsIgnoreCase("")) {
                            if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals(channelList.getKalturaOTTImageType())) {
                                String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                                String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                                assetCommonImages.setImageUrl(final_url);
                                imagesList.add(assetCommonImages);
                            }
                        } else {
                            if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                                String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                                String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                                assetCommonImages.setImageUrl(final_url);
                                imagesList.add(assetCommonImages);
                            }
                        }

                    } else {
                        if (channelList != null && channelList.getKalturaOTTImageType() != null && !channelList.getKalturaOTTImageType().equalsIgnoreCase("")) {
                            if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals(channelList.getKalturaOTTImageType())) {
                                String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                                String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                                assetCommonImages.setImageUrl(final_url);
                                imagesList.add(assetCommonImages);
                            }

                        } else {
                            if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                                String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                                String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                                assetCommonImages.setImageUrl(final_url);
                                imagesList.add(assetCommonImages);
                            }

                        }
                    }
                }


                break;

            case AppLevelConstants.TYPELDS:

                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        } else if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2x3")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPEPOSTER:
                Log.w("ImageRatio-->>", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (channelList != null && channelList.getKalturaOTTImageType() != null && !channelList.getKalturaOTTImageType().equalsIgnoreCase("")) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals(channelList.getKalturaOTTImageType())) {
                            Log.w("ImageRatio-->>in", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in", final_url);
                            Log.w("ImageUrl-->>in", image_url);
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2x3")) {
                            Log.w("ImageRatio-->>in", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in", final_url);
                            Log.w("ImageUrl-->>in", image_url);
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPEPR2:
                Log.w("ImageRatio-->>", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9") || list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("9:16")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2x3")) {
                            Log.w("ImageRatio-->>in", list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in", final_url);
                            Log.w("ImageUrl-->>in", image_url);
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;
            case AppLevelConstants.TYPE6:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (channelList != null && channelList.getKalturaOTTImageType() != null && !channelList.getKalturaOTTImageType().equalsIgnoreCase("")) {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        } else {
                            if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals(channelList.getKalturaOTTImageType())) {
                                String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                                String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.portrait_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.portrait_image_height) + AppLevelConstants.QUALITY;
                                assetCommonImages.setImageUrl(final_url);
                                imagesList.add(assetCommonImages);
                            }
                        }
                    } else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16x9")) {
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }
                break;
            case AppLevelConstants.TYPE7:
                if (list.get(position).results.getObjects().get(j).getImages().size() > 0) {
                    if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("1:1")) {
                        String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                        assetCommonImages.setImageUrl(final_url);
                        imagesList.add(assetCommonImages);
                    }

                }
                break;
        }

    }


    public static List<AssetHistory> getContinueWatchingPreferences(Context applicationContext) {
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(applicationContext);

        Gson gson = new Gson();
        String json = sharedPrefHelper.getString(AppLevelConstants.C_W_PREFERENCES, "");
        Type type = new TypeToken<ArrayList<AssetHistory>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static int getVersion(Context context) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
        return v;
    }

    public static void checkDMS(Context context, DMSCallBack callBack) {
        if (SharedPrefHelper.getInstance(context).getString("DMS_Date", "").equals("")) {
            new KsServices(context).hitApiDMSWithInner(callBack);
        } else {
            callBack.configuration(true);
        }
    }

    public static ResponseDmsModel callpreference(Context context) {
        try {
            Gson gson = new Gson();
            String json = SharedPrefHelper.getInstance(context).getString("DMS_Response", "");
            return gson.fromJson(json, ResponseDmsModel.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setContinueWatchingPreferences(List<AssetHistory> objects, Context activity) {
        List<AssetHistory> object = prepaireList(objects);
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(activity);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPrefHelper.setString(AppLevelConstants.C_W_PREFERENCES, json);

    }


    private static List<AssetHistory> prepaireList(List<AssetHistory> objects) {
        List<AssetHistory> assetHistories = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getPosition() != 0 || !objects.get(i).getFinishedWatching()) {

                assetHistories.add(objects.get(i));
            }
        }

        return assetHistories;
    }

    public static int getRailTypeAccToMedia(Context context, int layoutPosition, List<AssetCommonBean> dataList, int position) {
        Asset asset = dataList.get(layoutPosition).getSlides().get(position).getObjects();
        if (asset.getType() == MediaTypeConstant.getMovie(context)) {
            return AppLevelConstants.Rail3;
        } else if (asset.getType() == MediaTypeConstant.getWebEpisode(context)) {
            return AppLevelConstants.Rail3;
        } else {
            return AppLevelConstants.Rail5;
        }
    }


    public static boolean isAppInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }

        return false;
    }

    public static void openUrl(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        if (i.resolveActivity(context.getPackageManager()) != null) {

            context.startActivity(i);
        }
    }

    public static void setContentPreferences(ArrayList<PrefrenceBean> arrayList, Context applicationContext) {
//        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(applicationContext);
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        SharedPrefHelper.getInstance(applicationContext).setString(PrefConstant.CONTENT_PREFERENCES, json);
    }

    public static ArrayList<PrefrenceBean> getContentPrefences(Context applicationContext) {
//        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(applicationContext);

        Gson gson = new Gson();
        String json = SharedPrefHelper.getInstance(applicationContext).getString(PrefConstant.CONTENT_PREFERENCES, "");
        Type type = new TypeToken<ArrayList<PrefrenceBean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void setLogoutContentPreferences(ArrayList<PrefrenceBean> arrayList, Context applicationContext) {
//        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(applicationContext);
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        SharedPrefHelper.getInstance(applicationContext).setString(PrefConstant.LOGOUT_CONTENT_PREFERENCES, json);
    }

    public static ArrayList<PrefrenceBean> getLogoutContentPrefences(Context applicationContext) {
//        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(applicationContext);

        Gson gson = new Gson();
        String json = SharedPrefHelper.getInstance(applicationContext).getString(PrefConstant.LOGOUT_CONTENT_PREFERENCES, "");
        Type type = new TypeToken<ArrayList<PrefrenceBean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void requestFocus(Context context, View view) {
        if (view.requestFocus()) {
            ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void setContinueWatchingIDsPreferences(List<Long> objects, Context activity) {
//        List<Long> object = objects;
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(activity);
        Gson gson = new Gson();
        String json = gson.toJson(objects);
        sharedPrefHelper.setString(AppLevelConstants.C_W_IDS_PREFERENCES, json);

    }

    public static ArrayList<Long> getContinueWatchingIDsPreferences(Context applicationContext) {
        try {
            SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance((applicationContext));
            Gson gson = new Gson();
            String json = sharedPrefHelper.getString(AppLevelConstants.C_W_IDS_PREFERENCES, "");
            Type type = new TypeToken<ArrayList<Long>>() {
            }.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
            return null;
        }
    }

    public static long getChannelID(Context context, int tab) {
        long tabID = 0;
        if (tab == AppLevelConstants.TAB_HOME) {
            tabID = SubCategoriesPrefs.getInstance(context).getHomeTabId();
        } else if (tab == AppLevelConstants.TAB_LIVE) {
            tabID = SubCategoriesPrefs.getInstance(context).getLiveTabId();

        } else if (tab == AppLevelConstants.TAB_VIDEO) {
            tabID = SubCategoriesPrefs.getInstance(context).getVideoTabId();

        } else if (tab == AppLevelConstants.TAB_MOVIE_DESCRIPTION) {
            tabID = SubCategoriesPrefs.getInstance(context).getMovieDetailsId();


        } else if (tab == AppLevelConstants.TAB_SHORTFILM_DESCRIPTION) {
            tabID = SubCategoriesPrefs.getInstance(context).getShortVideoDetailsId();

        } else if (tab == AppLevelConstants.TAB_DRAMA_DETAILS) {
            tabID = SubCategoriesPrefs.getInstance(context).getDramaDetailsId();
        } else if (tab == AppLevelConstants.TAB_DRAMA_EPISODES_DETAILS) {
            tabID = SubCategoriesPrefs.getInstance(context).getDramaEpisodeDetailsId();
        } else if (tab == AppLevelConstants.TAB_LIVETV_DETAIL) {
            tabID = SubCategoriesPrefs.getInstance(context).getLiveTvDetailsId();
        } else if (tab == AppLevelConstants.TAB_FORWARDED_EPG_DETAIL) {
            tabID = SubCategoriesPrefs.getInstance(context).getForwardEpgId();
        }

        return tabID;
    }

    public static void setImages(RailCommonData railCommonData, Context context, ImageView webseriesimage) {
        try {
            if (railCommonData == null) {
                return;
            }
            if (railCommonData.getObject().getImages().size() > 0) {
                for (int i = 0; i < railCommonData.getObject().getImages().size(); i++) {
                    if (railCommonData.getObject().getImages().get(i).getRatio().equals("16x9")) {
                        String image_url = railCommonData.getObject().getImages().get(i).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.detail_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                        ImageHelper.getInstance(webseriesimage.getContext()).loadImageToPotrait(webseriesimage, final_url, R.drawable.square1);
                    }
                }
            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "" + e);
        }

    }

    public static Long getGenreID(String name) {
        long genreID = 0;
        if (name.equalsIgnoreCase(AppConstants.Vlog_Genre)) {
            genreID = AppConstants.Vlog;
        } else if (name.equalsIgnoreCase(AppConstants.Science_Genre)) {
            genreID = AppConstants.Science;
        } else if (name.equalsIgnoreCase(AppConstants.Sports_Genre)) {
            genreID = AppConstants.Sports;
        } else if (name.equalsIgnoreCase(AppConstants.Crime_Genre)) {
            genreID = AppConstants.Crime;
        } else if (name.equalsIgnoreCase(AppConstants.Chat_Shows_Genre)) {
            genreID = AppConstants.Chat_Shows;
        } else if (name.equalsIgnoreCase(AppConstants.Biopic_Genre)) {
            genreID = AppConstants.Biopic;
        } else if (name.equalsIgnoreCase(AppConstants.Family_Genre)) {
            genreID = AppConstants.Family;
        } else if (name.equalsIgnoreCase(AppConstants.Kids_Genre)) {
            genreID = AppConstants.Kids;
        } else if (name.equalsIgnoreCase(AppConstants.Documentary_Genre)) {
            genreID = AppConstants.Documentary;
        } else if (name.equalsIgnoreCase(AppConstants.Reality_Genre)) {
            genreID = AppConstants.Reality;
        } else if (name.equalsIgnoreCase(AppConstants.Horror_Genre)) {
            genreID = AppConstants.Horror;
        } else if (name.equalsIgnoreCase(AppConstants.Action_Genre)) {
            genreID = AppConstants.Action;
        } else if (name.equalsIgnoreCase(AppConstants.Thriller_Genre)) {
            genreID = AppConstants.Thriller;
        } else if (name.equalsIgnoreCase(AppConstants.Romance_Genre)) {
            genreID = AppConstants.Romance;
        } else if (name.equalsIgnoreCase(AppConstants.Drama_Genre)) {
            genreID = AppConstants.Drama;
        } else if (name.equalsIgnoreCase(AppConstants.Lifestyle_Genre)) {
            genreID = AppConstants.Lifestyle;
        } else if (name.equalsIgnoreCase(AppConstants.Comedy_Genre)) {
            genreID = AppConstants.Comedy;
        }
        return genreID;
    }


    public static String get24HourTime(Asset asset, int type) {
        String dateTimeValue = "";
        try {
            if (type == 1) {
                _time = asset.getStartDate();
            } else {
                _time = asset.getEndDate();
            }

            Date date = new Date(_time * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            dateTimeValue = simpleDateFormat.format(date);


        } catch (Exception e) {

            PrintLogging.printLog("Exception", "" + e);
        }
        return dateTimeValue;
    }

    public static String get24HourTime1(Asset asset, int type) {
        String dateTimeValue = "";
        try {
            if (type == 1) {
                _time = asset.getStartDate();
            } else {
                _time = asset.getEndDate();
            }

            Date date = new Date(_time * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            dateTimeValue = simpleDateFormat.format(date);


            Calendar c = Calendar.getInstance();
            c.setTime(date); // Now use today date.
            // c.add(Calendar.DATE, i); // Adding 5 days
            String output = simpleDateFormat.format(c.getTime());

            String month = checkDigit(c.get(Calendar.MONTH));
            String dd = checkDigit(c.get(Calendar.DATE));
            String year = checkDigit(c.get(Calendar.YEAR));

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(month).append(" ").append(dd).append(" ").append(year).append(" ").append(dateTimeValue);

            dateTimeValue = stringBuilder.toString();

            PrintLogging.printLog("", "currentDateIs" + dateTimeValue);


        } catch (Exception e) {

            PrintLogging.printLog("Exception", "" + e);
        }
        return dateTimeValue;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public static DecodedJWT jwtVerification(String token) {
        String txnId = null;
        DecodedJWT jwt = null;
        try {
            KeySpec keySpecPublic = new X509EncodedKeySpec(Base64.decode(AppConstants.JWT_PUBLIC_KEY, Base64.DEFAULT));
            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance(AppLevelConstants.JWT_ALGO).generatePublic(keySpecPublic);
            Algorithm publicKeyAlgo = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(publicKeyAlgo).withIssuer(AppLevelConstants.JWT_ISSUER).build();
            jwt = verifier.verify(token);
            Log.e("JWT TOKEN", token);
        } catch (InvalidKeySpecException ignored) {
            Log.e("VERIFICATION NI HUA", ignored.getMessage());
            ignored.printStackTrace();
        } catch (NoSuchAlgorithmException ignored) {
            Log.e("VERIFICATION NI HUA", ignored.getMessage());
            ignored.printStackTrace();
        } catch (Exception ignored) {
            Log.e("VERIFICATION NI HUA", ignored.getMessage());
            ignored.printStackTrace();
        }
        return jwt;
    }

    public static String getCteatorName(String name) {
        String value = "";
        try {
            if (name != null) {
                if (!name.equals("")) {
                    name = name.trim().replaceAll("\\s+", " ");
                    if (name.contains(" ")) {
                        String words[] = name.split(" ");
                        if (words.length != 0) {
                            String firstWord = String.valueOf(words[0].charAt(0)).toUpperCase();
                            if (words.length == 1) {
                                value = firstWord;
                            } else {
                                String secondWord = String.valueOf(words[1].charAt(0)).toUpperCase();
                                value = firstWord + secondWord;
                            }
                        }

                    } else {
                        if (name.length() > 2) {
                            value = String.valueOf(name.charAt(0)).toUpperCase() + "" + String.valueOf(name.charAt(1)).toUpperCase();
                        } else {
                            value = String.valueOf(name.charAt(0)).toUpperCase() + "" + String.valueOf(name.charAt(1)).toUpperCase();
                        }
                    }
                }
            }

        } catch (Exception e) {

            PrintLogging.printLog("Exception", "" + e);
        }

        return value;
    }

    public static int convertDpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int px = dp * (metrics.densityDpi / 160);
        return Math.round(px);
    }

    public static String getCteatorNameCaps(String name) {
        String st = "";
        if (name != null) {
            if (!name.equalsIgnoreCase("")) {
                char ch[] = name.toCharArray();
                for (int i = 0; i < name.length(); i++) {

                    // If first character of a word is found
                    if (i == 0 && ch[i] != ' ' ||
                            ch[i] != ' ' && ch[i - 1] == ' ') {

                        // If it is in lower-case
                        if (ch[i] >= 'a' && ch[i] <= 'z') {

                            // Convert into Upper-case
                            ch[i] = (char) (ch[i] - 'a' + 'A');
                        }
                    }

                    // If apart from first character
                    // Any one is in Upper-case
                    else if (ch[i] >= 'A' && ch[i] <= 'Z')

                        // Convert into Lower-Case
                        ch[i] = (char) (ch[i] + 'a' - 'A');
                }

                // Convert the char array to equivalent String
                st = new String(ch);

            } else {
                st = "";
            }
        } else {
            st = "";
        }
        return st;
    }

    public static ArrayList<String> splitString(String temp, String delim) {
        StringBuilder stringBuilder;

        if (temp.contains("##") && (temp != null)) {
            stringBuilder = new StringBuilder(temp.replace("##", "#StringTokenizer#"));
        } else {
            stringBuilder = new StringBuilder(temp);
        }

        ArrayList<String> mDetailList = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(stringBuilder.toString(), delim, true);
        boolean expectDelim = false;
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            if (!token.equalsIgnoreCase(delim)) {
                if (token.equalsIgnoreCase("StringTokenizer"))
                    mDetailList.add("0");
                else mDetailList.add(token.trim());
            }
            if (delim.equals(token)) {
                if (expectDelim) {
                    expectDelim = false;
                    continue;
                } /*else {
                    // unexpected delim means empty token
                    token = null;
                }*/
            }
            expectDelim = true;
        }

        return mDetailList;
    }


    public static void setBillingUi(ImageView imageView, Map<String, MultilingualStringValueArray> tags, Integer type, Activity mContext) {
        try {
            if (type == MediaTypeConstant.getSeries(mContext) || type == MediaTypeConstant.getCollection(mContext) || type == MediaTypeConstant.getLinear(mContext)) {
                if (AssetContent.getBillingIdForSeries(tags)) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            } else {
                if (AssetContent.getBillingId(tags)) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {

        }

    }

    public static void handleTitleDesc(RelativeLayout titleLayout, TextView tvTitle, TextView tvDescription, BaseCategory baseCategory, RailCommonData commonData, Context context) {
        try {
            if (baseCategory != null) {
                if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_ONLY.name())) {
                    titleLayout.setVisibility(View.VISIBLE);
                } else if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.CUS.name())) {
                    if (commonData.getObject().getType() == MediaTypeConstant.getProgram(context)) {
                        tvDescription.setVisibility(View.VISIBLE);
                    } else if (commonData.getObject().getType() == MediaTypeConstant.getLinear(context)) {
                        if (AssetContent.isLiveEvent(commonData.getObject().getMetas())) {
                            tvDescription.setVisibility(View.VISIBLE);
                        }
                    }
                    titleLayout.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    //titleLayout.setVisibility(View.VISIBLE);
                    if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_TITLE.name())) {
                        titleLayout.setVisibility(View.VISIBLE);
                        tvTitle.setVisibility(View.VISIBLE);
                    } else {
                        if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_TITLE_DESC.name())) {
                            titleLayout.setVisibility(View.VISIBLE);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvDescription.setVisibility(View.VISIBLE);
                        } else {
                            titleLayout.setVisibility(View.GONE);
                            tvTitle.setVisibility(View.GONE);
                            tvDescription.setVisibility(View.GONE);
                        }
                    }

                }

            }
        } catch (Exception ignored) {
            titleLayout.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);
        }
    }

    public static void handleTitleDesc(LinearLayout titleLayout, TextView tvTitle, TextView tvDescription, BaseCategory baseCategory, RailCommonData commonData, Context context) {
        try {
            if (baseCategory != null) {
                if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_ONLY.name())) {
                    titleLayout.setVisibility(View.VISIBLE);
                } else if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.CUS.name())) {
                    if (commonData.getObject().getType() == MediaTypeConstant.getProgram(context)) {
                        tvDescription.setVisibility(View.VISIBLE);
                    }
                    titleLayout.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    //titleLayout.setVisibility(View.VISIBLE);
                    if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_TITLE.name())) {
                        titleLayout.setVisibility(View.VISIBLE);
                        tvTitle.setVisibility(View.VISIBLE);
                    } else {
                        if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_TITLE_DESC.name())) {
                            titleLayout.setVisibility(View.VISIBLE);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvDescription.setVisibility(View.VISIBLE);
                        } else {
                            titleLayout.setVisibility(View.GONE);
                            tvTitle.setVisibility(View.GONE);
                            tvDescription.setVisibility(View.GONE);
                        }
                    }

                }

            }
        } catch (Exception ignored) {
            titleLayout.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);
        }
    }

    public static boolean isXofferWindow(String xofferValue) {
        String[] splitString = xofferValue.split(";");
        String[] splitStartTime;
        String[] splitEndTime;

        String startTime;
        String endTime;

        if (splitString[0] != null && splitString[1] != null && !splitString[0].equalsIgnoreCase("") && !splitString[1].equalsIgnoreCase("")) {
            splitStartTime = splitString[0].split("=");
            splitEndTime = splitString[1].split("=");

            if (splitStartTime[1] != null && !splitStartTime[1].equals("") && splitEndTime[1] != null && !splitEndTime[1].equals("")) {
                startTime = splitStartTime[1];
                endTime = splitEndTime[1];

                return compareStartEndTime(startTime, endTime);


            } else {
                return false;
            }


        } else {
            return false;
        }


    }

    public static boolean compareStartEndTime(String startTime, String endTime) {
        Date currentDate = new Date();
        Date endDate = new Date();
        Date startDate = new Date();


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getDefault());
        String currentTime = inputFormat.format(today);
        try {
            currentDate = inputFormat.parse(currentTime);
            startDate = inputFormat.parse(startTime);
            endDate = inputFormat.parse(endTime);

            if (currentDate.before(endDate) && currentDate.after(startDate)) {
                return true;
            } else {
                return false;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean comparePlaybackStartEndTime(String startTime, String endTime) {
        Date currentDate = new Date();
        Date endDate = new Date();
        Date startDate = new Date();


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getDefault());
        String currentTime = inputFormat.format(today);
        try {
            currentDate = inputFormat.parse(currentTime);
            startDate = inputFormat.parse(startTime);
            endDate = inputFormat.parse(endTime);

            if (currentDate.before(endDate) && currentDate.after(startDate)) {
                return true;
            } else {
                return false;

            }
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static String getSearchFieldsKsql(String searchString, String selectedGenre, int from, Context context) {
        // "(or name ~'collection' description ~'collection' director~'collection' Keywords~'collection' Actors~'collection'
//        Log.w("selectedGenre",selectedGenre);
        if (from == 1) {
            StringBuilderHolder.getInstance().clear();
            StringBuilderHolder.getInstance().append("(or name~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("description~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("director~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("Keywords~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("Actors~'");
            StringBuilderHolder.getInstance().append(searchString);

            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append("' (and ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
                StringBuilderHolder.getInstance().append("");
            } else {
                if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append("' (and ");
                    StringBuilderHolder.getInstance().append(selectedGenre);
                    StringBuilderHolder.getInstance().append("");
                } else {
                    StringBuilderHolder.getInstance().append("'");
                }
            }

            if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")) {
                if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append(" ");
                    StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                    StringBuilderHolder.getInstance().append("))");
                } else {
                    StringBuilderHolder.getInstance().append(" (and ");
                    StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                    StringBuilderHolder.getInstance().append("))");
                }

            } else {
                if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append("))");
                } else {
                    if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                        StringBuilderHolder.getInstance().append("))");
                    } else {
                        StringBuilderHolder.getInstance().append(")");
                    }

                }
            }


         /*   if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")){
                StringBuilderHolder.getInstance().append("' (and ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
                if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")){
                    StringBuilderHolder.getInstance().append(") (and ");
                    StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                    StringBuilderHolder.getInstance().append("))");
                }else {
                    StringBuilderHolder.getInstance().append("))");
                }

            }else {
                if (selectedGenre!=null && !selectedGenre.equalsIgnoreCase("")){
                    StringBuilderHolder.getInstance().append("' (and ");
                    StringBuilderHolder.getInstance().append(selectedGenre);
                    StringBuilderHolder.getInstance().append("))");
                }else {
                    StringBuilderHolder.getInstance().append("')");
                }
            }
*/

        } else {
            StringBuilderHolder.getInstance().clear();
            StringBuilderHolder.getInstance().append("(or name~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("description~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("director~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("Keywords~'");
            StringBuilderHolder.getInstance().append(searchString);
            StringBuilderHolder.getInstance().append("'");

            StringBuilderHolder.getInstance().append("Actors~'");
            StringBuilderHolder.getInstance().append(searchString);

            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append("' (and ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
                StringBuilderHolder.getInstance().append("");
            } else {
                if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append("' (and ");
                    StringBuilderHolder.getInstance().append(selectedGenre);
                    StringBuilderHolder.getInstance().append(" ");
                } else {
                    StringBuilderHolder.getInstance().append("'");
                }
            }

            if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")) {
                if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append(" ");
                    StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                    StringBuilderHolder.getInstance().append("))");
                } else {
                    StringBuilderHolder.getInstance().append(" (and ");
                    StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                    StringBuilderHolder.getInstance().append("))");
                }

            } else {
                if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append("))");
                } else {
                    if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                        StringBuilderHolder.getInstance().append("))");
                    } else {
                        StringBuilderHolder.getInstance().append(")");
                    }

                }
            }


            /*if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")){
                    StringBuilderHolder.getInstance().append("' (and ");
                    StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
                    if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")){
                        StringBuilderHolder.getInstance().append(") (and ");
                        StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                        StringBuilderHolder.getInstance().append("))");
                    }else {
                        StringBuilderHolder.getInstance().append("))");
                    }

            }else {
                if (selectedGenre!=null && !selectedGenre.equalsIgnoreCase("")){
                    StringBuilderHolder.getInstance().append("' (and ");
                    StringBuilderHolder.getInstance().append(selectedGenre);
                    StringBuilderHolder.getInstance().append("))");
                }else {
                    StringBuilderHolder.getInstance().append("')");
                }
            }*/


        }


        KsPreferenceKey.getInstance(context).setSearchKSQL(StringBuilderHolder.getInstance().getText().toString());
        return StringBuilderHolder.getInstance().getText().toString();
    }

    public static List<Asset> removePagesFromCollection(ListResponse<Asset> results, Context context) {
        List<Asset> sortedList = new ArrayList();
        BooleanValue sponsored;
        for (int i = 0; i < results.getObjects().size(); i++) {
            if (results.getObjects().get(i).getType() == MediaTypeConstant.getCollection(context)) {
                if (results.getObjects().get(i).getMetas() != null) {
                    sponsored = (BooleanValue) results.getObjects().get(i).getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                    if (sponsored != null) {
                        if (!sponsored.getValue()) {
                            sortedList.add(results.getObjects().get(i));
                        }

                    } else {
                        sortedList.add(results.getObjects().get(i));
                    }
                } else {
                    sortedList.add(results.getObjects().get(i));
                }
            } else {
                sortedList.add(results.getObjects().get(i));
            }

        }
        return sortedList;
    }

    public static List<Asset> addPagesFromCollection(ListResponse<Asset> results, Context context) {
        List<Asset> sortedList = new ArrayList();
        BooleanValue sponsored;
        for (int i = 0; i < results.getObjects().size(); i++) {
            if (results.getObjects().get(i).getType() == MediaTypeConstant.getCollection(context)) {
                if (results.getObjects().get(i).getMetas() != null) {
                    sponsored = (BooleanValue) results.getObjects().get(i).getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                    if (sponsored != null) {
                        if (sponsored.getValue()) {
                            sortedList.add(results.getObjects().get(i));
                        }

                    }
                }
            } else {
                sortedList.add(results.getObjects().get(i));
            }

        }
        return sortedList;
    }

    public static boolean isPages(Context context, Asset asset) {
        boolean isPages = false;
        BooleanValue sponsored;
        if (asset.getMetas() != null) {
            sponsored = (BooleanValue) asset.getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
            if (sponsored != null) {
                if (sponsored.getValue()) {
                    isPages = true;
                }
            }
        }
        return isPages;
    }

    public static List<Asset> applyFreePaidFilter(ListResponse<Asset> results, Context context) {
        List<Asset> sortedList = new ArrayList();
        BooleanValue sponsored = null;
        for (int i = 0; i < results.getObjects().size(); i++) {
            checkMediaType(sortedList, results.getObjects().get(i), context, sponsored);

            // sortedList.add(results.getObjects().get(i));
        }
        return sortedList;
    }

    private static void checkMediaType2(List<Asset> sortedList, Asset results, Context context, BooleanValue sponsored) {
        if (!KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase("")) {

            Map<String, MultilingualStringValueArray> tagMap = results.getTags();
            if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.ALL.name())) {
                sortedList.add(results);
            } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.FREE.name())) {
                MultilingualStringValueArray language_list = tagMap.get(AppLevelConstants.KEY_BILLING_ID);
                if (language_list != null) {
                    if (language_list.getObjects() != null && language_list.getObjects().size() > 0 && language_list.getObjects().get(0).getValue() != null
                            && !language_list.getObjects().get(0).getValue().equalsIgnoreCase("")) {

                    } else {
                        sortedList.add(results);
                    }
                } else {
                    sortedList.add(results);
                }
            } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.PAID.name())) {
                MultilingualStringValueArray language_list = tagMap.get(AppLevelConstants.KEY_BILLING_ID);
                if (language_list != null) {
                    if (language_list.getObjects() != null && language_list.getObjects().size() > 0 && language_list.getObjects().get(0).getValue() != null
                            && !language_list.getObjects().get(0).getValue().equalsIgnoreCase("")) {
                        sortedList.add(results);
                    }
                }
            }
        } else {
            sortedList.add(results);
        }

    }

    private static void checkMediaType(List<Asset> sortedList, Asset results, Context context, BooleanValue sponsored) {
        if (results.getType() == MediaTypeConstant.getMovie(context)
                || results.getType() == MediaTypeConstant.getCollection(context)) {
            if (!KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase("")) {

                Map<String, MultilingualStringValueArray> tagMap = results.getTags();
                if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.ALL.name())) {
                    if (results.getMetas() != null) {
                        sponsored = (BooleanValue) results.getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                        if (sponsored != null) {
                            if (sponsored.getValue() != null) {
                                if (!sponsored.getValue()) {
                                    sortedList.add(results);
                                }
                            } else {
                                sortedList.add(results);
                            }
                        } else {
                            sortedList.add(results);
                        }
                    } else {
                        sortedList.add(results);
                    }
                } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.FREE.name())) {
                    MultilingualStringValueArray language_list = tagMap.get(AppLevelConstants.KEY_BILLING_ID);
                    if (language_list != null) {
                        if (language_list.getObjects() != null && language_list.getObjects().size() > 0 && language_list.getObjects().get(0).getValue() != null
                                && !language_list.getObjects().get(0).getValue().equalsIgnoreCase("")) {

                        } else {
                            if (results.getMetas() != null) {
                                sponsored = (BooleanValue) results.getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                                if (sponsored != null) {
                                    if (sponsored.getValue() != null) {
                                        if (!sponsored.getValue()) {
                                            sortedList.add(results);
                                        }
                                    } else {
                                        sortedList.add(results);
                                    }
                                } else {
                                    sortedList.add(results);
                                }
                            } else {
                                sortedList.add(results);
                            }
                        }
                    } else {
                        if (results.getMetas() != null) {
                            sponsored = (BooleanValue) results.getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                            if (sponsored != null) {
                                if (sponsored.getValue() != null) {
                                    if (!sponsored.getValue()) {
                                        sortedList.add(results);
                                    }
                                } else {
                                    sortedList.add(results);
                                }
                            } else {
                                sortedList.add(results);
                            }
                        } else {
                            sortedList.add(results);
                        }
                    }
                } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.PAID.name())) {
                    MultilingualStringValueArray language_list = tagMap.get(AppLevelConstants.KEY_BILLING_ID);
                    if (language_list != null) {
                        if (language_list.getObjects() != null && language_list.getObjects().size() > 0 && language_list.getObjects().get(0).getValue() != null
                                && !language_list.getObjects().get(0).getValue().equalsIgnoreCase("")) {
                            if (results.getMetas() != null) {
                                sponsored = (BooleanValue) results.getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                                if (sponsored != null) {
                                    if (sponsored.getValue() != null) {
                                        if (!sponsored.getValue()) {
                                            sortedList.add(results);
                                        }
                                    } else {
                                        sortedList.add(results);
                                    }
                                } else {
                                    sortedList.add(results);
                                }
                            } else {
                                sortedList.add(results);
                            }
                        }
                    }
                }
            } else {
                if (results.getMetas() != null) {
                    sponsored = (BooleanValue) results.getMetas().get(AppLevelConstants.SEARCH_ISSPONSORED_CONSTATNT);
                    if (sponsored != null) {
                        if (sponsored.getValue() != null) {
                            if (!sponsored.getValue()) {
                                sortedList.add(results);
                            }
                        } else {
                            sortedList.add(results);
                        }
                    } else {
                        sortedList.add(results);
                    }
                } else {
                    sortedList.add(results);
                }
            }
        } else {
            checkMediaType2(sortedList, results, context, sponsored);
        }
    }

    public static void resetFilter(Context context) {
        KsPreferenceKey.getInstance(context).setFilterApply("false");
        KsPreferenceKey.getInstance(context).setFilterGenre("");
        KsPreferenceKey.getInstance(context).setFilterLanguage("");
        KsPreferenceKey.getInstance(context).setFilterSortBy("");
        KsPreferenceKey.getInstance(context).setFilterContentType("");
        KsPreferenceKey.getInstance(context).setFilterFreePaid("");
        KsPreferenceKey.getInstance(context).setFilterLanguageSelection("");
        KsPreferenceKey.getInstance(context).setFilterGenreSelection("");
    }

    public static List<String> createFilterLanguageList(String selectedGenres) {
        List<String> strings = new ArrayList<>();
        if (selectedGenres != null && !selectedGenres.equalsIgnoreCase("")) {
            strings = Arrays.asList(selectedGenres.split("\\s*,\\s*"));
        }

        return strings;
    }

    public static List<String> createFilterGenreList(String selectedGenres) {
        List<String> strings = new ArrayList<>();
        if (selectedGenres != null && !selectedGenres.equalsIgnoreCase("")) {
            strings = Arrays.asList(selectedGenres.split("\\s*,\\s*"));
        }

        return strings;
    }


    public static boolean checkLangeageAdded(String identifier, List<String> saved) {
        boolean contains = false;
        for (int i = 0; i < saved.size(); i++) {
            Log.w("savedata6", saved.get(i) + "-" + identifier + "    " + i);
            if (saved.get(i).equalsIgnoreCase(identifier)) {
                contains = true;
                break;
            } else {
                contains = false;
            }
        }
        return contains;
    }

    public static String getVODSearchKsql(String searchString, String selectedGenre, int from, Context context) {
        StringBuilderHolder.getInstance().clear();

        if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
            StringBuilderHolder.getInstance().append("(and ");
            StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
            StringBuilderHolder.getInstance().append("");
        } else {
            if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append("(and ");
                StringBuilderHolder.getInstance().append(selectedGenre);
                StringBuilderHolder.getInstance().append("");
            } else {
                StringBuilderHolder.getInstance().append("");
            }
        }

        if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")) {
            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(" ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                StringBuilderHolder.getInstance().append(" ");
            } else {
                StringBuilderHolder.getInstance().append("(and ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                StringBuilderHolder.getInstance().append(" ");
            }

        } else {
            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(" ");
            } else {
                if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append(" ");
                } else {
                    StringBuilderHolder.getInstance().append(" ");
                }

            }
        }

        StringBuilderHolder.getInstance().append("(or name~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

  /*      StringBuilderHolder.getInstance().append("description~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");*/

        StringBuilderHolder.getInstance().append("director~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

        StringBuilderHolder.getInstance().append("Keywords~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

        StringBuilderHolder.getInstance().append("Actors~'");
        StringBuilderHolder.getInstance().append(searchString);
        if (StringBuilderHolder.getInstance().getText().toString().contains("(and")) {
            StringBuilderHolder.getInstance().append("'))");
        } else {
            StringBuilderHolder.getInstance().append("')");
        }


        KsPreferenceKey.getInstance(context).setSearchKSQL(StringBuilderHolder.getInstance().getText().toString());
        return StringBuilderHolder.getInstance().getText().toString();
    }

    public static String getLiveSearchKsql(String searchString, String selectedGenre, int from, Context context) {
        StringBuilderHolder.getInstance().clear();

        if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
            StringBuilderHolder.getInstance().append("(and ");
            StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
            StringBuilderHolder.getInstance().append("");
        } else {
            if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append("(and ");
                StringBuilderHolder.getInstance().append(selectedGenre);
                StringBuilderHolder.getInstance().append("");
            } else {
                StringBuilderHolder.getInstance().append("");
            }
        }

        if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")) {
            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(" ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                StringBuilderHolder.getInstance().append(" ");
            } else {
                StringBuilderHolder.getInstance().append("(and ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                StringBuilderHolder.getInstance().append(" ");
            }

        } else {
            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(" ");
            } else {
                if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append(" ");
                } else {
                    StringBuilderHolder.getInstance().append(" ");
                }

            }
        }

        if (StringBuilderHolder.getInstance().getText().toString() != null && !StringBuilderHolder.getInstance().getText().toString().equalsIgnoreCase("") && StringBuilderHolder.getInstance().getText().toString().contains("(and")) {
            StringBuilderHolder.getInstance().append(" start_date>='0' ");
        } else {
            StringBuilderHolder.getInstance().append("(and start_date>='0' ");
        }

        StringBuilderHolder.getInstance().append("(or name~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

      /*  StringBuilderHolder.getInstance().append("description~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");*/

        StringBuilderHolder.getInstance().append("director~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

        StringBuilderHolder.getInstance().append("Keywords~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

        StringBuilderHolder.getInstance().append("Actors~'");
        StringBuilderHolder.getInstance().append(searchString);
        if (StringBuilderHolder.getInstance().getText().toString().contains("(and")) {
            StringBuilderHolder.getInstance().append("'))");
        } else {
            StringBuilderHolder.getInstance().append("')");
        }


        KsPreferenceKey.getInstance(context).setSearchKSQL(StringBuilderHolder.getInstance().getText().toString());
        return StringBuilderHolder.getInstance().getText().toString();
    }

    public static String getPagesSearchKsql(String searchString, String selectedGenre, int from, Context context) {
        StringBuilderHolder.getInstance().clear();

        if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
            StringBuilderHolder.getInstance().append("(and ");
            StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterGenre());
            StringBuilderHolder.getInstance().append("");
        } else {
            if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append("(and ");
                StringBuilderHolder.getInstance().append(selectedGenre);
                StringBuilderHolder.getInstance().append("");
            } else {
                StringBuilderHolder.getInstance().append("");
            }
        }

        if (!KsPreferenceKey.getInstance(context).getFilterLanguage().equalsIgnoreCase("")) {
            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(" ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                StringBuilderHolder.getInstance().append(" ");
            } else {
                StringBuilderHolder.getInstance().append("(and ");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getFilterLanguage());
                StringBuilderHolder.getInstance().append(" ");
            }

        } else {
            if (!KsPreferenceKey.getInstance(context).getFilterGenre().equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(" ");
            } else {
                if (selectedGenre != null && !selectedGenre.equalsIgnoreCase("")) {
                    StringBuilderHolder.getInstance().append(" ");
                } else {
                    StringBuilderHolder.getInstance().append(" ");
                }

            }
        }

        if (StringBuilderHolder.getInstance().getText().toString() != null && !StringBuilderHolder.getInstance().getText().toString().equalsIgnoreCase("") && StringBuilderHolder.getInstance().getText().toString().contains("(and")) {
            StringBuilderHolder.getInstance().append(" IsSponsored='1' ");
        } else {
            StringBuilderHolder.getInstance().append("(and IsSponsored='1' ");
        }

        StringBuilderHolder.getInstance().append("(or name~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

      /*  StringBuilderHolder.getInstance().append("description~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");*/

        StringBuilderHolder.getInstance().append("director~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

        StringBuilderHolder.getInstance().append("Keywords~'");
        StringBuilderHolder.getInstance().append(searchString);
        StringBuilderHolder.getInstance().append("'");

        StringBuilderHolder.getInstance().append("Actors~'");
        StringBuilderHolder.getInstance().append(searchString);
        if (StringBuilderHolder.getInstance().getText().toString().contains("(and")) {
            StringBuilderHolder.getInstance().append("'))");
        } else {
            StringBuilderHolder.getInstance().append("')");
        }


        KsPreferenceKey.getInstance(context).setSearchKSQL(StringBuilderHolder.getInstance().getText().toString());
        return StringBuilderHolder.getInstance().getText().toString();
    }

    public static List<AccountDeviceDetailsItem> checkCurrentDevice(List<AccountDeviceDetailsItem> accountDeviceDetails, Context context) {
        List<AccountDeviceDetailsItem> deviceDetailsItems = new ArrayList<>();
        deviceDetailsItems = accountDeviceDetails;

        List<String> deviceList = new ArrayList<>();
        for (AccountDeviceDetailsItem a : accountDeviceDetails) {
            deviceList.add(a.getSerialNo());
        }
        int index = deviceList.indexOf(AppCommonMethods.getDeviceId(context.getContentResolver()));
        deviceDetailsItems.add(0, deviceDetailsItems.get(index));
        deviceDetailsItems.remove(index + 1);
        return deviceDetailsItems;
    }

    public static String getProgramTimeDate(long timestamp) {
        try {

            Date date = new Date(timestamp * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM hh:mmaaa", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String dateTimeValue = simpleDateFormat.format(date);
            return dateTimeValue;
        } catch (IndexOutOfBoundsException e) {
        }
        return "";

    }

    public static String getPubDate(long timestamp) {
        try {

            Date date = new Date(timestamp * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String dateTimeValue = simpleDateFormat.format(date);
            return dateTimeValue;
        } catch (IndexOutOfBoundsException e) {
        }
        return "";

    }

    public static String maskedEmail(Activity context) {
        String email = "";
        try {
            String s = UserInfo.getInstance(context).getEmail();
            String pattern = "([^@]+)@(.*)\\.(.*)";

            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(s);
            if (m.find()) {
                StringBuilder sb = new StringBuilder("");
                sb.append(m.group(1).charAt(0));
                sb.append(m.group(1).substring(1));
                sb.append("@");
                sb.append(m.group(2).replaceAll(".", "*"));
                sb.append(".").append(m.group(3));
                email = sb.toString();
            }

        } catch (Exception ignored) {
            email = "";
        }
        return email;
    }

    public static boolean shouldItemIncluded(List<AssetHistory> continueWatchingList, String assetID) {
        boolean shouldInclude = false;
        try {
            for (int i = 0; i < continueWatchingList.size(); i++) {
                AssetHistory assetHistory = continueWatchingList.get(i);
                if (String.valueOf(assetHistory.getAssetId()).equalsIgnoreCase(assetID)) {
                    shouldInclude = checkContinueWatchingPercentage(assetHistory.getDuration(), assetHistory.getPosition());
                }
            }
        } catch (Exception e) {

        }
        return shouldInclude;
    }

    public static boolean checkContinueWatchingPercentage(int astDuration, int position) {
        boolean condition = false;
        try {
            /*if (count==0){
                playingPosition = 730;//assetHistory.getPosition();
                count++;
            }else {
                 playingPosition = assetHistory.getPosition();
            }*/
            int playingPosition = position;
            int totalDuration = astDuration;
            Double duration = Double.valueOf(totalDuration);
            double onePercent = (duration * 0.01);
            double nintyEightPercent = (duration * 0.98);
            Log.w("percentge-->>", onePercent + " " + nintyEightPercent);
            if (playingPosition > onePercent && playingPosition < nintyEightPercent) {
                condition = true;
            } else {
                condition = false;
            }
        } catch (Exception ignored) {

        }
        return condition;
    }


    public static String getMetas(Asset asset, int type) {
        Long liveEventStartDate = 0l;
        Long liveEventEndDate = 0l;
        StringBuilderHolder.getInstance().clear();
        if (type == 4) {
            String liveEventStartTime = AppCommonMethods.getLiveEventStartDate(asset.getStartDate()) + "";
            String liveEventEndTime = AppCommonMethods.getLiveEventEndTime(asset.getEndDate()) + "";
            if (liveEventStartTime != null && !liveEventStartTime.equalsIgnoreCase("") && liveEventEndTime != null && !liveEventEndTime.equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(liveEventStartTime + "-" + liveEventEndTime);
                StringBuilderHolder.getInstance().append(" | ");
            }
        } else if (type == 5) {

        } else if (type == 3) {
            Map<String, Value> metas = asset.getMetas();
            LongValue startValue = null, endValue = null;
            if (metas != null) {
                startValue = (LongValue) metas.get(AppLevelConstants.LiveEventProgramStartDate);
                endValue = (LongValue) metas.get(AppLevelConstants.LiveEventProgramEndDate);
                if (startValue != null) {
                    liveEventStartDate = startValue.getValue();
                }
                if (endValue != null) {
                    liveEventEndDate = endValue.getValue();
                }
            }
            String liveEventStartTime = AppCommonMethods.getLiveEventStartDate(liveEventStartDate) + "";
            String liveEventEndTime = AppCommonMethods.getLiveEventEndTime(liveEventEndDate) + "";
            if (liveEventStartTime != null && !liveEventStartTime.equalsIgnoreCase("") && liveEventEndTime != null && !liveEventEndTime.equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(liveEventStartTime + "-" + liveEventEndTime);
                StringBuilderHolder.getInstance().append(" | ");
            }
        } else {
            DoubleValue doubleValue = null;
            Map<String, Value> yearMap = asset.getMetas();
            if (yearMap != null && yearMap.get(AppLevelConstants.YEAR) instanceof DoubleValue) {
                doubleValue = (DoubleValue) yearMap.get(AppLevelConstants.YEAR);
            }
            if (doubleValue != null) {
                String s = String.valueOf(doubleValue.getValue());


                if (!TextUtils.isEmpty(s)) {
                    if (s.length() > 3) {
                        StringBuilderHolder.getInstance().append(s.substring(0, 4));
                    } else {
                        StringBuilderHolder.getInstance().append(s);
                    }
                    StringBuilderHolder.getInstance().append(" | ");

                }
            }
        }

        getSubGenre(StringBuilderHolder.getInstance(), asset.getTags());

        return StringBuilderHolder.getInstance().getText().toString();
    }

    private static void getSubGenre(StringBuilderHolder builder, Map<String, MultilingualStringValueArray> map) {
        String genre;
        List<MultilingualStringValue> genre_values = new ArrayList<>();
        MultilingualStringValueArray genre_list = map.get(AppLevelConstants.KEY_SUB_GENRE);
        if (genre_list != null)

            genre_values.addAll(genre_list.getObjects());

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

        if (!TextUtils.isEmpty(genre)) {
            builder.append(genre.trim());
            builder.append(" | ");

        }

        getLanguage(builder, map);
    }

    private static void getLanguage(StringBuilderHolder builder, Map<String, MultilingualStringValueArray> map) {
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


        if (!TextUtils.isEmpty(language)) {
            builder.append(language);
            builder.append(" | ");
        }

        getMovieRatings(builder, map);

    }

    private static void getMovieRatings(StringBuilderHolder builder, Map<String, MultilingualStringValueArray> map) {
        if (AssetContent.getParentalRating(map).length() > 0) {
            builder.append(AssetContent.getParentalRating(map));
            builder.append(" ");
        }
    }

    public static String getLiveEventStartDate(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM hh:mmaaa", Locale.US);
            sdf.setTimeZone(tz);
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public static String getLiveEventEndTime(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaaa", Locale.US);
            sdf.setTimeZone(tz);
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }


    public static String maskedMobile(Activity context) {
        StringBuilder email = new StringBuilder();
        try {
            String s = UserInfo.getInstance(context).getMobileNumber();
            for (int i = 0; i < s.length(); i++) {
                if (i <= s.length() - 5) {
                    email.append("*");
                } else {
                    email.append(s.charAt(i));
                }
            }

        } catch (Exception ignored) {
            email.append("");
            email.toString();
        }
        return email.toString();
    }

    public static void checkSocailLinking(Context context, List<SocialLoginTypesItem> socialLoginTypesItem) {
        for (SocialLoginTypesItem socialItem : socialLoginTypesItem) {
            if (socialItem.getSocialLoginType() != null && socialItem.getSocialLoginType().equalsIgnoreCase("Facebook")) {
                UserInfo.getInstance(context).setFbLinked(true);
            } else if (socialItem.getSocialLoginType() != null && socialItem.getSocialLoginType().equalsIgnoreCase("Google")) {
                UserInfo.getInstance(context).setGoogleLinked(true);
            }
        }
    }

    public static String getLiveEventTime(Asset asset) {
        try {
            Long liveEventStartDate = 0l;
            Long liveEventEndDate = 0l;
            StringBuilderHolder.getInstance().clear();
            Map<String, Value> metas = asset.getMetas();
            LongValue startValue = null, endValue = null;
            if (metas != null) {
                startValue = (LongValue) metas.get(AppLevelConstants.LiveEventProgramStartDate);
                endValue = (LongValue) metas.get(AppLevelConstants.LiveEventProgramEndDate);
                if (startValue != null) {
                    liveEventStartDate = startValue.getValue();
                }
                if (endValue != null) {
                    liveEventEndDate = endValue.getValue();
                }
            }
            String liveEventStartTime = AppCommonMethods.getLiveEventStartDate(liveEventStartDate) + "";
            String liveEventEndTime = AppCommonMethods.getLiveEventEndTime(liveEventEndDate) + "";
            if (liveEventStartTime != null && !liveEventStartTime.equalsIgnoreCase("") && liveEventEndTime != null && !liveEventEndTime.equalsIgnoreCase("")) {
                StringBuilderHolder.getInstance().append(liveEventStartTime + " - " + liveEventEndTime);
                StringBuilderHolder.getInstance().append(" | ");
            }
        } catch (Exception ignored) {

        }

        return StringBuilderHolder.getInstance().getText().toString();
    }

    public static String getAdsUrl(String url, Asset asset, Context context) {
        StringBuilder finalUrl = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
       /* stringBuilder.append(url);
        stringBuilder.append("&cust_params=");*/
        //did%3D" + AppCommonMethods.getDeviceId(context.getContentResolver())
       /* if (UserInfo.getInstance(context).getCpCustomerId() != null && !UserInfo.getInstance(context).getCpCustomerId().equalsIgnoreCase(""))
            stringBuilder.append("&cid%3D" + UserInfo.getInstance(context).getCpCustomerId());*/
        stringBuilder.append("vid=" + asset.getId());
        if (!asset.getName().equalsIgnoreCase(""))
            stringBuilder.append("&vtitle=" + asset.getName());

        /* stringBuilder.append("&ver%3D" + BuildConfig.VERSION_NAME);*/

        if (asset.getType() == MediaTypeConstant.getLinear(context)) {
            if (AssetContent.isLiveEvent(asset.getMetas())) {
                stringBuilder.append("&vtype=Live Event");
            } else {
                /*  stringBuilder.append("&ch%3D" + asset.getName());*/
                stringBuilder.append("&vtype=Linear Programme");
            }
        } else {
            stringBuilder.append("&vtype=VOD");
        }
        if (!AssetContent.getGenredataString(asset.getTags()).equalsIgnoreCase(""))
            stringBuilder.append("&vgenre=" + AssetContent.getGenredataString(asset.getTags()));
        if (!AssetContent.getSubGenredataString(asset.getTags()).equalsIgnoreCase(""))
            stringBuilder.append("&subgenre=" + AssetContent.getSubGenredataString(asset.getTags()));

        if (!AssetContent.getLanguageDataString(asset.getTags(), context).equalsIgnoreCase(""))
            stringBuilder.append("&vlang=" + AssetContent.getLanguageDataString(asset.getTags(), context));

        if (!AssetContent.getProvider(asset.getTags()).equalsIgnoreCase(""))
            stringBuilder.append("&vpro=" + AssetContent.getProvider(asset.getTags()));

       /* if (!AssetContent.getSubTitleLanguageDataString(asset.getTags(), context).equalsIgnoreCase(""))
            stringBuilder.append("&vsub%3D" + AssetContent.getSubTitleLanguageDataString(asset.getTags(), context));*/
        stringBuilder.append("&lang=" + "English");
        try {
            finalUrl.append(url);
            finalUrl.append("&cust_params=");
            finalUrl.append(URLEncoder.encode(stringBuilder.toString(), "UTF-8"));
            Log.w("addImaTagUrl", URLDecoder.decode(stringBuilder.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            finalUrl.append(url);
            finalUrl.append("&cust_params=");
            finalUrl.append(stringBuilder.toString());
            e.printStackTrace();
        }
        return finalUrl.toString();
    }

    public static String splitGenre(String customGenre, String customGenreRule) {
        StringBuilder ksql = new StringBuilder();
        try {

            String[] splitString = customGenre.split(";");
            if (splitString.length > 0 && !splitString[0].equalsIgnoreCase("")) {
                if (customGenreRule.equalsIgnoreCase("EXCLUDE")) {
                    ksql.append("(and");
                    for (String genre : splitString) {
                        ksql.append(" Genre!='" + genre + "'");
                    }
                } else {
                    ksql.append("(or");
                    for (String genre : splitString) {
                        ksql.append(" Genre='" + genre + "'");
                    }
                }
                ksql.append(")");
            }
        } catch (Exception e) {

        }
        return ksql.toString();
    }

    public static String getsubScriptionIds(List<Entitlement> objects) {
        StringBuilder idString = new StringBuilder();
        for (Entitlement entitlement : objects) {
            SubscriptionEntitlement subscriptionEntitlement = (SubscriptionEntitlement) entitlement;
            if (!subscriptionEntitlement.getIsRenewable()) {
                idString.append(entitlement.getProductId() + ",");
            }
        }
        return idString.toString();
    }

    public static void getIdArray(List<Subscription> objects) {
        List<String> arrayList = new ArrayList<>();

    }

    public static String getTypeIn(Context activity, String customMediaType) {
        StringBuilder typeIn = new StringBuilder();
        try {

            String[] splitString = customMediaType.split(";");
            if (splitString.length > 0 && !splitString[0].equalsIgnoreCase("")) {
                for (String typeInItem : splitString) {
                    if (typeInItem.equalsIgnoreCase("EPISODES"))
                        typeIn.append(MediaTypeConstant.getEpisode(activity) + ",");
                    if (typeInItem.equalsIgnoreCase("MOVIES"))
                        typeIn.append(MediaTypeConstant.getMovie(activity) + ",");

                }
            }
        } catch (Exception e) {

        }
        return typeIn.toString();
    }

    public static List<String> getSubscriptionSKUs(List<ProductsResponseMessageItem> productsResponseMessage, Activity context) {
        List<String> subSkuList = new ArrayList<>();

        for (ProductsResponseMessageItem responseMessageItem : productsResponseMessage) {
            if (responseMessageItem.getAppChannels() != null && responseMessageItem.getAppChannels().get(0) != null && responseMessageItem.getAppChannels().get(0).getAppChannel() != null && responseMessageItem.getAppChannels().get(0).getAppChannel().equalsIgnoreCase("Google Wallet") && responseMessageItem.getAppChannels().get(0).getAppID() != null) {
                if (responseMessageItem.getServiceType().equalsIgnoreCase("ppv")) {

                } else {
                    Log.w("subscriptionSKUs-->>>", responseMessageItem.getAppChannels().get(0).getAppID() + "");
                    subSkuList.add(responseMessageItem.getAppChannels().get(0).getAppID());
                }
            }
        }
        return subSkuList;
    }

    public static List<String> getProductSKUs(List<ProductsResponseMessageItem> productsResponseMessage, Activity context) {
        List<String> subSkuList = new ArrayList<>();

        for (ProductsResponseMessageItem responseMessageItem : productsResponseMessage) {
            if (responseMessageItem.getAppChannels() != null && responseMessageItem.getAppChannels().get(0) != null && responseMessageItem.getAppChannels().get(0).getAppChannel() != null && responseMessageItem.getAppChannels().get(0).getAppChannel().equalsIgnoreCase("Google Wallet") && responseMessageItem.getAppChannels().get(0).getAppID() != null) {
                if (responseMessageItem.getServiceType().equalsIgnoreCase("ppv")) {
                    Log.w("subscriptionSKUs-->>>", responseMessageItem.getAppChannels().get(0).getAppID() + "");
                    subSkuList.add(responseMessageItem.getAppChannels().get(0).getAppID());
                } else {

                }
            }
        }
        return subSkuList;
    }

    public static String getSubtitleName(String label, Context context) {
        String name = "";
        try {
            // Log.w("tracking 1",label);
            ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
            ArrayList<SubtitleLanguages> subtitleLanguages = responseDmsModel.getSubtitleLanguageList();
            for (int i = 0; i < subtitleLanguages.size(); i++) {
                //  Log.w("tracking 2",subtitleLanguages.size()+"");
                for (int j = 0; j < subtitleLanguages.get(i).getValue().size(); j++) {
                    // Log.w("tracking 3",subtitleLanguages.get(i).getValue().size()+"");
                    String valueKey = subtitleLanguages.get(i).getValue().get(j).getAsString();
                    // Log.w("tracking 4",valueKey+"");
                    if (label.equalsIgnoreCase(valueKey)) {
                        //Log.w("subtitleKeys",valueKey);
                        name = subtitleLanguages.get(i).getKey();
                        break;
                    }
                }

            }
        } catch (Exception e) {
            Log.w("tracking 5", e.toString());
        }
        return name;
    }

    public static String getAudioLanguageName(String label, Context context) {
        String name = "";
        try {
            //Log.w("tracking 1",label);
            ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
            ArrayList<AudioLanguages> subtitleLanguages = responseDmsModel.getAudioLanguageList();
            for (int i = 0; i < subtitleLanguages.size(); i++) {
                //Log.w("tracking 2",subtitleLanguages.size()+"");

                //Log.w("tracking 3",subtitleLanguages.size()+"");
                String valueKey = subtitleLanguages.get(i).getValue();
                //Log.w("tracking 4",valueKey+"");
                if (label.equalsIgnoreCase(valueKey)) {
                    //Log.w("subtitleKeys",valueKey);
                    name = subtitleLanguages.get(i).getKey();
                    break;
                }

            }
        } catch (Exception e) {
            Log.w("tracking 5", e.toString());
        }
        return name;
    }

    public static boolean isTokenExpired(Activity context) {
        boolean isExpired = false;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
        String getCurrentDateTime = sdf.format(c.getTime());
        String getMyTime = getDateCurrentTimeZone(KsPreferenceKey.getInstance(context).getExpiryTime());


        if (getCurrentDateTime.compareTo(getMyTime) < 0) {
            isExpired = false;
        } else {
            isExpired = true;
        }

        return isExpired;
    }
}
