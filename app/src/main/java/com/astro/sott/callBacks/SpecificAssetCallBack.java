package com.astro.sott.callBacks;

import android.widget.ImageView;

import com.kaltura.client.types.Asset;

public interface SpecificAssetCallBack {
    void getAsset(boolean status, Asset asset);
    void cancelReminder(ImageView reminderIcon,Asset asset);
    default void setReminder(ImageView reminderIcon, Asset asset){

    }
}
