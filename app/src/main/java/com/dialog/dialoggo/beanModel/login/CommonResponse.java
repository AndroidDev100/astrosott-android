package com.dialog.dialoggo.beanModel.login;

import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Channel;
import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public class CommonResponse {
    private boolean status;
    private String message;
    private int isDeviceAdded;
    private List<HouseholdDevice> deviceList;
    private String errorCode;
    private int isAssetAdded;
    private String assetID;
    private List<Asset> myUploadedVideos;
    private int totalEpisodes;
    private Asset currentProgram;
    private boolean livePrograme;

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    List<Integer> list;
    private int programTime;

    public List<RailCommonData> getRailCommonData() {
        return railCommonData;
    }

    public void setRailCommonData(List<RailCommonData> railCommonData) {
        this.railCommonData = railCommonData;
    }

    List<RailCommonData> railCommonData;
    private List<Channel> channelList;
    private Response<ListResponse<Asset>> assetList;
    private boolean isNotification = false;
    private int totalCount;

    public List<PersonalList> getPersonalLists() {
        return personalLists;
    }

    public void setPersonalLists(List<PersonalList> personalLists) {
        this.personalLists = personalLists;
    }

    private List<PersonalList> personalLists;



    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setIsDeviceAdded(int isDeviceAdded) {
        this.isDeviceAdded = isDeviceAdded;
    }

    public int getIsDeviceAdded() {
        return isDeviceAdded;
    }

    public void setDeviceList(List<HouseholdDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public List<HouseholdDevice> getDeviceList() {
        return deviceList;
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setIsAssetAdded(int isAssetAdded) {
        this.isAssetAdded = isAssetAdded;
    }

    public int getIsAssetAdded() {
        return isAssetAdded;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setMyUploadedVideos(List<Asset> myUploadedVideos) {
        this.myUploadedVideos = myUploadedVideos;
    }

    public List<Asset> getMyUploadedVideos() {
        return myUploadedVideos;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setCurrentProgram(Asset currentProgram) {
        this.currentProgram = currentProgram;
    }

    public Asset getCurrentProgram() {
        return currentProgram;
    }

    public void setLivePrograme(boolean livePrograme) {
        this.livePrograme = livePrograme;
    }


    public boolean getLivePrograme() {
        return livePrograme;
    }

    public void setProgramTime(int programTime) {
        this.programTime = programTime;
    }

    public int getProgramTime() {
        return programTime;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setAssetList(Response<ListResponse<Asset>> result) {
        this.assetList = result;
    }

    public Response<ListResponse<Asset>> getAssetList() {
        return assetList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
