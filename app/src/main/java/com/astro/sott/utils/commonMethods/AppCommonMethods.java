package com.astro.sott.utils.commonMethods;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.astro.sott.baseModel.PrefrenceBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.kalturaCallBacks.DMSCallBack;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrefConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.ksPreferenceKey.SubCategoriesPrefs;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.astro.sott.R;
import com.astro.sott.utils.constants.AppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.AssetHistory;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MediaImage;
import com.kaltura.client.utils.response.base.Response;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.LinkProperties;

public class AppCommonMethods {
    private static String progDuration;
    public static boolean isAdsEnable = false;
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd=hh:mm a", Locale.US);
            String dateTimeValue = simpleDateFormat.format(date);
            String _value[] = dateTimeValue.split("=");
            programTime = _value[1];

        } catch (Exception e) {

            PrintLogging.printLog("Exception", "", "" + e);
        }
        return programTime;
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
            formattedDate = df.format(today);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            formattedDate = df.format(tomorrow);
        }

        calendar.clear();
        return getTimeStamp(formattedDate, type);
    }


    private static String getTimeStamp(String todayDate, int type) {
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
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
                if (asset.getMediaFiles().get(i).getType().equals("DASH")) {
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
                return hours + " hr " + minsString + " min " + secsString;
            else if (mins > 0)
                return mins + " min";
            else return secsString;
        }
        return "";
    }


    public static void openShareDialog(final Activity activity, final Asset asset, Context context) {
        /*WeakReference<Activity> mActivity = new WeakReference<>(activity);
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle(asset.getName())
                .setContentDescription(asset.getDescription())
                .setContentImageUrl(AppCommonMethods.getSharingImage(context, asset.getImages(), asset.getType()));


        LinkProperties lp = new LinkProperties()
                .setChannel("Wactho Example")
                .addControlParameter("assetId", asset.getId() + "")
                .addControlParameter("mediaType", asset.getType() + "");

        buo.generateShortUrl(context, lp, (url, error) -> {

            String sharingURL;
            if (error == null) {
                sharingURL = url;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(Intent.EXTRA_TEXT, mActivity.get().getResources().getString(R.string.checkout) + " " + asset.getName() + " " + mActivity.get().getResources().getString(R.string.on_Dialog) + "\n" + sharingURL);

                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));

                Log.i("BRANCH SDK", "got my Branch link to share: " + sharingURL);
            }
        });*/
    }


    private static String getSharingImage(Context context, List<MediaImage> mediaImage, Integer type) {
        String imageURL = "";
        for (int i = 0; i < mediaImage.size(); i++) {

            if (type == MediaTypeConstant.getMovie(context)) {
                if (mediaImage.get(i).getRatio().equals("9:16")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.portrait_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.portrait_image_height) + AppLevelConstants.QUALITY;
                }

            } else {
                if (mediaImage.get(i).getRatio().equals("16:9")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                } else if (mediaImage.get(i).getRatio().equals("1:1")) {
                    imageURL = mediaImage.get(i).getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;

                }

            }
        }

        return imageURL;
    }

    public static String getDeviceName(Context context) {
        String deviceName;
        if (TextUtils.isEmpty(Settings.System.getString(context.getContentResolver(), AppLevelConstants.DEVICE_NAME))) {
            deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
        } else {
            deviceName = Settings.System.getString(context.getContentResolver(), AppLevelConstants.DEVICE_NAME);

        }
        return deviceName;
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
        Log.w("imageType-->>",tileType);
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
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPEPOSTER:
                Log.w("ImageRatio-->>",list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
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
                            Log.w("ImageRatio-->>in",list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in",final_url);
                            Log.w("ImageUrl-->>in",image_url);
                            assetCommonImages.setImageUrl(final_url);
                            imagesList.add(assetCommonImages);
                        }
                    }
                }


                break;

            case AppLevelConstants.TYPEPR2:
                Log.w("ImageRatio-->>",list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
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
                            Log.w("ImageRatio-->>in",list.get(position).results.getObjects().get(j).getImages().get(k).getRatio());
                            String image_url = list.get(position).results.getObjects().get(j).getImages().get(k).getUrl();
                            String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.poster_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.poster_image_height) + AppLevelConstants.QUALITY;
                            Log.w("FinalUrl-->>in",final_url);
                            Log.w("ImageUrl-->>in",image_url);
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
        Gson gson = new Gson();
        String json = SharedPrefHelper.getInstance(context).getString("DMS_Response", "");
        return gson.fromJson(json, ResponseDmsModel.class);
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
        if (temp.contains("##")) {
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


}
