package com.astro.sott.activities.search.ui;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.search.adapter.AutoSearchAdapter;
import com.astro.sott.activities.search.adapter.KeywordListAdapter;
import com.astro.sott.activities.search.adapter.SearchNormalAdapter;
import com.astro.sott.activities.search.adapter.SearchResponseAdapter;
import com.astro.sott.activities.search.constants.SearchFilterEnum;
import com.astro.sott.activities.search.viewModel.SearchViewModel;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.commonBeanModel.MediaTypeModel;
import com.astro.sott.beanModel.commonBeanModel.SearchModel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.callBacks.commonCallBacks.DataLoadedOnFragment;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NavigationItem;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.ActivitySearchBinding;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;


public class ActivitySearch extends BaseBindingActivity<ActivitySearchBinding> implements KeywordListAdapter.KeywordItemHolderListener, SearchResponseAdapter.ShowAllItemListener, SearchNormalAdapter.SearchNormalItemListener, AutoSearchAdapter.SearchNormalItemListener, DataLoadedOnFragment {

    private final ArrayList<SearchModel> laodMoreList = new ArrayList<>();
    List<Asset> list;
    AutoSearchAdapter autoAdapter;
    List<Asset> autoList = new ArrayList<>();
    private KeywordListAdapter keywordListAdapter;
    private List<MediaTypeModel> mediaList;
    private SearchViewModel viewModel;
    private RailCommonData railCommonData;
    private int autoCompleteCounter = 0;
    private int responseCompleted = 1;
    private int counter = 0;
    private SearchResponseAdapter adapter;
    boolean searchHappen = false;
    int searchBeginFrom = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCommonMethods.resetFilter(ActivitySearch.this);
        AppCommonMethods.setProgressBar(getBinding().progressLay.progressHeart);
        modelCall();
        FirebaseEventManager.getFirebaseInstance(ActivitySearch.this).trackScreenName(FirebaseEventManager.SEARCH);
        connectionObserver();

        getBinding().searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().noResult.setVisibility(View.GONE);
                getBinding().tvPopularSearch.setVisibility(View.GONE);
                getBinding().rvSearchResult.setVisibility(View.GONE);
                getBinding().popularSearchGroup.setVisibility(View.VISIBLE);
                getBinding().recyclerView.setVisibility(View.GONE);
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().separator.setVisibility(View.GONE);
                getBinding().autoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                if (!searchHappen) {
                    if (charSequence.toString().trim().length() == 0) {
                        // getBinding().searchText.setText("");
                        getBinding().clearEdt.setVisibility(View.GONE);
                        return;
                    }
                    if (charSequence.toString().trim().length() > 2) {
                        if (responseCompleted == 1) {
                            autoCompleteCounter = 0;
                            autoList.clear();
                            hitAutoCompleteApi(charSequence.toString().trim());
                        }
                    } else {
                        autoCompleteCounter = 0;
                        setAdapter(new ArrayList<>(), null, 0);
                    }
                }

