
package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories {

    @SerializedName("Root")
    @Expose
    private String root;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

}
