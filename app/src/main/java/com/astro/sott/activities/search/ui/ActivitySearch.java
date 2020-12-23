package com.astro.sott.activities.search.ui;

import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.search.adapter.AutoSearchAdapter;
import com.astro.sott.activities.search.adapter.KeywordListAdapter;
import com.astro.sott.activities.search.adapter.SearchNormalAdapter;
import com.astro.sott.activities.search.adapter.SearchResponseAdapter;
import com.astro.sott.activities.search.viewModel.SearchViewModel;
import com.astro.sott.activities.webEpisodeDescription.ui.WebEpisodeDescriptionActivity;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.commonBeanModel.MediaTypeModel;
import com.astro.sott.beanModel.commonBeanModel.SearchModel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.ActivitySearchBinding;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;


public class ActivitySearch extends BaseBindingActivity<ActivitySearchBinding> implements KeywordListAdapter.KeywordItemHolderListener, SearchResponseAdapter.ShowAllItemListener, SearchNormalAdapter.SearchNormalItemListener, AutoSearchAdapter.SearchNormalItemListener {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        connectionObserver();

        getBinding().toolbar.searchText.addTextChangedListener(new TextWatcher() {
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
                getBinding().autoRecyclerView.setVisibility(View.VISIBLE);
                getBinding().autoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                if (charSequence.length() > 2) {
                    if (responseCompleted == 1) {
                        autoCompleteCounter = 0;
                        autoList.clear();
                        hitAutoCompleteApi(charSequence);
                    }
                } else {
                    autoCompleteCounter = 0;
                    setAdapter(new ArrayList<>(), null, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                viewModel.insertRecentSearchKeywords(editable.toString());
            }
        });

    }

    private void hitAutoCompleteApi(CharSequence charSequence) {
        if (mediaList!=null) {
            if (autoCompleteCounter != mediaList.size() && autoCompleteCounter < mediaList.size()) {
                responseCompleted = 0;
                viewModel.autoCompleteHit(charSequence.toString(), mediaList, autoCompleteCounter).observe(ActivitySearch.this, searchModels -> {
                    if (searchModels != null) {
                        if (searchModels.size() > 0) {
                            if (searchModels.get(0).getAllItemsInSection().size() > 0) {
                                setAdapter(searchModels, charSequence.toString(), 1);
                            }
                        }
                        autoCompleteCounter++;
                        hitAutoCompleteApi(charSequence);
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

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            modelCall();
            resetLayout();
            UIinitialization();
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
            loadDataFromModel();
        } else {

            noConnectionLayout();
        }
    }

    private void resetLayout() {
        getBinding().noResult.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().popularSearchGroup.setVisibility(View.VISIBLE);
        getBinding().llRecentSearchLayout.setVisibility(View.VISIBLE);
        getBinding().llSearchResultLayout.setVisibility(View.GONE);
    }

    private void UIinitialization() {
        setMediaType();
        setRecyclerProperties(getBinding().searchKeywordRecycler);
        setRecyclerProperties(getBinding().recyclerView);
        setRecyclerProperties(getBinding().rvSearchResult);

        List<SearchedKeywords> mList = new ArrayList<>();
        getBinding().llRecentSearchLayout.setVisibility(View.GONE);

        keywordListAdapter = new KeywordListAdapter(getApplicationContext(), mList, ActivitySearch.this);
        getBinding().searchKeywordRecycler.setAdapter(keywordListAdapter);

        setListners();

    }

    private void setListners() {
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());

        getBinding().toolbar.backButton.setOnClickListener(view -> exitActivity());

        getBinding().deleteKeywords.setOnClickListener(view -> {
            if (NetworkConnectivity.isOnline(ActivitySearch.this))
                confirmDeletion();
            else
                noConnectionLayout();
        });

        getBinding().toolbar.searchText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
                    if ((getBinding().toolbar.searchText.getText().toString().length() > 0)) {
                        String keyword = getBinding().toolbar.searchText.getText().toString().trim();
                        if (!keyword.equalsIgnoreCase("")) {
                            getBinding().llSearchResultLayout.setVisibility(View.GONE);
                            counter = 0;
                            laodMoreList.clear();
                            adapter = null;
                            callViewModel(getBinding().toolbar.searchText.getText().toString());
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

    private void setMediaType() {
        mediaList = new ArrayList<>();
        mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_MOVIE, String.valueOf(MediaTypeConstant.getMovie(ActivitySearch.this))));
        mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SERIES, String.valueOf(MediaTypeConstant.getSeries(ActivitySearch.this))));
        mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_EPISODE, String.valueOf(MediaTypeConstant.getEpisode(ActivitySearch.this))));
        //mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_LINEAR, String.valueOf(MediaTypeConstant.getLinear(ActivitySearch.this))));
        //mediaList.add(new MediaTypeModel(AppLevelConstants.MEDIATYPE_SEARCH_SHORTFILM, String.valueOf(MediaTypeConstant.getShortFilm(ActivitySearch.this))));

    }

