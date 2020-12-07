package com.dialog.dialoggo.activities.search.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.commonBeanModel.MediaTypeModel;
import com.dialog.dialoggo.beanModel.commonBeanModel.SearchModel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.db.search.SearchedKeywords;
import com.dialog.dialoggo.repositories.search.SearchRepository;
import com.dialog.dialoggo.repositories.splash.SplashRepository;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;


public class SearchViewModel extends AndroidViewModel {

    private final SearchRepository repository;


    public SearchViewModel(@NonNull Application context) {
        super(context);
        repository = SearchRepository.getInstance();

    }

    public LiveData<ArrayList<SearchModel>> getListSearchResult(String searchString, List<MediaTypeModel> mediaList,int counter) {
        return repository.matchSetHitApi(searchString, getApplication().getApplicationContext(), mediaList,counter);
    }
    public LiveData<ArrayList<SearchModel>> autoCompleteHit(String searchString, List<MediaTypeModel> mediaList,int autoCompleteCounter) {
        return repository.autoCompleteHit(searchString, getApplication().getApplicationContext(), mediaList,autoCompleteCounter);
    }

    public LiveData<List<Asset>> getListLiveDataPopular() {
        return repository.hitApiPopularSearch(getApplication().getApplicationContext());
    }

    public LiveData<List<SearchedKeywords>> getRecentSearches() {
        return repository.recentSearch(getApplication().getApplicationContext());
    }

    public LiveData<List<SearchedKeywords>> getListNew() {
        return repository.getRecentListDetail(getApplication().getApplicationContext());
    }

    public LiveData<RailCommonData> getSpecificAsset(String assetId) {
        return new SplashRepository().getSpecificAsset(getApplication().getApplicationContext(), assetId);
    }

    public void deleteAllKeywordsRecent() {
        repository.deleteAllKeywords(getApplication().getApplicationContext());
    }
    public void insertRecentSearchKeywords(String text){
        repository.matchingKeyword(text);
    }

}