                if (charSequence.toString().trim().length() > 0) {
                    getBinding().clearEdt.setVisibility(View.VISIBLE);
                } else {
                    getBinding().clearEdt.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                viewModel.insertRecentSearchKeywords(editable.toString());
                // getBinding().searchText.setText("");
            }
        });

        getBinding().clearEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBinding().searchText.setText("");
                getBinding().clearEdt.setVisibility(View.GONE);
            }
        });

        getBinding().searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.w("focusValue 1", hasFocus + "");
                    showRecentSearchLayout(1);
                } else {
                    Log.w("focusValue 2", hasFocus + "");
                    showRecentSearchLayout(2);
                }
            }
        });


    }

    private void hitAutoCompleteApi(CharSequence charSequence) {
        if (mediaList != null) {
            if (autoCompleteCounter != mediaList.size() && autoCompleteCounter < mediaList.size()) {
                responseCompleted = 0;
                viewModel.autoCompleteHit(charSequence.toString(), mediaList, autoCompleteCounter).observe(ActivitySearch.this, searchModels -> {
                    if (searchModels != null) {
                        if (searchModels.size() > 0) {
                            if (searchModels.get(0).getAllItemsInSection().size() > 0) {
                                setAdapter(searchModels, charSequence.toString().trim(), 1);
                            }
                        }
                        autoCompleteCounter++;
                        hitAutoCompleteApi(charSequence.toString().trim());
                    }
                });
            } else {
                responseCompleted = 1;
            }
        }
    }

    private void setAdapter(ArrayList<SearchModel> list, String searchedText, int type) {
        try {
            if (searchedText == null) {
                autoList.clear();
                getBinding().autoRecyclerView.setAdapter(null);
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                getBinding().recyclerView.setVisibility(View.VISIBLE);
                resetLayout();
            } else {
                addAssets(list.get(0).getAllItemsInSection());
                autoAdapter = new AutoSearchAdapter(this, autoList, ActivitySearch.this, searchedText);
                getBinding().autoRecyclerView.setAdapter(autoAdapter);
            }
            if (autoList.size() > 0) {
                if (!searchHappen) {
                    if (getBinding().searchText.getText().toString() != null && getBinding().searchText.getText().toString().length() > 2) {
                        getBinding().autoRecyclerView.setVisibility(View.VISIBLE);
                        getBinding().separator.setVisibility(View.VISIBLE);
                    } else {

                    }
                }

            }

           /* if (autoList.size()>0){
                getBinding().autoRecyclerView.setVisibility(View.VISIBLE);
                getBinding().separator.setVisibility(View.VISIBLE);
            }else {
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().separator.setVisibility(View.GONE);
            }*/

        } catch (Exception ignored) {
            PrintLogging.printLog(this.getClass(), "", "crashValue" + ignored.getMessage());
        }

    }

    private void addAssets(List<Asset> allItemsInSection) {
        try {

            for (int i = 0; i < allItemsInSection.size(); i++) {
                autoList.add(allItemsInSection.get(i));
            }

        } catch (Exception ignored) {

        }
    }

    @Override
    public ActivitySearchBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySearchBinding.inflate(inflater);
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
    }

    List<SearchedKeywords> searchedKeywordsList;

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }

        try {
            if (UserInfo.getInstance(ActivitySearch.this).isActive()) {
                viewModel.getRecentSearchesKaltura().observe(this, searchedKeywords -> {
                    if (searchedKeywords.size() > 0) {
                        searchedKeywordsList = searchedKeywords;
                    }
                });
                viewModel.deleteAllKeywordsRecent();
            }
        } catch (Exception ignored) {

        }

      /*  getBinding().autoRecyclerView.addItemDecoration(new DividerItemDecoration(ActivitySearch.this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });*/
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            resetLayout();
            UIinitialization();
            getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
