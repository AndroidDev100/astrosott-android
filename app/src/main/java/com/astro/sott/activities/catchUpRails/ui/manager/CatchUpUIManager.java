package com.astro.sott.activities.catchUpRails.ui.manager;

public class CatchUpUIManager {

    private static CatchUpUIManager mInstance;
    private boolean isAssetLive;

    private CatchUpUIManager(){

    }

    public static synchronized CatchUpUIManager getInstance(){
        if(mInstance == null){
            mInstance = new CatchUpUIManager();
        }
        return mInstance;
    }

    public boolean getAssetLive() {
        return isAssetLive;
    }

    public void setAssetLive(boolean assetLive) {
        this.isAssetLive = assetLive;
    }
}
