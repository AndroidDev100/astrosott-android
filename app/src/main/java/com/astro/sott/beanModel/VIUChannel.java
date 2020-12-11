package com.astro.sott.beanModel;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.astro.sott.R;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.PrintLogging;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.Enum.AdType;
import com.enveu.Enum.ImageType;
import com.enveu.Enum.Layouts;
import com.enveu.Enum.PlaylistType;
import com.enveu.Enum.PredefinePlaylistType;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;

import static com.enveu.Enum.Layouts.CAR;
import static com.enveu.Enum.Layouts.HOR;

public class VIUChannel implements Parcelable {
    private int responseCode;
    private int contentSize;
    private int morePageSize;
    private int displayOrder;
    private boolean isProgram;
    private boolean status;
    private boolean showHeader;
    private boolean contentShowMoreButton;
    private boolean isSortable;

    private long id;

    private int widgetType;
    private String description;
    private String name;
    private String screen;
    private String type;
    private String contentID;
    private String referenceName;
    private String layout;

    private String adPlatformType;
    private String adType;
    private String adID;

    private String contentImageType;
    private String contentListinglayout;
    private String contentPlayListType;
    private String imageSource;

    private String imageURL;
    private String manualImageAssetId;
    private String landingPageType;
    private String landingPageAssetId;
    private String landingPagePlayListId;
    private String landingPagetarget;
    private String contentIndicator;
    private String htmlLink;

    private String predefPlaylistType;
    private String landingPageTitle;
    private String message;
    private String widgetImageType;

    private Object contentPlayListArray;
    private Object filter;
    private Object isAnonymousUser;
    private Object isLoggedInUser;
    private Asset asset;
    private BaseCategory category;

    public VIUChannel() {
    }


    public VIUChannel(Context context, BaseCategory category) {
        try {
            // TODO  description value is set via below method
            getScreenListing(context, category);
            this.category = category;
            this.responseCode = category.getResponseCode() == null ? 0 : category.getResponseCode();
            this.contentSize = category.getContentSize() == null ? 0 : category.getContentSize();
            this.morePageSize = category.getMorePageSize() == null ? 0 : category.getMorePageSize();
            this.displayOrder = category.getDisplayOrder() == null ? 0 : category.getDisplayOrder();


            this.isProgram = category.isProgram() == null ? false : category.isProgram();
            this.status = category.getStatus() == null ? false : category.getStatus();
            this.showHeader = category.getShowHeader() == null ? false : category.getShowHeader();
            this.contentShowMoreButton = category.getContentShowMoreButton() == null ? false : category.getContentShowMoreButton();
            this.isSortable = category.isSortable() == null ? false : category.isSortable();

            this.id = category.getContentID() == null ? 0 : Integer.parseInt(category.getContentID());

            this.name = category.getName() == null ? "" : (String) category.getName();
            this.screen = category.getScreen() == null ? "" : (String) category.getScreen();
            this.type = category.getType() == null ? "" : (String) category.getType();
            this.contentID = category.getContentID() == null ? "" : (String) category.getContentID();
            this.referenceName = category.getReferenceName() == null ? "" : (String) category.getReferenceName();
            this.layout = category.getLayout() == null ? "" : (String) category.getLayout();

            this.adPlatformType = category.getAdPlatformType() == null ? "" : (String) category.getAdPlatformType();
            this.adType = category.getAdType() == null ? "" : (String) category.getAdType();
            this.adID = category.getAdID() == null ? "" : (String) category.getAdID();
            this.contentImageType = category.getContentImageType() == null ? "" : (String) category.getContentImageType();
            this.contentListinglayout = category.getContentPlayListType() == null ? "" : (String) category.getContentPlayListType();
            this.contentPlayListType = category.getContentPlayListType() == null ? "" : (String) category.getContentPlayListType();
            this.imageSource = category.getImageSource() == null ? "" : (String) category.getImageSource();

            this.imageURL = category.getImageURL() == null ? "" : (String) category.getImageURL();
            this.manualImageAssetId = category.getManualImageAssetId() == null ? "" : (String) category.getManualImageAssetId();
            this.landingPageType = category.getLandingPageType() == null ? "" : (String) category.getLandingPageType();
            this.landingPageAssetId = category.getLandingPageAssetId() == null ? "" : (String) category.getLandingPageAssetId();
            this.landingPagePlayListId = category.getLandingPagePlayListId() == null ? "" : (String) category.getLandingPagePlayListId();
            this.landingPagetarget = category.getLandingPagetarget() == null ? "" : (String) category.getLandingPagetarget();
            this.contentIndicator = category.getContentIndicator() == null ? "" : (String) category.getContentIndicator();
            this.htmlLink = category.getHtmlLink() == null ? "" : (String) category.getHtmlLink();

            this.predefPlaylistType = category.getPredefPlaylistType() == null ? "" : (String) category.getPredefPlaylistType();
            this.landingPageTitle = category.getLandingPageTitle() == null ? "" : (String) category.getLandingPageTitle();
            this.message = category.getMessage() == null ? "" : (String) category.getMessage();
            this.widgetImageType = category.getWidgetImageType() == null ? "" : (String) category.getWidgetImageType();

            this.contentPlayListArray = category.getContentPlayListArray() == null ? new ArrayList<>() : category.getContentPlayListArray();
            this.filter = category.getFilter() == null ? new ArrayList<>() : category.getFilter();
            this.isAnonymousUser = category.isAnonymousUser() == null ? "" : category.isAnonymousUser();
            this.isLoggedInUser = category.isLoggedInUser() == null ? "" : category.isLoggedInUser();

        } catch (Exception e) {

        }
    }


