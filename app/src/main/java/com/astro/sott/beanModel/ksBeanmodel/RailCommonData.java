package com.astro.sott.beanModel.ksBeanmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.astro.sott.beanModel.VIUChannel;
import com.kaltura.client.types.Asset;

import java.util.List;

public class RailCommonData implements Parcelable {
    private Boolean catchUpBuffer;
    private Long id;
    private String tilte;
    private List<AssetCommonImages> images;
    private Integer type;
    private String name;
    private List<AssetCommonUrls> urls;
    private Asset object;
    private boolean status;
    private int seriesType;
    private String creatorName;
    private Long creatorId;
    private int totalCount;
    private boolean isEntitled;
    private int progress;
    private int position;
    private int liveAsset;
    private boolean isExpended;
    private boolean isChecked;
    private String selectedColor="";

    private boolean isReminderEnabled;

    public boolean isReminderEnabled() {
        return isReminderEnabled;
    }

    public void setReminderEnabled(boolean isReminderEnabled) {
        this.isReminderEnabled = isReminderEnabled;
    }


    public void setCatchUpBuffer(Boolean catchUpBuffer) {
        this.catchUpBuffer = catchUpBuffer;
    }

    public Boolean getCatchUpBuffer() {
        return catchUpBuffer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public Long getId() {
        return id;
    }

    public void setEntitled(boolean entitled) {
        isEntitled = entitled;
    }

    public boolean isEntitled() {
        return isEntitled;
    }

    public void setImages(List<AssetCommonImages> images) {
        this.images = images;
    }

    public List<AssetCommonImages> getImages() {
        return images;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrls(List<AssetCommonUrls> urls) {
        this.urls = urls;
    }

    public List<AssetCommonUrls> getUrls() {
        return urls;
    }

    public RailCommonData() {
    }


    public void setObject(Asset object) {
        this.object = object;
    }

    public Asset getObject() {
        return object;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setSeriesType(int seriesType) {
        this.seriesType = seriesType;
    }

    public int getSeriesType() {
        return seriesType;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setExpended(boolean expended) {
        isExpended = expended;
    }


    public boolean getExpended() {
        return isExpended;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int isLiveAsset() {
        return liveAsset;
    }

    public void setLiveAsset(int liveAsset) {
        this.liveAsset = liveAsset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.catchUpBuffer);
        dest.writeValue(this.id);
        dest.writeString(this.tilte);
        dest.writeTypedList(this.images);
        dest.writeValue(this.type);
        dest.writeString(this.name);
        dest.writeTypedList(this.urls);
        dest.writeParcelable(this.object, flags);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeInt(this.seriesType);
        dest.writeString(this.creatorName);
        dest.writeValue(this.creatorId);
        dest.writeInt(this.totalCount);
        dest.writeInt(this.progress);
        dest.writeInt(this.position);
        dest.writeInt(this.liveAsset);
    }

    protected RailCommonData(Parcel in) {
        this.catchUpBuffer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.tilte = in.readString();
        this.images = in.createTypedArrayList(AssetCommonImages.CREATOR);
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.urls = in.createTypedArrayList(AssetCommonUrls.CREATOR);
        this.object = in.readParcelable(Asset.class.getClassLoader());
        this.status = in.readByte() != 0;
        this.seriesType = in.readInt();
        this.creatorName = in.readString();
        this.creatorId = (Long) in.readValue(Long.class.getClassLoader());
        this.totalCount = in.readInt();
        this.progress = in.readInt();
        this.position = in.readInt();
        this.liveAsset = in.readInt();
    }

    public static final Creator<RailCommonData> CREATOR = new Creator<RailCommonData>() {
        @Override
        public RailCommonData createFromParcel(Parcel source) {
            return new RailCommonData(source);
        }

        @Override
        public RailCommonData[] newArray(int size) {
            return new RailCommonData[size];
        }
    };
    private VIUChannel railDetail;
    public VIUChannel getRailDetail() {
        return railDetail;
    }

    public void setRailDetail(VIUChannel railDetail) {
        this.railDetail = railDetail;
    }

    private AssetCommonBean assetCommonBean;
    public AssetCommonBean getAssetCommonBean() {
        return assetCommonBean;
    }

    public void setAssetCommonBean(AssetCommonBean assetCommonBean) {
        this.assetCommonBean = assetCommonBean;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public String getSelectedColor() {
        return selectedColor;
    }
}
