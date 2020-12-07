package com.dialog.dialoggo.fragments.viu.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.home.HomeActivity;
import com.dialog.dialoggo.baseModel.BaseBindingFragment;
import com.dialog.dialoggo.databinding.FragmentViuBinding;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.BitmapUtils;
import com.dialog.dialoggo.utils.helpers.ImageHelper;

import java.lang.ref.WeakReference;

public class ViuFragment extends BaseBindingFragment<FragmentViuBinding> implements View.OnClickListener {


    private HomeActivity homeActivity;
    private BitmapAsync bitmapAsync;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();


    }


    private void initData() {

        bitmapAsync = new BitmapAsync(getBinding().ivPapare, homeActivity);
        bitmapAsync.execute(R.drawable.viu1);
        ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivAltBalaji, R.drawable.alt, R.drawable.square1);
        ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivErosNow, R.drawable.eros_now_new, R.drawable.square1);
        ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivHungama, R.drawable.hungama_play_icon_new, R.drawable.square1);
        ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivGuru, R.drawable.viu7, R.drawable.square1);
       // ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivBongo, R.drawable.viu9, R.drawable.square1);
      //  ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivIflix, R.drawable.iflix_new, R.drawable.square1);
        ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivZee, R.drawable.zee5_new, R.drawable.square1);
       // ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivMusicSafari, R.drawable.viu12, R.drawable.square1);
       // ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivVandana, R.drawable.vindana_new, R.drawable.square1);
        ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivYuppTv, R.drawable.yupptv, R.drawable.square1);
       // ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().ivSpectrum, R.drawable.iv_spectrum, R.drawable.square1);


        // Glide.with(getActivity()).load(R.drawable.alt).into(getBinding().ivAltBalaji);
       /* bitmapAsync = new BitmapAsync(getBinding().ivAltBalaji, homeActivity);
        bitmapAsync.execute(R.drawable.alt);*/

       /* bitmapAsync = new BitmapAsync(getBinding().ivDialogMytv, homeActivity);
        bitmapAsync.execute(R.drawable.viu3);

        bitmapAsync = new BitmapAsync(getBinding().ivDaVinci, homeActivity);
        bitmapAsync.execute(R.drawable.viu4);

        bitmapAsync = new BitmapAsync(getBinding().ivErosNow, homeActivity);
        bitmapAsync.execute(R.drawable.eros_now_new);

        bitmapAsync = new BitmapAsync(getBinding().ivHungama, homeActivity);
        bitmapAsync.execute(R.drawable.hungama_play_icon_new);

        bitmapAsync = new BitmapAsync(getBinding().ivGuru, homeActivity);
        bitmapAsync.execute(R.drawable.viu7);


        bitmapAsync = new BitmapAsync(getBinding().ivHopster, homeActivity);
        bitmapAsync.execute(R.drawable.viu8);

        bitmapAsync = new BitmapAsync(getBinding().ivBongo, homeActivity);
        bitmapAsync.execute(R.drawable.viu9);

        bitmapAsync = new BitmapAsync(getBinding().ivIflix, homeActivity);
        bitmapAsync.execute(R.drawable.iflix_new);

        bitmapAsync = new BitmapAsync(getBinding().ivZee, homeActivity);
        bitmapAsync.execute(R.drawable.zee5_new);

        bitmapAsync = new BitmapAsync(getBinding().ivMusicSafari, homeActivity);
        bitmapAsync.execute(R.drawable.viu12);

        bitmapAsync = new BitmapAsync(getBinding().ivKiki, homeActivity);
        bitmapAsync.execute(R.drawable.kiki_new);

        bitmapAsync = new BitmapAsync(getBinding().ivVandana, homeActivity);
        bitmapAsync.execute(R.drawable.vindana_new);

        bitmapAsync = new BitmapAsync(getBinding().ivViuTube, homeActivity);
        bitmapAsync.execute(R.drawable.viu15);

        bitmapAsync = new BitmapAsync(getBinding().ivYuppTv, homeActivity);
        bitmapAsync.execute(R.drawable.yupptv);
*/

//        getBinding().ivPapare.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_papare, 80, 80));
//        getBinding().ivAltBalaji.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_alt_balaji, 80, 80));
//        getBinding().ivDialogMytv.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_dialog_mytv, 80, 80));
//        getBinding().ivDaVinci.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_da_vinci, 80, 80));
//        getBinding().ivErosNow.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_eros_now, 80, 80));
//        getBinding().ivHungama.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_hungama, 80, 80));
//        getBinding().ivGuru.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_guru, 80, 80));
//        getBinding().ivHopster.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_hopster, 80, 80));
//        getBinding().ivBongo.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_bongo, 80, 80));
//        getBinding().ivIflix.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_iflix, 80, 80));
//        getBinding().ivZee.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_zee, 80, 80));
//        getBinding().ivMusicSafari.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_music_safari, 80, 80));
//        getBinding().ivKiki.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_kiki, 80, 80));
//        getBinding().ivVandana.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_vandana, 80, 80));
//        getBinding().ivViuTube.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.id.iv_viu_tube, 80, 80));

        getBinding().ivPapare.setOnClickListener(this);
        getBinding().ivAltBalaji.setOnClickListener(this);
