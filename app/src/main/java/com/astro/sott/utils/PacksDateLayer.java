package com.astro.sott.utils;

import com.astro.sott.modelClasses.InApp.PackDetail;

import java.util.ArrayList;
import java.util.List;

public class PacksDateLayer {
    private ArrayList<PackDetail> packDetailList;
    private static PacksDateLayer packsDateLayer;


    public static PacksDateLayer getInstance() {
        if (packsDateLayer == null)
            packsDateLayer = new PacksDateLayer();
        return packsDateLayer;
    }

    public ArrayList<PackDetail> getPackDetailList() {
        return packDetailList;
    }

    public void setPackDetailList(ArrayList<PackDetail> packDetailList) {
        this.packDetailList = packDetailList;
    }
}
