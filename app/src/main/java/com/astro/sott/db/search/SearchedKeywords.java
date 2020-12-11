package com.astro.sott.db.search;


import io.realm.RealmObject;

public class SearchedKeywords extends RealmObject {

    private String keywords;
    private Long timeStamp;


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

}
