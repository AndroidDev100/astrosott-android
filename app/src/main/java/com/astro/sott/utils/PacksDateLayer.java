package com.astro.sott.utils;

import com.astro.sott.modelClasses.InApp.PackDetail;

import java.util.List;

public class PacksDateLayer {
    private List<PackDetail> packDetailList;
    private static PacksDateLayer packsDateLayer;


    public static PacksDateLayer getInstance() {
        if (packsDateLayer == null)
            packsDateLayer = new PacksDateLayer();
        return packsDateLayer;
    }

    public List<PackDetail> getPackDetailList() {
        return packDetailList;
    }

    public void setPackDetailList(List<PackDetail> packDetailList) {
        this.packDetailList = packDetailList;
    }
}
