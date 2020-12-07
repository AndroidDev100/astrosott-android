package com.dialog.dialoggo.activities.contentPreference.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.contentPreference.adapter.ContentPreferenceAdapter;
import com.dialog.dialoggo.activities.contentPreference.viewModel.GenreViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.baseModel.PrefrenceBean;
import com.dialog.dialoggo.databinding.ContentPreferenceBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ContentPreferenceActivity extends BaseBindingActivity<ContentPreferenceBinding> implements AlertDialogSingleButtonFragment.AlertDialogListener {
    private static final int VERTICAL_ITEM_SPACE = 30;
    private static final int HORIONTAL_ITEM_SPACE = 20;
    private GenreViewModel viewModel;
    //    String previousActivity;
    private ArrayList<PrefrenceBean> list;
    private ArrayList<PrefrenceBean> selectedList;
    private String firstName = "";
    private String secondName = "";
    private String thirdName = "";
    private String fourthName = "";
    private String fifthName = "";
    private ArrayList<PrefrenceBean> arrayList;
    private ContentPreferenceAdapter adatperContentPreference;

    @Override
    public ContentPreferenceBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ContentPreferenceBinding.inflate(inflater);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        connectionObserver();

        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.content_preferences));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
            getBinding().connection.rlConnection.setVisibility(View.GONE);

            modelCall();
            UIinitialization();

            loadDataFromModel();
            setClicks();


        } else {
            noConnectionLayout();
        }


    }


    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(GenreViewModel.class);
    }

    private void loadDataFromModel() {


        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getGenre().observe(this, new Observer<List<Asset>>() {
            @Override
            public void onChanged(@Nullable List<Asset> assets) {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                if (assets != null && assets.size() > 0) {
                    getBinding().btnUpdatePreference.setVisibility(View.VISIBLE);
                    getBinding().tvSelectGenre.setVisibility(View.VISIBLE);
                    getBinding().tvNoRecordFound.setVisibility(View.GONE);
                    boolean isActive = KsPreferenceKey.getInstance(getApplicationContext()).getUserActive();
                    if (isActive) {
                        checkUserPrefrences(assets);
                    } else {
                        setUIComponets(assets);
                    }
                } else {
                    getBinding().btnUpdatePreference.setVisibility(View.GONE);
                    getBinding().tvSelectGenre.setVisibility(View.GONE);
                    getBinding().tvNoRecordFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkUserPrefrences(final List<Asset> assets) {
        viewModel.checkUserPreferences().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                PrintLogging.printLog(this.getClass(), "", "valueStored" + assets.size() + " String" + s);
                if (s==null) {
                    list = new ArrayList<>();
                    AppCommonMethods.setContentPreferences(list, getApplicationContext());
                    setUIComponets(assets);
                } else {
                    setSavedValue(s);
                    setUIComponets(assets);
                }
            }
        });

    }

    private void setSavedValue(String s) {
        list = new ArrayList<>();
        if (!s.equals("")) {
            String arr[] = s.split(",");
            for (int i = 0; i < arr.length; i++) {
                PrefrenceBean prefrenceBean = new PrefrenceBean();
                prefrenceBean.setName(arr[i]);
                prefrenceBean.setChecked(true);
                prefrenceBean.setPosition(i);
                list.add(prefrenceBean);
            }
            AppCommonMethods.setContentPreferences(list, getApplicationContext());
        }
    }

    private void setUIComponets(List<Asset> assets) {

        boolean isActive = KsPreferenceKey.getInstance(getApplicationContext()).getUserActive();
        if (isActive) {
            ArrayList<PrefrenceBean> storeList = AppCommonMethods.getContentPrefences(getApplicationContext());
            if (storeList != null) {
                if (storeList.size() > 0) {
                    arrayList = mergeList(assets, storeList);
                    setAdapter();
                } else {
                    arrayList = mergeList(assets, AppCommonMethods.getLogoutContentPrefences(getApplicationContext()));
                    setUserPrefrences(arrayList, 2);
                    setAdapter();
                    ArrayList<PrefrenceBean> list = new ArrayList<>();
                    AppCommonMethods.setLogoutContentPreferences(list, getApplicationContext());

                }
            } else {
                arrayList = mergeList(assets, AppCommonMethods.getLogoutContentPrefences(getApplicationContext()));
                PrintLogging.printLog(this.getClass(), "", "sizeOfList" + arrayList.size());
                setAdapter();
                selectedList = adatperContentPreference.getGenreList();
                setUserPrefrences(selectedList, 2);
            }


        } else {
            ArrayList<PrefrenceBean> storeList = AppCommonMethods.getLogoutContentPrefences(getApplicationContext());
            arrayList = mergeList(assets, storeList);
            setAdapter();
        }


    }

    private void setUserPrefrences(ArrayList<PrefrenceBean> selectedList, int click) {
        String value = AssetContent.getSelectedPrefrences(selectedList);
        PrintLogging.printLog(this.getClass(), "", "selectedValues" + value);
        storeUserPrefrences(value, click);
    }

    private void storeUserPrefrences(String value, final int click) {
        viewModel.storeUserPrefrences(value).observe(ContentPreferenceActivity.this, s -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (s == null) {
                ToastHandler.show(getResources().getString(R.string.something_went_wrong), ContentPreferenceActivity.this);
                onBackPressed();
            } else {
                if (click == 1) {
                    showDialog(getResources().getString(R.string.preferencess_saved_message));
                }
            }
        });

    }

    private ArrayList<PrefrenceBean> mergeList(List<Asset> assetList, ArrayList<PrefrenceBean> storeList) {
//        PrintLogging.printLog("","valuessStored"+storeList.size());
        if (storeList != null) {
            if (storeList.size() > 0) {
                firstName = storeList.get(0).getName();
            }
            if (storeList.size() > 1) {
                secondName = storeList.get(1).getName();
            }
            if (storeList.size() > 2) {
                thirdName = storeList.get(2).getName();
            }
            if (storeList.size() > 3) {
                fourthName = storeList.get(3).getName();
            }
            if (storeList.size() > 4) {
                fifthName = storeList.get(4).getName();
            }

        }

        PrintLogging.printLog(this.getClass(), "", "valuessStored" + firstName + "-->>" + secondName + "-->>" + thirdName);
        //  Log.e("ASSET LIST", String.valueOf(assetList.size()));
//        Log.e("ASSET LIST", String.valueOf(storeList.size()));
        arrayList = new ArrayList<>();
        if (assetList != null) {
            if (assetList.size() > 0) {
                for (int i = 0; i < assetList.size(); i++) {
                    PrefrenceBean prefrenceBean = new PrefrenceBean();
                    if (assetList.get(i).getName().equals(firstName) || assetList.get(i).getName().equals(secondName) || assetList.get(i).getName().equals(thirdName) || assetList.get(i).getName().equals(fourthName) || assetList.get(i).getName().equals(fifthName)) {
                        prefrenceBean.setChecked(true);
                    } else {
                        prefrenceBean.setChecked(false);
                    }
                    prefrenceBean.setName(assetList.get(i).getName());
                    prefrenceBean.setPosition(i);

                    arrayList.add(prefrenceBean);
                }

            } else {
                arrayList = storeList;
            }

        } else {
            arrayList = storeList;
        }
        return arrayList;
    }

    private void setAdapter() {

        adatperContentPreference = new ContentPreferenceAdapter(ContentPreferenceActivity.this, arrayList/*, previousActivity*/);
        getBinding().recyclerView.setAdapter(adatperContentPreference);


    }

    private void setClicks() {
        selectedList = new ArrayList<>();
        getBinding().btnUpdatePreference.setOnClickListener(view -> {
            try {
                if(adatperContentPreference.getGenreList() != null && adatperContentPreference.getGenreList().size()>0){
                    selectedList = adatperContentPreference.getGenreList();
                }

                if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {

                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                    AppCommonMethods.setContentPreferences(selectedList, getApplicationContext());
                    setUserPrefrences(selectedList, 1);
                    //added on 11 nov for updating local preferences
                    AppCommonMethods.setLogoutContentPreferences(selectedList, getApplicationContext());

                } else {
                    AppCommonMethods.setLogoutContentPreferences(selectedList, getApplicationContext());
                    showDialog(getResources().getString(R.string.preferencess_saved_message));
                }
            }catch (Exception e){

            }
        });

    }

    private void UIinitialization() {
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//      staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,1);
//      getBinding().recyclerview.setLayoutManager(staggeredGridLayoutManager);
        getBinding().recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        getBinding().recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(HORIONTAL_ITEM_SPACE));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
//
        getBinding().recyclerView.setLayoutManager(flowLayoutManager);


    }

    private void noConnectionLayout() {
        getBinding().connection.rlConnection.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {
        onBackPressed();
    }
}
