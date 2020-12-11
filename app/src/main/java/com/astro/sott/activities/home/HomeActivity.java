package com.astro.sott.activities.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.astro.sott.activities.search.ui.ActivitySearch;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.AppUpdateCallBack;
import com.astro.sott.fragments.moreTab.ui.MoreFragment;
import com.astro.sott.fragments.video.ui.VideoFragment;
import com.astro.sott.fragments.viu.ui.ViuFragment;
import com.astro.sott.thirdParty.appUpdateManager.ApplicationUpdateManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.PrintLogging;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.ActivityHomeBinding;
import com.astro.sott.fragments.home.ui.HomeFragment;
import com.astro.sott.fragments.livetv.ui.LiveTvFragment;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.npaw.youbora.lib6.YouboraLog;

public class HomeActivity extends BaseBindingActivity<ActivityHomeBinding> implements DetailRailClick, AppUpdateCallBack {
    private final String TAG = this.getClass().getSimpleName();
    private TextView toolbarTitle;
    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    private LiveTvFragment liveTvFragment;
    private ViuFragment viuAppsFragment;
    private Fragment moreFragment;
    private Fragment active;
    private FragmentManager fragmentManager;
    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
    private AppUpdateInfo appUpdateInfo;
    private long mLastClickTime = 0;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    switchToHomeFragment();
                    return true;
                case R.id.navigation_live_tv:
                    if (liveTvFragment == null) {
                        liveTvFragment = new LiveTvFragment();
                        fragmentManager.beginTransaction().add(R.id.content_frame, liveTvFragment, "3").hide(liveTvFragment).commitAllowingStateLoss();
                        switchToLiveTvFragment();
                    } else {
                        switchToLiveTvFragment();
                    }
                    return true;
                case R.id.navigation_video:
                    if (videoFragment == null) {
                        videoFragment = new VideoFragment();
                        fragmentManager.beginTransaction().add(R.id.content_frame, videoFragment, "2").hide(videoFragment).commitAllowingStateLoss();
                        switchToVideoFragment();
                    } else {
                        switchToVideoFragment();
                    }
                    return true;
                case R.id.navigation_viu_apps:
                    if (viuAppsFragment == null) {
                        viuAppsFragment = new ViuFragment();
                        fragmentManager.beginTransaction().add(R.id.content_frame, viuAppsFragment, "4").hide(viuAppsFragment).commitAllowingStateLoss();
                        switchToViuFragment();

                    } else {
                        switchToViuFragment();

                    }

                    return true;

                case R.id.navigation_more:
                    if (moreFragment == null) {
                        moreFragment = new MoreFragment();
                        fragmentManager.beginTransaction().add(R.id.content_frame, moreFragment, "5").hide(moreFragment).commitAllowingStateLoss();
                        switchToMoreFragment();
                    } else {
                        switchToMoreFragment();
                    }

                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView navigation;

    @SuppressLint("RestrictedApi")
    private static void removeNavigationShiftMode(BottomNavigationView view) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        menuView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        menuView.buildMenuView();
    }

    @Override
    public ActivityHomeBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityHomeBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar((Toolbar) getBinding().toolbar);

        YouboraLog.setDebugLevel(YouboraLog.Level.VERBOSE);

        ApplicationUpdateManager.getInstance(getApplicationContext()).setAppUpdateCallBack(this);
        // Before starting an update, register a listener for updates.

        ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().registerListener(listener);

        ApplicationUpdateManager.getInstance(getApplicationContext()).isUpdateAvailable();

        createViewModel();

        setClicks();
//        getIFAID();
//        loadNativeAd();
    }

//    private void getIFAID() {
//        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                AdvertisingIdClient.Info idInfo = null;
//                try {
//                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String advertId = null;
//                try {
//                    advertId = idInfo.getId();
//                    Log.e("AD ID", advertId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return advertId;
//            }
//
//            @Override
//            protected void onPostExecute(String advertId) {
//                Log.e("AD ID", advertId);
//                Toast.makeText(HomeActivity.this, advertId, Toast.LENGTH_SHORT).show();
//            }
//        };
//        task.execute();
//    }

//    private void loadNativeAd() {
//        nativeAd = new NativeAd(this, AppConstants.FB_PLACEMENT_ID);
//        nativeAd.setAdListener(new NativeAdListener() {
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//                // Native ad finished downloading all assets
//                Log.e(TAG, "Native ad finished downloading all assets.");
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Native ad failed to load
//                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Native ad is loaded and ready to be displayed
//                Log.e(TAG, "Native ad is loaded and ready to be displayed!");
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(nativeAd);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Native ad clicked
//                Log.e(TAG, "Native ad clicked!");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Native ad impression
//                Log.e(TAG, "Native ad impression logged!");
//            }
//        });
//
//        // Request an ad
//        nativeAd.loadAd();
//    }

