package com.astro.sott.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
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
import com.astro.sott.fragments.subscription.dialog.DowngradeDialogFragment;
import com.astro.sott.fragments.subscription.dialog.UpgradeDialogFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;
import com.astro.sott.utils.PacksDateLayer;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.billing.BillingProcessor;
import com.astro.sott.utils.billing.InAppProcessListener;
import com.astro.sott.utils.billing.PurchaseDetailListener;
import com.astro.sott.utils.billing.PurchaseType;
import com.astro.sott.utils.billing.SKUsListListener;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.gson.JsonArray;
import com.kaltura.client.types.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionDetailActivity extends BaseBindingActivity<ActivitySubscriptionDetailBinding> implements CardCLickedCallBack, InAppProcessListener, UpgradeDialogFragment.UpgradeDialogListener, DowngradeDialogFragment.DowngradeDialogListener {
    private BillingProcessor billingProcessor;
    private SubscriptionViewModel subscriptionViewModel;
    private SkuDetails skuDetails;
    private ArrayList<PackDetail> packDetailList;
    private boolean haveSvod = false, haveTvod = false;

    String fileId = "";
    boolean isPlayable;
    String from = "", date = "";

    @Override
    protected ActivitySubscriptionDetailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySubscriptionDetailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabsData.getInstance().setDetail(true);
        intializeBilling();

        if (getIntent().getStringExtra(AppLevelConstants.FILE_ID_KEY) != null)
            fileId = getIntent().getStringExtra(AppLevelConstants.FILE_ID_KEY);

        if (getIntent().getStringExtra(AppLevelConstants.FROM_KEY) != null)
            from = getIntent().getStringExtra(AppLevelConstants.FROM_KEY);

        if (getIntent().getStringExtra(AppLevelConstants.DATE) != null)
            date = getIntent().getStringExtra(AppLevelConstants.DATE);

        isPlayable = getIntent().getBooleanExtra(AppLevelConstants.PLAYABLE, false);

        modelCall();
        getSubscriptionActionList();
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private String[] subscriptionIds;
    private int count = 0;

    private void getSubscriptionActionList() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (!from.equalsIgnoreCase("Live Event")) {
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
                        getProducts();
                        // setPackFragment();
                    }
                }
            });
        } else {
            if (isPlayable) {
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
                            getProducts();

                            // setPackFragment();
                        }
                    }
                });
            } else {
                subscriptionIds = fileId.split(",");
                if (subscriptionIds != null && subscriptionIds.length > 0)
                    getProducts();
                // setPackFragment();
            }
        }


    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    private void setPackFragment() {
        getBinding().frameContent.setVisibility(View.VISIBLE);
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

    private void getProducts() {
        if (subscriptionIds != null) {
            JsonArray jsonArray = new JsonArray();
            for (String id : subscriptionIds) {
                jsonArray.add(id);
            }
            subscriptionViewModel.getProductForLogin(UserInfo.getInstance(this).getAccessToken(), jsonArray, from).observe(this, evergentCommonResponse -> {
                if (evergentCommonResponse.isStatus()) {
                    if (evergentCommonResponse.getGetProductResponse() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage().size() > 0) {
                        checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage());
                    }
                } else {
                    if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                        EvergentRefreshToken.refreshToken(this, UserInfo.getInstance(this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                            if (evergentCommonResponse.isStatus()) {
                                getProducts();
                            } else {
                                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                                AppCommonMethods.removeUserPrerences(this);
                            }
                        });
                    } else {

                    }
                }
            });
        }
    }

    private void checkIfDetailAvailableOnPlaystore(List<ProductsResponseMessageItem> productsResponseMessage) {
        packDetailList = new ArrayList<>();
        List<String> subSkuList = AppCommonMethods.getSubscriptionSKUs(productsResponseMessage, this);
        List<String> productsSkuList = AppCommonMethods.getProductSKUs(productsResponseMessage, this);
        onListOfSKUs(subSkuList, productsSkuList, new SKUsListListener() {
            @Override
            public void onListOfSKU(@Nullable List<SkuDetails> purchases) {
                // Log.w("valuessAdded--->>",purchases.size()+"");
                // Log.w("valuessAdded--->>",purchases.get(0).getDescription());

                for (ProductsResponseMessageItem responseMessageItem : productsResponseMessage) {
                    if (responseMessageItem.getAppChannels() != null && responseMessageItem.getAppChannels().get(0) != null && responseMessageItem.getAppChannels().get(0).getAppChannel() != null && responseMessageItem.getAppChannels().get(0).getAppChannel().equalsIgnoreCase("Google Wallet") && responseMessageItem.getAppChannels().get(0).getAppID() != null) {
                        if (responseMessageItem.getServiceType().equalsIgnoreCase("ppv")) {
                            skuDetails = getPurchaseDetail(responseMessageItem.getAppChannels().get(0).getAppID());
                            haveTvod = true;
                        } else {
                            skuDetails = getSubscriptionDetail(responseMessageItem.getAppChannels().get(0).getAppID());
                            haveSvod = true;
                        }
                        if (skuDetails != null) {
                            PackDetail packDetail = new PackDetail();
                            packDetail.setSkuDetails(skuDetails);
                            packDetail.setProductsResponseMessageItem(responseMessageItem);
                            packDetailList.add(packDetail);
                        }
                    }
                }

                if (packDetailList.size() > 0) {
                    if (packDetailList.size() == 1) {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        onCardClicked(packDetailList.get(0).getProductsResponseMessageItem().getAppChannels().get(0).getAppID(), packDetailList.get(0).getProductsResponseMessageItem().getServiceType(), null, packDetailList.get(0).getProductsResponseMessageItem().getDisplayName(), packDetailList.get(0).getSkuDetails().getPrice());
                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        if (haveSvod && haveTvod == false) {
                            PacksDateLayer.getInstance().setPackDetailList(packDetailList);
                            Intent intent = new Intent(SubscriptionDetailActivity.this, ProfileSubscriptionActivity.class);
                            intent.putExtra("from", "Content Detail Page");
                            startActivity(intent);
                            finish();
                        } else {
                            setPackFragment();
                        }

                    }

                }

            }
        });
    }


    private void intializeBilling() {

       /* String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiyDBLi/JpQLoxikmVXqxK8M3ZhJNfW2tAdjnGnr7vnDiYOiyk+NomNLqmnLfQwkC+TNWn50A5XmA8FEuZmuqOzKNRQHw2P1Spl27mcZsjXcCFwj2Vy+eso3pPLjG4DfqCmQN2jZo97TW0EhsROdkWflUMepy/d6sD7eNfncA1Z0ECEDuSuOANlMQLJk7Ci5PwUHKYnUAIwbq0fU9LP6O8Ejx5BK6o5K7rtTBttCbknTiZGLo6rB+8RcSB4Z0v3Di+QPyvxjIvfSQXlWhRdyxAs/EZ/F4Hdfn6TB7mLZkKZZwI0xzOObJp2BiesclMi1wHQsNSgQ8pnZ8T52aJczpQIDAQAB";
        billingProcessor = new BillingProcessor(this, tempBase64, this);
        billingProcessor.initialize();
        billingProcessor.loadOwnedPurchasesFromGoogle();*/

        billingProcessor = new BillingProcessor(SubscriptionDetailActivity.this, SubscriptionDetailActivity.this);
        billingProcessor.initializeBillingProcessor();
    }

    public SkuDetails getSubscriptionDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(SubscriptionDetailActivity.this, productId);
    }

    public SkuDetails getPurchaseDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(SubscriptionDetailActivity.this, productId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabsData.getInstance().setDetail(false);
    }

    private String planName = "", planPrice = "", offerId = "", offerType = "";

    @Override
    public void onCardClicked(String productId, String serviceType, String active, String planName, String price) {
        this.planName = planName;
        offerId = productId;
        planPrice = price;
        FirebaseEventManager.getFirebaseInstance(this).packageEvent(planName, price, "trx_select", "");
        if (serviceType.equalsIgnoreCase("ppv")) {
            offerType = "TVOD";
            billingProcessor.purchase(SubscriptionDetailActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
        } else {
            if (billingProcessor != null && billingProcessor.isReady()) {
                billingProcessor.queryPurchases(SubscriptionDetailActivity.this, new PurchaseDetailListener() {
                    @Override
                    public void response(Purchase purchaseObject) {
                        if (purchaseObject != null) {
                            if (purchaseObject.getSku() != null && purchaseObject.getPurchaseToken() != null) {
                                offerType = "SVOD";
                                billingProcessor.updatePurchase(SubscriptionDetailActivity.this, productId, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name(), purchaseObject.getSku(), purchaseObject.getPurchaseToken());
                            }
                        } else {
                            offerType = "SVOD";
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

    private long lastClickTime = 0;

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases.get(0).getPurchaseToken() != null) {
                Log.w("elapseTiming", SystemClock.elapsedRealtime() + "----" + lastClickTime);
                if (SystemClock.elapsedRealtime() - lastClickTime < 7000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (TabsData.getInstance().isDetail())
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
        try {
            //  FirebaseEventManager.getFirebaseInstance(this).packageEvent();
        } catch (Exception e) {

        }
        subscriptionViewModel.addSubscription(UserInfo.getInstance(this).getAccessToken(), purchase.getSku(), purchase.getPurchaseToken(), orderId).observe(this, addSubscriptionResponseEvergentCommonResponse -> {
            if (addSubscriptionResponseEvergentCommonResponse.isStatus()) {
                if (addSubscriptionResponseEvergentCommonResponse.getResponse().getAddSubscriptionResponseMessage().getMessage() != null) {
                    Toast.makeText(this, getResources().getString(R.string.subscribed_success), Toast.LENGTH_SHORT).show();
                    try {
                        CleverTapManager.getInstance().charged(this, planName, offerId, offerType, planPrice, "In App Google", "Success", "Content Details Page");
                        FirebaseEventManager.getFirebaseInstance(this).packageEvent(planName, planPrice, FirebaseEventManager.TXN_SUCCESS, UserInfo.getInstance(this).getCpCustomerId());
                    } catch (Exception e) {
                        onBackPressed();
                    }
                    onBackPressed();
                } else {
                    onBackPressed();

                }

            } else {
                try {
                    CleverTapManager.getInstance().charged(this, planName, offerId, offerType, planPrice, "In App Google", "Failure", "Content Details Page");
                } catch (Exception ex) {
                    onBackPressed();
                }
                Toast.makeText(this, addSubscriptionResponseEvergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        });

    }


    @Override
    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {
        // SubscriptionPacksFragment.dataFeched(purchases);
    }

    @Override
    public void onBillingError(@Nullable BillingResult error) {
        onBackPressed();
    }

    @Override
    public void onUpgrade() {
        FragmentManager fm = getSupportFragmentManager();
        UpgradeDialogFragment upgradeDialogFragment = UpgradeDialogFragment.newInstance("Detail Page", "");
        upgradeDialogFragment.setEditDialogCallBack(SubscriptionDetailActivity.this);
        upgradeDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onDowngrade() {
        FragmentManager fm = getSupportFragmentManager();
        DowngradeDialogFragment downgradeDialogFragment = DowngradeDialogFragment.newInstance("Detail Page", "");
        downgradeDialogFragment.setEditDialogCallBack(SubscriptionDetailActivity.this);
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
    protected void onStop() {
        super.onStop();
        if (billingProcessor != null) {
            if (billingProcessor.isReady()) {
                billingProcessor.endConnection();
            }
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