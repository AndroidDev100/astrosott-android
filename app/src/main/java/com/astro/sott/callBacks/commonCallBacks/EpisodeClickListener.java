package com.astro.sott.callBacks.commonCallBacks;


import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;

public interface EpisodeClickListener {
    void onClick(int position);

    default void moveToPlay(int position, RailCommonData railCommonData, int type){

    }


}
