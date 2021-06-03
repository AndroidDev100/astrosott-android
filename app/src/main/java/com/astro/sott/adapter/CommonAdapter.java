package com.astro.sott.adapter;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.adapter.experiencemng.CommonCircleAdapter;
import com.astro.sott.adapter.experiencemng.CommonHeroRailAdapter;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.HeroItemClickListner;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.callBacks.commonCallBacks.RemoveAdsCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.DeleteFromFollowlistCallBack;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomLayoutManager;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.adapter.experiencemng.CommonPosterAdapter;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.CarouselPotraitLayoutBinding;
import com.astro.sott.databinding.CircularRecyclerItemBinding;
import com.astro.sott.databinding.ContinueWatchingRecycleritemBinding;
import com.astro.sott.databinding.DfpBannerLayoutBinding;
import com.astro.sott.databinding.FanAdsLayoutItemBinding;
import com.astro.sott.databinding.HeaderRecyclerItemBinding;
import com.astro.sott.databinding.HeroAdsLayoutBinding;
import com.astro.sott.databinding.LandscapeRecyclerItemBinding;
import com.astro.sott.databinding.PosterRecyclerItemBinding;
import com.astro.sott.databinding.PotraitRecyclerItemBinding;
import com.astro.sott.databinding.SquareRecyclerItemBinding;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.GravitySnapHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SpacingItemDecoration;
import com.astro.sott.utils.helpers.ToolBarHandler;
import com.enveu.Enum.LandingPageType;
import com.enveu.Enum.PDFTarget;
import com.enveu.Enum.PredefinePlaylistType;
import com.facebook.ads.AudienceNetworkAds;

import java.util.ArrayList;
import java.util.List;

import static com.astro.sott.utils.constants.AppConstants.ADS_BANNER;
import static com.astro.sott.utils.constants.AppConstants.ADS_MREC;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_CST_CUSTOM;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_LDS_LANDSCAPE;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_PR_POSTER;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_PR_POTRAIT;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_SQR_SQUARE;
import static com.astro.sott.utils.constants.AppConstants.HERO_CIR_CIRCLE;
import static com.astro.sott.utils.constants.AppConstants.HERO_LDS_BANNER;
import static com.astro.sott.utils.constants.AppConstants.HERO_LDS_LANDSCAPE;
import static com.astro.sott.utils.constants.AppConstants.HERO_PR_POSTER;
import static com.astro.sott.utils.constants.AppConstants.HERO_PR_POTRAIT;
import static com.astro.sott.utils.constants.AppConstants.HERO_RCG_BANNER;
import static com.astro.sott.utils.constants.AppConstants.HERO_SQR_SQUARE;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_CIR_CIRCLE;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_LDS_LANDSCAPE;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_PR_POSTER;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_PR_POTRAIT;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_SQR_SQUARE;
import static com.astro.sott.utils.constants.AppConstants.LIVE_CHANNEL_LIST;
import static com.astro.sott.utils.constants.AppConstants.PDF_TARGET_LGN;
import static com.astro.sott.utils.constants.AppConstants.RAIL_CONTINUE_WATCHING;
import static com.astro.sott.utils.constants.AppConstants.RAIL_FAN_MOBILE;
import static com.astro.sott.utils.constants.AppConstants.RAIL_FAN_TABLET;
import static com.astro.sott.utils.constants.AppConstants.RAIL_MY_WATCHLIST;
import static com.astro.sott.utils.constants.AppConstants.RAIL_RECOMMENDED;
import static com.astro.sott.utils.constants.AppConstants.SIMILLAR_UGC_VIDEOS;
import static com.astro.sott.utils.constants.AppConstants.YOU_MAY_LIKE;


