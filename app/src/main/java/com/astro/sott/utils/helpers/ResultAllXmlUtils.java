package com.astro.sott.utils.helpers;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import android.text.format.DateFormat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.astro.sott.R;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;

import java.util.Date;

public class ResultAllXmlUtils {


    public static String setTitle(Asset items) {
        String titleValue = items.getName();
        return titleValue.trim();
    }


    public static String getStartDate(long timeAsset) {

        return DateFormat.format("MM-dd-yyyy", new Date(timeAsset)).toString();
    }

    @BindingAdapter("setImageUrlSearch")
    public static void setImageResource(ImageView view, MediaImage urlImage) {

        if (urlImage != null) {
            Context context = view.getContext();

            String final_url = urlImage.getUrl() + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;

           /* Glide.with(context)
                    .setDefaultRequestOptions(option)
                    .load(final_url)
                    .into(view);*/

            ImageHelper.getInstance(view.getContext()).loadQuickSearchImage(view, final_url, R.drawable.landscape);
        }


    }

    @BindingAdapter("setImageUrlWeb")
    public static void setImage(ImageView view, String urlImage) {

        Context context = view.getContext();

        RequestOptions option = new RequestOptions()
                .placeholder(R.drawable.square1)
                .error(R.drawable.square1);


        Glide.with(context)
                .setDefaultRequestOptions(option)
                .load(urlImage)
                .into(view);

    }




   /* public static int setMyVisbility(Asset items)
    {
        int val = 0;
        switch (String.valueOf(items.getType())) {
            case AppConstants.MEDIATYPE_WEBEPISODE:
                break;
            case AppConstants.MEDIATYPE_SPOTLIGHT_SERIES:
                //no data
                break;
            case AppConstants.MEDIATYPE_SHORTFILM:
                val=8;
                break;
            case AppConstants.MEDIATYPE_MOVIE:
                break;
            case AppConstants.MEDIATYPE_PROGRAM:
                break;
            case AppConstants.MEDIATYPE_UGCVIDEO:
                break;
            case AppConstants.MEDIATYPE_LINEAR:
                break;
            case AppConstants.MEDIATYPE_WEBSERIES:
                break;
            case AppConstants.MEDIATYPE_SPOTLIGHT_EPISODE:
                break;
            default:
        }

        return val;

    }*/

    public static String setDetails(Context context, Asset items) {
        String titleValue = "";
        String mediaType = String.valueOf(items.getType());
        if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getWebEpisode(context)))) {
            if (items.getMetas().get("Season number") != null) {
                double d = Double.parseDouble(items.getMetas().get("Season number").toParams().get("value").toString());
                String season = "Season " + (int) d;
                d = Double.parseDouble(items.getMetas().get("Episode number").toParams().get("value").toString());
                String episode = "Episode " + (int) d;
                titleValue = episode + " | " + season;
            } else {
                if (items.getMetas().get("Episode number") != null) {
                    double d = Double.parseDouble(items.getMetas().get("Episode number").toParams().get("value").toString());
                    String episode = "Episode " + (int) d;
                    titleValue = episode + " | " + "Season";
                }
            }
        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getShortFilm(context)))) {
            titleValue = AssetContent.getWatchListGenre(items.getTags());
        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getMovie(context)))) {
            titleValue = AssetContent.getWatchListGenre(items.getTags());
        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getProgram(context)))) {
            if (!items.getTags().isEmpty())
                titleValue = AssetContent.getWatchListGenre(items.getTags());

        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getLinear(context)))) {
            titleValue = AssetContent.getWatchListGenre(items.getTags());
        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getDrama(context)))) {
            titleValue = AssetContent.getWatchListGenre(items.getTags());
        }
        return titleValue.trim();
    }


}
