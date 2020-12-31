package com.astro.sott.activities.movieDescription.layers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimilarMovie {

    private int assetID = 0;
    private int assetTYPE = 0;
    private List<AssetCommonBean> assetCommonList;
    private String genreskl;
    private AssetCommonBean assetCommonBean;
    private List<Response<ListResponse<Asset>>> responseList;

    private static SimilarMovie movieDescriptionRepository;

    public static SimilarMovie getInstance() {
        if (movieDescriptionRepository == null) {
            movieDescriptionRepository = new SimilarMovie();
        }

        return movieDescriptionRepository;
    }

    public static void resetObject() {
        movieDescriptionRepository = null;
    }


    public LiveData<List<AssetCommonBean>> fetchSimilarMovie(Context context,
                                                             int assetId,
                                                             int counter, int assetType,
                                                             Map<String, MultilingualStringValueArray> map,
                                                             int layoutType,
                                                             int screen_id, Asset asset) {

        assetID = assetId;
        assetTYPE = assetType;
        responseList = new ArrayList<>();
        genreskl = AppLevelConstants.KSQL_GENRE;
        final KsServices ksServices = new KsServices(context);
        List<MultilingualStringValue> genreskl_values = new ArrayList<>();
        assetCommonList = new ArrayList<>();
        MultilingualStringValueArray genreskl_list = map.get(AppLevelConstants.KEY_GENRE);

        if (genreskl_list != null)
            genreskl_values.addAll(genreskl_list.getObjects());

        genreSkl(genreskl_values);

        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        assetCommonBean = new AssetCommonBean();
        ksServices.simillarMovieListing(genreskl,assetType, assetId, counter, (status, commonResponse) -> {
            if (status) {

                assetCommonBean.setStatus(true);
                parseSimilarMovieAssests(context, commonResponse.getAssetList());
                //  assetCommonBean.setAssetList(commonResponse.getAssetList());
                connection.postValue(assetCommonList);
            } else {
                assetCommonBean.setStatus(false);
                assetCommonList.add(assetCommonBean);
                connection.postValue(assetCommonList);
            }

        });


        return connection;
    }

    private void genreSkl(List<MultilingualStringValue> genre_values) {
        StringBuilder stringBuilder = new StringBuilder(genreskl);
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            if (i != genre_values.size() - 1) {
                stringBuilder.append(genre_values.get(i).getValue());
                stringBuilder.append(AppLevelConstants.KSQL_GENRE_END);
            }
            else{
                stringBuilder.append(genre_values.get(i).getValue());
                stringBuilder.append("'");
            }
        }
        genreskl = stringBuilder.toString();
    }

    private void parseSimilarMovieAssests(Context context, Response<ListResponse<Asset>> assetList) {
        responseList.add(assetList);
        assetCommonBean.setStatus(true);
        long id=1;
        assetCommonBean.setID(id);
        assetCommonBean.setRailType(AppLevelConstants.Rail5);
        assetCommonBean.setMoreType(AppLevelConstants.SIMILAR_MOVIES);
        assetCommonBean.setMoreID(assetID);
        assetCommonBean.setMoreGenre(genreskl);
        assetCommonBean.setMoreAssetType(assetTYPE);
        assetCommonBean.setTitle(context.getResources().getString(R.string.similar_movie));

        setRailData(context, responseList, assetCommonBean);

    }

    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list, AssetCommonBean assetCommonBean) {

        int totalCount = responseList.get(0).results.getTotalCount();
        if (totalCount != 0) {
            List<RailCommonData> railList = new ArrayList<>();
            for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
                RailCommonData railCommonData = new RailCommonData();
             //   railCommonData.setCatchUpBuffer(list.get(0).results.getObjects().get(j).getEnableCatchUp());
                railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
                railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
                railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
                railCommonData.setObject(list.get(0).results.getObjects().get(j));

                List<AssetCommonImages> imagesList = new ArrayList<>();
                for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {

                    AssetCommonImages assetCommonImages = new AssetCommonImages();

                    AppCommonMethods.getImageList(context, AppLevelConstants.TYPE5, 0, j, k, list, assetCommonImages, imagesList);
                }

                List<AssetCommonUrls> urlList = new ArrayList<>();
                for (int k = 0; k < list.get(0).results.getObjects().get(j).getMediaFiles().size(); k++) {
                    AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                    assetCommonUrls.setUrl(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                    assetCommonUrls.setUrlType(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getType());
                    assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, 0, j, k));
                    urlList.add(assetCommonUrls);
                }
                railCommonData.setImages(imagesList);
                railCommonData.setUrls(urlList);
                railList.add(railCommonData);

            }
            assetCommonBean.setTotalCount(list.get(0).results.getTotalCount());
            assetCommonBean.setRailAssetList(railList);
            assetCommonList.add(assetCommonBean);
        }
    }
}
