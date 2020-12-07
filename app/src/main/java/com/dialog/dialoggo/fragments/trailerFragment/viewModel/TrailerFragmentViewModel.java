package com.dialog.dialoggo.fragments.trailerFragment.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.repositories.dtv.DTVRepository;
import com.dialog.dialoggo.repositories.trailerFragment.TrailerFragmentRepository;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.List;
import java.util.Map;

public class TrailerFragmentViewModel extends AndroidViewModel {
    public TrailerFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getRefIdLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getRefIdData(map);
    }

    public LiveData<List<Asset>> getTrailer(String refId, int assetType) {
        return TrailerFragmentRepository.getInstance().getTrailerAsset(getApplication(), refId, assetType);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());

    }
}
