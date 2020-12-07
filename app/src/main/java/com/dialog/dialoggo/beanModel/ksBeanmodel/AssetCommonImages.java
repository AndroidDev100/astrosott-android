package com.dialog.dialoggo.beanModel.ksBeanmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class AssetCommonImages implements Parcelable {


    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
    }

    public AssetCommonImages() {
    }

    private AssetCommonImages(Parcel in) {
        this.imageUrl = in.readString();
    }

    public static final Creator<AssetCommonImages> CREATOR = new Creator<AssetCommonImages>() {
        @Override
        public AssetCommonImages createFromParcel(Parcel source) {
            return new AssetCommonImages(source);
        }

        @Override
        public AssetCommonImages[] newArray(int size) {
            return new AssetCommonImages[size];
        }
    };

}
