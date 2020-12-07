package com.dialog.dialoggo.callBacks.kalturaCallBacks;


import com.dialog.dialoggo.beanModel.commonBeanModel.SearchModel;

import java.util.ArrayList;

public interface SearchResultCallBack {

    void response(boolean status, ArrayList<SearchModel> result, String remarks);
}
