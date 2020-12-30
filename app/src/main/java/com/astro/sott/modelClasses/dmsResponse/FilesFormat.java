
package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilesFormat {

    @SerializedName("SS")
    @Expose
    private String sS;
    @SerializedName("DASH")
    @Expose
    private String dASH;
    @SerializedName("DASH_WV")
    @Expose
    private String dASHWV;
    @SerializedName("HLS")
    @Expose
    private String hLS;

    public String getSS() {
        return sS;
    }

    public void setSS(String sS) {
        this.sS = sS;
    }

    public String getDASH() {
        return dASH;
    }

    public void setDASH(String dASH) {
        this.dASH = dASH;
    }

    public String getDASHWV() {
        return dASHWV;
    }

    public void setDASHWV(String dASHWV) {
        this.dASHWV = dASHWV;
    }

    public String getHLS() {
        return hLS;
    }

    public void setHLS(String hLS) {
        this.hLS = hLS;
    }
}
