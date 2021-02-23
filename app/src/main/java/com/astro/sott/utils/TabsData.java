package com.astro.sott.utils;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

public class TabsData {

    private static TabsData tabsData;
    private List<Asset> trailerData;
    private List<Asset> highlightsData;
    private List<RailCommonData> youMayAlsoLikeData;


    public static TabsData getInstance() {
        if (tabsData == null) {
            tabsData = new TabsData();
        }
        return tabsData;
    }


    public void setTrailerData(List<Asset> assets) {
       this.trailerData=assets;
    }

    public void setYouMayAlsoLikeData(List<RailCommonData> youMayAlsoLikeData) {
        this.youMayAlsoLikeData = youMayAlsoLikeData;
    }

    public List<RailCommonData> getYouMayAlsoLikeData() {
        return youMayAlsoLikeData;
    }

    public void setHighLightsData(List<Asset> assets) {
      this.highlightsData=assets;
    }

    public List<Asset> getHighLightsData() {
        return highlightsData;
    }

    public List<Asset> getTrailerData() {
        return trailerData;
    }
}
