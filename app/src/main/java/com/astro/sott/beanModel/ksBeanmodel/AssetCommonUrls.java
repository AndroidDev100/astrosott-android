package com.astro.sott.beanModel.ksBeanmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class AssetCommonUrls implements Parcelable {
    private String url;
    private String urlType;
    private String duration;


    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getUrlType() {
        return urlType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.urlType);
    }

    public AssetCommonUrls() {
    }

    private AssetCommonUrls(Parcel in) {
        this.url = in.readString();
        this.urlType = in.readString();
    }

    public static final Creator<AssetCommonUrls> CREATOR = new Creator<AssetCommonUrls>() {
        @Override
        public AssetCommonUrls createFromParcel(Parcel source) {
            return new AssetCommonUrls(source);
        }

        @Override
        public AssetCommonUrls[] newArray(int size) {
            return new AssetCommonUrls[size];
        }
    };


    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }
}
