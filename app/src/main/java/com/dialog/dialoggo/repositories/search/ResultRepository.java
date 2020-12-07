package com.dialog.dialoggo.repositories.search;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.dialog.dialoggo.beanModel.commonBeanModel.SearchModel;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;

public class ResultRepository {

    private static ResultRepository resultRepository;
    private final ArrayList<Asset> railList = new ArrayList<>();

    public static ResultRepository getInstance() {
        if (resultRepository == null) {
            resultRepository = new ResultRepository();
        }
        return resultRepository;
    }

    public LiveData<ArrayList<Asset>> getResultSearchAll(final Context context, final String searchType, final String searchString, final int counter, boolean isScrolling) {

        if (!isScrolling) {
            railList.clear();
        }
        final MutableLiveData<ArrayList<Asset>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.showAllKeyword(context, searchType, searchString, counter, (status1, result, remarks) -> {
                    if (status1) {
                        try {
                            if (remarks.equalsIgnoreCase(AppLevelConstants.NO_RESULT_FOUND))
                                connection.postValue(new ArrayList<>());
                            else
                                setValues(connection, result);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });


        return connection;
    }

    private void setValues(MutableLiveData<ArrayList<Asset>> connection, ArrayList<SearchModel> result) {
        if (result != null) {
            railList.addAll(result.get(0).getAllItemsInSection());
            connection.postValue(railList);
        }
    }

}
