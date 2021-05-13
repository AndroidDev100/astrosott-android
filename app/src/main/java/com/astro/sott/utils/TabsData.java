package com.astro.sott.utils;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

public class TabsData {

    private static TabsData tabsData;
    private List<Asset> trailerData, movieShows, seriesShows;
    private List<Asset> highlightsData;
    private String sortType;
    private String seriesType;
    private int selectedSeason;
    private List<RailCommonData> youMayAlsoLikeData;
    private List<AssetCommonBean> openSeriesData;
    private Asset seriesAsset;
    private List<AssetCommonBean> closedSeriesData;

    private List<Integer> seasonList;


    public static TabsData getInstance() {
        if (tabsData == null) {
            tabsData = new TabsData();
        }
        return tabsData;
    }


    public Asset getSeriesAsset() {
        return seriesAsset;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesAsset(Asset seriesAsset) {
        this.seriesAsset = seriesAsset;
    }

    public void setSeriesShows(List<Asset> seriesShows) {
        this.seriesShows = seriesShows;
    }

    public List<Asset> getSeriesShows() {
        return seriesShows;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public List<Integer> getSeasonList() {
        return seasonList;
    }

    public int getSelectedSeason() {
        return selectedSeason;
    }

    public void setSelectedSeason(int selectedSeason) {
        this.selectedSeason = selectedSeason;
    }

    public List<Asset> getMovieShows() {
        return movieShows;
    }

    public void setMovieShows(List<Asset> movieShows) {
        this.movieShows = movieShows;
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
        this.trailerData = assets;
    }

    public void setYouMayAlsoLikeData(List<RailCommonData> youMayAlsoLikeData) {
        this.youMayAlsoLikeData = youMayAlsoLikeData;
    }

    public List<RailCommonData> getYouMayAlsoLikeData() {
        return youMayAlsoLikeData;
    }

    public void setHighLightsData(List<Asset> assets) {
        this.highlightsData = assets;
    }

    public List<Asset> getHighLightsData() {
        return highlightsData;
    }

    public List<Asset> getTrailerData() {
        return trailerData;
    }
}
