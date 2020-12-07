
package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gateways {

    @SerializedName("JsonGW")
    @Expose
    private String jsonGW;

    public String getJsonGW() {
        return jsonGW;
    }

    public void setJsonGW(String jsonGW) {
        this.jsonGW = jsonGW;
    }

}
