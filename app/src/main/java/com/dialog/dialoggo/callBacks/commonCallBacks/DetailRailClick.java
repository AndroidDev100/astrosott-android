package com.dialog.dialoggo.callBacks.commonCallBacks;


import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;

public interface DetailRailClick {
    void detailItemClicked(String _url, int position, int type, RailCommonData commonData);
}
