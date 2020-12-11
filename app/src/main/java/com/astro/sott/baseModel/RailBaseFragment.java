package com.astro.sott.baseModel;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.activities.movieDescription.adapter.MovieDescriptionCommonAdapter;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.databinding.BaserailFragmentBinding;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RailBaseFragment extends BaseBindingFragment<BaserailFragmentBinding> {
    private MovieBaseViewModel viewModel;
    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    private MovieDescriptionCommonAdapter adapter;
    private int tempCount = 0;
    private List<VIUChannel> channelList;
    private List<VIUChannel> dtChannelsList;
    private int counterValueApiFail = 0;
    private int counter = 0;

    public void setViewModel(Class<? extends MovieBaseViewModel> viewModelClass) {
        viewModel = ViewModelProviders.of(this).get(viewModelClass);

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
                getBinding().commonRecyclerView.setNestedScrollingEnabled(false);
                getBinding().commonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                adapter = new MovieDescriptionCommonAdapter(getActivity(), loadedList);
                getBinding().commonRecyclerView.setAdapter(adapter);

            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }


    }

    @Override
    public BaserailFragmentBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return BaserailFragmentBinding.inflate(inflater);
    }

    public void callRailAPI(int assetId, int counter, int assetType, Map<String, MultilingualStringValueArray> map, int layoutType, int screen_id, Asset asset) {
      this.counter=0;
        if (getActivity() != null) {
            dtChannelsList = new ArrayList<>();
            viewModel.getChannelList(screen_id).observe(getActivity(), assetCommonBean -> {
                if (assetCommonBean != null && assetCommonBean.getStatus()) {
                    dtChannelsList = assetCommonBean.getDTChannelList();
                    callYouMayAlsoLike(assetId, counter, assetType, map, layoutType, screen_id, asset);
                }else {
                    PrintLogging.printLog("","channelNotAvailable");
                    callYouMayAlsoLike(assetId, counter, assetType, map, layoutType, screen_id, asset);
                }
            });

        }
    }

    private void callYouMayAlsoLike(int assetId, int counter, int assetType, Map<String, MultilingualStringValueArray> map, int layoutType, int screen_id, Asset asset) {
        viewModel.getYouMayAlsoLike(assetId, counter, assetType, map, layoutType, screen_id, asset).observe(this, assetCommonBeans -> {
            try {
                if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        resetRecyclerView(1);
                        setUIComponets(assetCommonBeans, tempCount, 0);
                        tempCount++;
                        callSimilarMovie(assetId, counter, assetType, map, layoutType, screen_id, asset);
                    } else {
                        callSimilarMovie(assetId, counter, assetType, map, layoutType, screen_id, asset);
                    }
                } else {
                    callSimilarMovie(assetId, counter, assetType, map, layoutType, screen_id, asset);
                }
            } catch (Exception e) {
                callSimilarMovie(assetId, counter, assetType, map, layoutType, screen_id, asset);
            }


        });

    }

    private void callSimilarMovie(int assetId, int counter, int assetType, Map<String, MultilingualStringValueArray> map, int layoutType, int screen_id, Asset asset) {
        viewModel.getSimilarMovie(assetId, counter, assetType, map, layoutType, screen_id, asset).observe(this, assetCommonBeans -> {
            try {
                if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        resetRecyclerView(1);
                        setUIComponets(assetCommonBeans, tempCount, 0);
                        tempCount++;
                        callCategoryRailAPI(dtChannelsList);
                    }else {
                        callCategoryRailAPI(dtChannelsList);
                    }
                } else {
                    callCategoryRailAPI(dtChannelsList);
                }
            } catch (Exception e) {
                Log.e("Exception",e.getMessage());
            }

        });
    }
    private void callCategoryRailAPI(List<VIUChannel> list) {
        Log.e("callCategoryRailAPI", String.valueOf(dtChannelsList.size()));

        if (dtChannelsList != null) {
            if (dtChannelsList.size() > 0) {
                channelList = list;
                if (getActivity() != null && counter != channelList.size() && counter < channelList.size()) {
                    viewModel.getListLiveData(channelList.get(counter).getId(), dtChannelsList, counter, 1).observe(getActivity(), assetCommonBeans -> {
                        if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                            boolean status = assetCommonBeans.get(0).getStatus();
                            if (status) {
                                setUIComponets(assetCommonBeans, counter, 1);
                                counter++;
                                counterValueApiFail++;
                                callCategoryRailAPI(channelList);

                            } else {
                                if (counter != channelList.size()) {
                                    counter++;
                                    callCategoryRailAPI(channelList);
                                }
                            }

                        }else {
                            if (counter != channelList.size()) {
                                counter++;
                                callCategoryRailAPI(channelList);
                            }
                        }


                    });

                }
            }
        }
    }


    public void resetRecyclerView(int type) {
        if (type == 0) {
            viewModel.resetObject();
            loadedList.clear();
            adapter = null;
            if (getBinding().commonRecyclerView.getVisibility() == View.VISIBLE) {
                getBinding().commonRecyclerView.setVisibility(View.GONE);
            } else {
                getBinding().commonRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            getBinding().commonRecyclerView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(viewModel !=null)
        viewModel.resetObject();
    }
}
