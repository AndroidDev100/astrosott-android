package com.astro.sott.utils.billing;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.SkuDetails;
import com.astro.sott.activities.subscriptionActivity.ui.ProfileSubscriptionActivity;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;
import com.astro.sott.utils.PacksDateLayer;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.gson.JsonArray;
import com.kaltura.client.types.Subscription;

import java.util.ArrayList;
import java.util.List;

public class BuyButtonManager {
    private SubscriptionViewModel subscriptionViewModel;
    private static BuyButtonManager buyButtonManager;
    private Activity activity;
    private AstroBillingProcessor billingProcessor;

    private ArrayList<PackDetail> packDetailList;

    private String[] subscriptionIds;
    private int count = 0;

    public static BuyButtonManager getInstance() {
        if (buyButtonManager == null) {
            buyButtonManager = new BuyButtonManager();
        }
        return buyButtonManager;
    }

    public void getPackages(Activity activity, String from, String fileId, Boolean isPlayable, BuyButtonListener buyButtonListener) {
        this.activity = activity;
        modelCall();
        getSubscriptionActionList(buyButtonListener, from, fileId, isPlayable);
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of((FragmentActivity) activity).get(SubscriptionViewModel.class);
    }

    private void intializeBilling() {

       /* String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiyDBLi/JpQLoxikmVXqxK8M3ZhJNfW2tAdjnGnr7vnDiYOiyk+NomNLqmnLfQwkC+TNWn50A5XmA8FEuZmuqOzKNRQHw2P1Spl27mcZsjXcCFwj2Vy+eso3pPLjG4DfqCmQN2jZo97TW0EhsROdkWflUMepy/d6sD7eNfncA1Z0ECEDuSuOANlMQLJk7Ci5PwUHKYnUAIwbq0fU9LP6O8Ejx5BK6o5K7rtTBttCbknTiZGLo6rB+8RcSB4Z0v3Di+QPyvxjIvfSQXlWhRdyxAs/EZ/F4Hdfn6TB7mLZkKZZwI0xzOObJp2BiesclMi1wHQsNSgQ8pnZ8T52aJczpQIDAQAB";
        billingProcessor = new BillingProcessor(this, tempBase64, this);
        billingProcessor.initialize();
        billingProcessor.loadOwnedPurchasesFromGoogle();*/

        billingProcessor = new AstroBillingProcessor(activity);
        billingProcessor.initializeBillingProcessor();
    }

    private void getSubscriptionActionList(BuyButtonListener buyButtonListener, String from, String fileId, Boolean isPlayable) {
        if (!from.equalsIgnoreCase("Live Event")) {
            subscriptionViewModel.getSubscriptionPackageList(fileId).observe((LifecycleOwner) activity, subscriptionList -> {
                if (subscriptionList != null) {
                    if (subscriptionList.size() > 0) {
                        subscriptionIds = new String[subscriptionList.size()];
                        for (Subscription subscription : subscriptionList) {
                            if (subscription.getId() != null) {
                                subscriptionIds[count] = subscription.getId();
                                count++;
                            }
                        }
                        getProducts(from);
                    }
                }
            });
        } else {
            if (isPlayable) {
                subscriptionViewModel.getSubscriptionPackageList(fileId).observe((LifecycleOwner) activity, subscriptionList -> {
                    if (subscriptionList != null) {
                        if (subscriptionList.size() > 0) {
                            subscriptionIds = new String[subscriptionList.size()];
                            for (Subscription subscription : subscriptionList) {
                                if (subscription.getId() != null) {
                                    subscriptionIds[count] = subscription.getId();
                                    count++;
                                }
                            }
                            getProducts(from);

                            // setPackFragment();
                        }
                    }
                });
            } else {
                subscriptionIds = fileId.split(",");
                if (subscriptionIds != null && subscriptionIds.length > 0)
                    getProducts(from);
                // setPackFragment();
            }
        }


    }


    private void getProducts(String from) {
        if (subscriptionIds != null) {
            JsonArray jsonArray = new JsonArray();
            for (String id : subscriptionIds) {
                jsonArray.add(id);
            }
            subscriptionViewModel.getProductForLogin(UserInfo.getInstance(activity).getAccessToken(), jsonArray, from).observe((LifecycleOwner) activity, evergentCommonResponse -> {
                if (evergentCommonResponse.isStatus()) {
                    if (evergentCommonResponse.getGetProductResponse() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage().size() > 0) {
                        checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage());
                    }
                } else {
                    if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                        EvergentRefreshToken.refreshToken(activity, UserInfo.getInstance(activity).getRefreshToken()).observe((LifecycleOwner) activity, evergentCommonResponse1 -> {
                            if (evergentCommonResponse.isStatus()) {
                                getProducts(from);
                            } else {
                                AppCommonMethods.removeUserPrerences(activity);
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
        List<String> subSkuList = AppCommonMethods.getSubscriptionSKUs(productsResponseMessage, activity);
        List<String> productsSkuList = AppCommonMethods.getProductSKUs(productsResponseMessage, activity);
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

            }
        });
    }
    public SkuDetails getSubscriptionDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(activity, productId);
    }

    public SkuDetails getPurchaseDetail(String productId) {
        return billingProcessor.getLocalSubscriptionSkuDetail(activity, productId);
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
