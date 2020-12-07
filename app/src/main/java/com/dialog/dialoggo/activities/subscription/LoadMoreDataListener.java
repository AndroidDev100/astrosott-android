package com.dialog.dialoggo.activities.subscription;

import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;

import java.util.List;

public interface LoadMoreDataListener {
    void onLoadMore(List<RailCommonData> moreChannelList);
}
