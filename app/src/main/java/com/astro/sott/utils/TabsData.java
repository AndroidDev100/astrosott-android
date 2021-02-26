package com.astro.sott.utils;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

public class TabsData {

    private static TabsData tabsData;
    private List<Asset> trailerData;
    private List<Asset> highlightsData;
    private List<RailCommonData> youMayAlsoLikeData;
    private List<AssetCommonBean> openSeriesData;
    private List<AssetCommonBean> closedSeriesData;

    private List<Integer> seasonList;



    public static TabsData getInstance() {
        if (tabsData == null) {
            tabsData = new TabsData();
        }
        return tabsData;
    }


    public List<Integer> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Integer> seasonList) {
        this.seasonList = seasonList;
    }

    public List<AssetCommonBean> getClosedSeriesData() {
        return closedSeriesData;
    }

    public void setClosedSeriesData(List<AssetCommonBean> closedSeriesData) {
        this.closedSeriesData = closedSeriesData;
    }

    public List<AssetCommonBean> getOpenSeriesData() {
        return openSeriesData;
    }

    public void setOpenSeriesData(List<AssetCommonBean> openSeriesData) {
        this.openSeriesData = openSeriesData;
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
