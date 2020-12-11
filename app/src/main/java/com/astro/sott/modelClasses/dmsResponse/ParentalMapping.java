package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.JsonElement;

public class ParentalMapping {
    private String key;
    private JsonElement mappingList;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JsonElement getMappingList() {
        return mappingList;
    }

    public void setMappingList(JsonElement mappingList) {
        this.mappingList = mappingList;
    }
}
