package com.astro.sott.adapter.experiencemng;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.HeroItemClickListner;
import com.astro.sott.databinding.LayoutHeroBannerItemBinding;
import com.astro.sott.databinding.LayoutHeroCircularItemBinding;
import com.astro.sott.databinding.LayoutHeroLandscapeItemBinding;
import com.astro.sott.databinding.LayoutHeroPosterItemBinding;
import com.astro.sott.databinding.LayoutHeroPotraitItemBinding;
import com.astro.sott.databinding.LayoutHeroSquareItemBinding;
import com.astro.sott.databinding.LayoutRcgBannerItemBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.bumptech.glide.Glide;
import com.enveu.Enum.ImageSource;
import com.enveu.Enum.ImageType;
import com.kaltura.client.Logger;
import com.kaltura.client.types.Asset;



public class CommonHeroRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    DisplayMetrics displaymetrics;
    boolean isTablet = false;
    private AssetCommonBean item;
    private int viewType;
    private HeroItemClickListner listner;
    private long mLastClickTime = 0;
    private String landscapeUrl;
    private String potraitUrl;
    private String squareUrl;
    private boolean isImage = false;
    private boolean rechargeLayout = false;


    public CommonHeroRailAdapter(Context context, AssetCommonBean item, boolean rechargeLayout) {
        this.mContext = context;
        this.item = item;
        this.viewType = item.getRailType();
        this.rechargeLayout = rechargeLayout;

        setImageUrl();
        displaymetrics = new DisplayMetrics();

        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        if (mContext.getResources().getBoolean(R.bool.isTablet))
            isTablet = true;
    }


    public CommonHeroRailAdapter(Context context, AssetCommonBean item, boolean rechargreLayout, HeroItemClickListner listner) {
        this.mContext = context;
        this.item = item;
        this.viewType = item.getRailType();
        this.listner = listner;
        this.rechargeLayout = rechargreLayout;

        setImageUrl();
        displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        if (mContext.getResources().getBoolean(R.bool.isTablet))
            isTablet = true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case AppConstants.HERO_CIR_CIRCLE:
                LayoutHeroCircularItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_circular_item, parent, false);
                return new CircularHeroHolder(itemBinding);
            case AppConstants.HERO_LDS_LANDSCAPE:
                LayoutHeroLandscapeItemBinding itemBinding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_landscape_item, parent, false);
                return new LandscapeHeroHolder(itemBinding1);

            case AppConstants.HERO_PR_POTRAIT:
                LayoutHeroPotraitItemBinding itemBinding2 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_potrait_item, parent, false);
                return new PotraitHeroHolder(itemBinding2);

            case AppConstants.HERO_SQR_SQUARE:
                LayoutHeroSquareItemBinding itemBinding5 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_square_item, parent, false);
                return new SquareHeroHolder(itemBinding5);
            case AppConstants.HERO_PR_POSTER:
                LayoutHeroPosterItemBinding itemBinding6 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_poster_item, parent, false);
                return new PosterHeroHolder(itemBinding6);
            case AppConstants.HERO_LDS_BANNER:
                LayoutHeroBannerItemBinding itemBinding7 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_banner_item, parent, false);
                return new LandscapeBannerHolder(itemBinding7);
            case AppConstants.HERO_RCG_BANNER:
                LayoutRcgBannerItemBinding itemBinding9 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_rcg_banner_item, parent, false);
                return new RCGHeroHolder(itemBinding9);
            default:
                LayoutHeroSquareItemBinding itemBinding8 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_square_item, parent, false);
                return new SquareHeroHolder(itemBinding8);

        }
    }

    public int getViewType() {
        int railType = 0;
        String layoutImageType = "";
        if (rechargeLayout) {
            layoutImageType = "RCG";
        } else {
            layoutImageType = item.getRailDetail().getContentImageType();
        }
        if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
            railType = AppConstants.HERO_LDS_LANDSCAPE;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
            railType = AppConstants.HERO_PR_POTRAIT;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
            railType = AppConstants.HERO_PR_POSTER;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
            railType = AppConstants.HERO_SQR_SQUARE;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
            railType = AppConstants.HERO_CIR_CIRCLE;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.LDS2.name())) {
            railType = AppConstants.HERO_LDS_BANNER;
        } else if (layoutImageType.equalsIgnoreCase(ImageType.RCG.name())) {
            railType = AppConstants.HERO_RCG_BANNER;
        }

        return railType;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        boolean isDark = false;
        isDark = true;
        setImageUrl();

        if (holder instanceof SquareHeroHolder) {
            try {
                ((SquareHeroHolder) holder).itemBinding.setIsDark(isDark);

                if (isImage) {
                    if (squareUrl != null)
                        ((SquareHeroHolder) holder).itemBinding.setPlaylistItem(bannerImageUrl());
                } else {
                    if (bannerImageUrl() != null)
                        ((SquareHeroHolder) holder).itemBinding.setPlaylistItem(bannerImageUrl());
                }
            } catch (Exception e) {

            }
            ((SquareHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                    Log.d("position","square");
                }
            });

        } else if (holder instanceof RCGHeroHolder) {
            ImageHelper.getInstance(((RCGHeroHolder) holder).itemBinding.heroImage.getContext()).loadImageTo(((RCGHeroHolder) holder).itemBinding.heroImage, bannerImageUrl(),R.drawable.ic_landscape_placeholder);

            ((RCGHeroHolder) holder).itemBinding.setIsDark(isDark);
            ((RCGHeroHolder) holder).itemBinding.setPlaylistItem(bannerImageUrl());
            ((RCGHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                    Log.d("position","RCGhero");

                }
            });
        } else if (holder instanceof PosterHeroHolder) {

            ((PosterHeroHolder) holder).itemBinding.setIsDark(isDark);
            ((PosterHeroHolder) holder).itemBinding.setPlaylistItem(bannerImageUrl());
            ((PosterHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                    Log.d("position","posterHero");

                }
            });
        } else if (holder instanceof PotraitHeroHolder) {
            ((PotraitHeroHolder) holder).itemBinding.setIsDark(isDark);
            ((PotraitHeroHolder) holder).itemBinding.setPlaylistItem(bannerImageUrl());
            ((PotraitHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                    Log.d("position","potrait");

                }
            });

        } else if (holder instanceof CircularHeroHolder) {

           /* if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                Glide.with(mContext)
                        .load(bannerImageUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.shimmer_circle).error(R.drawable.shimmer_circle))
                        .into(((CircularHeroHolder) holder).itemBinding.heroImage);
            } else {
                Glide.with(mContext)
                        .load(bannerImageUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.circle_dark).error(R.drawable.circle_dark))
                        .into(((CircularHeroHolder) holder).itemBinding.heroImage);

            }*/
            ((CircularHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                    Log.d("position","circular");

                }
            });
        } else if (holder instanceof LandscapeHeroHolder) {
            ((LandscapeHeroHolder) holder).itemBinding.setIsDark(isDark);
            ImageHelper.getInstance(((LandscapeHeroHolder) holder).itemBinding.heroImage.getContext()).
                    loadImageTo(((LandscapeHeroHolder) holder).itemBinding.heroImage, bannerImageUrl(),R.drawable.ic_landscape_placeholder);

            ((LandscapeHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(holder.getAdapterPosition());
                    Log.d("position",item.getRailDetail().getAsset().getImages().get(position).getUrl());

                    Log.d("position","landscapehero");

                }
            });
        } else if (holder instanceof LandscapeBannerHolder) {
            ((LandscapeBannerHolder) holder).itemBinding.setIsDark(isDark);
            ImageHelper.getInstance(((LandscapeBannerHolder) holder).itemBinding.heroImage.getContext()).loadImageTo(((LandscapeBannerHolder) holder).itemBinding.heroImage, bannerImageUrl(),R.drawable.ic_landscape_placeholder);
            ((LandscapeBannerHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                    Log.d("position","LandscapeBanner");

                }
            });
        }
    }

    public void itemClick(int position) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        RailCommonData railCommonData = new RailCommonData();
        if (item.getRailAssetList() != null)
            railCommonData = item.getRailAssetList().get(0);

        railCommonData.setRailDetail(item.getRailDetail());
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean = item;
        railCommonData.setAssetCommonBean(assetCommonBean);
        if (listner != null)
            listner.heroItemClick(position, railCommonData, item);

    }

    public String bannerImageUrl() {
        String imageUrl = "";
        try {
            if (item.getRailDetail().getImageSource().equalsIgnoreCase(ImageSource.AST.name())) {

                if (getViewType() == AppConstants.HERO_CIR_CIRCLE || getViewType() == AppConstants.HERO_SQR_SQUARE) {
                    imageUrl = squareUrl;
                } else if (getViewType() == AppConstants.HERO_PR_POTRAIT) {
                    imageUrl = potraitUrl;
                } else if (getViewType() == AppConstants.HERO_LDS_BANNER || getViewType() == AppConstants.HERO_LDS_LANDSCAPE || getViewType() == AppConstants.HERO_PR_POSTER || getViewType() == AppConstants.HERO_RCG_BANNER) {
                    imageUrl = landscapeUrl;
                    Log.d("vghgb1",landscapeUrl+"");
                }

            }

             else{
                imageUrl = item.getRailDetail().getImageURL();
                Log.d("vghgb",item.getRailDetail().getImageURL());

            }

        }catch (Exception e){

        }
        return imageUrl;
    }

    public void setImageUrl() {

        if (item.getRailDetail().getImageSource().equalsIgnoreCase(ImageSource.AST.name())) {
            Asset asset = item.getRailDetail().getAsset();
            Log.d("vghgb",asset.getImages().size()+"");
            if (asset.getImages().size() > 0) {

                for (int i = 0; i < asset.getImages().size(); i++) {
                    if (asset.getImages().get(i).getRatio().equals("16:9")) {
                        String image_url =AppConstants.WEBP_URL+ item.getRailDetail().getImageURL();
                        landscapeUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.landscape_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.landscape_image_height) + AppConstants.QUALITY;
                        isImage = true;


                    }
                    if (asset.getImages().get(i).getRatio().equals("9:16")) {
                        String image_url = AppConstants.WEBP_URL+asset.getImages().get(i).getUrl();
                        potraitUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.portrait_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.portrait_image_height) + AppConstants.QUALITY;
                        isImage = true;
                    }
                    if (asset.getImages().get(i).getRatio().equals("1:1")) {
                        String image_url =AppConstants.WEBP_URL+ asset.getImages().get(i).getUrl();
                        squareUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.square_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.square_image_height) + AppConstants.QUALITY;
                        isImage = true;
                    }
                }
            } else {
                isImage = false;
            }
        }


    }


    @Override
    public int getItemViewType(int position) {
        return getViewType();
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public void setCenterParams(CardView childView, ConstraintLayout parentView) {

        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) childView.getLayoutParams();
        layoutParams1.rightToRight = parentView.getRight();
        layoutParams1.leftToLeft = parentView.getLeft();
        layoutParams1.topToTop = parentView.getTop();
        layoutParams1.bottomToBottom = parentView.getBottom();

        childView.setLayoutParams(layoutParams1);
    }

    public class PotraitHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroPotraitItemBinding itemBinding;

        PotraitHeroHolder(LayoutHeroPotraitItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            if (isTablet) {
                ConstraintLayout.LayoutParams layoutParams = null;
                int width = displaymetrics.widthPixels;
                int height = (int) (width / 1.80);

                height = (int) (height / 1.7);
                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, height);
                layoutParams.width = (int) (height + (mContext.getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));

                int hhh = (int) (layoutParams.width / 9);
                int ttt = hhh * 16;
                layoutParams.height = ttt;

                if (layoutParams != null)
                    itemBinding.parent.setLayoutParams(layoutParams);

                setCenterParams(itemBinding.parent, itemBinding.root);
            }

        }
    }

    public class RCGHeroHolder extends RecyclerView.ViewHolder {
        LayoutRcgBannerItemBinding itemBinding;

        RCGHeroHolder(LayoutRcgBannerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            if (isTablet) {
                ConstraintLayout.LayoutParams layoutParams = null;
                int width = displaymetrics.widthPixels;
                width = (int) (width * 0.7);
                int height = (int) (width * 0.17);

                layoutParams = new ConstraintLayout.LayoutParams(width, height);
                layoutParams.height = height;
                layoutParams.width = width;


                if (layoutParams != null) {
                    itemBinding.parent.setLayoutParams(layoutParams);
                    setCenterParams(itemBinding.parent, itemBinding.rootView);
                }
            }
        }

    }

    public class LandscapeHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroLandscapeItemBinding itemBinding;

        LandscapeHeroHolder(LayoutHeroLandscapeItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            if (isTablet) {
                ConstraintLayout.LayoutParams layoutParams = null;
                int width = displaymetrics.widthPixels;
                int height = (int) (width / 1.4);
                height = (int) (height / 1.4);
                int tempWidth = (int) (height + (mContext.getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                int tempHeight = (int) (tempWidth / 16) * 9;

                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, height);
                layoutParams.height = tempHeight;
                layoutParams.width = tempWidth;


                if (layoutParams != null) {
                    itemBinding.parent.setLayoutParams(layoutParams);
                    setCenterParams(itemBinding.parent, itemBinding.rootView);
                }
            }

        }

    }

    public class LandscapeBannerHolder extends RecyclerView.ViewHolder {
        LayoutHeroBannerItemBinding itemBinding;

        LandscapeBannerHolder(LayoutHeroBannerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            if (isTablet) {
                ConstraintLayout.LayoutParams layoutParams = null;
                int width = displaymetrics.widthPixels;
                int height = (int) (width / 0.4);
                height = (int) (height / 1.4);
                int tempWidth = (int) (width - 2 * (mContext.getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                int tempHeight = (int) (tempWidth / 120) * 37;

                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, height);
                layoutParams.height = tempHeight;
                layoutParams.width = tempWidth;


                if (layoutParams != null) {
                    itemBinding.parent.setLayoutParams(layoutParams);
                    //  setCenterParams(itemBinding.parent, itemBinding.rootView);
                }
            }

        }

    }


    public class CircularHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroCircularItemBinding itemBinding;

        CircularHeroHolder(LayoutHeroCircularItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;


            if (isTablet) {

                ConstraintLayout.LayoutParams layoutParams = null;

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                int width = displaymetrics.widthPixels;
                int height = (int) (width / 1.80);

                height = (int) (height / 1.3);
                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height);
                layoutParams.width = (int) (height + (mContext.getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                layoutParams.height = layoutParams.width;


                if (layoutParams != null)
                    itemBinding.parent.setLayoutParams(layoutParams);


                setCenterParams(itemBinding.parent, itemBinding.root);


            }

        }
    }

    public class SquareHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroSquareItemBinding itemBinding;

        SquareHeroHolder(LayoutHeroSquareItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;


            if (isTablet) {

                ConstraintLayout.LayoutParams layoutParams = null;

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                int width = displaymetrics.widthPixels;
                int height = (int) (width / 1.80);

                height = (int) (height / 1.3);
                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, height);
                layoutParams.width = (int) (height + (mContext.getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                layoutParams.height = layoutParams.width;

                if (layoutParams != null)
                    itemBinding.parent.setLayoutParams(layoutParams);


                setCenterParams(itemBinding.parent, itemBinding.root);

            }
        }
    }

    public class PosterHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroPosterItemBinding itemBinding;

        PosterHeroHolder(LayoutHeroPosterItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;


            if (isTablet) {
                ConstraintLayout.LayoutParams layoutParams = null;

                int width = displaymetrics.widthPixels;
                int height = (int) (width / 1.80);

                height = (int) (height / 1.9);
                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, height);
                layoutParams.width = (int) (height + (mContext.getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));

                int hhh = (int) (layoutParams.width / 2);
                int ttt = hhh * 3;
                layoutParams.height = ttt;


                if (layoutParams != null)
                    itemBinding.parent.setLayoutParams(layoutParams);

                setCenterParams(itemBinding.parent, itemBinding.root);
            }
        }
    }


}
