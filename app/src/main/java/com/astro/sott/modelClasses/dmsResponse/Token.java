
package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("valid")
    @Expose
    private Long valid;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValid() {
        return valid;
    }

    public void setValid(Long valid) {
        this.valid = valid;
    }

}
