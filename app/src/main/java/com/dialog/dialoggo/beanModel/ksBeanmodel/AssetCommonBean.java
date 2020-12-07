package com.dialog.dialoggo.beanModel.ksBeanmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.utils.helpers.carousel.model.Slide;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Channel;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class AssetCommonBean implements Parcelable {
    private int railType;
    private List<RailCommonData> railAssetList;
    private String title;
    private String fanPlacementId;
    private Long ID;
    private int contestId;
    private ArrayList<Slide> slides;
    private boolean status;
    private int totalVideoCount;
    private int totalCount;
    private int moreType;
    private int moreID;
    private String moreGenre;
    private int moreAssetType;
    private String moreSeriesID;
    private List<Channel> channelList;
    private List<VIUChannel> dtChannelList;
    private Response<ListResponse<Asset>> assetList;
    private Asset asset;
    private String posterURL;
    private VIUChannel railDetail;
    private int widgetType;
    private BaseCategory category;
    private boolean isClip;

    public boolean isClip() {
        return isClip;
    }

    public void setClip(boolean clip) {
        isClip = clip;
    }

    public BaseCategory getCategory() {
        return category;
    }

    public void setCategory(BaseCategory category) {
        this.category = category;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(int widgetType) {
        this.widgetType = widgetType;
    }

    public VIUChannel getRailDetail() {
        return railDetail;
    }

    public void setRailDetail(VIUChannel railDetail) {
        this.railDetail = railDetail;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setRailType(int railType) {
        this.railType = railType;
    }

    public int getRailType() {
        return railType;
    }

    public void setRailAssetList(List<RailCommonData> railAssetList) {
        this.railAssetList = railAssetList;
    }

    public List<RailCommonData> getRailAssetList() {
        return railAssetList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setFanPlacementId(String fanPlacementId) {
        this.fanPlacementId = fanPlacementId;
    }

    public String getFanPlacementId() {
        return fanPlacementId;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getID() {
        return ID;
    }

    public void setStatus(boolean connected) {
        this.status = connected;
    }

    public boolean getStatus() {
        return status;
    }

    public AssetCommonBean() {
    }

    public void setSlides(ArrayList<Slide> slides) {
        this.slides = slides;
    }

    public ArrayList<Slide> getSlides() {
        return slides;
    }


    public void setTotalVideoCount(int totalVideoCount) {
        this.totalVideoCount = totalVideoCount;
    }

    public int getTotalVideoCount() {
        return totalVideoCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setMoreType(int moreType) {
        this.moreType = moreType;
    }

    public int getMoreType() {
        return moreType;
    }

    public void setMoreID(int moreID) {
        this.moreID = moreID;
    }

    public int getMoreID() {
        return moreID;
    }

    public void setMoreGenre(String moreGenre) {
        this.moreGenre = moreGenre;
    }

    public String getMoreGenre() {
        return moreGenre;
    }

    public void setMoreAssetType(int moreAssetType) {
        this.moreAssetType = moreAssetType;
    }

    public int getMoreAssetType() {
        return moreAssetType;
    }

    public void setMoreSeriesID(String moreSeriesID) {
        this.moreSeriesID = moreSeriesID;
    }

    public String getMoreSeriesID() {
        return moreSeriesID;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setDTChannelList(List<VIUChannel> channelList) {
        this.dtChannelList = channelList;
    }

    public List<VIUChannel> getDTChannelList() {
        return dtChannelList;
    }

    public void setAssetList(Response<ListResponse<Asset>> result) {
        this.assetList = result;
    }

    public Response<ListResponse<Asset>> getAssetList() {
        return assetList;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Asset getAsset() {
        return asset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.railType);
        dest.writeTypedList(this.railAssetList);
        dest.writeString(this.title);
        dest.writeString(this.fanPlacementId);
        dest.writeValue(this.ID);
        dest.writeList(this.slides);

        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalVideoCount);
        dest.writeInt(this.totalCount);
        dest.writeInt(this.moreType);
        dest.writeInt(this.moreID);
        dest.writeString(this.moreGenre);
        dest.writeInt(this.moreAssetType);
        dest.writeString(this.moreSeriesID);
        dest.writeList(this.channelList);
        dest.writeList(this.dtChannelList);
        dest.writeParcelable(this.asset, flags);
        dest.writeInt(this.contestId);
    }

    protected AssetCommonBean(Parcel in) {
        this.railType = in.readInt();
        this.railAssetList = in.createTypedArrayList(RailCommonData.CREATOR);
        this.title = in.readString();
        this.fanPlacementId = in.readString();
        this.ID = (Long) in.readValue(Long.class.getClassLoader());
        this.slides = new ArrayList<Slide>();
        in.readList(this.slides, Slide.class.getClassLoader());
        this.status = in.readByte() != 0;
        this.totalVideoCount = in.readInt();
        this.totalCount = in.readInt();
        this.moreType = in.readInt();
        this.moreID = in.readInt();
        this.moreGenre = in.readString();
        this.moreAssetType = in.readInt();
        this.moreSeriesID = in.readString();
        this.channelList = new ArrayList<Channel>();
        in.readList(this.channelList, Channel.class.getClassLoader());
        this.dtChannelList = new ArrayList<VIUChannel>();
        in.readList(this.dtChannelList, VIUChannel.class.getClassLoader());
        this.asset = in.readParcelable(Asset.class.getClassLoader());
        this.contestId=in.readInt();

    }

    public static final Creator<AssetCommonBean> CREATOR = new Creator<AssetCommonBean>() {
        @Override
        public AssetCommonBean createFromParcel(Parcel source) {
            return new AssetCommonBean(source);
        }

        @Override
        public AssetCommonBean[] newArray(int size) {
            return new AssetCommonBean[size];
        }
    };
}

