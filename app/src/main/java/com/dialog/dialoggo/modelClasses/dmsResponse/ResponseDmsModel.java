
package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDmsModel {

    @SerializedName("token")
    @Expose
    private Token token;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("udid")
    @Expose
    private String udid;
    @SerializedName("version")
    @Expose
    private Version version;
    @SerializedName("params")
    @Expose
    private Params params;

    private ArrayList<ParentalLevels> parentalLevels;
    private ArrayList<ParentalDescription> parentalDescriptions;
    private ArrayList<ParentalMapping> mappingArrayList;

    public ArrayList<ParentalMapping> getMappingArrayList() {
        return mappingArrayList;
    }

    public void setMappingArrayList(ArrayList<ParentalMapping> mappingArrayList) {
        this.mappingArrayList = mappingArrayList;
    }

    public ArrayList<ParentalDescription> getParentalDescriptions() {
        return parentalDescriptions;
    }

    public void setParentalDescriptions(ArrayList<ParentalDescription> parentalDescriptions) {
        this.parentalDescriptions = parentalDescriptions;
    }

    public ArrayList<ParentalLevels> getParentalLevels() {
        return parentalLevels;
    }

    public void setParentalLevels(ArrayList<ParentalLevels> parentalLevels) {
        this.parentalLevels = parentalLevels;
    }

    private ArrayList<String> parentalRestrictions;

    public ArrayList<String> getParentalRestrictions() {
        return parentalRestrictions;
    }

    public void setParentalRestrictions(ArrayList<String> parentalRestrictions) {
        this.parentalRestrictions = parentalRestrictions;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

}
