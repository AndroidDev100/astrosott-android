package com.astro.sott.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.ActivitySubscriptionDetailBinding;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.utils.billing.BillingProcessor;
import com.astro.sott.utils.billing.InAppProcessListener;
import com.astro.sott.utils.billing.PurchaseDetailListener;
import com.astro.sott.utils.billing.PurchaseType;
import com.astro.sott.utils.billing.SKUsListListener;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;
import com.kaltura.client.types.Subscription;

import java.util.List;

public class SubscriptionDetailActivity extends BaseBindingActivity<ActivitySubscriptionDetailBinding> implements CardCLickedCallBack, InAppProcessListener {
    private BillingProcessor billingProcessor;
    private SubscriptionViewModel subscriptionViewModel;

    String fileId = "";
    String from = "", date = "";

    @Override
    protected ActivitySubscriptionDetailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySubscriptionDetailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intializeBilling();
        if (getIntent().getStringExtra(AppLevelConstants.FILE_ID_KEY) != null)
            fileId = getIntent().getStringExtra(AppLevelConstants.FILE_ID_KEY);

        if (getIntent().getStringExtra(AppLevelConstants.FROM_KEY) != null)
            from = getIntent().getStringExtra(AppLevelConstants.FROM_KEY);

        if (getIntent().getStringExtra(AppLevelConstants.DATE) != null)
            date = getIntent().getStringExtra(AppLevelConstants.DATE);

        modelCall();
        getSubscriptionActionList();
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private String[] subscriptionIds;
    private int count = 0;

    private void getSubscriptionActionList() {

        subscriptionViewModel.getSubscriptionPackageList(fileId).observe(this, subscriptionList -> {
            if (subscriptionList != null) {
                if (subscriptionList.size() > 0) {
                    subscriptionIds = new String[subscriptionList.size()];
                    for (Subscription subscription : subscriptionList) {
                        if (subscription.getId() != null) {
                            subscriptionIds[count] = subscription.getId();
                            count++;
                        }
                    }
                    setPackFragment();
                }
            }
        });


    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    private void setPackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SubscriptionPacksFragment subscriptionPacksFragment = new SubscriptionPacksFragment();
        Bundle bundle = new Bundle();
        if (!from.equalsIgnoreCase("")) {
            bundle.putString(AppLevelConstants.FROM_KEY, from);

        } else {
            bundle.putString(AppLevelConstants.FROM_KEY, "detail");
        }
        bundle.putString(AppLevelConstants.DATE, date);
        bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
        subscriptionPacksFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameContent, subscriptionPacksFragment).commitNow();
    }

    private void intializeBilling() {

       /* String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiyDBLi/JpQLoxikmVXqxK8M3ZhJNfW2tAdjnGnr7vnDiYOiyk+NomNLqmnLfQwkC+TNWn50A5XmA8FEuZmuqOzKNRQHw2P1Spl27mcZsjXcCFwj2Vy+eso3pPLjG4DfqCmQN2jZo97TW0EhsROdkWflUMepy/d6sD7eNfncA1Z0ECEDuSuOANlMQLJk7Ci5PwUHKYnUAIwbq0fU9LP6O8Ejx5BK6o5K7rtTBttCbknTiZGLo6rB+8RcSB4Z0v3Di+QPyvxjIvfSQXlWhRdyxAs/EZ/F4Hdfn6TB7mLZkKZZwI0xzOObJp2BiesclMi1wHQsNSgQ8pnZ8T52aJczpQIDAQAB";
        billingProcessor = new BillingProcessor(this, tempBase64, this);
        billingProcessor.initialize();
        billingProcessor.loadOwnedPurchasesFromGoogle();*/

        billingProcessor = new BillingProcessor(SubscriptionDetailActivity.this, this);
        billingProcessor.initializeBillingProcessor();
    }

    public SkuDetails getSubscriptionDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(SubscriptionDetailActivity.this, productId);
    }

    public SkuDetails getPurchaseDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(SubscriptionDetailActivity.this, productId);
    }


    @Override
    public void onCardClicked(String productId, String serviceType, String active) {
        if (serviceType.equalsIgnoreCase("ppv")) {
            billingProcessor.purchase(SubscriptionDetailActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
        } else {
            if (billingProcessor != null && billingProcessor.isReady()) {
                billingProcessor.queryPurchases(SubscriptionDetailActivity.this, new PurchaseDetailListener() {
                    @Override
                    public void response(Purchase purchaseObject) {
                        if (purchaseObject != null) {
                            if (purchaseObject.getSku() != null && purchaseObject.getPurchaseToken() != null) {
                                billingProcessor.updatePurchase(SubscriptionDetailActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name(), purchaseObject.getSku(), purchaseObject.getPurchaseToken());
                            }
                        } else {
                            billingProcessor.purchase(SubscriptionDetailActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name());
                        }
                    }
                });

            }
        }
    }

    @Override
    public void onBillingInitialized() {
        Log.w("billingProcessor_play", "intialized");

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases.get(0).getPurchaseToken() != null) {
                processPurchase(purchases);
            }
        }
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

    private boolean isAddSubscriptionCalled = false;

    private void handlePurchase(Purchase purchase) {

        String orderId;
        Log.w("billingProcessor_play", UserInfo.getInstance(this).getAccessToken() + "------" + purchase);
        if (purchase.getOrderId() != null) {
            orderId = purchase.getOrderId();
        } else {
            orderId = "";
        }

        if (!isAddSubscriptionCalled) {
            isAddSubscriptionCalled = true;
            subscriptionViewModel.addSubscription(UserInfo.getInstance(this).getAccessToken(), purchase.getSku(), purchase.getPurchaseToken(), orderId).observe(this, addSubscriptionResponseEvergentCommonResponse -> {
                isAddSubscriptionCalled = false;
                if (addSubscriptionResponseEvergentCommonResponse.isStatus()) {
                    if (addSubscriptionResponseEvergentCommonResponse.getResponse().getAddSubscriptionResponseMessage().getMessage() != null) {
                        Toast.makeText(this, getResources().getString(R.string.subscribed_success), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        onBackPressed();

                    }

                } else {
                    Toast.makeText(this, addSubscriptionResponseEvergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
            });
        }
    }


    @Override
    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {
        // SubscriptionPacksFragment.dataFeched(purchases);
    }

    @Override
    public void onBillingError(@Nullable BillingResult error) {

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