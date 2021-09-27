package com.astro.sott.utils.billing;

import com.astro.sott.modelClasses.InApp.PackDetail;

import java.util.ArrayList;

public interface BuyButtonListener {
    void onPackagesAvailable(ArrayList<PackDetail> packDetailList,String packageType,String lowestPackagePrice,String[] subscriptionIds);
}
