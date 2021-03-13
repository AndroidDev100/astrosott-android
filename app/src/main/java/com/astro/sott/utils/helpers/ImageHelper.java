package com.astro.sott.utils.helpers;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.astro.sott.utils.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ImageHelper {

    private static final ImageHelper ourInstance = new ImageHelper();
    private static Glide mGlideObj;
    private static RequestOptions requestOptions;
    private static Context context;
    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public static ImageHelper getInstance(Context context) {
        mGlideObj = Glide.get(context);
        ImageHelper.context = context;
        requestOptions = new RequestOptions();
        return ourInstance;
    }

    public void loadImageTo(ImageView imageView, String imageUrl, int placeholder) {
        GlideApp.with(context).setDefaultRequestOptions(requestOptions).load(imageUrl)
                .apply(requestOptions.placeholder(placeholder))
                .thumbnail(0.6f)
                .into(imageView);

    }


    public void loadImageToNil(ImageView imageView, String imageUrl, int placeholder) {
        GlideApp.with(context).load(placeholder)
                .transition(withCrossFade(factory))
                .apply(requestOptions.placeholder(placeholder))
                .thumbnail(0.6f)
                .into(imageView);

    }


    public void loadImageTo(ImageView imageView, String imageUrl, RequestOptions requestOptions) {
        GlideApp.with(context).setDefaultRequestOptions(requestOptions).
                load(imageUrl).thumbnail(0.1f).into(imageView);
    }

    public void loadImageTo(ImageView imageView, Uri imageUrl, int placeholder) {
        GlideApp.with(context).load(imageUrl)
                .apply(requestOptions.placeholder(placeholder))
                .thumbnail(0.1f).into(imageView);
    }

    public void loadImageTo(ImageView imageView, int alt, int placeholder) {

        GlideApp.with(context).load(alt)
                .transition(withCrossFade(factory))
                .apply(requestOptions.placeholder(placeholder))
                .thumbnail(0.1f).into(imageView);
    }


    public void loadImageToPotrait(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).override(1024,768).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }
    public void loadImageToLandscape(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }
    public void loadImageToCarousal(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }

    public void loadImageToPortraitListing(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }
    public void loadImageToPortraitDetailListing(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }
    public void loadImageTocontinueWatchingListing(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }
    public void loadImageToLandscapeListingAdapter(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }

    public void loadImageToLandscapeDetailListingAdapter(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }
    public void loadImageToPlaceholder(ImageView imageView, Uri imageUrl, int placeholder) {

        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).thumbnail(0.1f).into(imageView);
        //setImageDescription(imageView, imageUrl.getPath());
    }

    public void setImageDescription(ImageView imageView, String imageUrl) {
        try {
            if (!StringUtils.isNullOrEmptyOrZero(imageUrl) && imageView != null) {
                if (imageUrl.length() > 20) {
                    String tempTitle = imageUrl.substring(imageUrl.length() - 15);
                    imageView.setContentDescription(tempTitle);
                } else {
                    imageView.setContentDescription(String.valueOf(System.currentTimeMillis()));
                }
            }
        } catch (Exception ignored) {

        }


    }

    public void loadImageOfGenre(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).thumbnail(0.6f).into(imageView);


    }

    public void loadQuickSearchImage(ImageView imageView, String imageUrl, int placeholder) {
        requestOptions.placeholder(placeholder);
        GlideApp.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);


    }


}