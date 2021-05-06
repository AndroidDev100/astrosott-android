package com.astro.sott.activities.liveChannel.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import android.util.Log;

import com.astro.sott.activities.movieDescription.layers.YouMayAlsoLike;
import com.astro.sott.baseModel.ChannelLayer;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.fragments.nowPlaying.layers.LiveNowPrograms;
import com.astro.sott.fragments.nowPlaying.layers.SimilarChannels;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.baseModel.CategoryRailLayer;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.liveChannel.LiveChannelRepository;
import com.astro.sott.repositories.splash.SplashRepository;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class LiveChannelViewModel extends AndroidViewModel {

    public MutableLiveData<Asset> programAssetData = new MutableLiveData<>();
    private String time;
    private int pos = 0;
    private String programTime = "";
    private String progDuration;

    public LiveChannelViewModel(@NonNull Application application) {
        super(application);
    }

    public void setProgramData(Asset asset) {
        programAssetData.setValue(asset);
    }

    public LiveData<String> getGenreLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getGenredata(map);
    }

    public LiveData<List<RailCommonData>> getEPGChannelsList(String externalId, String startDate, String endDate, int type, int counter) {
        return LiveChannelRepository.getInstance().loadChannelsData(getApplication().getApplicationContext(), externalId, startDate, endDate, type, counter);
    }

    public LiveData<Asset> getCridDetail(String cridId) {
        return LiveChannelRepository.getInstance().getCridDetail(getApplication().getApplicationContext(), cridId);
    }

    public LiveData<List<AssetCommonBean>> getYouMayAlsoLike(int assetId,
                                                             int counter,
                                                             int assetType,
                                                             Map<String, MultilingualStringValueArray> map
    ) {
        return YouMayAlsoLike.getInstance().fetchSimilarMovie(getApplication().getApplicationContext(), assetId, counter, assetType, map);
    }

    public String getStartDate(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d 'at' hh:mm aaa");
            sdf.setTimeZone(tz);
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public void setYouMayAlsoLikeData(List<RailCommonData> trailerData) {
        TabsData.getInstance().setYouMayAlsoLikeData(trailerData);
    }

    public String getProgramTime(Long endTime) {
        try {
            Date date = new Date(endTime * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String dateTimeValue = simpleDateFormat.format(date);
            String _value[] = dateTimeValue.split(" ");
            time = _value[1];

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
        return time;
    }

    public boolean getPlayBackControl(Map<String, Value> metas) {
        return AssetContent.plabackControl(metas);
    }

    public boolean isXofferWindow(String xofferValue) {
        return AppCommonMethods.isXofferWindow(xofferValue);
    }

    public String getProgramTime(Asset asset, int type) {
        Long _time;
        try {
            if (type == 1) {
                _time = asset.getStartDate();
            } else {
                _time = asset.getEndDate();
            }

            Date date = new Date(_time * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd=HH:mm aa", Locale.US);
            String dateTimeValue = simpleDateFormat.format(date);
            String _value[] = dateTimeValue.split("=");
            programTime = _value[1];

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
        return programTime;
    }

    public String getProgramDurtion(String endTime, String stTime) {
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

            PrintLogging.printLog(this.getClass(), "", "progremTime--" + progTimeHour + ":" + progTimeMin + "  " + currTimeHour + ":" + currTimeMin);

            int hDiff;
            if (Integer.parseInt(progTimeHour) < Integer.parseInt(currTimeHour)) {
                hDiff = (24 + Integer.parseInt(progTimeHour)) - Integer.parseInt(currTimeHour);
            } else {
                hDiff = Integer.parseInt(progTimeHour) - Integer.parseInt(currTimeHour);

            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
            Date date1 = simpleDateFormat.parse(progTimeHour + ":" + progTimeMin);
            Date date2 = simpleDateFormat.parse(currTimeHour + ":" + currTimeMin);

            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);

            if (hours == 0) {
                hDiff = 0;
            }
            int mDiff;

            if (Integer.parseInt(progTimeMin) < Integer.parseInt(currTimeMin)) {
                PrintLogging.printLog(this.getClass(), "", "getFinalStr" + progTimeHour + "-->>" + currTimeHour);
                mDiff = (60 + Integer.parseInt(progTimeMin)) - Integer.parseInt(currTimeMin);
            } else {
                mDiff = Integer.parseInt(progTimeMin) - Integer.parseInt(currTimeMin);
            }

            // progDuration=String.valueOf(hDiff)+" hrs"+" : "+String.valueOf(mDiff)+" mins";

            if (mDiff == 0) {
                if (hDiff == 1) {
                    progDuration = String.valueOf(hDiff) + " hr";
                } else {

                    progDuration = String.valueOf(hDiff) + " hrs";

                }

            }

            if (hDiff == 0) {
                progDuration = String.valueOf(mDiff) + " mins";
            } else {
                if (hDiff > 0 && mDiff > 0) {

                    if (hDiff == 1) {
                        progDuration = String.valueOf(hDiff) + " hr" + " : " + String.valueOf(mDiff) + " mins";
                    } else {
                        progDuration = String.valueOf(hDiff) + " hrs" + " : " + String.valueOf(mDiff) + " mins";
                    }
                }

            }


        } catch (Exception e) {
            Log.e("erroroccured", e.toString());
        }
        return progDuration;
    }

    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh) {
        return CategoryRailLayer.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh);
    }

    public LiveData<List<AssetCommonBean>> getLiveNowData(Asset asset) {
        return LiveNowPrograms.getInstance().loadData(asset, getApplication());
    }

    public LiveData<List<AssetCommonBean>> getAllChannelData(int pageCount, Asset asset) {
        return LiveNowPrograms.getInstance().loadAllChannelsData(pageCount, asset, getApplication());
    }

    public LiveData<List<AssetCommonBean>> getSimilarChannelData(Asset asset) {
        return SimilarChannels.getInstance().loadData(asset, getApplication());
    }

    public LiveData<List<AssetCommonBean>> getListData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh) {
        return CategoryRailLayer.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh);
    }

    public LiveData<AssetCommonBean> getChannelList(int screen_id) {
        return ChannelLayer.getInstance().getChannelList(getApplication(), screen_id);
    }

    public LiveData<String> getCastLiveData(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCastActorsData(map);
    }

    public LiveData<RailCommonData> getSpecificAsset(String assetId) {
        return new SplashRepository().getSpecificAsset(getApplication(), assetId);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }
}
