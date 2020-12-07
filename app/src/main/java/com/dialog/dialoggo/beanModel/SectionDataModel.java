package com.dialog.dialoggo.beanModel;

import java.util.ArrayList;

public class SectionDataModel {



    private String headerTitle;
    private ArrayList<SingleItemModel> allItemsInSection;
    private int type;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, ArrayList<SingleItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
