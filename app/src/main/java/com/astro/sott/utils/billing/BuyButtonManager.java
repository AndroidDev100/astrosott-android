package com.astro.sott.utils.billing;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.SkuDetails;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
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
    private SkuDetails skuDetails;
    public static String TVOD = "TVOD", SVOD = "SVOD", SVOD_TVOD = "SVOD+TVOD";
    private boolean haveSvod = false, haveTvod = false;
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
        intializeBilling();
        count=0;
        getSubscriptionActionList(buyButtonListener, from, fileId, isPlayable);
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of((FragmentActivity) activity).get(SubscriptionViewModel.class);
    }

    private void intializeBilling() {
        billingProcessor = new AstroBillingProcessor(activity);
        billingProcessor.initializeBillingProcessor();
    }

    private void getSubscriptionActionList(BuyButtonListener buyButtonListener, String from, String fileId, Boolean isPlayable) {
        if (!from.equalsIgnoreCase(AppLevelConstants.LIVE_EVENT)) {
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
                        getProducts(from, buyButtonListener);
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
                            getProducts(from, buyButtonListener);
                        }
                    }
                });
            } else {
                subscriptionIds = fileId.split(",");
                if (subscriptionIds != null && subscriptionIds.length > 0)
                    getProducts(from, buyButtonListener);
            }
        }


    }


    private void getProducts(String from, BuyButtonListener buyButtonListener) {
        if (subscriptionIds != null) {
            JsonArray jsonArray = new JsonArray();
            for (String id : subscriptionIds) {
                jsonArray.add(id);
            }
            subscriptionViewModel.getProductForLogin(UserInfo.getInstance(activity).getAccessToken(), jsonArray, from).observe((LifecycleOwner) activity, evergentCommonResponse -> {
                if (evergentCommonResponse.isStatus()) {
                    if (evergentCommonResponse.getGetProductResponse() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage().size() > 0) {
                        checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage(), buyButtonListener);
                    }
                } else {
                    if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                        EvergentRefreshToken.refreshToken(activity, UserInfo.getInstance(activity).getRefreshToken()).observe((LifecycleOwner) activity, evergentCommonResponse1 -> {
                            if (evergentCommonResponse.isStatus()) {
                                getProducts(from, buyButtonListener);
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

    private void checkIfDetailAvailableOnPlaystore(List<ProductsResponseMessageItem> productsResponseMessage, BuyButtonListener buyButtonListener) {
        packDetailList = new ArrayList<>();
        List<String> subSkuList = AppCommonMethods.getSubscriptionSKUs(productsResponseMessage, activity);
        List<String> productsSkuList = AppCommonMethods.getProductSKUs(productsResponseMessage, activity);
        onListOfSKUs(subSkuList, productsSkuList, new SKUsListListener() {
            @Override
            public void onListOfSKU(@Nullable List<SkuDetails> purchases) {
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
                        if (haveTvod) {
                            buyButtonListener.onPackagesAvailable(packDetailList, TVOD, packDetailList.get(0).getSkuDetails().getPrice(), subscriptionIds);
                        } else {
                            buyButtonListener.onPackagesAvailable(packDetailList, SVOD, packDetailList.get(0).getSkuDetails().getPrice(), subscriptionIds);
                        }
                    } else {
                        if (haveTvod && haveSvod) {
                            buyButtonListener.onPackagesAvailable(packDetailList, SVOD_TVOD, getLowestPrice(packDetailList), subscriptionIds);
                        } else if (haveSvod) {
                            buyButtonListener.onPackagesAvailable(packDetailList, SVOD, getLowestPrice(packDetailList), subscriptionIds);
                        } else {
                            buyButtonListener.onPackagesAvailable(packDetailList, TVOD, getLowestPrice(packDetailList), subscriptionIds);
                        }
                    }
                } else {

                }
            }
        });
    }

    private String getLowestPrice(ArrayList<PackDetail> packDetailList) {
        String lowestPrice = "";
        double comparedPrice = packDetailList.get(0).getSkuDetails().getOriginalPriceAmountMicros();
        for (PackDetail packDetail : packDetailList) {
            if (comparedPrice > packDetail.getSkuDetails().getIntroductoryPriceAmountMicros()) {
                comparedPrice = packDetail.getSkuDetails().getOriginalPriceAmountMicros();
                lowestPrice = packDetail.getSkuDetails().getPrice();
            }
        }
        return lowestPrice;
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
