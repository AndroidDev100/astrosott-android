package com.astro.sott.fragments.nowPlaying;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.activities.webSeriesDescription.adapter.LiveChannelCommonAdapter;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.R;
import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.liveChannel.viewModel.LiveChannelViewModel;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.FragmentNowPlayingBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NowPlaying extends BaseBindingFragment<FragmentNowPlayingBinding> {
    public static String EXTERNAL_IDS;
    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    private Asset asset;
    private Map<String, MultilingualStringValueArray> map;
    private LiveChannelViewModel viewModel;
    private String startTimeStamp;
    private String endTimeStamp;
    private MediaAsset mediaAsset;
    private long lastClickTime;
    private BaseActivity baseActivity;
    private RailCommonData railCommonData;
    private List<VIUChannel> channelList;
    private List<VIUChannel> viuChannelList;
    private int counter = 0;
    private LiveChannelCommonAdapter adapter;
    private int tempCount = 0;

    @Override
    public FragmentNowPlayingBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentNowPlayingBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
    }

    private void intentValues() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            railCommonData = bundle.getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            if (railCommonData != null) {
                asset = railCommonData.getObject();
                mediaAsset = (MediaAsset) asset;
                EXTERNAL_IDS = mediaAsset.getExternalIds();
                map = asset.getTags();
                getChannelGenres();
                getStartEndTimestamp();
                getEPGChannels(asset);
                getRails(asset);
            }
        }
    }

    private void setSpecificData(Asset asset) {
        if (getBinding() != null && asset != null) {
            AppCommonMethods.setImages(railCommonData, getActivity(), getBinding().channelLogo);
            getBinding().programName.setText(asset.getName());
            getBinding().descriptionText.setText(asset.getDescription());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSpecificData(asset);
    }

    private void getCasts(Map<String, MultilingualStringValueArray> tags) {
        viewModel.getCastLiveData(tags).observe(this, castTest -> {
            if (TextUtils.isEmpty(castTest)) {
                getBinding().castLayout.setVisibility(View.GONE);
            } else {
                getBinding().castLayout.setVisibility(View.VISIBLE);
                getBinding().setCastValue(" " + castTest.trim());
            }

        });
    }

    private void getRails(Asset asset) {
        viewModel.getChannelList(AppLevelConstants.TAB_LIVETV_DETAIL).observe(this, assetCommonBean -> {
            if (assetCommonBean.getStatus()) {
                viuChannelList = assetCommonBean.getDTChannelList();
            }
        });

        viewModel.getLiveNowData(asset).observe(this, assetCommonBeans -> {
            //setUIComponets(assetCommonBeans);
            try {
               /* if (assetCommonBeans.get(0).getStatus()) {
                    setUIComponets(assetCommonBeans, tempCount, 0);
                    tempCount++;
//                    similarRail(asset);
                    callCategoryRailAPI(viuChannelList);

                } else {
//                    similarRail(asset);
                    callCategoryRailAPI(viuChannelList);

                }*/
            } catch (Exception e) {
//                similarRail(asset);
                callCategoryRailAPI(viuChannelList);

            }


        });
    }

    private void similarRail(Asset assest) {
        viewModel.getSimilarChannelData(assest).observe(this, assetCommonBeans -> {
            try {
                if (assetCommonBeans.get(0).getStatus()) {
                    setUIComponets(assetCommonBeans, tempCount, 0);
                    callCategoryRailAPI(viuChannelList);
                } else {
                    callCategoryRailAPI(viuChannelList);
                }
            } catch (Exception e) {
                callCategoryRailAPI(viuChannelList);
            }
        });
    }

    private void callCategoryRailAPI(List<VIUChannel> list) {
        if (viuChannelList != null) {
            if (viuChannelList.size() > 0) {
                channelList = list;
                if (counter != channelList.size() && counter < channelList.size()) {
                    viewModel.getListLiveData(channelList.get(counter).getId(), viuChannelList, counter, 1).observe(this, assetCommonBeans -> {
                        if (assetCommonBeans.size() > 0) {
                            //  PrintLogging.printLog("","sizeAsset"+assetCommonBeans.size()+"");
                            boolean status = assetCommonBeans.get(0).getStatus();

                            if (status == true) {
                                setUIComponets(assetCommonBeans, counter, 1);
                                counter++;
                                callCategoryRailAPI(channelList);

                            } else {
                                if (counter != channelList.size()) {

                                    counter++;
                                    callCategoryRailAPI(channelList);
                                }
                            }

                        }
                    });

                }
            }
        }
    }

    private void getEPGChannels(Asset asset) {
        viewModel.getEPGChannelsList(mediaAsset.getExternalIds(), startTimeStamp, endTimeStamp, 1,1).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                getBinding().textViewNoEpg.setVisibility(View.GONE);
                getBinding().detailsLayout.setVisibility(View.VISIBLE);
                getBinding().moreFrame.setVisibility(View.VISIBLE);
                setMetaDataValue(railCommonData);
            } else {
                getBinding().textViewNoEpg.setVisibility(View.VISIBLE);
                getBinding().moreFrame.setVisibility(View.INVISIBLE);
                getBinding().detailsLayout.setVisibility(View.GONE);
                Constants.programName = "";
            }
        });
    }

    private void setMetaDataValue(List<RailCommonData> railCommonData) {
        if (railCommonData.size() > 0) {
            Constants.currentProgramID = railCommonData.get(0).getObject().getId().intValue();
            Constants.startTime = railCommonData.get(0).getObject().getStartDate().toString();
            setValues(railCommonData.get(0));
        }
    }

    private void setValues(RailCommonData commonData) {
        Asset asset = commonData.getObject();
        getBinding().programName.setText(asset.getName());
       // viewModel.setProgramData(asset);
        Constants.programName = asset.getName();
        PrintLogging.printLog(this.getClass(), "", "imageListSize" + asset.getImages().size());
        if (asset.getImages().size() > 0) {
            LiveChannel liveChannel = (LiveChannel) baseActivity;
            String image_url = asset.getImages().get(0).getUrl();
            image_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.catchup_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.catchup_image_height) + AppLevelConstants.QUALITY;

            if (liveChannel != null)
                ImageHelper.getInstance(baseActivity).loadImageTo(liveChannel.getBinding().playerImage, image_url, R.drawable.ic_landscape_placeholder);
        }

        getBinding().descriptionText.setText(asset.getDescription());

        String stTime = viewModel.getProgramTime(asset, 1);
        String endTime = viewModel.getProgramTime(asset, 2);
        String totalDuration = AppCommonMethods.getProgramDurtion(endTime, stTime);

        getBinding().start.setText(AppCommonMethods.setTime(commonData.getObject(), 1));
        getBinding().end.setText(AppCommonMethods.setTime(commonData.getObject(), 2));
        getBinding().totalDurationTxt.setText(totalDuration);
        getCasts(commonData.getObject().getTags());

        setExpandable();
    }

    private void setExpandable() {

        getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);


        getBinding().setExpandabletext(getResources().getString(R.string.more));

        getBinding().descriptionText.post(() -> {
            int lineCount = getBinding().descriptionText.getLineCount();
            getBinding().descriptionText.setMaxLines(2);
            if (lineCount > 2) {
                getBinding().lessButton.setVisibility(View.VISIBLE);
            } else {
                if (!getBinding().castText.getText().toString().equalsIgnoreCase("")) {
                    getBinding().lessButton.setVisibility(View.VISIBLE);
                } else {
                    getBinding().lessButton.setVisibility(View.INVISIBLE);
                }
            }
            // Use lineCount here
        });

        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {

            getBinding().descriptionText.toggle();

            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.setEllipsize(null);
            } else {
                getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
            }

            if (getBinding().expandableLayout.isExpanded()) {
                getBinding().descriptionText.setMaxLines(2);
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

                PrintLogging.printLog(this.getClass(), "", "genreValueforNowPlayingIs" + s);

                if (TextUtils.isEmpty(s)) {
                    getBinding().genreTxt.setVisibility(View.GONE);
                    getBinding().dot.setVisibility(View.GONE);
                } else {

                    s = s.replaceAll("(?<=[,.!?;:])(?!$)", " ");
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
        if (NetworkConnectivity.isOnline(baseActivity)) {
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
            //noConnectionLayout();
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(LiveChannelViewModel.class);

    }

    private void noConnectionLayout() {
        if(getBinding().noConnectionLayout != null){
            getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        }
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans, int counter, int type) {
        try {

            if (adapter != null) {
                if (type > 0) {
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(counter + tempCount);
                } else {
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(counter);
                }


            } else {
                loadedList.add(assetCommonBeans.get(0));
                adapter = new LiveChannelCommonAdapter(getActivity(), loadedList);
                getBinding().myRecyclerView.setAdapter(adapter);

            }

        } catch (Exception ignored) {

        }


    }


    private void openShareDialouge() {
        AppCommonMethods.openShareDialog(baseActivity, asset, baseActivity.getApplicationContext());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecyclerComponenets();

        getBinding().shareWith.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();
        });
    }

    private void setRecyclerComponenets() {
        getBinding().myRecyclerView.hasFixedSize();
        getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

    }

}