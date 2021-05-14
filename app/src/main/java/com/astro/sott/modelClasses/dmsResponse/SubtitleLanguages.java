package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.JsonArray;

public class SubtitleLanguages {

    String key;
    JsonArray value;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setValue(JsonArray value) {
        this.value = value;
    }

    public JsonArray getValue() {
        return value;
    }

}
