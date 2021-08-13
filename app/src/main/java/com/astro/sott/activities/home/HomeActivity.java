package com.astro.sott.activities.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.search.ui.ActivitySearch;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.AppUpdateCallBack;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.fragments.home.ui.ViewPagerFragmentAdapter;
import com.astro.sott.fragments.homenewtab.ui.HomeTabNew;
import com.astro.sott.fragments.moreTab.ui.MoreNewFragment;
import com.astro.sott.fragments.sports.ui.SportsFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.ui.NewSubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.fragments.video.ui.VideoFragment;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.billing.BillingProcessor;

import com.astro.sott.utils.billing.InAppProcessListener;
import com.astro.sott.utils.billing.PurchaseDetailListener;
import com.astro.sott.utils.billing.PurchaseType;
import com.astro.sott.utils.billing.SKUsListListener;
import com.astro.sott.utils.billing.SkuDetailsListener;
import com.astro.sott.utils.billing.TransactionDetails;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.NavigationItem;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.ActivityHomeBinding;
import com.astro.sott.fragments.home.ui.HomeFragment;
import com.astro.sott.fragments.livetv.ui.LiveTvFragment;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class HomeActivity extends BaseBindingActivity<ActivityHomeBinding> implements DetailRailClick, InAppProcessListener, CardCLickedCallBack {
    private final String TAG = this.getClass().getSimpleName();
    private TextView toolbarTitle;
    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    private BillingProcessor billingProcessor;
    private boolean notFirstTime = true;
    private LiveTvFragment liveTvFragment;
    private SubscriptionViewModel subscriptionViewModel;
    private Fragment moreFragment;
    private MoreNewFragment moreNewFragment;
    private String oldLang, newLang;
    private Fragment active;
    private FragmentManager fragmentManager;
    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
    private int indicatorWidth;
    /*private AppUpdateInfo appUpdateInfo;*/
    private long mLastClickTime = 0;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    switchToHomeFragment();
                    return true;
                case R.id.navigation_live_tv:
                    if (liveTvFragment == null) {
                        initFrameFragment();
                    } else {
                        switchToLiveTvFragment();
                    }
                    return true;
                /*case R.id.navigation_video:
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
                        viuAppsFragment = new ViuFragmentNew();
                        fragmentManager.beginTransaction().add(R.id.content_frame, viuAppsFragment, "4").hide(viuAppsFragment).commitAllowingStateLoss();
                        switchToViuFragment();

                    } else {
                        switchToViuFragment();

                    }

                    return true;*/

//                case R.id.navigation_more:
//                    if (moreFragment == null) {
//                        moreFragment = new MoreFragment();
//                        fragmentManager.beginTransaction().add(R.id.content_frame, moreFragment, "5").hide(moreFragment).commitAllowingStateLoss();
//                        switchToMoreFragment();
//                    } else {
//                        switchToMoreFragment();
//                    }
//
//                    return true;
                case R.id.navigation_more:
                    if (moreNewFragment == null) {
                        setProfileFragment();
                    } else {
                        switchToNewMoreFragment();
                    }

                    return true;
            }
            return false;
        }
    };

    private void setProfileFragment() {
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "Profile");
        getBinding().tabs.setVisibility(View.GONE);
        getBinding().viewPager.setVisibility(View.GONE);
        getBinding().mainLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.setVisibility(View.GONE);
        getBinding().appbar.setVisibility(View.GONE);
        setMargins(0, 0);
        moreNewFragment = new MoreNewFragment();
        active = moreNewFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, moreNewFragment, "5").hide(moreNewFragment).commit();
        fragmentManager.beginTransaction().hide(active).show(moreNewFragment).commit();
        active = moreNewFragment;


    }

    private void initFrameFragment() {
        setToolBarScroll(0);
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "Live TV");
        NavigationItem.getInstance().setTab("Live TV");
        getBinding().appbar.setVisibility(View.VISIBLE);
        getBinding().tabs.setVisibility(View.GONE);
        getBinding().viewPager.setVisibility(View.GONE);
        getBinding().mainLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.setVisibility(View.VISIBLE);
        getBinding().indicator.setVisibility(View.GONE);
        liveTvFragment = new LiveTvFragment();
        setMargins(150, 110);
        active = liveTvFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, liveTvFragment, "1").hide(liveTvFragment).commit();
        fragmentManager.beginTransaction().hide(active).show(liveTvFragment).commit();
        active = liveTvFragment;
    }

    private void setToolBarScroll(int type) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if (type == 1) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            params.setScrollFlags(0);
        }
    }

    private void setMargins(int marginTop, int marginBottom) {
        try {
            CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, marginTop, 0, marginBottom);
            findViewById(R.id.main_layout).setLayoutParams(layoutParams);
        } catch (Exception e) {

        }
    }


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

    private String fragmentType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldLang = new KsPreferenceKey(HomeActivity.this).getAppLangName();
        if (UserInfo.getInstance(this).isHouseHoldError()){
            UserInfo.getInstance(this).setHouseHoldError(false);
            new ActivityLauncher(this).signupActivity(this, SignUpActivity.class, CleverTapManager.HOME);
        }
        setSupportActionBar((Toolbar) getBinding().toolbar);
        if (getIntent().getStringExtra("fragmentType") != null)
            fragmentType = getIntent().getStringExtra("fragmentType");
        modelCall();
       // ApplicationUpdateManager.getInstance(getApplicationContext()).setAppUpdateCallBack(this);
        // Before starting an update, register a listener for updates.

     //   ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().registerListener(listener);

        //ApplicationUpdateManager.getInstance(getApplicationContext()).isUpdateAvailable();

        createViewModel();
        intializeBilling();
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
    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void setClicks() {
        toolbarTitle = getBinding().toolbar.findViewById(R.id.toolbar_text);
        getBinding().toolbar.setPadding(0, 0, 0, 0);
//        getBinding().toolbar.setContentInsetsAbsolute(0,0);
        ImageView searchIcon = getBinding().toolbar.findViewById(R.id.search_icon);
        // ImageView notification_Icon = getBinding().toolbar.findViewById(R.id.notification_icon);
        ImageView appsLaunchIcon = getBinding().toolbar.findViewById(R.id.apps_launch_icon);
        ImageView homeIcon = getBinding().toolbar.findViewById(R.id.home_icon);
//        if(KsPreferenceKey.getInstance(this).getUserActive())
//            notification_Icon.setVisibility(View.VISIBLE);
//        else
//            notification_Icon.setVisibility(View.GONE);

        homeIcon.setOnClickListener(view -> {
          /*  navigation.setSelectedItemId(R.id.navigation_home);
            homeFragment.sameClick();*/
        });

//        notification_Icon.setOnClickListener(view -> {
//            new ActivityLauncher(HomeActivity.this).notification(HomeActivity.this, NotificationActivity.class);
//        });

        searchIcon.setOnClickListener(view -> new ActivityLauncher(HomeActivity.this).searchActivity(HomeActivity.this, ActivitySearch.class));
        appsLaunchIcon.setOnClickListener(view -> {
            // navigation.setSelectedItemId(R.id.navigation_viu_apps);
        });
    }

    private void createViewModel() {
        if (fragmentType.equalsIgnoreCase("profile")) {
            setProfileFragment();
            UIinitialization();
            navigation.setSelectedItemId(R.id.navigation_more);
            setViewPager();
        } else {
            initialFragment(this);
        }


    }

    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;

    private void setViewPager() {
        getBinding().viewPager.setUserInputEnabled(false);
        //getBinding().viewPager.setOffscreenPageLimit(2);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this, titles);
        getBinding().viewPager.setAdapter(viewPagerFragmentAdapter);
        // attaching tab mediator
        new TabLayoutMediator(getBinding().tabs, getBinding().viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();

//        getBinding().tabs.setupWithViewPager(getBinding().pager);


        getBinding().viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) getBinding().indicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + position) * (indicatorWidth);
                params.leftMargin = (int) translationOffset;
                getBinding().indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    NavigationItem.getInstance().setTab("All");
                    Fragment currentFragment = viewPagerFragmentAdapter.getFragment(position);
                    if (currentFragment != null)
                        ((HomeTabNew) currentFragment).refreshData();
                } else if (position == 1) {
                    NavigationItem.getInstance().setTab("TV Shows");
                    Fragment currentFragment = viewPagerFragmentAdapter.getFragment(position);
                    if (currentFragment != null)
                        ((HomeFragment) currentFragment).refreshData();
                } else if (position == 2) {
                    NavigationItem.getInstance().setTab("Movies");
                    Fragment currentFragment = viewPagerFragmentAdapter.getFragment(position);
                    if (currentFragment != null)
                        ((VideoFragment) currentFragment).refreshData();
                } else if (position == 3) {
                    NavigationItem.getInstance().setTab("Sports");
                    Fragment currentFragment = viewPagerFragmentAdapter.getFragment(position);
                    if (currentFragment != null)
                        ((SportsFragment) currentFragment).refreshData();
                }
                FirebaseEventManager.getFirebaseInstance(HomeActivity.this).trackScreenName(NavigationItem.getInstance().getTab());
                FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Landing Page Navigation", NavigationItem.getInstance().getTab());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    // tab titles
    private String[] titles = new String[]{"All", "TV Shows", "Movies", "Sports"};

    private void initialFragment(HomeActivity homeActivity) {
        setViewPager();
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
        /*if (homeFragment!=null){
            fragmentManager.beginTransaction().hide(active).show(homeFragment).commitAllowingStateLoss();
            checkSameClick();
            active = homeFragment;
        }*/
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "Home");
        setToolBarScroll(1);
        setMargins(150, 0);
        getBinding().appbar.setVisibility(View.VISIBLE);
        getBinding().mainLayout.setVisibility(View.GONE);
        getBinding().tabs.setVisibility(View.VISIBLE);
        getBinding().toolbar.setVisibility(View.VISIBLE);
        getBinding().indicator.setVisibility(View.VISIBLE);
        getBinding().viewPager.setVisibility(View.VISIBLE);

        int pos = getBinding().viewPager.getCurrentItem();

        if (pos == 0) {
            NavigationItem.getInstance().setTab("All");
            Fragment currentFragment = viewPagerFragmentAdapter.getFragment(pos);
            if (currentFragment != null)
                ((HomeTabNew) currentFragment).refreshData();
        } else if (pos == 1) {
            NavigationItem.getInstance().setTab("TV Shows");
            Fragment currentFragment = viewPagerFragmentAdapter.getFragment(pos);
            if (currentFragment != null)
                ((HomeFragment) currentFragment).refreshData();
        } else if (pos == 2) {
            NavigationItem.getInstance().setTab("Movies");
            Fragment currentFragment = viewPagerFragmentAdapter.getFragment(pos);
            if (currentFragment != null)
                ((VideoFragment) currentFragment).refreshData();
        } else if (pos == 3) {
            NavigationItem.getInstance().setTab("Sports");
            Fragment currentFragment = viewPagerFragmentAdapter.getFragment(pos);
            if (currentFragment != null)
                ((SportsFragment) currentFragment).refreshData();
        }
        getBinding().tabs.post(new Runnable() {
            @Override
            public void run() {
                if (getBinding().tabs.getTabCount() > 0) {
                    indicatorWidth = getBinding().tabs.getWidth() / getBinding().tabs.getTabCount();
                }
                //Assign new width
                AppBarLayout.LayoutParams indicatorParams = (AppBarLayout.LayoutParams) getBinding().indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                getBinding().indicator.setLayoutParams(indicatorParams);
            }
        });
    }


    private void switchToLiveTvFragment() {
        setToolBarScroll(0);
        if (liveTvFragment != null)
            liveTvFragment.refreshData();
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "Live TV");
        NavigationItem.getInstance().setTab("Live TV");
        getBinding().appbar.setVisibility(View.VISIBLE);
        getBinding().tabs.setVisibility(View.GONE);
        getBinding().viewPager.setVisibility(View.GONE);
        getBinding().indicator.setVisibility(View.GONE);
        getBinding().mainLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.setVisibility(View.VISIBLE);
        setMargins(150, 110);
        fragmentManager.beginTransaction().hide(active).show(liveTvFragment).commitAllowingStateLoss();
        checkSameClick();
        active = liveTvFragment;
    }

    private void switchToVideoFragment() {
        fragmentManager.beginTransaction().hide(active).show(videoFragment).commitAllowingStateLoss();
        checkSameClick();
        active = videoFragment;
    }


    //    private void switchToMoreFragment() {
