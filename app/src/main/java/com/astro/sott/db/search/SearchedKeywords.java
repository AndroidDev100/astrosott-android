package com.astro.sott.db.search;


import io.realm.RealmObject;

public class SearchedKeywords extends RealmObject {

    private String keywords;
    private Long timeStamp;
    boolean isSelected=false;


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getKeyWords() {
        return keywords;
    }

    public void setKeyWords(String keyords) {
        this.keywords = keyords;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
