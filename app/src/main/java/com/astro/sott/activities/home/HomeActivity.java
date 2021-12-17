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
import com.astro.sott.utils.helpers.ToastHandler;
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
    //    private AppUpdateInfo appUpdateInfo;
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
        liveTvFragment = new LiveTvFragment();
        active = liveTvFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, liveTvFragment, "1").hide(liveTvFragment).commit();
        fragmentManager.beginTransaction().hide(active).show(liveTvFragment).commit();
        setToolBarScroll(0);
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "Live TV");
        NavigationItem.getInstance().setTab("Live TV");
        getBinding().appbar.setVisibility(View.VISIBLE);
        getBinding().tabs.setVisibility(View.GONE);
        getBinding().viewPager.setVisibility(View.GONE);
        getBinding().mainLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.setVisibility(View.VISIBLE);
        getBinding().indicator.setVisibility(View.GONE);

        setMargins(24, 110);

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

    @SuppressLint({"RestrictedApi", "WrongConstant"})
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
        if (UserInfo.getInstance(this).isHouseHoldError()) {
            UserInfo.getInstance(this).setHouseHoldError(false);
            new ActivityLauncher(this).signupActivity(this, SignUpActivity.class, CleverTapManager.HOME);
        }
        setSupportActionBar((Toolbar) getBinding().toolbar);
        if (getIntent().getStringExtra("fragmentType") != null)
            fragmentType = getIntent().getStringExtra("fragmentType");
        modelCall();
        createViewModel();
        intializeBilling();
        setClicks();
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void setClicks() {
        toolbarTitle = getBinding().toolbar.findViewById(R.id.toolbar_text);
        getBinding().toolbar.setPadding(0, 0, 0, 0);
        ImageView searchIcon = getBinding().toolbar.findViewById(R.id.search_icon);
        ImageView appsLaunchIcon = getBinding().toolbar.findViewById(R.id.apps_launch_icon);
        ImageView homeIcon = getBinding().toolbar.findViewById(R.id.home_icon);

        homeIcon.setOnClickListener(view -> {

        });

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
        fragmentManager.beginTransaction().hide(active).show(liveTvFragment).commitAllowingStateLoss();
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
        setMargins(24, 110);

        checkSameClick();
        active = liveTvFragment;
    }

    private void switchToVideoFragment() {
        fragmentManager.beginTransaction().hide(active).show(videoFragment).commitAllowingStateLoss();
        checkSameClick();
        active = videoFragment;
    }

    private void switchToNewMoreFragment() {
        FirebaseEventManager.getFirebaseInstance(HomeActivity.this).navEvent("Navigation", "PROFILE");
        getBinding().appbar.setVisibility(View.GONE);
        getBinding().tabs.setVisibility(View.GONE);
        getBinding().viewPager.setVisibility(View.GONE);
        setMargins(0, 0);
        getBinding().toolbar.setVisibility(View.GONE);
        getBinding().mainLayout.setVisibility(View.VISIBLE);
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLang();
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


    private void intializeBilling() {
        billingProcessor = new BillingProcessor(HomeActivity.this, HomeActivity.this);
        billingProcessor.initializeBillingProcessor();
        // stopProcessor();
    }

    public void stopProcessor() {
        if (billingProcessor != null) {
            if (billingProcessor.isReady()) {
                billingProcessor.endConnection();
            }
        }
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

        subscriptionViewModel.addSubscription(UserInfo.getInstance(this).getAccessToken(), purchase.getSku(), purchase.getPurchaseToken(), orderId,"").observe(this, addSubscriptionResponseEvergentCommonResponse -> {
            if (addSubscriptionResponseEvergentCommonResponse.isStatus()) {
                if (addSubscriptionResponseEvergentCommonResponse.getResponse().getAddSubscriptionResponseMessage().getMessage() != null) {
                    ToastHandler.show(getResources().getString(R.string.subscribed_success) + "", HomeActivity.this);

                }
            } else {
                ToastHandler.show(addSubscriptionResponseEvergentCommonResponse.getErrorMessage() + "",
                        HomeActivity.this);
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

    @Override
    public void onAcknowledged(String productId, String purchaseToken, String orderId) {
        subscriptionViewModel.addSubscription(UserInfo.getInstance(this).getAccessToken(), productId, purchaseToken, orderId,"").observe(this, addSubscriptionResponseEvergentCommonResponse -> {
            if (addSubscriptionResponseEvergentCommonResponse.isStatus()) {
                if (addSubscriptionResponseEvergentCommonResponse.getResponse().getAddSubscriptionResponseMessage().getMessage() != null) {
                }
            } else {

            }
        });
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

    @Override
    public void onCardClicked(String productId, String serviceType, String activePlan, String name, Long price) {

    }
}
