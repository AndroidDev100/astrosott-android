package com.dialog.dialoggo.utils.ksPreferenceKey;

import android.content.Context;

import com.dialog.dialoggo.utils.constants.AppConstants;
import com.dialog.dialoggo.utils.helpers.SharedPrefHelper;

public class SubCategoriesPrefs {


    private SharedPrefHelper session;
    private static SubCategoriesPrefs mInstance;

    private static final String USER = "User";
    private static final String DEFAULT_ENTITLEMENT = "DefaultEntitlement";

    public static SubCategoriesPrefs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SubCategoriesPrefs(context);
        }
        return mInstance;
    }

    private SubCategoriesPrefs(Context context) {
        if (session == null)
            session = SharedPrefHelper.getInstance(context);
    }

    public void setHomeTabId(int homeTabId) {
        session.setInt(AppConstants.TAB_HOME_KEY, homeTabId);
    }

    public int getHomeTabId() {
        return session.getInt(AppConstants.TAB_HOME_KEY, AppConstants.HOME_TAB_ID);
    }

    public void setLiveTabId(int liveTabId) {
        session.setInt(AppConstants.TAB_LIVE_TV_KEY, liveTabId);
    }

    public int getLiveTabId() {
        return session.getInt(AppConstants.TAB_LIVE_TV_KEY, AppConstants.LIVE_TAB_ID);
    }

    public void setVideoTabId(int videoTabId) {
        session.setInt(AppConstants.TAB_VIDEO_KEY, videoTabId);
    }

    public int getVideoTabId() {
        return session.getInt(AppConstants.TAB_VIDEO_KEY, AppConstants.VIDEO_TAB_ID);
    }

    public void setMovieDetailsId(int movieDetailsId) {
        session.setInt(AppConstants.TAB_MOVIE_DETAILS_KEY, movieDetailsId);
    }

    public int getMovieDetailsId() {
        return session.getInt(AppConstants.TAB_MOVIE_DETAILS_KEY, AppConstants.MOVIE_DETAIL_ID);
    }
    public void setShortVideoDetailsId(int shortVideoDetailsId) {
        session.setInt(AppConstants.TAB_SHORT_FILM_DETAILS_KEY, shortVideoDetailsId);
    }

    public int getShortVideoDetailsId() {
        return session.getInt(AppConstants.TAB_SHORT_FILM_DETAILS_KEY, AppConstants.SHORT_FILMDETAIL_ID);
    }
    public void setDramaDetailsId(int shortVideoDetailsId) {
        session.setInt(AppConstants.TAB_DRAMA_DETAILS_KEY, shortVideoDetailsId);
    }

    public int getDramaDetailsId() {
        return session.getInt(AppConstants.TAB_DRAMA_DETAILS_KEY , AppConstants.DRAMA_DETAILS_ID);
    }
    public void setDramaEpisodeDetailsId(int dramaEpisodeDetailsId) {
        session.setInt(AppConstants.TAB_DRAMA_EPISODE_KEY, dramaEpisodeDetailsId);
    }

    public int getDramaEpisodeDetailsId() {
        return session.getInt(AppConstants.TAB_DRAMA_EPISODE_KEY, AppConstants.DRAMA_EPISODE_DETAILS_ID);
    }
    public void setPopularSearchId(int searchId) {
        session.setInt(AppConstants.TAB_POPULAR_SEARCH_KEY, searchId);
    }

    public int getPopularSearchId() {
        return session.getInt(AppConstants.TAB_POPULAR_SEARCH_KEY, AppConstants.POPULAR_SEARCH_CHANNEL_ID);
    }

    public void setLiveTvDetailsId(int liveTvDetailsId) {
        session.setInt(AppConstants.TAB_LIVE_TV_DETAILS_KEY, liveTvDetailsId);
    }

    public int getLiveTvDetailsId() {
        return session.getInt(AppConstants.TAB_LIVE_TV_DETAILS_KEY, AppConstants.LIVETV_DETAIL_ID);
    }

    public void setForwardEpgId(int forwardEpgId) {
        session.setInt(AppConstants.TAB_FORWARDED_EPG_DETAIL_KEY, forwardEpgId);
    }

    public int getForwardEpgId() {
        return session.getInt(AppConstants.TAB_FORWARDED_EPG_DETAIL_KEY, AppConstants.TAB_FORWARDED_EPG_DETAIL);
    }
}
