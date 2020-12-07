package com.dialog.dialoggo.activities.catchUpRails.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.liveChannel.viewModel.LiveChannelViewModel;
import com.dialog.dialoggo.activities.webSeriesDescription.adapter.LiveChannelCommonAdapter;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.databinding.ActivityCatchupBinding;
import com.dialog.dialoggo.player.ui.DTPlayer;
import com.dialog.dialoggo.repositories.player.PlayerRepository;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.ImageHelper;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.ProgramAsset;

import java.util.List;
import java.util.Map;

public class CatchupActivity extends BaseBindingActivity<ActivityCatchupBinding> implements DetailRailClick {
    private RailCommonData railData;
    private Asset asset;
    private LiveChannelViewModel viewModel;
    private Map<String, MultilingualStringValueArray> map;
    private DTPlayer fragment;
    private String startTimeStamp;
    private String endTimeStamp;
    private String externalIDs;
    private String channelLogoUrl;
    private String imageUrl;
    private Boolean isLivePlayer;

    @Override
    public ActivityCatchupBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityCatchupBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
    }

    private void intentValues() {
        int layoutType = getIntent().getIntExtra(AppLevelConstants.LAYOUT_TYPE, 0);
        if(getIntent().getExtras() !=null) {
            railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            isLivePlayer = getIntent().getBooleanExtra("isLivePlayer",false);

            if(railData !=null) {
                asset = railData.getObject();
                if (asset.getType() == MediaTypeConstant.getProgram(CatchupActivity.this)) {
                    getSpecificAsset(asset.getId());
                    getDataFromBack(railData);
                } else {
                    getDataFromBack(railData);
                }
            }
        }

    }

    private void getSpecificAsset(Long id) {
        if (asset.getType() == MediaTypeConstant.getProgram(CatchupActivity.this)) {
            ProgramAsset progAsset = (ProgramAsset) asset;
            KsPreferenceKey.getInstance(getApplicationContext()).setCatchUpId(progAsset.getId() + "");
            PrintLogging.printLog(this.getClass(), "", "programAssetId" + progAsset.getLinearAssetId());
            viewModel.getSpecificAsset(progAsset.getLinearAssetId().toString()).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                    //getDataFromBack(railCommonData,1);

                    int size = railCommonData.getObject().getImages().size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            if (railCommonData.getObject().getImages().get(i).getRatio().equalsIgnoreCase("16:9")) {
                                channelLogoUrl = railCommonData.getObject().getImages().get(i).getUrl();
                            }
                        }
                        ImageHelper.getInstance(this).loadImageTo(getBinding().channelLogo,channelLogoUrl,R.drawable.square1);
                    }

                    fragment.getUrl(" ", railCommonData.getObject(), railData.getProgress(), isLivePlayer,"");

                }
            });
        } else {
            viewModel.getSpecificAsset(String.valueOf(asset.getId())).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                    getDataFromBack(railCommonData);

                }
            });
        }
    }

    private void getDataFromBack(RailCommonData commonRailData) {
        railData = commonRailData;
        asset = commonRailData.getObject();
       /* mediaAsset=(MediaAsset)asset;
        externalIDs=mediaAsset.getExternalIds();
        AppConstants.EXTERNAL_IDS=externalIDs;*/
        map = asset.getTags();
        getChannelGenres();
        getStartEndTimestamp();
        setMetaDataValue();
        //getEPGChannels(asset);
        getRails(asset);
        setPlayerFragment();
        getUrlToPlay(asset);


    }

    private void setPlayerFragment() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = (DTPlayer) manager.findFragmentById(R.id.player_fragment);

        if (fragment!=null && fragment.getBinding() != null) {
            fragment.getBinding().ivCancel.setOnClickListener(view -> CatchupActivity.super.onBackPressed());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayerRepository.getInstance().releasePlayer();
        // this.finish();

    }

    private void getUrlToPlay(Asset asset) {
        int imageSize = asset.getImages().size();
        for (int i = 0; i < imageSize; i++) {
            if (asset.getImages().get(i).getRatio().equalsIgnoreCase("16:9")) {
                imageUrl = asset.getImages().get(i).getUrl();
            }
        }
        PrintLogging.printLog(this.getClass(), "", "forwardedUrl" + imageUrl);

    }

    private void getSpecificAsset(Asset asset) {
        viewModel.getSpecificAsset(String.valueOf(asset.getId())).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.getStatus()) {
                for (int i = 0; i < railCommonData.getObject().getImages().size(); i++) {
                    String ratio = railCommonData.getObject().getImages().get(i).getRatio();
                    if (ratio.equalsIgnoreCase("1:1")) {
                        ImageHelper.getInstance(this).loadImageTo(getBinding().channelLogo,railCommonData.getObject().getImages().get(i).getUrl(),R.drawable.square1);
                    }
                }
            }
        });
    }

    private void getCasts(Map<String, MultilingualStringValueArray> tags) {
        viewModel.getCastLiveData(tags).observe(this, castTest -> {
            if (TextUtils.isEmpty(castTest)) {
                getBinding().castHint.setVisibility(View.INVISIBLE);
            } else {
                getBinding().castHint.setVisibility(View.VISIBLE);
                getBinding().setCastValue(" " + castTest.trim());
            }

        });
    }

    private void getRails(Asset asset) {
        // viewModel.getCategoryData(asset,0).observe(this, this::setUIComponets);
    }

    private void getEPGChannels(Asset asset) {
        viewModel.getEPGChannelsList(externalIDs, startTimeStamp, endTimeStamp, 1,1).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                getBinding().contentFrame.setVisibility(View.VISIBLE);
                getBinding().moreFrame.setVisibility(View.VISIBLE);
                setMetaDataValue();
            } else {
                getBinding().moreFrame.setVisibility(View.INVISIBLE);
                getBinding().contentFrame.setVisibility(View.GONE);
            }
        });
    }

    private void setMetaDataValue() {
        /*if (railCommonData.size() > 0) {
            AppConstants.currentProgramID = railCommonData.get(0).getObject().getId().intValue();
            setValues(railCommonData.get(0));
        }*/

        setValues(railData);
    }

    private void setValues(RailCommonData commonData) {
        Asset asset = commonData.getObject();
        getBinding().programName.setText(asset.getName());
        PrintLogging.printLog(this.getClass(), "", "imageListSize" + asset.getImages().size());


        getBinding().descriptionText.setText(asset.getDescription());

        String stTime = viewModel.getProgramTime(asset, 1);
        String endTime = viewModel.getProgramTime(asset, 2);
        String totalDuration = viewModel.getProgramDurtion(endTime, stTime);

        getBinding().start.setText(AppCommonMethods.setTime(commonData.getObject(), 1));
        getBinding().end.setText(AppCommonMethods.setTime(commonData.getObject(), 2));
        getBinding().totalDurationTxt.setText(totalDuration);
        getCasts(commonData.getObject().getTags());

        setExpandable();
    }

    private void setExpandable() {

        getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);


        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {

            getBinding().descriptionText.toggle();

            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.setEllipsize(null);
            } else {
                getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
            }

            if (getBinding().expandableLayout.isExpanded()) {
                getBinding().setExpandabletext(getResources().getString(R.string.more));

            } else {
                getBinding().setExpandabletext(getResources().getString(R.string.less));
            }
            if (view != null) {
                getBinding().expandableLayout.expand();
            }
            getBinding().expandableLayout.collapse();
        });


    }


    private void getChannelGenres() {
        viewModel.getGenreLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (TextUtils.isEmpty(s)) {
                    getBinding().genreTxt.setVisibility(View.GONE);
                    PrintLogging.printLog(this.getClass(), "", "genreValueforNowPlayingIs" + s);
                    getBinding().dot.setVisibility(View.GONE);
                } else {
                    getBinding().genreTxt.setVisibility(View.VISIBLE);
                    getBinding().dot.setVisibility(View.VISIBLE);
                    getBinding().genreTxt.setText(s.trim());

                }

            }
        });
    }

    private void getStartEndTimestamp() {
        startTimeStamp = AppCommonMethods.getCurrentDateTimeStamp(1);
        endTimeStamp = AppCommonMethods.getCurrentDateTimeStamp(2);

        PrintLogging.printLog(this.getClass(), "", "timeStampp" + "-->" + startTimeStamp + "--" + endTimeStamp);

    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(CatchupActivity.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            modelCall();
            intentValues();
        } else {
            noConnectionLayout();
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(LiveChannelViewModel.class);

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void setUIComponets(List<AssetCommonBean> assetCommonBeanList) {
        getBinding().myRecyclerView.hasFixedSize();
        getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(CatchupActivity.this, RecyclerView.VERTICAL, false));
        LiveChannelCommonAdapter adapter = new LiveChannelCommonAdapter(CatchupActivity.this, assetCommonBeanList);
        getBinding().myRecyclerView.setAdapter(adapter);
    }


//    private void openShareDialouge() {
//        AppCommonMethods.openShareDialog(CatchupActivity.this, asset, ApplicationMain.getAppContext());
//    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }


   /* private void getCurrentProgram(List<RailCommonData> railCommonData) {
        for (int i = 0; i < railCommonData.size(); i++) {
            Asset assetData = railCommonData.get(i).getObject();
            comparingPrograms(assetData, i);
        }
        setValues(railCommonData.get(currentProgIndex));
       PrintLogging.printLog(this.getClass(),"", "currentProgIndex" + currentProgIndex);
    }

    private void comparingPrograms(Asset assetData, int position) {
        Long endTime = assetData.getEndDate();
        String programTime = viewModel.getProgramTime(endTime);
        String currentTime = AppCommonMethods.getCurrentTime();
        currentProgIndex = viewModel.getTimeDifference(programTime, currentTime, position);
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams params2 = getBinding().playerLayout.getLayoutParams();
            params2.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params2.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getBinding().playerLayout.requestLayout();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams params2 = getBinding().playerLayout.getLayoutParams();
            params2.width = 0;
            params2.height = 0;
            getBinding().playerLayout.requestLayout();

        }

    }
}

