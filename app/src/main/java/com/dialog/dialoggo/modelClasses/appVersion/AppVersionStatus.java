package com.dialog.dialoggo.modelClasses.appVersion;

public class AppVersionStatus {


    private boolean status;
    private int currentVersion;
    private int playStoreVersion;
    private String flavor;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
       return status;
    }


    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setPlayStoreVersion(int playStoreVersion) {
        this.playStoreVersion = playStoreVersion;
    }

    public int getPlayStoreVersion() {
        return playStoreVersion;
    }
}
