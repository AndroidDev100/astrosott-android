package com.astro.sott.callBacks.reminder;

import android.widget.ImageView;

import com.kaltura.client.types.Asset;

public interface ReminderAddRemoveListener {
    void setReminder(ImageView notification, Asset asset);
    void cancelReminder(Asset asset);
}
