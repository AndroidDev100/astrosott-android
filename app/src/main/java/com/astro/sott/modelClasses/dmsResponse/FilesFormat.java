
package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilesFormat {

    @SerializedName("HLS_SD")
    @Expose
    private String hlsSD;
    @SerializedName("HLS_HD")
    @Expose
    private String hlsHD;
    @SerializedName("DASH_SD")
    @Expose
    private String dashSD;
    @SerializedName("DASH_HD")
    @Expose
    private String dashHD;

    public String getHlsSD() {
        return hlsSD;
    }

    public void setHlsSD(String hlsSD) {
        this.hlsSD = hlsSD;
    }

    public String getHlsHD() {
        return hlsHD;
    }

    public void setHlsHD(String hlsHD) {
        this.hlsHD = hlsHD;
    }

    public String getDashSD() {
        return dashSD;
    }

    public void setDashSD(String dashSD) {
        this.dashSD = dashSD;
    }

    public String getDashHD() {
        return dashHD;
    }

    public void setDashHD(String dashHD) {
        this.dashHD = dashHD;
    }
}
