package com.astro.sott.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.ActivityProfileSubscriptionBinding;
import com.astro.sott.fragments.episodeFrament.EpisodeDialogFragment;
import com.astro.sott.fragments.subscription.dialog.DowngradeDialogFragment;
import com.astro.sott.fragments.subscription.dialog.UpgradeDialogFragment;
import com.astro.sott.fragments.subscription.ui.NewSubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.billing.BillingProcessor;
import com.astro.sott.utils.billing.InAppProcessListener;
import com.astro.sott.utils.billing.PurchaseDetailListener;
import com.astro.sott.utils.billing.PurchaseType;
import com.astro.sott.utils.billing.SKUsListListener;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ProfileSubscriptionActivity extends BaseBindingActivity<ActivityProfileSubscriptionBinding> implements CardCLickedCallBack, InAppProcessListener, UpgradeDialogFragment.UpgradeDialogListener, DowngradeDialogFragment.DowngradeDialogListener {
    private BillingProcessor billingProcessor;
    private SubscriptionViewModel subscriptionViewModel;


    @Override
    protected ActivityProfileSubscriptionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityProfileSubscriptionBinding.inflate(inflater);
    }

    String from = "Profile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intializeBilling();
        modelCall();
        FirebaseEventManager.getFirebaseInstance(this).subscribeClicked = false;

        from = getIntent().getStringExtra("from");
        setFragment();
    }

    private void setFragment() {
        NewSubscriptionPacksFragment someFragment = new NewSubscriptionPacksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("productList", new ArrayList<String>());
        bundle.putString("from", from);
        someFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContent, someFragment, "SubscriptionFragment"); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
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
                    try {
                        CleverTapManager.getInstance().charged(this, planName, offerId, offerType, planPrice, "In App Google", "Success", "Content Details Page");
                        FirebaseEventManager.getFirebaseInstance(this).packageEvent(planName, planPrice, FirebaseEventManager.TXN_SUCCESS, UserInfo.getInstance(this).getCpCustomerId());
                    } catch (Exception e) {

                    }
                    Toast.makeText(this, getResources().getString(R.string.subscribed_success), Toast.LENGTH_SHORT).show();
                    if (from.equalsIgnoreCase("Content Detail Page")) {
                        onBackPressed();
                    } else {
                        setFragment();
                    }
                }
            } else {
                try {
                    CleverTapManager.getInstance().charged(this, planName, offerId, offerType, planPrice, "In App Google", "Failure", "Content Details Page");
                } catch (Exception ex) {
                }
                if (from.equalsIgnoreCase("Content Detail Page")) {
                    onBackPressed();
                } else {
                    setFragment();
                }
                Toast.makeText(this, addSubscriptionResponseEvergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (billingProcessor != null) {
            if (billingProcessor.isReady()) {
                billingProcessor.endConnection();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public SkuDetails getSubscriptionDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(ProfileSubscriptionActivity.this, productId);
    }

    private void intializeBilling() {
        billingProcessor = new BillingProcessor(ProfileSubscriptionActivity.this, ProfileSubscriptionActivity.this);
        billingProcessor.initializeBillingProcessor();
    }

    private String planName = "", planPrice = "", offerId = "", offerType = "";

    @Override
    public void onCardClicked(String productId, String serviceType, String activePlan, String name, String price) {

        this.planName = name;
        offerId = productId;
        planPrice = price;
        FirebaseEventManager.getFirebaseInstance(this).packageEvent(name, price, "trx_select", "");
        if (serviceType.equalsIgnoreCase("ppv")) {
            offerType = "TVOD";
            billingProcessor.purchase(ProfileSubscriptionActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
        } else {
            if (billingProcessor != null && billingProcessor.isReady()) {
                billingProcessor.queryPurchases(ProfileSubscriptionActivity.this, new PurchaseDetailListener() {
                    @Override
                    public void response(Purchase purchaseObject) {
                        if (purchaseObject != null) {
                            if (purchaseObject.getSku() != null && purchaseObject.getPurchaseToken() != null) {
                                offerType = "SVOD";
                                billingProcessor.updatePurchase(ProfileSubscriptionActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name(), purchaseObject.getSku(), purchaseObject.getPurchaseToken());
                            }
                        } else {
                            offerType = "SVOD";
                            billingProcessor.purchase(ProfileSubscriptionActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name());
                        }
                    }
                });

            }
        }
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases.get(0).getPurchaseToken() != null) {
                if (!TabsData.getInstance().isDetail())
                    processPurchase(purchases);
            }
        }
    }

    @Override
    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {

    }

    @Override
    public void onBillingError(@Nullable BillingResult error) {

    }

    @Override
    public void onUpgrade() {
        FragmentManager fm = getSupportFragmentManager();
        UpgradeDialogFragment upgradeDialogFragment = UpgradeDialogFragment.newInstance("Detail Page", "");
        upgradeDialogFragment.setEditDialogCallBack(ProfileSubscriptionActivity.this);
        upgradeDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);

    }

    @Override
    public void onDowngrade() {
        FragmentManager fm = getSupportFragmentManager();
        DowngradeDialogFragment downgradeDialogFragment = DowngradeDialogFragment.newInstance("Detail Page", "");
        downgradeDialogFragment.setEditDialogCallBack(ProfileSubscriptionActivity.this);
        downgradeDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);

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
    public void onUpgradeClick() {
        billingProcessor.upgrade();
    }

    @Override
    public void onDowngradeClick() {
        billingProcessor.downgrade();
    }
}