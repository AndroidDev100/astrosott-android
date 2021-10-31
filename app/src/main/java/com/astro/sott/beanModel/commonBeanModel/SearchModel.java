package com.astro.sott.beanModel.commonBeanModel;


import android.os.Parcel;
import android.os.Parcelable;

import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

public class SearchModel implements Parcelable {

    private String headerTitle;
    private List<Asset> allItemsInSection;
    private int type;
    private int totalCount;
    private String searchString;

    public static final String MEDIATYPE_SEARCH_MOVIE = "Movie";
    public static final String MEDIATYPE_SEARCH_WEBEPISODE = "Web Episode";
    public static final String MEDIATYPE_SEARCH_WEBSERIES = "Drama";
    public static final String MEDIATYPE_SEARCH_LINEAR = "Linear";
    public static final String MEDIATYPE_SEARCH_LIVE_CHANNELS = "Live Channel";
    public static final String MEDIATYPE_SEARCH_SHORTFILM = "Short Video";
    public static final String MEDIATYPE_SEARCH_UGCVIDEO = "UGC Video";
    public static final String MEDIATYPE_SEARCH_PROGRAM = "Program";
    public static final String MEDIATYPE_SEARCH_SPOTLIGHT_SERIES = "Spotlight Series";
    public static final String MEDIATYPE_SEARCH_SPOTLIGHT_EPISODE = "Spotlight Episode";

    public static final String MEDIATYPE_SERIES = "Series";
    public static final String MEDIATYPE_EPISODE = "Episode";
    public static final String MEDIATYPE_COLLECTION = "Collection";
    public static final String MEDIATYPE_LINEAR = "Live";

    public static final String SEARCH_VOD = "VOD";
    public static final String SEARCH_TV_SHOWS = "TV Shows";
    public static final String SEARCH_LIVE = "Live TV";
    public static final String SEARCH_PAGE = "Collections";

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void addItem(Asset item) {
        this.allItemsInSection.add(item);
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<Asset> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(List<Asset> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }




    public SearchModel() {
        allItemsInSection = new ArrayList<>();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headerTitle);
        dest.writeList(this.allItemsInSection);
        dest.writeInt(this.type);
        dest.writeInt(this.totalCount);
        dest.writeString(this.searchString);
    }

    private SearchModel(Parcel in) {
        this.headerTitle = in.readString();
        this.allItemsInSection = new ArrayList<>();
        in.readList(this.allItemsInSection, Asset.class.getClassLoader());
        this.type = in.readInt();
        this.totalCount = in.readInt();
        this.searchString = in.readString();
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel source) {
            return new SearchModel(source);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };
}