//            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
            // loadDataFromModel();
            addGenreFragment();

            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().searchText.setEnabled(true);
                    }
                }, 1000);
            } catch (Exception ignored) {

            }
        } else {

            noConnectionLayout();
        }
    }

    QuickSearchGenre detailRailFragment;

    private void addGenreFragment() {
        getBinding().searchText.setEnabled(false);
        FragmentManager fm = getSupportFragmentManager();
        detailRailFragment = new QuickSearchGenre();
        fm.beginTransaction().replace(R.id.genre_fragment, detailRailFragment).commitNow();
    }

    private void resetLayout() {
        getBinding().noResult.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().popularSearchGroup.setVisibility(View.GONE);
        getBinding().llRecentSearchLayout.setVisibility(View.GONE);
        getBinding().llSearchResultLayout.setVisibility(View.GONE);
    }

    private void UIinitialization() {
        getBinding().quickSearchLayout.setVisibility(View.GONE);
        setMediaType(1);
        setRecyclerProperties(getBinding().searchKeywordRecycler);
        setRecyclerProperties(getBinding().recyclerView);
        setRecyclerProperties(getBinding().rvSearchResult);


        setListners();

    }

    String selectedGenreValues;
    private long lastClickTime = 0;

    private void setListners() {
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());

        getBinding().backButton.setOnClickListener(view -> exitActivity());
        getBinding().filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                if (!searchHappen) {
                    FirebaseEventManager.getFirebaseInstance(ActivitySearch.this).itemListEvent(FirebaseEventManager.SEARCH, FirebaseEventManager.FITLER, FirebaseEventManager.BTN_CLICK);
                    Intent intent = new Intent(ActivitySearch.this, SearchKeywordActivity.class);
                    startActivity(intent);
                }

            }
        });

        getBinding().deleteKeywords.setOnClickListener(view -> {
            if (NetworkConnectivity.isOnline(ActivitySearch.this))
                confirmDeletion();
            else
                noConnectionLayout();
        });

        getBinding().quickSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailRailFragment != null) {
                    selectedGenreValues = detailRailFragment.getSelectedGenres();
                    if (selectedGenreValues == null || selectedGenreValues.equalsIgnoreCase("")) {
                        ToastHandler.show(getResources().getString(R.string.select_one), getApplicationContext());
                    } else {
                        if (!searchHappen) {
                            String genre = selectedGenreValues;
                            genre = genre.replace("FilterGenre=", "");
                            FirebaseEventManager.getFirebaseInstance(ActivitySearch.this).itemListEvent(FirebaseEventManager.SEARCH_BY_GENRE, genre, FirebaseEventManager.VIEW_SEARCH_RESULT);
                            counter = 0;
                            laodMoreList.clear();
                            adapter = null;
                            getBinding().filter.setVisibility(View.GONE);
                            callQuickSearch("", 1);
                        }
                    }
                }
            }
        });

        getBinding().searchText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
                    if ((getBinding().searchText.getText().toString().length() > 2)) {
                        String keyword = getBinding().searchText.getText().toString().trim();
                        if (!keyword.equalsIgnoreCase("")) {
                            if (!searchHappen) {
                                FirebaseEventManager.getFirebaseInstance(ActivitySearch.this).itemListEvent(FirebaseEventManager.SEARCH_BY_KEYWORD, keyword, FirebaseEventManager.VIEW_SEARCH_RESULT);
                                getBinding().llSearchResultLayout.setVisibility(View.GONE);
                                counter = 0;
                                laodMoreList.clear();
                                adapter = null;
                                getBinding().filter.setVisibility(View.GONE);
                                callViewModel(getBinding().searchText.getText().toString(), 1);
                            }
                        }
                    }
                } else
                    ToastHandler.show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
            }
            return false;
        });


    }

    private void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

    }

    private void setMediaType(int contentTypeFilter) {
        mediaList = new ArrayList<>();
        if (contentTypeFilter == 1) {
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_MOVIE, String.valueOf(MediaTypeConstant.getMovie(ActivitySearch.this))));
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SERIES, String.valueOf(MediaTypeConstant.getSeries(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getEpisode(ActivitySearch.this))));
            // mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_EPISODE, ""));
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_LINEAR, String.valueOf(MediaTypeConstant.getLinear(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getProgram(ActivitySearch.this))));
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_COLLECTION, String.valueOf(MediaTypeConstant.getCollection(ActivitySearch.this))));
            //mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_LINEAR, String.valueOf(MediaTypeConstant.getLinear(ActivitySearch.this))));
            //mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_SHORTFILM, String.valueOf(MediaTypeConstant.getShortFilm(ActivitySearch.this))));

        } else if (contentTypeFilter == 2) {
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_MOVIE, String.valueOf(MediaTypeConstant.getMovie(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getCollection(ActivitySearch.this))));
        } else if (contentTypeFilter == 3) {
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_LINEAR, String.valueOf(MediaTypeConstant.getLinear(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getProgram(ActivitySearch.this))));
        } else {
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_MOVIE, String.valueOf(MediaTypeConstant.getMovie(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getCollection(ActivitySearch.this))));
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SERIES, String.valueOf(MediaTypeConstant.getSeries(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getEpisode(ActivitySearch.this))));
            // mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_EPISODE, ""));
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_LINEAR, String.valueOf(MediaTypeConstant.getLinear(ActivitySearch.this)) + "," + String.valueOf(MediaTypeConstant.getProgram(ActivitySearch.this))));
            mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_COLLECTION, String.valueOf(MediaTypeConstant.getCollection(ActivitySearch.this))));
            //mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_LINEAR, String.valueOf(MediaTypeConstant.getLinear(ActivitySearch.this))));
            //mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_SHORTFILM, String.valueOf(MediaTypeConstant.getShortFilm(ActivitySearch.this))));

        }

    }

    private void exitActivity() {
        if (NetworkConnectivity.isOnline(ActivitySearch.this))
            onBackPressed();
        else {
            noConnectionLayout();
            getBinding().searchText.setText("");
        }
    }

    private void callRefreshOnDelete() {
        viewModel.getRecentSearches().observe(this, searchedKeywords -> {
            if (searchedKeywords != null && searchedKeywords.size() > 0) {
                getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                keywordListAdapter.notifyKeywordAdapter(searchedKeywords);
            } else
                getBinding().llRecentSearchLayout.setVisibility(View.GONE);
        });

    }


    private void setUiComponents(ArrayList<SearchModel> searchmodels) {
        try {
            if (adapter != null) {
                laodMoreList.add(searchmodels.get(0));
                adapter.notifyItemChanged(counter);
            } else {
                laodMoreList.add(searchmodels.get(0));
                adapter = new SearchResponseAdapter(ActivitySearch.this, laodMoreList, ActivitySearch.this);
                getBinding().rvSearchResult.setAdapter(adapter);
            }
            if (laodMoreList.size() > 0) {
                getBinding().filter.setVisibility(View.VISIBLE);
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().separator.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {

        }
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void loadDataFromModel() {
/*
        viewModel.getListLiveDataPopular().observe(this, assets -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (assets != null) {
                SearchNormalAdapter searchNormalAdapter = new SearchNormalAdapter(ActivitySearch.this, assets, ActivitySearch.this);
                getBinding().recyclerView.setAdapter(searchNormalAdapter);
                if (getResources().getBoolean(R.bool.isTablet))
                    getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                else
                    getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

                setRecentListAfterPopular();
            } else {
                setRecentListAfterPopular();
                getBinding().popularSearchGroup.setVisibility(View.GONE);
            }

        });
*/
    }

    private void setRecentListAfterPopular() {
        viewModel.getRecentSearches().observe(this, searchedKeywords -> {
            if (searchedKeywords != null && searchedKeywords.size() > 0) {
                getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                keywordListAdapter.notifyKeywordAdapter(searchedKeywords);
            }
        });

    }


    private void confirmDeletion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySearch.this, R.style.AlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.dialog));
        builder.setMessage(this.getResources().getString(R.string.delete_confirmation))
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> {
                    viewModel.deleteAllKeywordsRecent();
                    callRefreshOnDelete();
                    dialog.cancel();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        bn.setTextColor(ContextCompat.getColor(ActivitySearch.this, R.color.white));
        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        bp.setTextColor(ContextCompat.getColor(ActivitySearch.this, R.color.primary_blue));

    }

    @Override
    public void onItemClicked(SearchedKeywords itemValue) {
        if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
            if (itemValue.getKeyWords().length() > 0) {
                if (!searchHappen) {
                    FirebaseEventManager.getFirebaseInstance(ActivitySearch.this).itemListEvent(FirebaseEventManager.SEARCH_BY_HISTORY, itemValue.getKeyWords(), FirebaseEventManager.VIEW_SEARCH_RESULT);
                    getBinding().searchText.setText(itemValue.getKeyWords());
                    counter = 0;
                    laodMoreList.clear();
                    adapter = null;
                    callViewModel(itemValue.getKeyWords(), 1);
                }
            }
        } else
            ToastHandler.show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
    }

    @Override
    public void onItemClicked(SearchModel itemValue) {
        if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
            if (itemValue.getAllItemsInSection().size() > 0) {
                new ActivityLauncher(ActivitySearch.this).resultActivityBundle(ActivitySearch.this, ResultActivity.class, itemValue, getBinding().searchText.getText().toString());
            }
        } else
            ToastHandler.show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
    }


    @Override
    public void onItemClicked(Asset itemValue, int position) {
        if (getBinding().autoRecyclerView.getVisibility() == View.VISIBLE) {
            getBinding().autoRecyclerView.setVisibility(View.GONE);
            getBinding().separator.setVisibility(View.GONE);
        }

        if (itemValue != null && itemValue.getType() == MediaTypeConstant.getMovie(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).detailActivity(this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);

        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getEpisode(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).webDetailRedirection(railCommonData, AppLevelConstants.Rail5);

            //new ActivityLauncher(this).webEpisodeActivity(this, WebEpisodeDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);

        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getSeries(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).webSeriesActivity(this, WebSeriesDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getLinear(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).liveChannelActivity(this, LiveChannel.class, railCommonData);

        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getCollection(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).boxSetDetailActivity(this, railCommonData, AppConstants.Rail5);

        }

    }

    private void getRailCommonData(Asset itemValue) {
        railCommonData = new RailCommonData();
        ArrayList<AssetCommonImages> imagelist = new ArrayList<>();
        for (int i = 0; i < itemValue.getImages().size(); i++) {
            AssetCommonImages assetCommonImages = new AssetCommonImages();
            assetCommonImages.setImageUrl(itemValue.getImages().get(i).getUrl());
            imagelist.add(assetCommonImages);
        }

        ArrayList<AssetCommonUrls> videoList = new ArrayList<>();
        for (int i = 0; i < itemValue.getMediaFiles().size(); i++) {
            AssetCommonUrls assetCommonImages = new AssetCommonUrls();
            assetCommonImages.setUrl(itemValue.getMediaFiles().get(i).getUrl());
            assetCommonImages.setDuration(String.valueOf(itemValue.getMediaFiles().get(i).getDuration()));
            assetCommonImages.setUrlType(itemValue.getMediaFiles().get(i).getType());
            videoList.add(assetCommonImages);
        }


        if (itemValue.getImages().size() == imagelist.size())
            railCommonData.setUrls(videoList);
        if (itemValue.getImages().size() == imagelist.size())
            railCommonData.setImages(imagelist);
        railCommonData.setObject(itemValue);
        // railCommonData.setCatchUpBuffer(itemValue.getEnableCatchUp());
        railCommonData.setId(itemValue.getId());
        railCommonData.setType(itemValue.getType());
        railCommonData.setName(itemValue.getName());


    }


    @Override
    public void isDataLoaded(boolean isLoaded) {
        getBinding().searchText.setEnabled(true);
        if (isLoaded) {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            getBinding().quickSearchLayout.setVisibility(View.VISIBLE);
        } else {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            getBinding().quickSearchLayout.setVisibility(View.GONE);
        }

    }

    private void showRecentSearchLayout(int i) {
        if (i == 1) {
            getBinding().genreFragment.setVisibility(View.GONE);
            getBinding().filter.setVisibility(View.GONE);
            List<SearchedKeywords> mList = new ArrayList<>();
            getBinding().llRecentSearchLayout.setVisibility(View.GONE);

            keywordListAdapter = new KeywordListAdapter(getApplicationContext(), mList, ActivitySearch.this);
            getBinding().searchKeywordRecycler.setAdapter(keywordListAdapter);
            getBinding().quickSearchLayout.setVisibility(View.GONE);
            getBinding().noResult.setVisibility(View.GONE);
            if (UserInfo.getInstance(ActivitySearch.this).isActive()) {
                if (searchedKeywordsList != null && searchedKeywordsList.size() > 0) {
                    getBinding().llRecentSearchLayout.setVisibility(View.VISIBLE);
                    keywordListAdapter.notifyKeywordAdapter(searchedKeywordsList);
                }
            } else {
                setRecentSearchData();
            }

            if (laodMoreList != null && laodMoreList.size() > 0) {
                counter = 0;
                laodMoreList.clear();
                adapter = null;
                getBinding().rvSearchResult.setVisibility(View.GONE);
            }

        } else {
            getBinding().llRecentSearchLayout.setVisibility(View.GONE);
            getBinding().quickSearchLayout.setVisibility(View.VISIBLE);
            getBinding().genreFragment.setVisibility(View.VISIBLE);
        }
    }

    private void setRecentSearchData() {
        viewModel.getRecentSearches().observe(this, searchedKeywords -> {
            if (searchedKeywords != null && searchedKeywords.size() > 0) {
                getBinding().llRecentSearchLayout.setVisibility(View.VISIBLE);
                keywordListAdapter.notifyKeywordAdapter(searchedKeywords);
            }
        });

    }

    private void callViewModel(final String searchString, int from) {
        getBinding().quickSearchLayout.setVisibility(View.GONE);
        if (counter == 0) {
            getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);

//            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        }
        if (counter != mediaList.size() && counter < mediaList.size()) {
            searchHappen = true;
            viewModel.getListSearchResult(searchString, mediaList, counter, from, searchBeginFrom).observe(this, searchmodels -> {
                if (mediaList.size() > 0) {
                    if (counter == 4)
                        getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                }
                if (searchmodels.size() > 0) {
                    getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getBinding().llSearchResultLayout.setVisibility(View.VISIBLE);
                    getBinding().rvSearchResult.setVisibility(View.VISIBLE);
                    getBinding().noResult.setVisibility(View.GONE);
                    getBinding().popularSearchGroup.setVisibility(View.GONE);
                    getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                    getBinding().autoRecyclerView.setVisibility(View.GONE);
                    getBinding().separator.setVisibility(View.GONE);
                    searchBeginFrom = 1;
                    FirebaseEventManager.getFirebaseInstance(this).searchString = searchString;
                    setUiComponents(searchmodels);
                    counter++;
                    callViewModel(searchString, from);
                } else {
                    if (counter != mediaList.size()) {
                        counter++;
                        callViewModel(searchString, from);
                    } else {
                        getBinding().noResult.setVisibility(View.VISIBLE);
                        getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                        getBinding().popularSearchGroup.setVisibility(View.GONE);
                        getBinding().autoRecyclerView.setVisibility(View.GONE);
                        getBinding().recyclerView.setVisibility(View.VISIBLE);
                        loadDataFromModel();
                    }

                }
            });

        } else {
            searchHappen = false;
            if (laodMoreList != null && laodMoreList.size() == 0) {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                getBinding().noResult.setVisibility(View.VISIBLE);
                getBinding().popularSearchGroup.setVisibility(View.GONE);
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().recyclerView.setVisibility(View.VISIBLE);
                getBinding().rvSearchResult.setVisibility(View.GONE);
                loadDataFromModel();
            }
        }
    }


    private void callQuickSearch(final String searchString, int from) {
        getBinding().genreFragment.setVisibility(View.GONE);
        getBinding().quickSearchLayout.setVisibility(View.GONE);
        if (counter == 0) {
            getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);

//            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        }
        if (counter != mediaList.size() && counter < mediaList.size()) {
            searchHappen = true;
            // getBinding().toolbar.progressBar.setVisibility(View.VISIBLE);
            viewModel.getQuickSearchResult(searchString, mediaList, counter, selectedGenreValues, from, searchBeginFrom).observe(this, searchmodels -> {
                if (mediaList.size() > 0) {
                    if (counter == 4)
                        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);

//                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                }
                if (searchmodels.size() > 0) {
                    getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getBinding().llSearchResultLayout.setVisibility(View.VISIBLE);
                    getBinding().rvSearchResult.setVisibility(View.VISIBLE);
                    getBinding().noResult.setVisibility(View.GONE);
                    getBinding().popularSearchGroup.setVisibility(View.GONE);
                    getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                    getBinding().autoRecyclerView.setVisibility(View.GONE);
                    getBinding().separator.setVisibility(View.GONE);
                    searchBeginFrom = 2;
                    FirebaseEventManager.getFirebaseInstance(this).searchString = searchString;
                    setUiComponents(searchmodels);
                    counter++;
                    callQuickSearch(searchString, from);
                } else {
                    if (counter != mediaList.size()) {
                        counter++;
                        callQuickSearch(searchString, from);
                    } else {
                        getBinding().noResult.setVisibility(View.VISIBLE);
                        getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                        getBinding().popularSearchGroup.setVisibility(View.GONE);
                        getBinding().autoRecyclerView.setVisibility(View.GONE);
                        getBinding().recyclerView.setVisibility(View.VISIBLE);
                        loadDataFromModel();
                    }

                }
            });

        } else {
            searchHappen = false;
            if (laodMoreList != null && laodMoreList.size() == 0) {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);

//                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                getBinding().noResult.setVisibility(View.VISIBLE);
                getBinding().popularSearchGroup.setVisibility(View.GONE);
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().recyclerView.setVisibility(View.VISIBLE);
                getBinding().rvSearchResult.setVisibility(View.GONE);
                loadDataFromModel();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!KsPreferenceKey.getInstance(ActivitySearch.this).getFilterApply().equalsIgnoreCase("")) {
            if (KsPreferenceKey.getInstance(ActivitySearch.this).getFilterApply().equalsIgnoreCase("true")) {
                KsPreferenceKey.getInstance(ActivitySearch.this).setFilterApply("false");
                counter = 0;
                laodMoreList.clear();
                adapter = null;
                if (KsPreferenceKey.getInstance(ActivitySearch.this).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.ALL.name())) {
                    setMediaType(1);
                } else if (KsPreferenceKey.getInstance(ActivitySearch.this).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.ONDEMAND.name())) {
                    setMediaType(2);
                } else if (KsPreferenceKey.getInstance(ActivitySearch.this).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.LIVE.name())) {
                    setMediaType(3);
                } else {
                    setMediaType(4);
                }
                if (searchBeginFrom == 1) {
                    callViewModel(getBinding().searchText.getText().toString(), 1);
                } else {
                    selectedGenreValues = "";
                    callQuickSearch("", 2);
                }

            }

        }
    }
}


