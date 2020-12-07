package com.dialog.dialoggo.player.adapter;

import java.util.Objects;

/**
 * Created by anton.afanasiev on 16/03/2017.
 */

public class TrackItem {


    private final String trackName; //Readable name of the track.
    private final String uniqueId; //Unique id, which should be passed to player in order to change track.
    private String trackDescription;
    private boolean isSelected;

    public TrackItem(String trackName, String uniqueId) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;

    }

    public TrackItem(String trackName, String uniqueId, String trackDescription) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;
        this.trackDescription = trackDescription;
    }

    public TrackItem(String trackName, String uniqueId, boolean isSelected) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;
        this.isSelected = isSelected;
    }

    public String getTrackDescription() {
        return trackDescription;
    }

    public void setTrackDescription(String trackdescription) {
        trackDescription = trackdescription;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getUniqueId() {
        return uniqueId;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        TrackItem purchaseModel = (TrackItem) obj;
        return String.valueOf(this.isSelected).equals(String.valueOf(purchaseModel.isSelected));
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackName, uniqueId, isSelected);
    }
}
