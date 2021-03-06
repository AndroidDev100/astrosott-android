package com.astro.sott.utils.helpers.carousel.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.R;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.util.ArrayList;
import java.util.List;


public class SliderAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final AdapterView.OnItemClickListener itemClickListener;
    private final List<Slide> items;
    private final Context context;
    private int widgetType;
    private long mLastClickTime = 0;
    private int carouselPosition = -1;
    ResponseDmsModel responseDmsModel;
    MediaTypes mediaTypes;

    public SliderAdapter(@NonNull Context context, int type, ArrayList<Slide> items, AdapterView.OnItemClickListener itemClickListener, int position) {
        this.items = items;
        this.itemClickListener = itemClickListener;
        this.context = context;
        notifyDataSetChanged();
        layoutInflater = LayoutInflater.from(context);
        this.widgetType = type;
        this.carouselPosition = position;
        try {
            responseDmsModel = AppCommonMethods.callpreference(context);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }


    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        View view = null;
        if (items.get(position).getType() == 1) {
            view = layoutInflater.inflate(R.layout.row_slider_live, container, false);
            ImageView sliderImage = view.findViewById(R.id.sliderImage);
            ImageView hungama = view.findViewById(R.id.hungama);
            TextView title_text = view.findViewById(R.id.slider_text);

            boolean isProviderAvailable = AssetContent.getHungamaTag(items.get(position).getObjects().getTags());
            if (isProviderAvailable){
                hungama.setVisibility(View.VISIBLE);
            }else {
               hungama.setVisibility(View.GONE);
            }



            //title_text.setText(items.get(position).getTitle());
            if (items.get(position).getType() == MediaTypeConstant.getLinear(context)) {
                ImageHelper.getInstance(context).loadImageTo(sliderImage, items.get(position).getImageFromUrl(), R.drawable.landscape);
            }else {
                ImageHelper.getInstance(context).loadImageToCarousal(sliderImage, items.get(position).getImageFromUrl(), R.drawable.landscape);
            }
            sliderImage.setOnClickListener(v -> itemClickListener.onItemClick(null, container.getRootView(), position, carouselPosition));

            container.addView(view);
            return view;
        } else {
            return view;
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        try {

            container.removeView((View) object);
//            object = null;
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }


//    private void loadImage(ImageView imageView, String url, int corner) {
//        if (!TextUtils.isEmpty(url)) {
//           /* Glide.with(imageView.getContext()) // Bind it with the context of the actual view used
//                    .load(url) // Load the image
//                    .bitmapTransform(new CenterCrop(imageView.getContext()), new RoundedCornersTransformations(imageView.getContext(), corner, 0, RoundedCornersTransformations.CornerType.ALL))
//                    .animate(R.anim.fade_in) // need to manually set the animation as bitmap cannot use cross fade
//                    .into(imageView);*/
//        }
//    }
}
