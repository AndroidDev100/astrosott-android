package com.astro.sott.baseModel;

public class PrefrenceBean {
    private String name;
    private int position;
    private boolean checked;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean getChecked() {
        return checked;
    }
}
