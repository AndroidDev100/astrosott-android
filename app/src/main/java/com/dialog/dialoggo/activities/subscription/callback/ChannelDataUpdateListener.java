package com.dialog.dialoggo.activities.subscription.callback;

import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;

import java.util.List;

public interface ChannelDataUpdateListener {
    void addDataToChannelList(List<RailCommonData> channelDataList);
    void noDataFound();
}
