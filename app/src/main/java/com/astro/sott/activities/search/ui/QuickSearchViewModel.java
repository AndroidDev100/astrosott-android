package com.astro.sott.activities.search.ui;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.repositories.moreTab.MoreFragmentRepository;

import java.util.List;

public class QuickSearchViewModel extends AndroidViewModel {
    public QuickSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<RailCommonData>> getGenreData(Context context, int filterGenre) {
        return QuickSearchLayer.getInstance().getGenreData(context,filterGenre);
    }

}