//    private void inflateAd(NativeAd nativeAd) {
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//        nativeAdLayout = findViewById(R.id.native_ad_container);
//        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        adView = (LinearLayout) inflater.inflate(R.layout.layout_native_ad, nativeAdLayout, false);
//        nativeAdLayout.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(HomeActivity.this, nativeAd, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAd.registerViewForInteraction(
//                adView,
//                nativeAdMedia,
//                nativeAdIcon,
//                clickableViews);
//    }

    private void setClicks() {
        toolbarTitle = getBinding().toolbar.findViewById(R.id.toolbar_text);

        ImageView searchIcon = getBinding().toolbar.findViewById(R.id.search_icon);
       // ImageView notification_Icon = getBinding().toolbar.findViewById(R.id.notification_icon);
        ImageView appsLaunchIcon = getBinding().toolbar.findViewById(R.id.apps_launch_icon);
        ImageView homeIcon = getBinding().toolbar.findViewById(R.id.home_icon);
//        if(KsPreferenceKey.getInstance(this).getUserActive())
//            notification_Icon.setVisibility(View.VISIBLE);
//        else
//            notification_Icon.setVisibility(View.GONE);

        homeIcon.setOnClickListener(view -> {
            navigation.setSelectedItemId(R.id.navigation_home);
            homeFragment.sameClick();
        });

//        notification_Icon.setOnClickListener(view -> {
//            new ActivityLauncher(HomeActivity.this).notification(HomeActivity.this, NotificationActivity.class);
//        });

        searchIcon.setOnClickListener(view -> new ActivityLauncher(HomeActivity.this).searchActivity(HomeActivity.this, ActivitySearch.class));
        appsLaunchIcon.setOnClickListener(view -> {
            navigation.setSelectedItemId(R.id.navigation_viu_apps);
        });
    }

    private void createViewModel() {
        initialFragment(this);

    }

    private void initialFragment(HomeActivity homeActivity) {
        homeFragment = new HomeFragment();
        active = homeFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, homeFragment, "1").hide(homeFragment).commit();
        fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
        active = homeFragment;
        UIinitialization();
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    private void UIinitialization() {
        navigation = findViewById(R.id.navigation);
        removeNavigationShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void checkSameClick() {
        if (active != null) {
            if (active == homeFragment)
                homeFragment.sameClick();
            else if (active == liveTvFragment)
                liveTvFragment.sameClick();
            else if (active == videoFragment)
                videoFragment.sameClick();
        }
    }

    private void switchToHomeFragment() {
        fragmentManager.beginTransaction().hide(active).show(homeFragment).commitAllowingStateLoss();
        checkSameClick();
        active = homeFragment;
    }

    private void switchToViuFragment() {
        fragmentManager.beginTransaction().hide(active).show(viuAppsFragment).commitAllowingStateLoss();
        active = viuAppsFragment;
    }

    private void switchToLiveTvFragment() {
        fragmentManager.beginTransaction().hide(active).show(liveTvFragment).commitAllowingStateLoss();
        checkSameClick();
        active = liveTvFragment;
    }

    private void switchToVideoFragment() {
        fragmentManager.beginTransaction().hide(active).show(videoFragment).commitAllowingStateLoss();
        checkSameClick();
        active = videoFragment;
    }


    private void switchToMoreFragment() {
        fragmentManager.beginTransaction().hide(active).show(moreFragment).commit();
        active = moreFragment;
    }

    public void setToHome() {
        if (navigation != null)
            navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        setToHome();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Checks that the update is not stalled during 'onResume()'.
        // However, you should execute this check at all entry points into the app.
        ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // If the update is downloaded but not installed,
                // notify the user to complete the update.

                popupSnackbarForCompleteUpdate();


                // When status updates are no longer needed, unregister the listener.
                ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().unregisterListener(listener);
            }
        });
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }

    @Override
    public void getAppUpdateCallBack(AppUpdateInfo appUpdateInfo) {

        this.appUpdateInfo = appUpdateInfo;
        if (appUpdateInfo != null) {

            ApplicationUpdateManager.getInstance(getApplicationContext()).startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE, this, ApplicationUpdateManager.APP_UPDATE_REQUEST_CODE);
        } else {
            PrintLogging.printLog("InApp update", "NoUpdate available");
        }
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(getBinding().mainLayout, getResources().getString(R.string.update_has_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.restart), view -> ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    InstallStateUpdatedListener listener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }
    };
}