//        getBinding().ivDialogMytv.setOnClickListener(this);
//        getBinding().ivDaVinci.setOnClickListener(this);
        getBinding().ivErosNow.setOnClickListener(this);
        getBinding().ivHungama.setOnClickListener(this);
        getBinding().ivGuru.setOnClickListener(this);
//        getBinding().ivHopster.setOnClickListener(this);
      //  getBinding().ivBongo.setOnClickListener(this);
     //   getBinding().ivIflix.setOnClickListener(this);
        getBinding().ivZee.setOnClickListener(this);
       // getBinding().ivMusicSafari.setOnClickListener(this);
//        getBinding().ivKiki.setOnClickListener(this);
       // getBinding().ivVandana.setOnClickListener(this);
//        getBinding().ivViuTube.setOnClickListener(this);
        getBinding().ivYuppTv.setOnClickListener(this);
      //  getBinding().ivSpectrum.setOnClickListener(this);
    }


//    private void itemDecoration() {
////        spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
////        getBinding().myRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacing, true));
////        gridLayoutManager = new GridLayoutManager(getBaseActivity(), 3);
////        getBinding().myRecyclerView.setLayoutManager(gridLayoutManager);
//        getBinding().myRecyclerView.setLayoutManager(new GridLayoutManager(homeActivity, 3));
//        getBinding().myRecyclerView.addItemDecoration(itemDecoration);
//        getBinding().myRecyclerView.setItemAnimator(new DefaultItemAnimator());
//    }

    @Override
    public FragmentViuBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentViuBinding.inflate(inflater);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_papare:

                openUrl(AppLevelConstants.PAPARE);

                break;
            case R.id.iv_alt_balaji:

                openUrl(AppLevelConstants.BALAJI);

                break;
            case R.id.iv_eros_now:

                openUrl(AppLevelConstants.EROS_NOW);

                break;
            case R.id.iv_hungama:

                openUrl(AppLevelConstants.HUNGAMA);

                break;
            case R.id.iv_guru:

                openUrl(AppLevelConstants.GURU);

                break;
        /*    case R.id.iv_bongo:

                openUrl(AppLevelConstants.BONGO);

                break;*/
//            case R.id.iv_iflix:
//
//              //  openUrl(AppLevelConstants.IFLIX);
//
//                break;
            case R.id.iv_zee:

                openUrl(AppLevelConstants.ZEE_5);

                break;
      /*      case R.id.iv_music_safari:

                openUrl(AppLevelConstants.MUSIC_SAFARI);

                break;*/

      /*      case R.id.iv_vandana:

                openUrl(AppLevelConstants.VINDANA);

                break;*/

            case R.id.iv_yupp_tv:

                openUrl(AppLevelConstants.YUPP_TV);

                break;
         /*   case  R.id.iv_spectrum:
              //  openUrl(AppLevelConstants.SPECTRUM);
                break;*/
            default:
                break;
        }
    }


    private void openUrl(String url) {
        if (url.startsWith(AppLevelConstants.HTTP)) {
            AppCommonMethods.openUrl(homeActivity, url);
        } else {

//                    Use package name which we want to check
            boolean isAppInstalled = AppCommonMethods.isAppInstalledOrNot(homeActivity, url);

            if (isAppInstalled) {
                //This intent will help you to launch if the package is already installed
                Intent LaunchIntent = homeActivity.getPackageManager().getLaunchIntentForPackage(url);
                startActivity(LaunchIntent);

//                        Log.i("Application is already installed.");
            } else {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppLevelConstants.MARKET_DETAIL_ID + url)));
                } catch (android.content.ActivityNotFoundException anfe) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppLevelConstants.GOOGLE_PLAY + url)));
                }
//                        Log.i("Application is not currently installed.");
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        bitmapAsync = null;
    }

    public void sameClick() {

    }

    private static class BitmapAsync extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final WeakReference<Context> contextWeakReference;

        private BitmapAsync(ImageView imageView, Context context) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            contextWeakReference = new WeakReference<>(context);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {

            int data = params[0];
            Context context = contextWeakReference.get();
            return BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), data, 80, 80);


        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
