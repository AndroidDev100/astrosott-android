package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.HouseholdDevice;

import java.util.List;

public interface LoginCallBack {

    void loginProcess(boolean status, int apiType, List<HouseholdDevice> list);
}