    private void getScreenListing(Context context, BaseCategory screenWidget) {
        String type = "";
        PrintLogging.printLog("", "screenWidgetType--" + screenWidget.getType());
        if (screenWidget.getType() != null)
            type = screenWidget.getType();
        if (type.equalsIgnoreCase(AppConstants.WIDGET_TYPE_CONTENT)) {
            getRailDetails(screenWidget);
        } else if (type.equalsIgnoreCase(AppConstants.WIDGET_TYPE_AD)) {
            getAdsDetails(context, screenWidget);
        }
    }

    private void getAdsDetails(Context context, BaseCategory screenWidget) {

        if (screenWidget.getAdPlatformType().equalsIgnoreCase(AdType.DFP.name())) {
            this.description = AppConstants.KEY_DFP_ADS;
            if (screenWidget.getLayout().equalsIgnoreCase(Layouts.BAN.name()))
                this.setWidgetType(AppConstants.ADS_BANNER);
            else
                this.setWidgetType(AppConstants.ADS_MREC);
        } else {
            boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                this.description = AppConstants.KEY_FB_FANTABLET;
                this.widgetType = AppConstants.RAIL_FAN_TABLET;
            } else {
                this.description = AppConstants.KEY_FB_FAN;
                this.widgetType = AppConstants.RAIL_FAN_MOBILE;

            }

        }

    }

    private void getRailDetails(BaseCategory screenWidget) {
        if (screenWidget.getLayout() != null && screenWidget.getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            getHeroDetails(screenWidget);
        } else {
            if (screenWidget.getContentPlayListType() != null && (screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.BVC.name()) || screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.EN_OVP.name()))) {
                //TODO: Get Playlist data for Other
            } else if (screenWidget.getContentPlayListType() != null && screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.K_PDF.name())) {
                //TODO: Get Playlist data from Predefined Kaltura

                if (screenWidget.getType() != null) {

                    if (screenWidget.getPredefPlaylistType().equalsIgnoreCase(PredefinePlaylistType.CON_W.name())) {
                        this.description = AppConstants.KEY_CONTINUE_WATCHING;
                        setRailType(screenWidget, false);
                    } else if (screenWidget.getPredefPlaylistType().equalsIgnoreCase(PredefinePlaylistType.MY_W.name())) {
                        this.description = AppConstants.KEY_MY_WATCHLIST;
                        setRailType(screenWidget, false);
                    }
                }

            } else if (screenWidget.getContentPlayListType() != null && screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.KTM.name())) {
                //TODO: Get Playlist data from Kaltura
                setRailType(screenWidget, true);
            }
        }
    }


    private void setRailType(BaseCategory screenWidget, boolean isRail) {
        String railType = "";
        String layoutType = screenWidget.getLayout();
        String layoutImageType = screenWidget.getContentImageType();
        if (layoutType.equalsIgnoreCase(CAR.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.CAROUSEL_LANDSCAPE;
                this.widgetType = AppConstants.CAROUSEL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.CAROUSEL_POTRAIT;
                this.widgetType = AppConstants.CAROUSEL_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.CAROUSEL_SQUARE;
                this.widgetType = AppConstants.CAROUSEL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CST.name())) {
                railType = AppConstants.CAROUSEL_CUSTOM;
                this.widgetType = AppConstants.CAROUSEL_CST_CUSTOM;
            }


        } else if (layoutType.equalsIgnoreCase(HOR.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.HORIZONTAL_LANDSCAPE;
                this.widgetType = AppConstants.HORIZONTAL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.HORIZONTAL_POTRAIT;
                this.widgetType = AppConstants.HORIZONTAL_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
                railType = AppConstants.HORIZONTAL_POSTER;
                this.widgetType = AppConstants.HORIZONTAL_PR_POSTER;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.HORIZONTAL_SQUARE;
                this.widgetType = AppConstants.HORIZONTAL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.HORIZONTAL_CIRCLE;
                this.widgetType = AppConstants.HORIZONTAL_CIR_CIRCLE;
            }
        }
        if (isRail)
            this.description = railType;

    }

    public int getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(int widgetType) {
        this.widgetType = widgetType;
    }


    public BaseCategory getCategory() {
        return category;
    }

    public void setCategory(BaseCategory category) {
        this.category = category;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }


    private void getHeroDetails(BaseCategory screenWidget) {
        this.description = Layouts.HRO.name();
        String layoutImageType = screenWidget.getContentImageType();
        if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
            this.widgetType = AppConstants.HERO_LDS_LANDSCAPE;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
            this.widgetType = AppConstants.HERO_PR_POTRAIT;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
            this.widgetType = AppConstants.HERO_PR_POSTER;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
            this.widgetType = AppConstants.HERO_SQR_SQUARE;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
            this.widgetType = AppConstants.HERO_CIR_CIRCLE;
        }else if (layoutImageType.equalsIgnoreCase(ImageType.LDS2.name())) {
            this.widgetType = AppConstants.HERO_LDS_BANNER;
        }


    }


    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }

    public int getMorePageSize() {
        return morePageSize;
    }

    public void setMorePageSize(int morePageSize) {
        this.morePageSize = morePageSize;
    }

    public boolean isProgram() {
        return isProgram;
    }

    public void setProgram(boolean program) {
        isProgram = program;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public boolean isContentShowMoreButton() {
        return contentShowMoreButton;
    }

    public void setContentShowMoreButton(boolean contentShowMoreButton) {
        this.contentShowMoreButton = contentShowMoreButton;
    }

    public boolean isSortable() {
        return isSortable;
    }

    public void setSortable(boolean sortable) {
        isSortable = sortable;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getAdPlatformType() {
        return adPlatformType;
    }

    public void setAdPlatformType(String adPlatformType) {
        this.adPlatformType = adPlatformType;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getContentImageType() {
        return contentImageType;
    }

    public void setContentImageType(String contentImageType) {
        this.contentImageType = contentImageType;
    }

    public String getContentListinglayout() {
        return contentListinglayout;
    }

    public void setContentListinglayout(String contentListinglayout) {
        this.contentListinglayout = contentListinglayout;
    }

    public String getContentPlayListType() {
        return contentPlayListType;
    }

    public void setContentPlayListType(String contentPlayListType) {
        this.contentPlayListType = contentPlayListType;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getManualImageAssetId() {
        return manualImageAssetId;
    }

    public void setManualImageAssetId(String manualImageAssetId) {
        this.manualImageAssetId = manualImageAssetId;
    }

    public String getLandingPageType() {
        return landingPageType;
    }

    public void setLandingPageType(String landingPageType) {
        this.landingPageType = landingPageType;
    }

    public String getLandingPageAssetId() {
        return landingPageAssetId;
    }

    public void setLandingPageAssetId(String landingPageAssetId) {
        this.landingPageAssetId = landingPageAssetId;
    }

    public String getLandingPagePlayListId() {
        return landingPagePlayListId;
    }

    public void setLandingPagePlayListId(String landingPagePlayListId) {
        this.landingPagePlayListId = landingPagePlayListId;
    }

    public String getLandingPagetarget() {
        return landingPagetarget;
    }

    public void setLandingPagetarget(String landingPagetarget) {
        this.landingPagetarget = landingPagetarget;
    }

    public String getContentIndicator() {
        return contentIndicator;
    }

    public void setContentIndicator(String contentIndicator) {
        this.contentIndicator = contentIndicator;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public String getPredefPlaylistType() {
        return predefPlaylistType;
    }

    public void setPredefPlaylistType(String predefPlaylistType) {
        this.predefPlaylistType = predefPlaylistType;
    }

    public String getLandingPageTitle() {
        return landingPageTitle;
    }

    public void setLandingPageTitle(String landingPageTitle) {
        this.landingPageTitle = landingPageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWidgetImageType() {
        return widgetImageType;
    }

    public void setWidgetImageType(String widgetImageType) {
        this.widgetImageType = widgetImageType;
    }

    public Object getContentPlayListArray() {
        return contentPlayListArray;
    }

    public void setContentPlayListArray(Object contentPlayListArray) {
        this.contentPlayListArray = contentPlayListArray;
    }

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public Object getIsAnonymousUser() {
        return isAnonymousUser;
    }

    public void setIsAnonymousUser(Object isAnonymousUser) {
        this.isAnonymousUser = isAnonymousUser;
    }


    public Object getIsLoggedInUser() {
        return isLoggedInUser;
    }

    public void setIsLoggedInUser(Object isLoggedInUser) {
        this.isLoggedInUser = isLoggedInUser;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.responseCode);
        dest.writeInt(this.contentSize);
        dest.writeInt(this.morePageSize);
        dest.writeInt(this.displayOrder);
        dest.writeByte(this.isProgram ? (byte) 1 : (byte) 0);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showHeader ? (byte) 1 : (byte) 0);
        dest.writeByte(this.contentShowMoreButton ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSortable ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
        dest.writeInt(this.widgetType);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(this.screen);
        dest.writeString(this.type);
        dest.writeString(this.contentID);
        dest.writeString(this.referenceName);
        dest.writeString(this.layout);
        dest.writeString(this.adPlatformType);
        dest.writeString(this.adType);
        dest.writeString(this.adID);
        dest.writeString(this.contentImageType);
        dest.writeString(this.contentListinglayout);
        dest.writeString(this.contentPlayListType);
        dest.writeString(this.imageSource);
        dest.writeString(this.imageURL);
        dest.writeString(this.manualImageAssetId);
        dest.writeString(this.landingPageType);
        dest.writeString(this.landingPageAssetId);
        dest.writeString(this.landingPagePlayListId);
        dest.writeString(this.landingPagetarget);
        dest.writeString(this.contentIndicator);
        dest.writeString(this.htmlLink);
        dest.writeString(this.predefPlaylistType);
        dest.writeString(this.landingPageTitle);
        dest.writeString(this.message);
        dest.writeString(this.widgetImageType);
        //  dest.writeParcelable((Parcelable)this.contentPlayListArray, flags);
        //  dest.writeList((Parcelable)this.filter, flags);
        // dest.writeParcelable((Parcelable)this.isAnonymousUser, flags);
        // dest.writeParcelable((Parcelable) this.isLoggedInUser, flags);
        dest.writeParcelable(this.asset, flags);
        dest.writeParcelable(this.category, flags);
    }

    protected VIUChannel(Parcel in) {
        this.responseCode = in.readInt();
        this.contentSize = in.readInt();
        this.morePageSize = in.readInt();
        this.displayOrder = in.readInt();
        this.isProgram = in.readByte() != 0;
        this.status = in.readByte() != 0;
        this.showHeader = in.readByte() != 0;
        this.contentShowMoreButton = in.readByte() != 0;
        this.isSortable = in.readByte() != 0;
        this.id = in.readLong();
        this.widgetType = in.readInt();
        this.description = in.readString();
        this.name = in.readString();
        this.screen = in.readString();
        this.type = in.readString();
        this.contentID = in.readString();
        this.referenceName = in.readString();
        this.layout = in.readString();
        this.adPlatformType = in.readString();
        this.adType = in.readString();
        this.adID = in.readString();
        this.contentImageType = in.readString();
        this.contentListinglayout = in.readString();
        this.contentPlayListType = in.readString();
        this.imageSource = in.readString();
        this.imageURL = in.readString();
        this.manualImageAssetId = in.readString();
        this.landingPageType = in.readString();
        this.landingPageAssetId = in.readString();
        this.landingPagePlayListId = in.readString();
        this.landingPagetarget = in.readString();
        this.contentIndicator = in.readString();
        this.htmlLink = in.readString();
        this.predefPlaylistType = in.readString();
        this.landingPageTitle = in.readString();
        this.message = in.readString();
        this.widgetImageType = in.readString();
        // this.contentPlayListArray = in.readParcelable(Object.class.getClassLoader());
        // this.filter = in.readParcelable(Object.class.getClassLoader());
        // this.isAnonymousUser = in.readParcelable(Object.class.getClassLoader());
        //   this.isLoggedInUser = in.readParcelable(Object.class.getClassLoader());
        this.asset = in.readParcelable(Asset.class.getClassLoader());
        this.category = in.readParcelable(BaseCategory.class.getClassLoader());
    }

    public static final Creator<VIUChannel> CREATOR = new Creator<VIUChannel>() {
        @Override
        public VIUChannel createFromParcel(Parcel source) {
            return new VIUChannel(source);
        }

        @Override
        public VIUChannel[] newArray(int size) {
            return new VIUChannel[size];
        }
    };
}