package com.astro.sott.callBacks.commonCallBacks;


import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;

import java.util.List;

public interface EpisodeClickListener {
    void onClick(int position);

    default void moveToPlay(int position, RailCommonData railCommonData, int type, List<RailCommonData> railList){

    }


}
