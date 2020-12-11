package com.astro.sott.activities.subscription.callback;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;

import java.util.List;

public interface ChannelDataUpdateListener {
    void addDataToChannelList(List<RailCommonData> channelDataList);
    void noDataFound();
}