public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity activity;
    private final List<AssetCommonBean> dataList;
    private final ArrayList<Slide> slides;
    private long lastClickTime = 0;
    private int count = 0;
    private int cwIndex = -1;
    private DetailRailClick detailRailClick;
    RemoveAdsCallBack removeAdsCallBack;
    ContinueWatchingRemove continueWatchingRemove;
    HeroItemClickListner listner;

    /*public CommonAdapter(Activity activity, List<AssetCommonBean> demoList, ArrayList<Slide> slides) {
        this.activity = activity;
        this.dataList = demoList;
        this.slides = slides;
        this.detailRailClick = ((DetailRailClick) activity);

    }*/

    public CommonAdapter(Activity activity, List<AssetCommonBean> demoList, ArrayList<Slide> slides,
                         ContinueWatchingRemove callBack, RemoveAdsCallBack removeCallBack) {
        PrintLogging.printLog("", "onConstructor--");
        this.activity = activity;
        this.dataList = demoList;
        this.slides = slides;
        this.removeAdsCallBack = removeCallBack;
        AudienceNetworkAds.initialize(activity);
        // AdSettings.addTestDevice("42ba4e54-5bb1-4f5d-a1ce-3187e8c3ffc5");
        //FacebookSdk.setIsDebugEnabled(true);
        this.continueWatchingRemove = callBack;
        this.detailRailClick = ((DetailRailClick) activity);
    }

    public CommonAdapter(Activity activity, List<AssetCommonBean> demoList, ArrayList<Slide> slides,
                         ContinueWatchingRemove callBack, RemoveAdsCallBack removeCallBack, HeroItemClickListner listner) {
        PrintLogging.printLog("", "onConstructor--");
        this.activity = activity;
        this.dataList = demoList;
        this.slides = slides;
        this.removeAdsCallBack = removeCallBack;
        AudienceNetworkAds.initialize(activity);
        // AdSettings.addTestDevice("42ba4e54-5bb1-4f5d-a1ce-3187e8c3ffc5");
        //FacebookSdk.setIsDebugEnabled(true);
        this.continueWatchingRemove = callBack;
        this.listner = listner;
        this.detailRailClick = ((DetailRailClick) activity);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getRailDetail().getWidgetType();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PrintLogging.printLog("", "viewTypes--" + viewType);
        switch (viewType) {
            case ADS_BANNER:
            case ADS_MREC:
                DfpBannerLayoutBinding bannerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dfp_banner_layout, parent, false);
                DfpBannerHolder dfpBannerHolder = new DfpBannerHolder(bannerLayoutBinding);
                return dfpBannerHolder;
            case RAIL_FAN_TABLET:
            case RAIL_FAN_MOBILE:
                FanAdsLayoutItemBinding fanAdsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fan_ads_layout_item, parent, false);
                FANHolder fanHolder = new FANHolder(fanAdsLayoutBinding);

                return fanHolder;
            case PDF_TARGET_LGN:
            case RAIL_CONTINUE_WATCHING:
                ContinueWatchingRecycleritemBinding watchingRecycleritemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.continue_watching_recycleritem, parent, false);
                ContinueWatchingHolder watchingHolder = new ContinueWatchingHolder(watchingRecycleritemBinding);
                setRecyclerProperties(watchingHolder.landscapeRecyclerItemBinding.recyclerViewList4);
                return watchingHolder;
            case RAIL_RECOMMENDED:
               /* LandscapeRecyclerItemBinding landscapeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                RecommendedHolder recommendedHolder = new RecommendedHolder(landscapeRecyclerItemBinding);
                setRecyclerProperties(recommendedHolder.landscapeRecyclerItemBinding.recyclerViewList3, true);
                return recommendedHolder;*/

            case RAIL_MY_WATCHLIST:
            case CAROUSEL_PR_POTRAIT:
                CarouselPotraitLayoutBinding carouselPotraitLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.carousel_potrait_layout, parent, false);
                return new PotraitCarouselHolder(carouselPotraitLayoutBinding, viewType);
            case CAROUSEL_LDS_LANDSCAPE:
            case CAROUSEL_SQR_SQUARE:
            case CAROUSEL_PR_POSTER:
            case CAROUSEL_CST_CUSTOM:
                HeaderRecyclerItemBinding headerRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_recycler_item, parent, false);
                return new HeaderHolder(headerRecyclerItemBinding, viewType);
            case HORIZONTAL_LDS_LANDSCAPE:
                LandscapeRecyclerItemBinding landscapeRecyclerItemBinding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                LandscapeHolder landscapeHolder = new LandscapeHolder(landscapeRecyclerItemBinding1);
                setRecyclerProperties(landscapeHolder.landscapeRecyclerItemBinding.recyclerViewList4);
                return landscapeHolder;
            case HORIZONTAL_PR_POTRAIT:
                PotraitRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_recycler_item, parent, false);
                PotraitHolder childHolder = new PotraitHolder(binding);
                setRecyclerProperties(childHolder.potraitRecyclerItemBinding.recyclerViewList4);
                return childHolder;
            case HORIZONTAL_PR_POSTER:
                PosterRecyclerItemBinding posterRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_recycler_item, parent, false);
                PosterHolder posterHolder = new PosterHolder(posterRecyclerItemBinding);
                setRecyclerProperties(posterHolder.itemBinding.recyclerViewList4);
                return posterHolder;

            case HORIZONTAL_SQR_SQUARE:
                SquareRecyclerItemBinding squareRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
                SquareHolder squreHolder = new SquareHolder(squareRecyclerItemBinding);
                setRecyclerProperties(squreHolder.squareRecyclerItemBinding.recyclerViewList4);
                return squreHolder;
            case HORIZONTAL_CIR_CIRCLE:
                CircularRecyclerItemBinding circularRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.circular_recycler_item, parent, false);
                CircleHolder circleHolder = new CircleHolder(circularRecyclerItemBinding);
                setRecyclerProperties(circleHolder.circularRecyclerItemBinding.recyclerViewList1);
                return circleHolder;
            case HERO_LDS_LANDSCAPE:
            case HERO_LDS_BANNER:
            case HERO_PR_POTRAIT:
            case HERO_RCG_BANNER:
            case HERO_PR_POSTER:
            case HERO_SQR_SQUARE:
            case HERO_CIR_CIRCLE:
                HeroAdsLayoutBinding heroAdsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hero_ads_layout, parent, false);
                return new HeroAdsHolder(heroAdsLayoutBinding);
            default:
                LandscapeRecyclerItemBinding landscapeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                LandscapeHolder landscapeHolder1 = new LandscapeHolder(landscapeRecyclerItemBinding);
                setRecyclerProperties(landscapeHolder1.landscapeRecyclerItemBinding.recyclerViewList4);
                return landscapeHolder1;


        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PotraitCarouselHolder) {

            try {
                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.titleLayout.setVisibility(View.VISIBLE);
                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());

                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.slider.getRootView();


                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) activity).getWindowManager()
                        .getDefaultDisplay()
                        .getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                KsPreferenceKey.getInstance(activity).setAutoDuration(dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration() == null ? 0 : dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration());
                KsPreferenceKey.getInstance(activity).setAutoRotation(dataList.get(position).getRailDetail().getCategory().getAutoRotate() == null ? true : dataList.get(position).getRailDetail().getCategory().getAutoRotate());
                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.slider.addSlides(dataList.get(position).getSlides(), dataList.get(position).getWidgetType(), dataList.get(position).getRailDetail(), position, width, dataList.get(position).getRailDetail().getCategory().getAutoRotate() == null ? true : dataList.get(position).getRailDetail().getCategory().getAutoRotate(), dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration() == null ? 0 : dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration());
                setHeaderAndMoreVisibility(((PotraitCarouselHolder) holder).headerRecyclerItemBinding.headerTitle, ((HeaderHolder) holder).headerRecyclerItemBinding.moreText, dataList.get(position));
                ((PotraitCarouselHolder) holder).headerRecyclerItemBinding.moreText.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                PrintLogging.printLog("", "crashOnRail" + e.toString());
            }
        } else if (holder instanceof HeaderHolder) {

            try {
                ((HeaderHolder) holder).headerRecyclerItemBinding.titleLayout.setVisibility(View.VISIBLE);
                ((HeaderHolder) holder).headerRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
                ((HeaderHolder) holder).headerRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());

                ((HeaderHolder) holder).headerRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
                ((HeaderHolder) holder).headerRecyclerItemBinding.slider.getRootView();
                KsPreferenceKey.getInstance(activity).setAutoDuration(dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration() == null ? 0 : dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration());
                KsPreferenceKey.getInstance(activity).setAutoRotation(dataList.get(position).getRailDetail().getCategory().getAutoRotate() == null ? true : dataList.get(position).getRailDetail().getCategory().getAutoRotate());
                ((HeaderHolder) holder).headerRecyclerItemBinding.slider.addSlides(dataList.get(position).getSlides(), dataList.get(position).getWidgetType(), dataList.get(position).getRailDetail(), position, dataList.get(position).getRailDetail().getCategory().getAutoRotate() == null ? true : dataList.get(position).getRailDetail().getCategory().getAutoRotate(), dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration() == null ? 0 : dataList.get(position).getRailDetail().getCategory().getAutoRotateDuration());
                setCrauselHeaderAndMoreVisibility(((HeaderHolder) holder).headerRecyclerItemBinding.headerTitle, ((HeaderHolder) holder).headerRecyclerItemBinding.moreText, dataList.get(position));

            } catch (Exception e) {
                e.printStackTrace();
                PrintLogging.printLog("", "crashOnRail" + e.toString());
            }
        } else if (holder instanceof CircleHolder) {
            try {
                circleDataLogic(((CircleHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        } else if (holder instanceof LandscapeHolder) {
            try {
                landscapeDataLogic(((LandscapeHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        } else if (holder instanceof SquareHolder) {
            try {
                squareDataLogic(((SquareHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        } else if (holder instanceof PotraitHolder) {

            potraitDataLogic(((PotraitHolder) holder), dataList, position);

        } else if (holder instanceof PosterHolder) {

            posterDataLogic(((PosterHolder) holder), dataList, position);

        } else if (holder instanceof ContinueWatchingHolder) {
            try {
                continueWatchingDataLogic(((ContinueWatchingHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        } /*else if (holder instanceof IFPHolder) {
            try {
                IfpDataLogic(((IFPHolder) holder), dataList, position);
                //fanDataLogic(((FANHolder) holder),dataList,position);
            } catch (ClassCastException e) {

            }
        }*/ else if (holder instanceof FANHolder) {
            try {
                fanDataLogic(((FANHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        }/* else if (holder instanceof RecommendedHolder) {
            try {
                recommendedDataLogic(((RecommendedHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        }*/ /*else if (holder instanceof BannerSubscriptionHolder) {
            try {

            } catch (ClassCastException ea) {

            }
        }*/ else if (holder instanceof DfpBannerHolder) {
            try {
                DfpBannerAdapter adapter = new DfpBannerAdapter(activity, dataList.get(position));
                ((DfpBannerHolder) holder).dfpBannerLayoutBinding.rvDfpBanner.setNestedScrollingEnabled(false);
                ((DfpBannerHolder) holder).dfpBannerLayoutBinding.rvDfpBanner.setHasFixedSize(true);
                ((DfpBannerHolder) holder).dfpBannerLayoutBinding.rvDfpBanner.setLayoutManager(new CustomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                //SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
                //snapHelperStart.attachToRecyclerView(((DfpBannerHolder) holder).dfpBannerLayoutBinding.rvDfpBanner);
                ((DfpBannerHolder) holder).dfpBannerLayoutBinding.rvDfpBanner.setAdapter(adapter);
            } catch (ClassCastException ea) {

            }
        } else if (holder instanceof HeroAdsHolder) {
            heroAdsLayout((HeroAdsHolder) holder, position);
        } /*else if (holder instanceof SeriesHolder) {
            try {
                SeriesDataLogic(((SeriesHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        }*/
    }

    private void heroAdsLayout(HeroAdsHolder holder, int position) {
        CommonHeroRailAdapter adapter;
        if (dataList.get(position).getRailDetail() != null && dataList.get(position).getRailDetail().getLandingPageType() != null && dataList.get(position).getRailDetail().getLandingPageType().equals(LandingPageType.PDF.name())) {
            if (dataList.get(position).getRailDetail().getLandingPagetarget() != null && dataList.get(position).getRailDetail().getLandingPagetarget().equals(PDFTarget.RCG.name())) {
                if (listner != null) {
                    adapter = new CommonHeroRailAdapter(activity, dataList.get(position), true, listner);
                } else {
                    adapter = new CommonHeroRailAdapter(activity, dataList.get(position), true);
                }
            } else {
                if (listner != null) {
                    adapter = new CommonHeroRailAdapter(activity, dataList.get(position), false, listner);
                } else {
                    adapter = new CommonHeroRailAdapter(activity, dataList.get(position), false);
                }
            }

        } else {
            if (listner != null) {
                adapter = new CommonHeroRailAdapter(activity, dataList.get(position), false, listner);
            } else {
                adapter = new CommonHeroRailAdapter(activity, dataList.get(position), false);
            }
        }


        holder.heroAdsHolder.rvHeroBanner.setNestedScrollingEnabled(false);
        holder.heroAdsHolder.rvHeroBanner.setHasFixedSize(true);
        holder.heroAdsHolder.rvHeroBanner.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        holder.heroAdsHolder.rvHeroBanner.setAdapter(adapter);

    }


    private void fanDataLogic(FANHolder holder, List<AssetCommonBean> dataList, int position) {
        try {
            FanAdapter adapter = new FanAdapter(activity, dataList.get(position), removeAdsCallBack, position);
            holder.fanItemBinding.rvFanBanner.setNestedScrollingEnabled(false);
            holder.fanItemBinding.rvFanBanner.setHasFixedSize(true);
            holder.fanItemBinding.rvFanBanner.setLayoutManager(new CustomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
            snapHelperStart.attachToRecyclerView(holder.fanItemBinding.rvFanBanner);
            holder.fanItemBinding.rvFanBanner.setAdapter(adapter);
        } catch (ClassCastException ea) {
            PrintLogging.printLog("", "FBAdsError--in" + ea);
        }
        // loadNativeAd(holder, dataList, position);

    }


    CommonCircleAdapter commonCircleAdapter;

    private void circleDataLogic(CircleHolder holder, List<AssetCommonBean> dataList, int position) {

        new ToolBarHandler(activity).setMoreListener(((CircleHolder) holder).circularRecyclerItemBinding.moreText, AppConstants.TYPE2, dataList.get(position), activity);


        ((CircleHolder) holder).circularRecyclerItemBinding.titleLayout.setVisibility(View.VISIBLE);
        ((CircleHolder) holder).circularRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        ((CircleHolder) holder).circularRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        boolean isContinueRail = dataList.get(position).getRailDetail().getDescription().equalsIgnoreCase(AppConstants.KEY_CONTINUE_WATCHING);

        if (isContinueRail) {
            commonCircleAdapter = new CommonCircleAdapter(activity, singleSectionItems, AppConstants.Rail5, new ContinueWatchingRemove() {
                @Override
                public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {
                    if (continueWatchingIndex != -1) {
                        removeAssetApi(assetID, singleSectionItems, continueWatchingAdapter, continueWatchingRemove, listSize, position, continueWatchingIndex);
                    }
                }
            }, position, dataList.get(position).getTitle(), isContinueRail, dataList.get(position).getCategory());
        } else {
            commonCircleAdapter = new CommonCircleAdapter(activity, singleSectionItems, AppConstants.Rail2, dataList.get(position).getTitle(), dataList.get(position).getCategory());
        }
        // setRecyclerProperties(((CircleHolder) holder).circularRecyclerItemBinding.recyclerViewList1,false);
        holder.circularRecyclerItemBinding.recyclerViewList1.setAdapter(commonCircleAdapter);
        setHeaderAndMoreVisibility(holder.circularRecyclerItemBinding.headerTitle, holder.circularRecyclerItemBinding.moreText, dataList.get(position));
        //  if (dataList.get(position).getMoreType() == 8 || dataList.get(position).getMoreType() == 9 || dataList.get(position).getMoreType() == 10) {
        holder.circularRecyclerItemBinding.headerTitle.setVisibility(View.VISIBLE);
        int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 10)
            holder.circularRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
    }

    private void removeAssetApi(Long assetID, List<RailCommonData> singleSectionItems, TabsContinueWatchingAdapter continueWatchingAdapter,
                                ContinueWatchingRemove continueWatchingRemove, int listSize, int position, int continueWatchingIndex) {
        KsServices ksServices = new KsServices(activity);
        ksServices.removeCWAPI(assetID, new DeleteFromFollowlistCallBack() {
            @Override
            public void deleteFollowlistDetail(Boolean status) {
                try {
                    if (status) {
                        if (listSize == 1) {
                            cwIndex = -1;
                            singleSectionItems.remove(position);
                            continueWatchingAdapter.notifyItemRemoved(position);
                            PrintLogging.printLog("", "indexToDelete" + position);
                            KsPreferenceKey.getInstance(activity).setCWListSize(singleSectionItems.size() + 1);
                            continueWatchingRemove.remove(assetID, position, continueWatchingIndex, listSize);

                        } else {
                            cwIndex = -1;
                            singleSectionItems.remove(position);
                            KsPreferenceKey.getInstance(activity).setCWListSize(singleSectionItems.size() + 1);
                            continueWatchingAdapter.notifyItemRemoved(position);
                            PrintLogging.printLog("", "indexToDelete" + position);
                        }
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    CommonPosterAdapter commonPosterAdapter;

    private void posterDataLogic(PosterHolder holder, List<AssetCommonBean> dataList, int position) {
        Log.w("ImageListSize-->>", dataList.get(position).getRailAssetList().get(0).getImages().size() + "");
        int totalCount = dataList.get(position).getTotalCount();
        new ToolBarHandler(activity).setMoreListener(holder.itemBinding.moreText, AppConstants.TYPE3, dataList.get(position), activity);


        holder.itemBinding.titleLayout.setVisibility(View.VISIBLE);
        holder.itemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.itemBinding.headerTitle.setText(dataList.get(position).getTitle());
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        boolean isContinueRail = dataList.get(position).getRailDetail().getDescription().equalsIgnoreCase(AppConstants.KEY_CONTINUE_WATCHING);


        if (isContinueRail) {
            commonPosterAdapter = new CommonPosterAdapter(activity, singleSectionItems, AppConstants.Rail5, new ContinueWatchingRemove() {
                @Override
                public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {
                    if (continueWatchingIndex != -1) {
                        removeAssetApi(assetID, singleSectionItems, continueWatchingAdapter, continueWatchingRemove, listSize, position, continueWatchingIndex);
                    }
                }
            }, position, dataList.get(position).getTitle(), isContinueRail, dataList.get(position).getRailDetail().getCategory());


        } else {
            commonPosterAdapter = new CommonPosterAdapter(activity, singleSectionItems, AppConstants.Rail3, dataList.get(position).getTitle(), dataList.get(position).getRailDetail().getCategory());
        }

        holder.itemBinding.recyclerViewList4.setAdapter(commonPosterAdapter);

        setHeaderAndMoreVisibility(holder.itemBinding.headerTitle, holder.itemBinding.moreText, dataList.get(position));

    }


    TabsContinueWatchingAdapter continueWatchingAdapter;

    private void continueWatchingDataLogic(ContinueWatchingHolder holder, List<AssetCommonBean> dataList, int position) {
        /* *//* int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*//*
        new ToolBarHandler(activity).setContinueWatchingListener(((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.moreText, AppLevelConstants.TYPE4, dataList.get(position));
        holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
        *//*} else {
            holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*//*

        holder.landscapeRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.landscapeRecyclerItemBinding.headerTitleLayout.setVisibility(View.VISIBLE);
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        CommonContWatchListingAdapter commonSquareAapter = new CommonContWatchListingAdapter(activity, singleSectionItems, AppLevelConstants.Rail3);

        holder.landscapeRecyclerItemBinding.recyclerViewList4.setAdapter(commonSquareAapter);*/

        int totalCount = dataList.get(position).getTotalCount();
        // new ToolBarHandler(activity).setContinueWatchingListener(((ContinueWatchingHolder)holder).landscapeRecyclerItemBinding.moreText, AppConstants.TYPE5,dataList.get(position));
        if (totalCount >= 20) {
            new ToolBarHandler(activity).setContinueWatchingListener(((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.moreText, AppConstants.TYPE5, dataList.get(position));
        } else {
            ((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.moreText.setVisibility(View.INVISIBLE);
        }


        ((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        ((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        ((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.titleLayout.setVisibility(View.VISIBLE);

        cwIndex = position;
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();


        continueWatchingAdapter = new TabsContinueWatchingAdapter(activity, singleSectionItems, AppConstants.Rail5, new ContinueWatchingRemove() {
            @Override
            public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {
                if (continueWatchingIndex != -1) {
                    removeAssetApi(assetID, singleSectionItems, continueWatchingAdapter, continueWatchingRemove, listSize, position, continueWatchingIndex);
                }
            }
        }, position, dataList.get(position).getTitle());


        ((ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.recyclerViewList4.setAdapter(continueWatchingAdapter);
        setHeaderAndMoreVisibility(holder.landscapeRecyclerItemBinding.headerTitle, holder.landscapeRecyclerItemBinding.moreText, dataList.get(position));


    }

    CommonLandscapeAdapter commonLandscapeAdapter;

    private void landscapeDataLogic(LandscapeHolder holder, List<AssetCommonBean> dataList, int position) {

        /*int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*/
        // new ToolBarHandler(activity).setMoreListener(holder.landscapeRecyclerItemBinding.moreText, AppLevelConstants.TYPE5, dataList.get(position));
        //  holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
       /* } else {
            holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/

       /* holder.landscapeRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.landscapeRecyclerItemBinding.headerTitleLayout.setVisibility(View.VISIBLE);

        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        CommonLandscapeAdapter commonLandscapeAdapter = new CommonLandscapeAdapter(activity, singleSectionItems, AppLevelConstants.Rail5);
        holder.landscapeRecyclerItemBinding.recyclerViewList4.setAdapter(commonLandscapeAdapter);



*/
        int totalCount = dataList.get(position).getTotalCount();
        new ToolBarHandler(activity).setMoreListener(((LandscapeHolder) holder).landscapeRecyclerItemBinding.moreText, AppConstants.TYPE5, dataList.get(position));

        //new ToolBarHandler(activity).setMoreListener(((LandscapeHolder) holder).landscapeRecyclerItemBinding.moreText, AppConstants.TYPE5, dataList.get(position));
        ((LandscapeHolder) holder).landscapeRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        ((LandscapeHolder) holder).landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        //  ((LandscapeHolder) holder).landscapeRecyclerItemBinding.titleLayout.setVisibility(View.VISIBLE);

        boolean isContinueRail = false;
        if (dataList.get(position).getRailDetail().getPredefPlaylistType() != null)
            isContinueRail = dataList.get(position).getRailDetail().getPredefPlaylistType().equalsIgnoreCase(PredefinePlaylistType.CON_W.name());

        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();


        if (isContinueRail) {
            cwIndex = position;
            commonLandscapeAdapter = new CommonLandscapeAdapter(activity, singleSectionItems, AppConstants.Rail5, new ContinueWatchingRemove() {
                @Override
                public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {
                    if (continueWatchingIndex != -1) {
                        // removeAssetApi2(assetID, singleSectionItems, commonLandscapeAdapter, continueWatchingRemove, listSize, position, continueWatchingIndex);
                    }
                }
            }, position, dataList.get(position).getTitle(), isContinueRail, dataList.get(position).getRailDetail().getCategory());
        } else {
            commonLandscapeAdapter = new CommonLandscapeAdapter(activity, singleSectionItems, AppConstants.Rail5, dataList.get(position).getTitle(), dataList.get(position).getRailDetail().getCategory());

        }

        //  setRecyclerProperties(((LandscapeHolder) holder).landscapeRecyclerItemBinding.recyclerViewList3, true);
        ((LandscapeHolder) holder).landscapeRecyclerItemBinding.recyclerViewList4.setAdapter(commonLandscapeAdapter);
        setHeaderAndMoreVisibility(holder.landscapeRecyclerItemBinding.headerTitle, holder.landscapeRecyclerItemBinding.moreText, dataList.get(position));
        if (dataList.get(position).getMoreType() == LIVE_CHANNEL_LIST || dataList.get(position).getMoreType() == SIMILLAR_UGC_VIDEOS || dataList.get(position).getMoreType() == YOU_MAY_LIKE) {
            holder.landscapeRecyclerItemBinding.headerTitle.setVisibility(View.VISIBLE);
            if (dataList.get(position).getMoreType() == SIMILLAR_UGC_VIDEOS) {
                if (totalCount > 5)
                    holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
            } else {
                if (totalCount > 20)
                    holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
            }

        } else if (isContinueRail) {
            if (activity instanceof HomeActivity) {
                if (totalCount >= 20) {
                    new ToolBarHandler(activity).setContinueWatchingListener(((LandscapeHolder) holder).landscapeRecyclerItemBinding.moreText, AppConstants.TYPE5, dataList.get(position));
                } else {
                 //   ((LandscapeHolder) holder).landscapeRecyclerItemBinding.moreText.setVisibility(View.INVISIBLE);
                }
            } else {
                if (totalCount >= 10) {
                    new ToolBarHandler(activity).setContinueWatchingListener(((LandscapeHolder) holder).landscapeRecyclerItemBinding.moreText, AppConstants.TYPE5, dataList.get(position));
                } else {
                 //   ((LandscapeHolder) holder).landscapeRecyclerItemBinding.moreText.setVisibility(View.INVISIBLE);
                }
            }

        } else {
            if (totalCount > 20) {
                holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
            } else {
               // holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.INVISIBLE);

            }
        }


    }

    private void squareDataLogic(SquareHolder holder, List<AssetCommonBean> dataList, int position) {

       /* int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*/
        new ToolBarHandler(activity).setMoreListener(holder.squareRecyclerItemBinding.moreText, AppLevelConstants.TYPE4, dataList.get(position));
        holder.squareRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
       /* } else {
            holder.squareRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/
        holder.squareRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.squareRecyclerItemBinding.headerTitleLayout.setVisibility(View.VISIBLE);
        holder.squareRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        holder.squareRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);

        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();

        CommonSquareAapter commonSquareAapter = new CommonSquareAapter(activity, singleSectionItems, AppLevelConstants.Rail4, dataList.get(position).getCategory());
        holder.squareRecyclerItemBinding.recyclerViewList4.setAdapter(commonSquareAapter);
    }

    private void potraitDataLogic(PotraitHolder holder, List<AssetCommonBean> dataList, int position) {
        /*int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*/
        new ToolBarHandler(activity).setMoreListener(holder.potraitRecyclerItemBinding.moreText, AppLevelConstants.TYPE3, dataList.get(position));
        holder.potraitRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
        /*} else {
            holder.potraitRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/


        holder.potraitRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.potraitRecyclerItemBinding.headerTitleLayout.setVisibility(View.VISIBLE);
        holder.potraitRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.potraitRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());

        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();

        CommonPotraitAdapter commonPotraitAdapter = new CommonPotraitAdapter(activity, singleSectionItems, AppLevelConstants.Rail3, dataList.get(position).getRailDetail().getCategory());
        holder.potraitRecyclerItemBinding.recyclerViewList4.setAdapter(commonPotraitAdapter);
    }

    private void setRecyclerProperties(RecyclerView rView) {
        // rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(true);
        rView.addItemDecoration(new SpacingItemDecoration(20, SpacingItemDecoration.HORIZONTAL));

        rView.setLayoutManager(new CustomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(rView);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class HeaderHolder extends RecyclerView.ViewHolder {

        final HeaderRecyclerItemBinding headerRecyclerItemBinding;

        public HeaderHolder(HeaderRecyclerItemBinding flightItemLayoutBinding, int viewType) {
            super(flightItemLayoutBinding.getRoot());
            headerRecyclerItemBinding = flightItemLayoutBinding;

            LinearLayout.LayoutParams layoutParams = null;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) headerRecyclerItemBinding.slider.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            boolean isTablet = false;

            if (headerRecyclerItemBinding.constraintLayout.getResources().getBoolean(R.bool.isTablet))
                isTablet = true;

            switch (viewType) {
                case CAROUSEL_LDS_LANDSCAPE: {
                    int height = (int) (width / 1.80);
                    if (isTablet)
                        height = (int) (height / 1.7);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + (headerRecyclerItemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                }
                break;
                case CAROUSEL_PR_POTRAIT: {
                    int height = (int) (width / 1.77);
                    if (isTablet)
                        height = (int) (height / 1.7);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);

                    if (isTablet) {
                        layoutParams.height = (int) (height + (headerRecyclerItemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                    } else {
                        int hhh = (int) ((width) / 9);
                        int ttt = hhh * 16;
                        layoutParams.height = ttt;

                        int potraitWidth = (width * 3) / 4;
                        int potraitHeight = (potraitWidth * 16) / 9;
                        layoutParams.height = potraitHeight;
                        layoutParams.width = potraitWidth;

                    }

                }
                break;
                case CAROUSEL_PR_POSTER: {
                    int height = (int) (width / 1.80);
                    if (isTablet)
                        height = (int) (height / 1.7);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + (headerRecyclerItemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                }
                break;

                case CAROUSEL_CST_CUSTOM: {
                    int height = (int) (width / 1.77);
                    if (isTablet)
                        height = (int) (height / 4.3);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);


                    layoutParams.height = (int) (height + headerRecyclerItemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_potrait_indicator_padding));


                    break;
                }
                case CAROUSEL_SQR_SQUARE: {
                    int height = width;
                    if (isTablet)
                        height = (int) (height / 2.65);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + headerRecyclerItemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_square_indicator_padding));
                    break;
                }


            }
            if (layoutParams != null)
                headerRecyclerItemBinding.constraintLayout.setLayoutParams(layoutParams);


            final String name = activity.getClass().getSimpleName();
            headerRecyclerItemBinding.slider.setItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();

//                    PrintLogging.printLog("", "mediatypeClick-->>" + slides.get(i).getRailCommonData().getObject().getType() + "");
                    int pos = (int) l;
                    if (dataList.get(pos).getSlides() != null && dataList.get(pos).getSlides().get(i).getRailCommonData().getObject().getType() == MediaTypeConstant.getIFP(activity)) {
                        /*boolean status = new KsPreferenceKeys(activity).getUserActive();
                        if (status) {
                            new ActivityLauncher(activity).ifpActivity(activity, IFPActivity.class);
                        } else {
                            new ActivityLauncher(activity).loginActivity(activity, LoginActivity.class, 1);
                        }*/
                        new ActivityLauncher(activity).astrLoginActivity(activity, AstrLoginActivity.class, "profile");

                    } else {
                        int layoutType = AppCommonMethods.getRailTypeAccToMedia(activity, getLayoutPosition(), dataList, i);

                        new ActivityLauncher(activity).railClickCondition("", "", name, dataList.get(pos).getSlides().get(i).getRailCommonData(), getLayoutPosition(), layoutType, dataList.get(pos).getRailAssetList(), new MediaTypeCallBack() {
                            @Override
                            public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
                                if (detailRailClick != null)
                                    detailRailClick.detailItemClicked(_url, position, type, commonData);
                                //   Toast.makeText(activity,"CommonAdapter:HeaderHolder",Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                }
            });
        }
    }


    private class SquareHolder extends RecyclerView.ViewHolder {

        final SquareRecyclerItemBinding squareRecyclerItemBinding;

        private SquareHolder(SquareRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            squareRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    private class LandscapeHolder extends RecyclerView.ViewHolder {

        final LandscapeRecyclerItemBinding landscapeRecyclerItemBinding;

        public LandscapeHolder(LandscapeRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class PotraitHolder extends RecyclerView.ViewHolder {

        final PotraitRecyclerItemBinding potraitRecyclerItemBinding;

        private PotraitHolder(PotraitRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class ContinueWatchingHolder extends RecyclerView.ViewHolder {
        ContinueWatchingRecycleritemBinding landscapeRecyclerItemBinding;

        public ContinueWatchingHolder(ContinueWatchingRecycleritemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    //experience manager changes
    public class DfpBannerHolder extends RecyclerView.ViewHolder {

        DfpBannerLayoutBinding dfpBannerLayoutBinding;

        public DfpBannerHolder(@NonNull DfpBannerLayoutBinding itemView) {
            super(itemView.getRoot());
            dfpBannerLayoutBinding = itemView;
        }
    }

    public class FANHolder extends RecyclerView.ViewHolder {
        FanAdsLayoutItemBinding fanItemBinding;

        public FANHolder(FanAdsLayoutItemBinding binding) {
            super(binding.getRoot());
            fanItemBinding = binding;
        }
    }

    public class PotraitCarouselHolder extends RecyclerView.ViewHolder {
        CarouselPotraitLayoutBinding headerRecyclerItemBinding;

        public PotraitCarouselHolder(CarouselPotraitLayoutBinding flightItemLayoutBinding, int viewType) {
            super(flightItemLayoutBinding.getRoot());
            headerRecyclerItemBinding = flightItemLayoutBinding;
            int potraitWidth = -1;
            int potraitHeight = -1;
            LinearLayout.LayoutParams layoutParams = null;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) headerRecyclerItemBinding.slider.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            boolean isTablet = false;
            Log.e("screen Width", "width" + width);
            if (headerRecyclerItemBinding.slider.getResources().getBoolean(R.bool.isTablet))
                isTablet = true;

            layoutParams = new LinearLayout.LayoutParams(headerRecyclerItemBinding.slider.getLayoutParams());

            if (isTablet) {
                potraitWidth = (width * 20) / 100;
                potraitHeight = (potraitWidth * 16) / 9;
                layoutParams.height = potraitHeight + AppConstants.INDICATOR_BOTTOM;
                headerRecyclerItemBinding.slider.setLayoutParams(layoutParams);
            } else {
                potraitWidth = (width * 52) / 100;
                potraitHeight = (potraitWidth * 16) / 9;
                layoutParams.height = potraitHeight + AppConstants.INDICATOR_BOTTOM;
                headerRecyclerItemBinding.slider.setLayoutParams(layoutParams);
            }


            final String name = activity.getClass().getSimpleName();
            headerRecyclerItemBinding.slider.setItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();

//                    PrintLogging.printLog("", "mediatypeClick-->>" + slides.get(i).getRailCommonData().getObject().getType() + "");
                    int pos = (int) l;
                    if (dataList.get(pos).getSlides().get(i).getRailCommonData().getObject().getType() == MediaTypeConstant.getIFP(activity)) {
                       /* boolean status = new KsPreferenceKeys(activity).getUserActive();
                        if (status) {
                            new ActivityLauncher(activity).ifpActivity(activity, IFPActivity.class);
                        } else {
                            new ActivityLauncher(activity).loginActivity(activity, LoginActivity.class, 1);
                        }*/
                        new ActivityLauncher(activity).astrLoginActivity(activity, AstrLoginActivity.class, "profile");

                    } else {
                        int layoutType = AppCommonMethods.getRailTypeAccToMedia(activity, getLayoutPosition(), dataList, i);

                        new ActivityLauncher(activity).railClickCondition("", "", name, dataList.get(pos).getSlides().get(i).getRailCommonData(), getLayoutPosition(), layoutType, dataList.get(pos).getRailAssetList(), new MediaTypeCallBack() {
                            @Override
                            public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

                            }
                        });

                    }

                }
            });
        }
    }

    public class PosterHolder extends RecyclerView.ViewHolder {
        PosterRecyclerItemBinding itemBinding;

        public PosterHolder(PosterRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class CircleHolder extends RecyclerView.ViewHolder {

        CircularRecyclerItemBinding circularRecyclerItemBinding;

        public CircleHolder(CircularRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            circularRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    public class HeroAdsHolder extends RecyclerView.ViewHolder {
        HeroAdsLayoutBinding heroAdsHolder;

        HeroAdsHolder(@NonNull HeroAdsLayoutBinding itemHolder) {
            super(itemHolder.getRoot());
            this.heroAdsHolder = itemHolder;
        }
    }

    public void setHeaderAndMoreVisibility(TextView header, ImageView moreTextView, AssetCommonBean data) {
        if (data.getRailDetail() != null)
            if (data.getRailDetail().isShowHeader())
                header.setVisibility(View.VISIBLE);
            else
                header.setVisibility(View.INVISIBLE);
        if (data.getRailDetail() != null)
            if (data.getRailDetail().isContentShowMoreButton())
                moreTextView.setVisibility(View.VISIBLE);
            else
                moreTextView.setVisibility(View.INVISIBLE);


    }

    public void setCrauselHeaderAndMoreVisibility(TextView header, ImageView moreTextView, AssetCommonBean data) {
        if (data.getRailDetail() != null)
            if (data.getRailDetail().isShowHeader())
                header.setVisibility(View.VISIBLE);
            else
                header.setVisibility(View.GONE);
        if (data.getRailDetail() != null)
            if (data.getRailDetail().isContentShowMoreButton())
                moreTextView.setVisibility(View.VISIBLE);
            else
                moreTextView.setVisibility(View.GONE);


    }

    public void setHeaderAndMoreVisibility(TextView header, LinearLayout moreTextView, AssetCommonBean data) {
        if (data.getRailDetail() != null)
            if (data.getRailDetail().isShowHeader())
                header.setVisibility(View.VISIBLE);
            else
                header.setVisibility(View.INVISIBLE);
        if (data.getRailDetail() != null)
            if (data.getRailDetail().isContentShowMoreButton())
                moreTextView.setVisibility(View.VISIBLE);
            else
                moreTextView.setVisibility(View.INVISIBLE);


    }


}