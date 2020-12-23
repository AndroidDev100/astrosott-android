package com.astro.sott.repositories.search;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

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
                    }else{
                        connection.postValue(null);

                    }
                });
            }

        });
        return connection;

    }


    public LiveData<ArrayList<SearchModel>> matchSetHitApi(String searchString, final Context context, final List<MediaTypeModel> list, int counter) {
        initRealm(context);
        matchingKeyword(searchString);

        String tag3 = "name ~ '";
        String tag2 = "'";

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
                    ksServices.searchKeyword(context, modifyString, list,counter, (status1, result, remarks) -> {
                        if (status1) {

                                if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND)) {
                                    connection.postValue(new ArrayList<>());
                                } else
                                    connection.postValue(sortMediaTypeList(context, result));

                        }
                    });
                }

            }
        });


        return connection;
    }

    public LiveData<ArrayList<SearchModel>> autoCompleteHit(String searchString, final Context context, final List<MediaTypeModel> list,int autoCompleteCounter) {
        initRealm(context);

        String tag3 = "name ~ '";
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
                    ksServices.searchKeywordAuto(autoCompleteCounter,context, modifyString, list, new SearchResultCallBack() {
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
                    });
                }

            }
        });


        return connection;
    }


    private ArrayList<SearchModel> sortMediaTypeList(Context context, ArrayList<SearchModel> list) {
        ArrayList<SearchModel> allSampleData = new ArrayList<>();

        //please do not break sequence
        SearchModel Movie = new SearchModel();
        SearchModel Series = new SearchModel();
        SearchModel Episode = new SearchModel();



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
        }
        if (Movie.getAllItemsInSection().size() > 0) {
            Movie.setHeaderTitle(SearchModel.MEDIATYPE_SEARCH_MOVIE);
            allSampleData.add(Movie);
        }

        if (Series.getAllItemsInSection().size() > 0) {
            Series.setHeaderTitle(SearchModel.MEDIATYPE_SERIES);
            allSampleData.add(Series);
        }


        if (Episode.getAllItemsInSection().size() > 0) {
            Episode.setHeaderTitle(SearchModel.MEDIATYPE_EPISODE);
            allSampleData.add(Episode);
        }


        return allSampleData;
    }


}


