package com.astro.sott.activities.language.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.audio.ui.AudioLanguageActivity;
import com.astro.sott.activities.language.ui.adapter.AppLanguageAdapter;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.activities.search.ui.SearchKeywordActivity;
import com.astro.sott.activities.subtitle.ui.SubtitleLanguageActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.callBacks.commonCallBacks.SettingExpendableItemClick;
import com.astro.sott.databinding.ActivityLanguageSettingsBinding;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.modelClasses.dmsResponse.SubtitleLanguages;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageSettingsActivity extends BaseBindingActivity<ActivityLanguageSettingsBinding> implements SettingExpendableItemClick {
    private String oldLang, newLang;
    private List<String> headerList;
    private List<String> audioList, subTitleList, appLanguageList;
    private HashMap<String, List<String>> listHashMap;


    @Override
    protected ActivityLanguageSettingsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLanguageSettingsBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldLang = new KsPreferenceKey(LanguageSettingsActivity.this).getAppLangName();
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(FirebaseEventManager.LANGUAGE_SETTINGS);
        setClicks();
        initData();

    }

    List<AudioLanguages> audioLanguageList;
    List<SubtitleLanguages> subtitleLanguageList;

    private void initData() {
        headerList = new ArrayList<>();
        audioList = new ArrayList<>();
        subTitleList = new ArrayList<>();
        appLanguageList = new ArrayList<>();

        listHashMap = new HashMap<>();

        headerList.add(getResources().getString(R.string.App_language));
        headerList.add(getResources().getString(R.string.Audio_language));
        headerList.add(getResources().getString(R.string.Subtitle_language));
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(this);
        audioLanguageList = responseDmsModel.getAudioLanguageList();
        subtitleLanguageList = responseDmsModel.getSubtitleLanguageList();

        for (AudioLanguages audioLanguages : audioLanguageList) {
            if (audioLanguages.getKey() != null && !audioLanguages.getKey().equalsIgnoreCase(""))
                audioList.add(audioLanguages.getKey());
        }
        appLanguageList.add("English");
        //  appLanguageList.add("Malay");

        for (SubtitleLanguages subtitleLanguages : subtitleLanguageList) {
            if (subtitleLanguages.getKey() != null && !subtitleLanguages.getKey().equalsIgnoreCase(""))
                subTitleList.add(subtitleLanguages.getKey());
        }
        listHashMap.put(headerList.get(0), appLanguageList);
        listHashMap.put(headerList.get(1), audioList);
        listHashMap.put(headerList.get(2), subTitleList);


        loadDataFromModel();

    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadDataFromModel() {
        AppLanguageAdapter adapter = new AppLanguageAdapter(headerList, listHashMap, LanguageSettingsActivity.this);
        getBinding().expandableListView.setAdapter(adapter);
    }

    private void updateLang() {
        newLang = new KsPreferenceKey(LanguageSettingsActivity.this).getAppLangName();
        if (!oldLang.equals(newLang)) {
            oldLang = newLang;
            if (newLang.equalsIgnoreCase("ms")) {
                getBinding().title.setText("Pemilihan Bahasa");

            } else {
                getBinding().title.setText("Language Selection");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // updateLang();
    }

    @Override
    public void onClick(int caption, int clickedFrom) {
        updateLang();
        initData();
        if (clickedFrom == 1) {

        } else if (clickedFrom == 2) {
            if (audioLanguageList != null && audioLanguageList.size() > 0) {
                new KsPreferenceKey(LanguageSettingsActivity.this).setAudioLangKey(audioLanguageList.get(caption).getValue());
            } else {
                new KsPreferenceKey(LanguageSettingsActivity.this).setAudioLangKey("");
            }
        } else if (clickedFrom == 3) {
            if (subtitleLanguageList != null && subtitleLanguageList.size() > 0) {
                new KsPreferenceKey(LanguageSettingsActivity.this).setSubTitleLangKey(subtitleLanguageList.get(caption).getKey().toString());
            } else {
                new KsPreferenceKey(LanguageSettingsActivity.this).setSubTitleLangKey("");
            }
        }
    }
}