    private void exitActivity() {
        if (NetworkConnectivity.isOnline(ActivitySearch.this))
            onBackPressed();
        else {
            noConnectionLayout();
            getBinding().toolbar.searchText.setText("");
        }
    }

    private void callRefreshOnDelete() {
        viewModel.getRecentSearches().observe(this, searchedKeywords -> {
            if (searchedKeywords != null && searchedKeywords.size() > 0) {
                getBinding().llRecentSearchLayout.setVisibility(View.VISIBLE);
                keywordListAdapter.notifyKeywordAdapter(searchedKeywords);
            } else
                getBinding().llRecentSearchLayout.setVisibility(View.GONE);
        });

    }

    private void callViewModel(final String searchString) {
        if (counter == 0) {
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        }
        if (counter != mediaList.size() && counter < mediaList.size()) {
            viewModel.getListSearchResult(searchString, mediaList, counter).observe(this, searchmodels -> {
                if (mediaList.size() > 0) {
                    if (counter == 4)
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                }
                if (searchmodels.size() > 0) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getBinding().llSearchResultLayout.setVisibility(View.VISIBLE);
                    getBinding().rvSearchResult.setVisibility(View.VISIBLE);
                    getBinding().noResult.setVisibility(View.GONE);
                    getBinding().popularSearchGroup.setVisibility(View.GONE);
                    getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                    setUiComponents(searchmodels);
                    counter++;
                    callViewModel(searchString);
                } else {
                    if (counter != mediaList.size()) {
                        counter++;
                        callViewModel(searchString);
                    } else {
                        getBinding().noResult.setVisibility(View.VISIBLE);
                        getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                        getBinding().popularSearchGroup.setVisibility(View.VISIBLE);
                        getBinding().autoRecyclerView.setVisibility(View.GONE);
                        getBinding().recyclerView.setVisibility(View.VISIBLE);
                        loadDataFromModel();
                    }

                }
            });

        } else {
            if (laodMoreList != null && laodMoreList.size() == 0) {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
                getBinding().noResult.setVisibility(View.VISIBLE);
                getBinding().popularSearchGroup.setVisibility(View.VISIBLE);
                getBinding().autoRecyclerView.setVisibility(View.GONE);
                getBinding().recyclerView.setVisibility(View.VISIBLE);
                loadDataFromModel();
            }
        }
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
        } catch (Exception ignored) {

        }
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void loadDataFromModel() {
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
    }

    private void setRecentListAfterPopular() {
        viewModel.getRecentSearches().observe(this, searchedKeywords -> {
            if (searchedKeywords != null && searchedKeywords.size() > 0) {
                getBinding().llRecentSearchLayout.setVisibility(View.VISIBLE);
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
                getBinding().toolbar.searchText.setText(itemValue.getKeyWords());
                counter = 0;
                callViewModel(itemValue.getKeyWords());
            }
        } else
            ToastHandler.show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
    }

    @Override
    public void onItemClicked(SearchModel itemValue) {
        if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
            if (itemValue.getAllItemsInSection().size() > 0) {
                new ActivityLauncher(ActivitySearch.this).resultActivityBundle(ActivitySearch.this, ResultActivity.class, itemValue, getBinding().toolbar.searchText.getText().toString());
            }
        } else
            ToastHandler.show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
    }


    @Override
    public void onItemClicked(Asset itemValue, int position) {
        if (itemValue != null && itemValue.getType() == MediaTypeConstant.getMovie(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).detailActivity(this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);

        }  else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getEpisode(ActivitySearch.this)) {
            getRailCommonData(itemValue);

            new ActivityLauncher(this).webEpisodeActivity(this, WebEpisodeDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);

        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getSeries(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).webSeriesActivity(this, WebSeriesDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getLinear(ActivitySearch.this)) {
            getRailCommonData(itemValue);
            new ActivityLauncher(this).liveChannelActivity(this, LiveChannel.class, railCommonData);

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


}


