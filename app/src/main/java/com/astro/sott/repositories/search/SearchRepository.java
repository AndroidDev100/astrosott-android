package com.astro.sott.repositories.search;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.util.Log;

import com.astro.sott.beanModel.commonBeanModel.MediaTypeModel;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.beanModel.commonBeanModel.SearchModel;
import com.astro.sott.callBacks.kalturaCallBacks.DMSCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SearchResultCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class SearchRepository {

    private static SearchRepository instance;
    private Realm realm;
    private ArrayList<SearchedKeywords> searchedKeywords;

    private SearchRepository() {
    }

    public static SearchRepository getInstance() {
        if (instance == null) {
            instance = new SearchRepository();
        }
        return (instance);
    }


    private void initRealm(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }


    public void deleteAllKeywords(Context context) {
        initRealm(context);
        realm.beginTransaction();
        RealmResults<SearchedKeywords> results = realm.where(SearchedKeywords.class).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }


    private boolean insertString(String value) {
        boolean added = false;
        realm.beginTransaction();
        RealmResults<SearchedKeywords> results = realm.where(SearchedKeywords.class).findAll();
        searchedKeywords = new ArrayList<>();
//        for (SearchedKeywords student : results) {
//            searchedKeywords.add(student);
//        }
        searchedKeywords.addAll(results);
        //check if string already in list
        if (!compareListData(searchedKeywords, value)) {
            if (searchedKeywords.size() == 5) {
                deleteOldString();
                SearchedKeywords keyword = realm.createObject(SearchedKeywords.class);
                keyword.setKeyWords(value);
                keyword.setTimeStamp(System.currentTimeMillis());
                added = true;
            } else {
                SearchedKeywords keyword = realm.createObject(SearchedKeywords.class);
                keyword.setKeyWords(value);
                keyword.setTimeStamp(System.currentTimeMillis());
                added = true;
            }
        }

        return added;

    }

    private boolean compareListData(ArrayList<SearchedKeywords> list, String value) {
        boolean matchFound = false;
        for (int j = 0; j < list.size(); j++) {
            if (value.equals(list.get(j).getKeyWords())) {
                matchFound = true;
            }
        }
        return matchFound;
    }


    public void matchingKeyword(String text) {
        searchedKeywords = new ArrayList<>();
        if (insertString(text)) {
            PrintLogging.printLog(this.getClass(), "", "");
        }
        realm.commitTransaction();
    }

    private void deleteOldString() {
        if (searchedKeywords.size() == 5) {
            RealmResults<SearchedKeywords> results = realm.where(SearchedKeywords.class).findAll();
            int deletePos = 0;
            for (int i = 0; i < results.size(); i++) {
                if (results.get(deletePos) != null && results.get(i) != null) {
                    if (Objects.requireNonNull(results.get(deletePos)).getTimeStamp() != null && Objects.requireNonNull(results.get(i)).getTimeStamp() != null && Objects.requireNonNull(results.get(deletePos)).getTimeStamp() > Objects.requireNonNull(results.get(i)).getTimeStamp())
                        deletePos = i;
                }
            }
            SearchedKeywords key = results.get(deletePos);
            if (key != null)
                key.deleteFromRealm();
        }
    }


    public LiveData<List<SearchedKeywords>> getRecentListDetail(Context context) {
        initRealm(context);
        return recentSearch(context);

    }

    public LiveData<List<SearchedKeywords>> recentSearch(Context context) {
        initRealm(context);
        ArrayList<SearchedKeywords> realmSearchedList = new ArrayList<>();
        MutableLiveData<List<SearchedKeywords>> listMutableRecent = new MutableLiveData<>();
        realm.beginTransaction();
        RealmResults<SearchedKeywords> realmResults = realm.where(SearchedKeywords.class).findAll();

        List<SearchedKeywords> list = new ArrayList<>(realmResults);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SearchedKeywords item = new SearchedKeywords();
                item.setTimeStamp(list.get(i).getTimeStamp());
                item.setKeyWords(list.get(i).getKeyWords());
                realmSearchedList.add(item);
            }
        }
        Collections.reverse(realmSearchedList);
        realm.commitTransaction();
        listMutableRecent.setValue(realmSearchedList);
        return listMutableRecent;
    }


    public LiveData<List<Asset>> hitApiPopularSearch(final Context context) {
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<Asset>> connection = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.popularSearch(context, (status1, result) -> {
                    if (status1) {
                        try {
                            if (result.results != null) {
                                if (result.results.getObjects() != null) {
                                    if (result.results.getObjects().size() > 0) {
                                        connection.postValue(result.results.getObjects());
                                    } else {
                                        connection.postValue(null);
                                    }
                                } else {
                                    connection.postValue(null);
                                }
                            } else {
                                connection.postValue(null);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        connection.postValue(null);

                    }
                });
            }

        });
        return connection;

    }


    public LiveData<ArrayList<SearchModel>> matchSetHitApi(String searchString, final Context context, final List<MediaTypeModel> list, int counter, int from, int beginFrom) {
        initRealm(context);
        matchingKeyword(searchString);
        //Log.w("valuesFromList",list.get(counter).getId()+"");
        final MutableLiveData<ArrayList<SearchModel>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        callNonCollectiondata(searchString, context, list, counter, ksServices, connection, "", 1);
       /* if (counter==0){
            callNonCollectiondata(searchString,context,list,counter,ksServices,connection,"",1);

        }else if (counter==3){
           // callNonCollectiondata(searchString,context,list,counter,ksServices,connection);
            callCollectiondata(searchString,context,list,counter,ksServices,connection,"",1);
        }else {
            callNonCollectiondata(searchString,context,list,counter,ksServices,connection,"",1);
        }*/
      /*  if (list.get(counter).getId().contains(",")){
            if (counter==0){
                callMovieCollectiondata(searchString,context,list,counter,ksServices,connection);
            }else {
                callNonCollectiondata(searchString,context,list,counter,ksServices,connection);
            }

        }else {
            if (list.get(counter).getId().equalsIgnoreCase(String.valueOf(MediaTypeConstant.getCollection(context)))){
                callCollectiondata(searchString,context,list,counter,ksServices,connection);
            }else {
                callNonCollectiondata(searchString,context,list,counter,ksServices,connection);
            }
        }
*/
        return connection;
    }

    private void callMovieCollectiondata(String searchString, Context context, List<MediaTypeModel> list, int counter, KsServices ksServices, MutableLiveData<ArrayList<SearchModel>> connection) {
        Log.w("valuesFromList M", list.get(counter).getId() + "");
        String tag3 = "name ~ '";
        String tag2 = "'";

        final String modifyString =
                tag3 +
                        searchString +
                        tag2;


        AppCommonMethods.checkDMS(context, new DMSCallBack() {
            @Override
            public void configuration(boolean status) {
                if (status) {
                    ksServices.searchMovieKeyword(context, modifyString, list, counter, (status1, result, remarks) -> {
                        if (status1) {

                            if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND)) {
                                connection.postValue(new ArrayList<>());
                            } else {
                                callVodCollection(searchString, context, list, counter, ksServices, connection, result);
                                //  connection.postValue(sortMediaTypeList(context, result,counter));
                            }
                        }
                    });
                }

            }
        });

    }

    private void callVodCollection(String searchString, Context context, List<MediaTypeModel> list, int counter, KsServices ksServices, MutableLiveData<ArrayList<SearchModel>> connection, ArrayList<SearchModel> results) {
        Log.w("valuesFromList M", list.get(counter).getId() + "");
        String tag3 = "";
        String tag2 = "(and IsSponsored='0')";

        final String modifyString =
                tag3 +
                        tag2;


        AppCommonMethods.checkDMS(context, new DMSCallBack() {
            @Override
            public void configuration(boolean status) {
                if (status) {
                    ksServices.searchVodCollectionKeyword(context, modifyString, list, counter, (status1, result, remarks) -> {
                        if (status1) {

                            if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND)) {
                                connection.postValue(results);
                            } else {
                                List<Asset> list = results.get(0).getAllItemsInSection();
                                List<Asset> list2 = result.get(0).getAllItemsInSection();

                                list.addAll(list2);

                                results.get(0).setAllItemsInSection(list);

                                connection.postValue(sortMediaTypeList(context, results, counter));
                            }

                        } else {
                            connection.postValue(results);
                        }
                    }, searchString, "");
                }

            }
        });

    }

    private void callCollectiondata(String searchString, Context context, List<MediaTypeModel> list, int counter, KsServices ksServices, MutableLiveData<ArrayList<SearchModel>> connection, String selectedGenre, int from) {
        String searchKsql = AppCommonMethods.getSearchFieldsKsql(searchString, selectedGenre, from, context);
        String tag3 = "(and name ~ '";
        String tag2 = "' (and IsSponsored='1'))";

        final String modifyString =
                tag3 +
                        searchString +
                        tag2;


        AppCommonMethods.checkDMS(context, new DMSCallBack() {
            @Override
            public void configuration(boolean status) {
                if (status) {
                    ksServices.searchVodCollectionKeyword(context, searchKsql, list, counter, (status1, result, remarks) -> {
                        if (status1) {

                            if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND)) {
                                connection.postValue(new ArrayList<>());
                            } else
                                connection.postValue(sortMediaTypeList(context, result, counter));

                        }
                    }, searchString, selectedGenre);
                }

            }
        });

    }

    String searchKsql = "";

    private void callNonCollectiondata(String searchString, Context context, List<MediaTypeModel> list, int counter, KsServices ksServices, MutableLiveData<ArrayList<SearchModel>> connection, String selectedGenre, int from) {
        Log.w("valuesFromList N", list.get(counter).getId() + "");
        searchKsql = AppCommonMethods.getVODSearchKsql(searchString, selectedGenre, from, context);
        if (list.get(counter).getId().contains(",")) {
            String[] mediaTypes = list.get(counter).getId().split(",");
            String one = mediaTypes[0];
            String two = mediaTypes[1];
            if (one.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getLinear(context))) && two.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getProgram(context)))) {
                searchKsql = AppCommonMethods.getLiveSearchKsql(searchString, selectedGenre, from, context);
            } else {
                searchKsql = AppCommonMethods.getVODSearchKsql(searchString, selectedGenre, from, context);
            }
        } else {
            searchKsql = AppCommonMethods.getPagesSearchKsql(searchString, selectedGenre, from, context);
        }

        Log.w("SearchKsql-->>", searchKsql);
        String tag3 = "name ~ '";
        String tag2 = "'";

        final String modifyString =
                tag3 +
                        searchString +
                        tag2;


        AppCommonMethods.checkDMS(context, new DMSCallBack() {
            @Override
            public void configuration(boolean status) {
                if (status) {
                    ksServices.searchKeyword(context, searchKsql, list, counter, (status1, result, remarks) -> {
                        try {
                            if (status1) {

                                if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND)) {
                                    connection.postValue(new ArrayList<>());
                                } else {
                                    if (list.get(counter).getId().contains(",")) {
                                        connection.postValue(sortMediaTypeList(context, result, counter));
                                    } else {
                                        if (result.get(0).getAllItemsInSection() != null && result.get(0).getAllItemsInSection().size() > 0) {
                                            Asset asset = result.get(0).getAllItemsInSection().get(0);

                                            connection.postValue(sortMediaTypeList2(context, result, counter));

                                        } else {
                                            connection.postValue(new ArrayList<>());
                                        }

                                    }
                                }
                            }

                        } catch (Exception e) {
                            connection.postValue(new ArrayList<>());
                        }
                    }, searchString, selectedGenre);
                }

            }
        });

    }

    public LiveData<ArrayList<SearchModel>> autoCompleteHit(String searchString, final Context context, final List<MediaTypeModel> list, int autoCompleteCounter) {
        initRealm(context);

        String tag3 = "name * '";
        //below tags are future to be used string tags
        String tag2 = "'";

        // "(or description ~ 'big' name ~ 'big')"
        // below string will be used to search
        final String modifyString =
                tag3 +
                        searchString +
                        tag2;

        final MutableLiveData<ArrayList<SearchModel>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context, new DMSCallBack() {
            @Override
            public void configuration(boolean status) {
                if (status) {
                    ksServices.searchKeywordAuto(autoCompleteCounter, context, modifyString, list, new SearchResultCallBack() {
                        @Override
                        public void response(boolean status, ArrayList<SearchModel> result, String remarks) {
                            if (status) {
                                try {
                                    if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND)) {
                                        PrintLogging.printLog(this.getClass(), "", "omlens");

                                        connection.postValue(new ArrayList<>());
                                    } else
                                        connection.postValue(result);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, searchString, "");
                }

            }
        });


        return connection;
    }

    private ArrayList<SearchModel> sortMediaTypeList2(Context context, ArrayList<SearchModel> list, int counter) {
        ArrayList<SearchModel> allSampleData = new ArrayList<>();

        //please do not break sequence
        SearchModel Movie = new SearchModel();
        SearchModel Series = new SearchModel();
        SearchModel Episode = new SearchModel();
        SearchModel Collection = new SearchModel();
        SearchModel Linear = new SearchModel();
        SearchModel Program = new SearchModel();

        if (list.get(0).getAllItemsInSection() != null && list.get(0).getAllItemsInSection().size() > 0) {
            Asset asset = list.get(0).getAllItemsInSection().get(0);
            if (asset.getType() == MediaTypeConstant.getMovie(context)) {
                Movie = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getCollection(context)) {
                /*   if (AppCommonMethods.isPages(context, asset)) {*/
                Collection = list.get(0);
               /* } else {
                    Collection = list.get(0);
                }*/

            } else if (asset.getType() == MediaTypeConstant.getSeries(context)) {
                Episode = list.get(0);
                Series = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getEpisode(context)) {
                Episode = list.get(0);
                Series = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getLinear(context)) {
                Linear = list.get(0);
                Program = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getProgram(context)) {
                Linear = list.get(0);
                Program = list.get(0);
            }
        }

        /*if (counter==0){
            Movie = list.get(0);
        }else if (counter==1){
            Episode = list.get(0);
            Series = list.get(0);
        }else if (counter==2){
            Linear = list.get(0);
            Program = list.get(0);
        }
        else if (counter==3){
            Collection=list.get(0);
        }*/


        Log.w("valuesFromList S", list.size() + "");
/*
        for (int i = 0; i < list.size(); i++) {

            String checkId = String.valueOf(list.get(i).getType());
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + checkId);

            if (checkId.equals(String.valueOf(MediaTypeConstant.getMovie(context)))) {
                Movie = list.get(i);

            } else if (checkId.equals(String.valueOf(MediaTypeConstant.getEpisode(context)))) {
                Episode = list.get(i);

            } else if (checkId.equals(String.valueOf(MediaTypeConstant.getSeries(context)))) {
                Series = list.get(i);

            }
            else if (checkId.equals(String.valueOf(MediaTypeConstant.getCollection(context)))) {
                Collection = list.get(i);

            }
            else if (checkId.equals(String.valueOf(MediaTypeConstant.getLinear(context)))) {
                Linear = list.get(i);

            }
        }
*/
        if (Movie.getAllItemsInSection() != null && Movie.getAllItemsInSection().size() > 0) {
            Movie.setHeaderTitle(SearchModel.SEARCH_VOD);
            allSampleData.add(Movie);
        }

        if (Series.getAllItemsInSection() != null && Series.getAllItemsInSection().size() > 0) {
            Series.setHeaderTitle(SearchModel.SEARCH_TV_SHOWS);
            allSampleData.add(Series);
        }


        if (Episode.getAllItemsInSection() != null && Episode.getAllItemsInSection().size() > 0) {
            Episode.setHeaderTitle(SearchModel.SEARCH_TV_SHOWS);
            allSampleData.add(Episode);
        }

        if (Collection.getAllItemsInSection() != null && Collection.getAllItemsInSection().size() > 0) {
            Collection.setHeaderTitle(SearchModel.SEARCH_PAGE);
            allSampleData.add(Collection);
        }

        if (Linear.getAllItemsInSection() != null && Linear.getAllItemsInSection().size() > 0) {
            Linear.setHeaderTitle(SearchModel.SEARCH_LIVE);
            allSampleData.add(Linear);
        }

        if (Program.getAllItemsInSection() != null && Program.getAllItemsInSection().size() > 0) {
            Linear.setHeaderTitle(SearchModel.SEARCH_LIVE);
            allSampleData.add(Linear);
        }


        return allSampleData;
    }


    private ArrayList<SearchModel> sortMediaTypeList(Context context, ArrayList<SearchModel> list, int counter) {
        ArrayList<SearchModel> allSampleData = new ArrayList<>();

        //please do not break sequence
        SearchModel Movie = new SearchModel();
        SearchModel Series = new SearchModel();
        SearchModel Episode = new SearchModel();
        SearchModel Collection = new SearchModel();
        SearchModel Linear = new SearchModel();
        SearchModel Program = new SearchModel();

        if (list.get(0).getAllItemsInSection() != null && list.get(0).getAllItemsInSection().size() > 0) {
            Asset asset = list.get(0).getAllItemsInSection().get(0);
            if (asset.getType() == MediaTypeConstant.getMovie(context)) {
                Movie = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getCollection(context)) {
                if (AppCommonMethods.isPages(context, asset)) {
                    Collection = list.get(0);
                } else {
                    Movie = list.get(0);
                }

            } else if (asset.getType() == MediaTypeConstant.getSeries(context)) {
                Episode = list.get(0);
                Series = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getEpisode(context)) {
                Episode = list.get(0);
                Series = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getLinear(context)) {
                Linear = list.get(0);
                Program = list.get(0);
            } else if (asset.getType() == MediaTypeConstant.getProgram(context)) {
                Linear = list.get(0);
                Program = list.get(0);
            }
        }

        /*if (counter==0){
            Movie = list.get(0);
        }else if (counter==1){
            Episode = list.get(0);
            Series = list.get(0);
        }else if (counter==2){
            Linear = list.get(0);
            Program = list.get(0);
        }
        else if (counter==3){
            Collection=list.get(0);
        }*/


        Log.w("valuesFromList S", list.size() + "");
/*
        for (int i = 0; i < list.size(); i++) {

            String checkId = String.valueOf(list.get(i).getType());
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + checkId);

            if (checkId.equals(String.valueOf(MediaTypeConstant.getMovie(context)))) {
                Movie = list.get(i);

            } else if (checkId.equals(String.valueOf(MediaTypeConstant.getEpisode(context)))) {
                Episode = list.get(i);

            } else if (checkId.equals(String.valueOf(MediaTypeConstant.getSeries(context)))) {
                Series = list.get(i);

            }
            else if (checkId.equals(String.valueOf(MediaTypeConstant.getCollection(context)))) {
                Collection = list.get(i);

            }
            else if (checkId.equals(String.valueOf(MediaTypeConstant.getLinear(context)))) {
                Linear = list.get(i);

            }
        }
*/
        if (Movie.getAllItemsInSection() != null && Movie.getAllItemsInSection().size() > 0) {
            Movie.setHeaderTitle(SearchModel.SEARCH_VOD);
            allSampleData.add(Movie);
        }

        if (Series.getAllItemsInSection() != null && Series.getAllItemsInSection().size() > 0) {
            Series.setHeaderTitle(SearchModel.SEARCH_TV_SHOWS);
            allSampleData.add(Series);
        }


        if (Episode.getAllItemsInSection() != null && Episode.getAllItemsInSection().size() > 0) {
            Episode.setHeaderTitle(SearchModel.SEARCH_TV_SHOWS);
            allSampleData.add(Episode);
        }

        if (Collection.getAllItemsInSection() != null && Collection.getAllItemsInSection().size() > 0) {
            Collection.setHeaderTitle(SearchModel.SEARCH_PAGE);
            allSampleData.add(Collection);
        }

        if (Linear.getAllItemsInSection() != null && Linear.getAllItemsInSection().size() > 0) {
            Linear.setHeaderTitle(SearchModel.SEARCH_LIVE);
            allSampleData.add(Linear);
        }

        if (Program.getAllItemsInSection() != null && Program.getAllItemsInSection().size() > 0) {
            Linear.setHeaderTitle(SearchModel.SEARCH_LIVE);
            allSampleData.add(Linear);
        }


        return allSampleData;
    }


    public LiveData<ArrayList<SearchModel>> hitQuickSearchAPI(String searchString, final Context context, final List<MediaTypeModel> list, int counter, String selectedGenre, int from, int beginFrom) {
        initRealm(context);
        if (searchString != null && !searchString.equalsIgnoreCase("")) {
            matchingKeyword(searchString);
        }
        final MutableLiveData<ArrayList<SearchModel>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        if (counter == 0) {
            callNonCollectiondata(searchString, context, list, counter, ksServices, connection, selectedGenre, from);
        } else if (counter == 3) {
            callCollectiondata(searchString, context, list, counter, ksServices, connection, selectedGenre, from);
        } else {
            callNonCollectiondata(searchString, context, list, counter, ksServices, connection, selectedGenre, from);
        }
        return connection;
    }

    public LiveData<List<SearchedKeywords>> recentSearchKaltura(Context context) {
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<SearchedKeywords>> connection = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {

                ksServices.recentSearchKaltura(context, searchedKeywords1 -> {
                    if (searchedKeywords1.size() > 0) {
                        connection.postValue(searchedKeywords1);
                    } else {
                        connection.postValue(searchedKeywords1);
                    }
                });
/*
                ksServices.recentSearchKaltura(context, (status1, result) -> {
                    if (status1) {
                        try {
                            if (result.results != null) {
                                if (result.results.getObjects() != null) {
                                    if (result.results.getObjects().size() > 0) {
                                        connection.postValue(result.results.getObjects());
                                    } else {
                                        connection.postValue(null);
                                    }
                                } else {
                                    connection.postValue(null);
                                }
                            } else {
                                connection.postValue(null);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        connection.postValue(null);

                    }
                });
*/
            }

        });
        return connection;

    }


}


