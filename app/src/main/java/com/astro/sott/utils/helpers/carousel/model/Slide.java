package com.astro.sott.utils.helpers.carousel.model;


import android.service.autofill.UserData;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.utils.helpers.carousel.entity.CategoryData;
import com.kaltura.client.types.Asset;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farzad Farazmand on 28,June,2017
 * farzad.farazmand@gmail.com
 */

public class Slide implements Serializable {

    private Integer beamId;
    private String videoBeamType;
    private List<CategoryData> categories = null;
    private String orientation;
    private String startDateTime;
    private String title;
    private String description;
    private String thumbnail;
    private UserData userDTO;
    private String playerHlsPlaybackUrl;
    private String duration;
    private String vodPublish;
    private String status;
    private int imageResource;
    private String imageFromUrl;
    private String imageFromUrl16By9;
    private String imageFromUrl2By3;
    private Asset objects;
    private RailCommonData railCommonData;
    private int type;
    private String beamCycle;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getBeamId() {
        return beamId;
    }

    public void setBeamId(Integer beamId) {
        this.beamId = beamId;
    }

    public String getVideoBeamType() {
        return videoBeamType;
    }

    public void setVideoBeamType(String videoBeamType) {
        this.videoBeamType = videoBeamType;
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryData> categories) {
        this.categories = categories;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public UserData getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserData userDTO) {
        this.userDTO = userDTO;
    }

    public String getPlayerHlsPlaybackUrl() {
        return playerHlsPlaybackUrl;
    }

    public void setPlayerHlsPlaybackUrl(String playerHlsPlaybackUrl) {
        this.playerHlsPlaybackUrl = playerHlsPlaybackUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVodPublish() {
        return vodPublish;
    }

    public void setVodPublish(String vodPublish) {
        this.vodPublish = vodPublish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBeamCycle() {
        return beamCycle;
    }

    public void setBeamCycle(String beamCycle) {
        this.beamCycle = beamCycle;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getImageFromUrl() {
        return imageFromUrl;
    }

    public void setImageFromUrl(String imageFromUrl) {
        this.imageFromUrl = imageFromUrl;
    }

    public String getImageFromUrl16By9() {
        return imageFromUrl16By9;
    }

    public void setImageFromUrl16By9(String imageFromUrl16By9) {
        this.imageFromUrl16By9 = imageFromUrl16By9;
    }

    public String getImageFromUrl2By3() {
        return imageFromUrl2By3;
    }

    public void setImageFromUrl2By3(String imageFromUrl2By3) {
        this.imageFromUrl2By3 = imageFromUrl2By3;
    }

    public Asset getObjects() {
        return objects;
    }

    public void setObjects(Asset objects) {
        this.objects = objects;
    }

    public RailCommonData getRailCommonData() {
        return railCommonData;
    }

    public void setRailCommonData(RailCommonData railCommonData) {
        this.railCommonData = railCommonData;
    }
}
