package com.dialog.dialoggo.baseModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelLayer {
    private static ChannelLayer channelLayer;
    private List<VIUChannel> VIUChannelList;
    private int recommendedIndex = -1;
    public static ChannelLayer getInstance() {
        if (channelLayer == null)
            channelLayer = new ChannelLayer();
        return channelLayer;
    }

    public LiveData<AssetCommonBean> getChannelList(Context context, int channelType) {
        recommendedIndex=-1;
        VIUChannelList = new ArrayList<>();
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<AssetCommonBean> connection = new MutableLiveData<>();
        ksServices.callChannelList(context,0, channelType, (status, response) -> {
            if (status) {
                assetCommonBean.setStatus(true);
                VIUChannelList = createVIUChannelList(response.getChannelList());
                checkRecomendateInjected(context,response.getChannelList());
                assetCommonBean.setChannelList(response.getChannelList());
                assetCommonBean.setDTChannelList(VIUChannelList);
                connection.postValue(assetCommonBean);
            } else {
                assetCommonBean.setStatus(false);
                connection.postValue(assetCommonBean);
            }
        });

        return connection;
    }

    private List<VIUChannel> createVIUChannelList(List<Channel> channelList) {
        List<VIUChannel> VIUChannels = new ArrayList<>();
        for (int i = 0; i < channelList.size(); i++) {
            VIUChannel VIUChannel = new VIUChannel();
            VIUChannel.setId(channelList.get(i).getId());
            VIUChannel.setDescription(channelList.get(i).getDescription());
            VIUChannel.setName(channelList.get(i).getName());
            VIUChannels.add(VIUChannel);
        }
        return VIUChannels;
    }
    private void checkRecomendateInjected(Context context,List<Channel> dtChannelList) {
        int size = dtChannelList.size();
        for (int i = 0; i < size; i++) {
            if (dtChannelList.get(i).getDescription().equalsIgnoreCase(AppLevelConstants.KEY_RECOMMENDED)) {
                recommendedIndex = i;
            }
        }
        if (recommendedIndex != -1) {
            increaseChannelList(context);
        }
    }
    private void increaseChannelList(Context context) {
        if (KsPreferenceKey.getInstance(context).getUserActive()) {
            ArrayList<PrefrenceBean> storeList = AppCommonMethods.getContentPrefences(context);
            if (storeList != null) {
                if (storeList.size() > 0) {
                    int storedSize = storeList.size();
                    VIUChannelList.remove(recommendedIndex);
                    for (int i = 0; i < storedSize; i++) {
                        long genreId = AppCommonMethods.getGenreID(storeList.get(i).getName());
                        if (genreId > 0) {
                            VIUChannel channel=new VIUChannel();
                            channel.setId(genreId);
                            channel.setDescription(AppLevelConstants.KEY_RECOMMENDED);
                            channel.setName(AppLevelConstants.KEY_RECOMMENDED_TITLE + "-" + storeList.get(i).getName());
                            VIUChannelList.add(recommendedIndex, channel);
                        }
                    }
                }else {
                    VIUChannelList.remove(recommendedIndex);
                }
            }else {
                VIUChannelList.remove(recommendedIndex);
            }
        } else {
            ArrayList<PrefrenceBean> storeList = AppCommonMethods.getLogoutContentPrefences(context);
            if (storeList != null) {
                if (storeList.size() > 0) {
                    int storedSize = storeList.size();
                    VIUChannelList.remove(recommendedIndex);
                    for (int i = 0; i < storedSize; i++) {
                        VIUChannel channel=new VIUChannel();
                        channel.setId(AppCommonMethods.getGenreID(storeList.get(i).getName()));
                        channel.setDescription(AppLevelConstants.KEY_RECOMMENDED);
                        channel.setName(AppLevelConstants.KEY_RECOMMENDED + "-" + storeList.get(i).getName());
                        VIUChannelList.add(recommendedIndex, channel);
                    }

                }else {
                    VIUChannelList.remove(recommendedIndex);
                }
            }else {
                VIUChannelList.remove(recommendedIndex);
            }
        }
    }
}
