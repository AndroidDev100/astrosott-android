package com.astro.sott.callBacks.kalturaCallBacks;


import com.astro.sott.beanModel.commonBeanModel.SearchModel;

import java.util.ArrayList;

public interface SearchResultCallBack {

    void response(boolean status, ArrayList<SearchModel> result, String remarks);
}
