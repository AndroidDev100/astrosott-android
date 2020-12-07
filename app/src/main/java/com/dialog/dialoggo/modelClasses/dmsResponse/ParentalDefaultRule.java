package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentalDefaultRule {

    @SerializedName("DefaultRule")
    @Expose
    private String defaultRule;


    public String getDefaultRule() {
        return defaultRule;
    }

    public void setDefaultRule(String defaultRule) {
        this.defaultRule = defaultRule;
    }

    @Override
    public String toString() {
        return "ParentalDefaultRule{" +
                "defaultRule='" + defaultRule + '\'' +
                '}';
    }
}
