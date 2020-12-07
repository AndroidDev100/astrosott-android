package com.dialog.dialoggo.callBacks.commonCallBacks;


import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;

public interface HeroItemClickListner {

    void heroItemClick(int position, RailCommonData railCommonData, AssetCommonBean commonData);
}
