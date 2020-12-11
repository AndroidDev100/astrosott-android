package com.astro.sott.activities.subscription.model;

public class PlanModel {

    private String title, duration, description;
    private boolean isSelected;

    public PlanModel(boolean isSelected, String title, String duration, String description) {
        this.isSelected =isSelected;
        this.title = title;
        this.duration = duration;
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return "Rs. "+duration+"/day";
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
