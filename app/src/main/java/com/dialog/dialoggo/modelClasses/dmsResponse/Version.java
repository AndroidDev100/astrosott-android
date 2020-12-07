
package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_default")
    @Expose
    private Boolean isDefault;
    @SerializedName("group_configuration_id")
    @Expose
    private String groupConfigurationId;
    @SerializedName("appname")
    @Expose
    private String appname;
    @SerializedName("clientversion")
    @Expose
    private String clientversion;
    @SerializedName("isforceupdate")
    @Expose
    private Boolean isforceupdate;
    @SerializedName("platform")
    @Expose
    private Integer platform;
    @SerializedName("partnerid")
    @Expose
    private Integer partnerid;
    @SerializedName("external_push_id")
    @Expose
    private String externalPushId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getGroupConfigurationId() {
        return groupConfigurationId;
    }

    public void setGroupConfigurationId(String groupConfigurationId) {
        this.groupConfigurationId = groupConfigurationId;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getClientversion() {
        return clientversion;
    }

    public void setClientversion(String clientversion) {
        this.clientversion = clientversion;
    }

    public Boolean getIsforceupdate() {
        return isforceupdate;
    }

    public void setIsforceupdate(Boolean isforceupdate) {
        this.isforceupdate = isforceupdate;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(Integer partnerid) {
        this.partnerid = partnerid;
    }

    public String getExternalPushId() {
        return externalPushId;
    }

    public void setExternalPushId(String externalPushId) {
        this.externalPushId = externalPushId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
