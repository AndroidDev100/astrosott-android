package com.astro.sott.callBacks.kalturaCallBacks;


import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;

import java.util.List;

public interface ContinuewWatchingList {
    void response(boolean status, List<AssetCommonBean> commonResponse);
}