//        fragmentManager.beginTransaction().hide(active).show(moreFragment).commit();
//        active = moreFragment;
//    }
    private void switchToNewMoreFragment() {
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "PROFILE");
        getBinding().appbar.setVisibility(View.GONE);
        getBinding().tabs.setVisibility(View.GONE);
        getBinding().viewPager.setVisibility(View.GONE);
        setMargins(0, 0);
        getBinding().toolbar.setVisibility(View.GONE);
        getBinding().mainLayout.setVisibility(View.VISIBLE);
        // getBinding().appbar.setVisibility(View.GONE);
        fragmentManager.beginTransaction().hide(active).show(moreNewFragment).commit();
        active = moreNewFragment;


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
        updateLang();

        // Checks that the update is not stalled during 'onResume()'.
        // However, you should execute this check at all entry points into the app.
       /* ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // If the update is downloaded but not installed,
                // notify the user to complete the update.

                popupSnackbarForCompleteUpdate();


                // When status updates are no longer needed, unregister the listener.
                ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().unregisterListener(listener);
            }
        });*/
    }

    private void updateLang() {
        newLang = new KsPreferenceKey(HomeActivity.this).getAppLangName();
        if (!oldLang.equals(newLang)) {
            oldLang = newLang;
            if (newLang.equalsIgnoreCase("ms")) {
                navigation.getMenu().getItem(0).setTitle("Rumah");
                navigation.getMenu().getItem(1).setTitle("Siaran langsung TV");
                navigation.getMenu().getItem(2).setTitle("Profil");
            } else {
                navigation.getMenu().getItem(0).setTitle("Home");
                navigation.getMenu().getItem(1).setTitle("Live TV");
                navigation.getMenu().getItem(2).setTitle("Profile");
            }


        }
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }

   /* @Override
    public void getAppUpdateCallBack(AppUpdateInfo appUpdateInfo) {

        this.appUpdateInfo = appUpdateInfo;
        if (appUpdateInfo != null) {

            ApplicationUpdateManager.getInstance(getApplicationContext()).startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE, this, ApplicationUpdateManager.APP_UPDATE_REQUEST_CODE);
        } else {
            PrintLogging.printLog("InApp update", "NoUpdate available");
        }
    }*/

    /* Displays the snackbar notification and call to action. */
   /* private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(getBinding().mainLayout, getResources().getString(R.string.update_has_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.restart), view -> ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }*/

    /*InstallStateUpdatedListener listener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }
    };*/

    private void intializeBilling() {
        billingProcessor = new BillingProcessor(HomeActivity.this, HomeActivity.this);
        billingProcessor.initializeBillingProcessor();
        // stopProcessor();
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    public void stopProcessor() {
        if (billingProcessor != null) {
            if (billingProcessor.isReady()) {
                billingProcessor.endConnection();
            }
        }
    }

    @Override
    public void onCardClicked(String productId, String serviceType, String active,String name,String price) {
       /* if (serviceType.equalsIgnoreCase("ppv")) {
            billingProcessor.purchase(HomeActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
        } else {
            if (billingProcessor != null && billingProcessor.isReady()) {
                billingProcessor.queryPurchases(HomeActivity.this, new PurchaseDetailListener() {
                    @Override
                    public void response(Purchase purchaseObject) {
                        if (purchaseObject != null) {
                            if (purchaseObject.getSku() != null && purchaseObject.getPurchaseToken() != null) {
                                billingProcessor.updatePurchase(HomeActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name(), purchaseObject.getSku(), purchaseObject.getPurchaseToken());
                            }
                        } else {
                            billingProcessor.purchase(HomeActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name());
                        }
                    }
                });

            }
        }*/
    }

    public SkuDetails getSubscriptionDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(HomeActivity.this, productId);
    }


    @Override
    public void onBillingInitialized() {
        billingProcessor.queryPurchases(this);

    }

    private long lastClickTime = 0;

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
       /* if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases.get(0).getPurchaseToken() != null) {
                if (SystemClock.elapsedRealtimeNanos() - lastClickTime < 8000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (!TabsData.getInstance().isDetail())
                    processPurchase(purchases);
            }
        }*/
    }

    private void processPurchase(List<Purchase> purchases) {
        try {
            for (Purchase purchase : purchases) {
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    handlePurchase(purchase);
                } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                    //  PrintLogging.printLog("PurchaseActivity", "Received a pending purchase of SKU: " + purchase.getSku());
                    // handle pending purchases, e.g. confirm with users about the pending
                    // purchases, prompt them to complete it, etc.
                    // TODO: 8/24/2020 handle this in the next release.
                }
            }
        } catch (Exception ignored) {

        }

    }

    private void handlePurchase(Purchase purchase) {

        String orderId;
        Log.w("billingProcessor_play", UserInfo.getInstance(this).getAccessToken() + "------" + purchase);
        if (purchase.getOrderId() != null) {
            orderId = purchase.getOrderId();
        } else {
            orderId = "";
        }

        subscriptionViewModel.addSubscription(UserInfo.getInstance(this).getAccessToken(), purchase.getSku(), purchase.getPurchaseToken(), orderId).observe(this, addSubscriptionResponseEvergentCommonResponse -> {
            if (addSubscriptionResponseEvergentCommonResponse.isStatus()) {
                if (addSubscriptionResponseEvergentCommonResponse.getResponse().getAddSubscriptionResponseMessage().getMessage() != null) {
                    Toast.makeText(this, getResources().getString(R.string.subscribed_success), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, addSubscriptionResponseEvergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void refreshFragment() {
        NewSubscriptionPacksFragment newSubscriptionFragment = (NewSubscriptionPacksFragment) getSupportFragmentManager().findFragmentByTag("SubscriptionFragment");
        if (newSubscriptionFragment != null && newSubscriptionFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(newSubscriptionFragment);
            fragmentTransaction.attach(newSubscriptionFragment);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {
        // SubscriptionPacksFragment.dataFeched(purchases);
    }

    @Override
    public void onBillingError(@Nullable BillingResult error) {

    }

    @Override
    public void onUpgrade() {

    }

    @Override
    public void onDowngrade() {

    }

    public void onListOfSKUs(List<String> subSkuList, List<String> productsSkuList, SKUsListListener callBacks) {
        if (billingProcessor != null && billingProcessor.isReady()) {
            billingProcessor.getAllSkuDetails(subSkuList, productsSkuList, new SKUsListListener() {
                @Override
                public void onListOfSKU(@Nullable List<SkuDetails> purchases) {
                    Log.w("callbackCalled", purchases.size() + "");
                    callBacks.onListOfSKU(purchases);
                }
            });
        }
    }

}
