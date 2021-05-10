package com.astro.sott.utils.helpers;

public class NavigationItem {

    private static NavigationItem navigationItem;
private String tab;

    public static NavigationItem getInstance() {
        if (navigationItem == null) {
            navigationItem = new NavigationItem();
        }
        return navigationItem;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getTab() {
        return tab;
    }
}
