package com.astro.sott.utils.helpers;

import android.app.Activity;

import com.astro.sott.beanModel.SingleItemModel;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.beanModel.SectionDataModel;

import java.util.ArrayList;

public class ShimmerDataModel {

    public ShimmerDataModel(Activity activity) {

    }

    public ArrayList<SectionDataModel> getList(int type) {
        ArrayList<SectionDataModel> allSampleData = new ArrayList<>();
        if (type == 1) {
            for (int i = 1; i <= 5; i++) {

                SectionDataModel dm = new SectionDataModel();
                if (i == 1) {
                    dm.setType(2);
                } else if (i == 2) {
                    dm.setType(2);
                } else if (i == 3) {
                    dm.setType(2);

                } else if (i == 4) {
                    dm.setType(2);
                } else {
                    dm.setType(2);
                }

                dm.setHeaderTitle("Section " + i);
                ArrayList<SingleItemModel> singleItem = new ArrayList<>();
                for (int j = 0; j <= 5; j++) {
                    singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                }

                dm.setAllItemsInSection(singleItem);
                allSampleData.add(dm);

            }
        } else {
            for (int i = 1; i <= 5; i++) {

                SectionDataModel dm = new SectionDataModel();
                if (i == 1) {
                    dm.setType(0);
                } else if (i == 2) {
                    dm.setType(1);
                } else if (i == 3) {
                    dm.setType(2);

                } else if (i == 4) {
                    dm.setType(4);
                } else {
                    dm.setType(3);
                }

                dm.setHeaderTitle("Section " + i);
                ArrayList<SingleItemModel> singleItem = new ArrayList<>();
                for (int j = 0; j <= 5; j++) {
                    singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                }

                dm.setAllItemsInSection(singleItem);
                allSampleData.add(dm);

            }
        }

        return allSampleData;
    }

    public ArrayList<Slide> getSlides() {
        ArrayList<Slide> slides = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Slide slide = new Slide();
            //slide.setImageResource(R.drawable.banner_img);
            slide.setType(1);
            slides.add(slide);
        }
        return slides;
    }
}
