package com.astro.sott.activities.search.adapter;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astro.sott.activities.catchUpRails.ui.CatchupActivity;
import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.webEpisodeDescription.ui.WebEpisodeDescriptionActivity;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.R;
import com.astro.sott.activities.forwardEPG.ForwardedEPGActivity;
import com.astro.sott.activities.liveChannel.liveChannelManager.LiveChannelManager;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.beanModel.commonBeanModel.SearchModel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.SearchReItemBinding;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SearchNormalAdapter.SearchNormalItemListener {
    private final ShowAllItemListener clickListner;
    private RailCommonData railCommonData;
    private final List<SearchModel> dataList;
    private final Activity activity;
    private String heading;

    private int count;

    public SearchResponseAdapter(Activity activity, List<SearchModel> demoList, ShowAllItemListener clickClass) {
        this.activity = activity;
        this.dataList = demoList;
        this.clickListner = clickClass;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchReItemBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.search_re_item, parent, false);
        if (viewType == MediaTypeConstant.getMovie(activity)) {
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + viewType);
            return new MovieTypeViewHolder(binding);
        } /*else if (viewType == MediaTypeConstant.getProgram(activity)) {
            return new ProgramTypeViewHolder(binding);
        } else if (viewType == MediaTypeConstant.getShortFilm(activity)) {
            return new SeasonTypeViewHolder(binding);
        }*/ else if (viewType == MediaTypeConstant.getSeries(activity)) {
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + viewType);
            return new SeriesTypeViewHolder(binding);
        } else if (viewType == MediaTypeConstant.getCollection(activity)) {
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + viewType);
            return new CollectionTypeViewHolder(binding);
        } else if (viewType == MediaTypeConstant.getLinear(activity)) {
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + viewType);
            return new LinearTypeViewHolder(binding);
        }
        else if (viewType == MediaTypeConstant.getProgram(activity)) {
            return new ProgramTypeViewHolder(binding);
        }
        else {
            PrintLogging.printLog(this.getClass(), "", "checkSearchId" + viewType);
            return new EpisodeTypeViewHolder(binding);
        }

    }


    private void setHeaderData(TextView textView, TextView headerSearchCount,int type, TextView all) {
        if (activity.getResources().getBoolean(R.bool.isTablet)) {
            textView.setTextColor(activity.getResources().getColor(R.color.primary_blue));
        }
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getType() == type) {
                heading = dataList.get(i).getHeaderTitle();

                count = dataList.get(i).getTotalCount();
                PrintLogging.printLog(this.getClass(), "", "headerTitles" + heading);
            }
        }


        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();

        if (count == 1) {
            stringBuilder.append(heading);
            stringBuilder2.append(" (" + count+" ");
            stringBuilder2.append(activity.getResources().getString(R.string.result)+")");

        } else {
            stringBuilder.append(heading);
            stringBuilder2.append(" (" + count+" ");
            stringBuilder2.append(activity.getResources().getString(R.string.results)+")");

        }

        textView.setText(stringBuilder.toString());
        headerSearchCount.setText(stringBuilder2.toString());

        if (count <= 20)
            all.setVisibility(View.GONE);


       // StringBuilderHolder.getInstance().clear();
       // StringBuilderHolder.getInstance().append(activity.getResources().getString(R.string.show_all));
       // StringBuilderHolder.getInstance().append(" ");

       //  all.setText(StringBuilderHolder.getInstance().getText());
    }

    @Override
    public void onItemClicked(final Asset itemValue, int position) {
        if (itemValue != null && itemValue.getType() == MediaTypeConstant.getMovie(activity)) {
            getRailCommonData(itemValue, activity.getResources().getString(R.string.movie));
            if (railCommonData.getImages().size() == itemValue.getImages().size())
                new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getShortFilm(activity)) {
            getRailCommonData(itemValue, activity.getResources().getString(R.string.short_film_onitem_clicked));
            if (railCommonData.getImages().size() == itemValue.getImages().size())
                new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getSeries(activity)) {
            getRailCommonData(itemValue, activity.getResources().getString(R.string.short_film_onitem_clicked));
            if (railCommonData.getImages().size() == itemValue.getImages().size())
                new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getEpisode(activity)) {
            getRailCommonData(itemValue, activity.getResources().getString(R.string.short_film_onitem_clicked));
            if (railCommonData.getImages().size() == itemValue.getImages().size())
                new ActivityLauncher(activity).webDetailRedirection(railCommonData.getObject(), AppLevelConstants.Rail5);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getCollection(activity)) {
            getRailCommonData(itemValue, activity.getResources().getString(R.string.short_film_onitem_clicked));
            if (railCommonData.getImages().size() == itemValue.getImages().size())
                new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getLinear(activity)) {
            getRailCommonData(itemValue, activity.getResources().getString(R.string.ugc_video_item_clicked));
            if (railCommonData.getImages().size() == itemValue.getImages().size())
                new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, railCommonData);
        } else if (itemValue != null && itemValue.getType() == MediaTypeConstant.getProgram(activity)) {
            new LiveChannelManager().getLiveProgram(activity, itemValue, asset -> {
                if (asset.getStatus()) {
                    if (asset.getLivePrograme()) {
                        getProgramRailCommonData(asset.getCurrentProgram(), "liveChannelCall-->>" + asset.getStatus());
                        new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, railCommonData);
                    } else {
                        getProgramRailCommonData(asset.getCurrentProgram(), "liveChannelCall-->>" + asset.getStatus() + "--" + asset.getProgramTime());
                        if (asset.getProgramTime() == 1) {
                            getProgramRailCommonData(asset.getCurrentProgram(), "Program VideoItemClicked");
                            new ActivityLauncher(activity).catchUpActivity(activity, CatchupActivity.class, railCommonData);
                        } else {
                            //  new ActivityLauncher(activity).forwardeEPGActivity(activity, ForwardedEPGActivity.class, railCommonData);
                        }
                    }
                }
            });
        }
    }


    private void getProgramRailCommonData(Asset currentProgram, String program_videoItemClicked) {
        railCommonData = new RailCommonData();
        railCommonData.setObject(currentProgram);
    }

    private void getRailCommonData(Asset itemValue, String title) {
        railCommonData = new RailCommonData();
        ArrayList<AssetCommonImages> imagelist = new ArrayList<>();
        for (int i = 0; i < itemValue.getImages().size(); i++) {
            AssetCommonImages assetCommonImages = new AssetCommonImages();
            assetCommonImages.setImageUrl(itemValue.getImages().get(i).getUrl());
            imagelist.add(assetCommonImages);
        }

        ArrayList<AssetCommonUrls> videoList = new ArrayList<>();
        for (int i = 0; i < itemValue.getMediaFiles().size(); i++) {
            AssetCommonUrls assetCommonImages = new AssetCommonUrls();
            assetCommonImages.setUrl(itemValue.getMediaFiles().get(i).getUrl());
            assetCommonImages.setDuration(String.valueOf(itemValue.getMediaFiles().get(i).getDuration()));
            assetCommonImages.setUrlType(itemValue.getMediaFiles().get(i).getType());
            videoList.add(assetCommonImages);
        }


        if (itemValue.getImages().size() == imagelist.size())
            railCommonData.setUrls(videoList);
        if (itemValue.getImages().size() == imagelist.size())
            railCommonData.setImages(imagelist);
        railCommonData.setTilte(title);
        railCommonData.setObject(itemValue);
        //  railCommonData.setCatchUpBuffer(itemValue.getEnableCatchUp());
        railCommonData.setId(itemValue.getId());
        railCommonData.setType(itemValue.getType());
        railCommonData.setName(itemValue.getName());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final List<Asset> singleSectionItems = dataList.get(position).getAllItemsInSection();
        SearchNormalAdapter itemListDataAdapter1 = new SearchNormalAdapter(activity, singleSectionItems, this);
        if (viewHolder instanceof MovieTypeViewHolder) {
            try {
                setRecyclerProperties(((MovieTypeViewHolder) viewHolder).binding.recyclerView);
                setHeaderData(((MovieTypeViewHolder) viewHolder).binding.headerTitleSearch,((MovieTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getMovie(activity), ((MovieTypeViewHolder) viewHolder).binding.tvShowAll);
                ((MovieTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
                ((MovieTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));
            } catch (ClassCastException e) {
                PrintLogging.printLog("Exception", "", "" + e);
            }
        } else if (viewHolder instanceof EpisodeTypeViewHolder) {
            setRecyclerProperties(((EpisodeTypeViewHolder) viewHolder).binding.recyclerView);
            ((EpisodeTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
            setHeaderData(((EpisodeTypeViewHolder) viewHolder).binding.headerTitleSearch,((EpisodeTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getEpisode(activity), ((EpisodeTypeViewHolder) viewHolder).binding.tvShowAll);
            ((EpisodeTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));

        } else if (viewHolder instanceof CollectionTypeViewHolder) {
            setRecyclerProperties(((CollectionTypeViewHolder) viewHolder).binding.recyclerView);
            ((CollectionTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
            setHeaderData(((CollectionTypeViewHolder) viewHolder).binding.headerTitleSearch,((CollectionTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getCollection(activity), ((CollectionTypeViewHolder) viewHolder).binding.tvShowAll);
            ((CollectionTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));

        } else if (viewHolder instanceof LinearTypeViewHolder) {

            setRecyclerProperties(((LinearTypeViewHolder) viewHolder).binding.recyclerView);
            setHeaderData(((LinearTypeViewHolder) viewHolder).binding.headerTitleSearch,((LinearTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getLinear(activity), ((LinearTypeViewHolder) viewHolder).binding.tvShowAll);

            ((LinearTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
            ((LinearTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));
        } else if (viewHolder instanceof SeasonTypeViewHolder) {
            setRecyclerProperties(((SeasonTypeViewHolder) viewHolder).binding.recyclerView);
            setHeaderData(((SeasonTypeViewHolder) viewHolder).binding.headerTitleSearch,((SeasonTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getShortFilm(activity), ((SeasonTypeViewHolder) viewHolder).binding.tvShowAll);

            ((SeasonTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
            ((SeasonTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));


        } else if (viewHolder instanceof SeriesTypeViewHolder) {
            setRecyclerProperties(((SeriesTypeViewHolder) viewHolder).binding.recyclerView);
            setHeaderData(((SeriesTypeViewHolder) viewHolder).binding.headerTitleSearch,((SeriesTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getSeries(activity), ((SeriesTypeViewHolder) viewHolder).binding.tvShowAll);

            ((SeriesTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
            ((SeriesTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));

        } else if (viewHolder instanceof ProgramTypeViewHolder) {
            setRecyclerProperties(((ProgramTypeViewHolder) viewHolder).binding.recyclerView);
            setHeaderData(((ProgramTypeViewHolder) viewHolder).binding.headerTitleSearch,((ProgramTypeViewHolder) viewHolder).binding.headerSearchCount, MediaTypeConstant.getProgram(activity), ((ProgramTypeViewHolder) viewHolder).binding.tvShowAll);

            ((ProgramTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
            ((ProgramTypeViewHolder) viewHolder).binding.tvShowAll.setOnClickListener(view -> callResultActivity(dataList.get(position).getType()));

        }
    }

    private void callResultActivity(int type) {
        clickListner.onItemClicked(forwardData(type));

    }

    public List<Asset> getListOfAll(int type) {
        List<Asset> allList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getType() == type && dataList.get(i).getAllItemsInSection().size() > 0) {
                allList = dataList.get(i).getAllItemsInSection();
            }
        }
        return allList;

    }

    private SearchModel forwardData(int type) {

        SearchModel allList = new SearchModel();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getType() == type && dataList.get(i).getAllItemsInSection().size() > 0) {
                allList = dataList.get(i);
            }
        }
        return allList;
    }


    private void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        boolean isTablet = activity.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {

            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        } else {

            recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    public interface ShowAllItemListener {
        void onItemClicked(SearchModel itemValue);
    }

    //view model of 9  media Types
    class MovieTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private MovieTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private class SeriesTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private SeriesTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private class CollectionTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private CollectionTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private class SeasonTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private SeasonTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ProgramTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private ProgramTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class LinearTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private LinearTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class EpisodeTypeViewHolder extends RecyclerView.ViewHolder {

        final SearchReItemBinding binding;

        private EpisodeTypeViewHolder(SearchReItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
