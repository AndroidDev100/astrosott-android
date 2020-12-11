package com.astro.sott.activities.subscription.viewmodel;

public class InvokeModel {
    private boolean status;
    private String error;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
