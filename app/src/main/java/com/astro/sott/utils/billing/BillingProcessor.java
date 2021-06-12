/**
 * Copyright 2014 AnjLab
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.astro.sott.utils.billing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.billingclient.api.BillingFlowParams.ProrationMode.DEFERRED;
import static com.android.billingclient.api.BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_PRORATED_PRICE;
import static com.android.billingclient.api.BillingFlowParams.ProrationMode.IMMEDIATE_WITHOUT_PRORATION;
import static com.android.billingclient.api.BillingFlowParams.ProrationMode.IMMEDIATE_WITH_TIME_PRORATION;


public class BillingProcessor implements PurchasesUpdatedListener {
    private static WeakReference<Activity> mActivity;
    private BillingClient myBillingClient = null;
    private InAppProcessListener inAppProcessListener;

    public BillingProcessor(Activity activity, InAppProcessListener billingCallBacks) {
        mActivity = new WeakReference<>(activity);
        inAppProcessListener = billingCallBacks;
    }

    public BillingProcessor() {

    }

    public static boolean isIabServiceAvailable(Activity context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentServices(getBindServiceIntent(), 0);
        return list != null && list.size() > 0;
    }

    private static Intent getBindServiceIntent() {
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        return intent;
    }

    /**
     * A reference to BillingClient
     */
    public void initializeBillingProcessor() {
        myBillingClient =
                BillingClient.newBuilder(mActivity.get())
                        .enablePendingPurchases()
                        .setListener(this)
                        .build();
        // clears billing manager when the jvm exits or gets terminated.
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        // starts play billing service connection
        connectToPlayBillingService();
    }

    /**
     * Initiates Google Play Billing Service.
     */
    private void connectToPlayBillingService() {
        PrintLogging.printLog(TAG, "connectToPlayBillingService");
        if (!myBillingClient.isReady()) {
            startServiceConnection(
                    () -> {
                        // IAB is fully set up. Now, let's get an inventory of stuff we own.
                        PrintLogging.printLog(TAG, "Setup successful. Querying inventory.");
                        if (inAppProcessListener != null)
                            inAppProcessListener.onBillingInitialized();
                        // querySkuDetails();
                        // queryPurchaseHistoryAsync();
                        // queryPurchasesLocally();
                    });
        }
    }

    private void querySkuDetails() {
        Map<String, SkuDetails> skuResultMap = new HashMap<>();
        List<String> subscriptionSkuList = BillingConstants.getSkuList(BillingClient.SkuType.SUBS);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subscriptionSkuList).setType(BillingClient.SkuType.SUBS);
        querySkuDetailsAsync(
                skuResultMap,
                params,
                BillingClient.SkuType.SUBS,
                () -> {
                    List<String> inAppSkuList = BillingConstants.getSkuList(BillingClient.SkuType.INAPP);
                    SkuDetailsParams.Builder params1 = SkuDetailsParams.newBuilder();
                    params1.setSkusList(inAppSkuList).setType(BillingClient.SkuType.INAPP);
                    querySkuDetailsAsync(skuResultMap, params1, BillingClient.SkuType.INAPP, null);
                });
    }

    /**
     * Makes connection with BillingClient.
     *
     * @param executeOnSuccess A runnable implementation.
     */
    private void startServiceConnection(Runnable executeOnSuccess) {
        myBillingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        // The billing client is ready. You can query purchases here.
                        PrintLogging.printLog(TAG, "Setup finished");
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (executeOnSuccess != null) {
                                executeOnSuccess.run();
                            }
                        }
                        logErrorType(billingResult);
                    }

                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                });
    }

    /**
     * Logs Billing Client Success, Failure and error responses.
     *
     * @param billingResult to identify the states of Billing Client Responses.
     * @see <a
     * href="https://developer.android.com/google/play/billing/billing_reference.html">Google
     * Play InApp Purchase Response Types Guide</a>
     */
    public static final String TAG = BillingProcessor.class.getName();

    private void logErrorType(BillingResult billingResult) {
        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                inAppProcessListener.onBillingError(billingResult);
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                PrintLogging.printLog(
                        TAG,
                        "Billing unavailable. Make sure your Google Play app is setup correctly");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                //notifyBillingError(R.string.err_service_disconnected);
                // connectToPlayBillingService();
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.OK:
                PrintLogging.printLog(TAG, "Setup successful!");
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                PrintLogging.printLog(TAG, "User has cancelled Purchase!");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                //notifyBillingError(R.string.err_no_internet);
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                PrintLogging.printLog(TAG, "Product is not available for purchase");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ERROR:
                PrintLogging.printLog(TAG, "fatal error during API action");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                PrintLogging.printLog(TAG, "Failure to purchase since item is already owned");
                inAppProcessListener.onBillingError(billingResult);
                // queryPurchasesLocally();
                break;
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                PrintLogging.printLog(TAG, "Failure to consume since item is not owned");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                PrintLogging.printLog(TAG, "Billing feature is not supported on your device");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                PrintLogging.printLog(TAG, "Billing service timeout occurred");
                inAppProcessListener.onBillingError(billingResult);
                break;
            default:
                PrintLogging.printLog(TAG, "Billing unavailable. Please check your device");
                inAppProcessListener.onBillingError(billingResult);
                break;
        }
    }

    private void destroy() {
        PrintLogging.printLog(TAG, "Destroying the billing manager.");
        if (myBillingClient.isReady()) {
            myBillingClient.endConnection();
        }
        // networkManager.removeCallback(this);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        // PrintLogging.printLog(TAG, "onPurchasesUpdate() responseCode: " + billingResult.getResponseCode());
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (inAppProcessListener != null) {
                inAppProcessListener.onPurchasesUpdated(billingResult, purchases);

                try {
                    if (purchaseType != null && !purchaseType.equalsIgnoreCase("") && purchaseType.equalsIgnoreCase(PurchaseType.SUBSCRIPTION.name())) {
                        if (myBillingClient != null && myBillingClient.isReady()) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                                if (purchases.get(0).getPurchaseToken() != null) {
                                    for (Purchase purchase : purchases) {
                                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                            acknowledgeNonConsumablePurchasesAsync(purchase);
                                        }
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {

                }

            }
            //  processPurchases(purchases);
        } else {
            // Handle any other error codes.
            logErrorType(billingResult);
        }
    }

    /**
     * Queries SKU Details from Google Play Remote Server of SKU Types (InApp and Subscription).
     *
     * @param skuResultLMap contains SKU ID and Price Details returned by the sku details query.
     * @param params contains list of SKU IDs and SKU Type (InApp or Subscription).
     * @param billingType InApp or Subscription.
     * @param executeWhenFinished contains query for InApp SKU Details that will be run after
     */
    SkuDetails skuDetails = null;

    private void querySkuDetailsAsync(
            Map<String, SkuDetails> skuResultLMap,
            SkuDetailsParams.Builder params,
            @BillingClient.SkuType String billingType,
            Runnable executeWhenFinished) {
        final SkuDetailsResponseListener listener =
                (billingResult, skuDetailsList) -> {
                    // Process the result.
                    if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                        PrintLogging.printLog(
                                TAG,
                                "Unsuccessful query for type: "
                                        + billingType
                                        + ". Error code: "
                                        + billingResult.getResponseCode());
                    } else if (skuDetailsList != null && skuDetailsList.size() > 0) {
                        for (SkuDetails skuDetails : skuDetailsList) {
                            Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                            this.skuDetails = skuDetails;
                            skuResultLMap.put(skuDetails.getSku(), skuDetails);
                        }
                    }
                    if (executeWhenFinished != null) {
                        executeWhenFinished.run();
                        return;
                    }
                    if (skuResultLMap.size() == 0) {
                        PrintLogging.printLog(
                                TAG, "sku error: " + "nosku");
                    } else {
                        PrintLogging.printLog(TAG, "storing sku list locally");
                        //storeSkuDetailsLocally(skuResultLMap);
                    }
                };
        // Creating a runnable from the request to use it inside our connection retry policy below
        executeServiceRequest(() -> myBillingClient.querySkuDetailsAsync(params.build(), listener));
    }

    private void executeServiceRequest(Runnable runnable) {
        if (myBillingClient.isReady()) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }


    /**
     * Stores Purchased Items, consumes consumable items, acknowledges non-consumable items.
     *
     * @param purchases list of Purchase Details returned from the queries.
     */
    private void processPurchases(@NonNull List<Purchase> purchases) {
        if (purchases.size() > 0) {
            PrintLogging.printLog(TAG, "purchase list size: " + purchases.size());
        }
        for (Purchase purchase : purchases) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                Log.w("purchaseToken-->>>", purchase.getPurchaseToken());
                // handlePurchase(purchase);
            } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                PrintLogging.printLog(
                        TAG, "Received a pending purchase of SKU: " + purchase.getSku());
                // handle pending purchases, e.g. confirm with users about the pending
                // purchases, prompt them to complete it, etc.
                // TODO: 8/24/2020 handle this in the next release.
            }
        }
        //storePurchaseResultsLocally(myPurchasesResultList);
        for (Purchase purchase : purchases) {
            if (purchase.getSku().equals(BillingConstants.SKU_BUY_APPLE)) {
                // handleConsumablePurchasesAsync(purchase);
            } else {
                // acknowledgeNonConsumablePurchasesAsync(purchase);
            }
        }
    }

    public void initiatePurchaseFlow(@NonNull Activity activity, @NonNull SkuDetails skuDetails) {
        if (skuDetails.getType().equals(BillingClient.SkuType.SUBS) && areSubscriptionsSupported()
                || skuDetails.getType().equals(BillingClient.SkuType.INAPP)) {

            final BillingFlowParams purchaseParams =
                    BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();
            //myBillingClient.launchPriceChangeConfirmationFlow();

            executeServiceRequest(
                    () -> {
                        PrintLogging.printLog(TAG, "Launching in-app purchase flow.");
                        myBillingClient.launchBillingFlow(activity, purchaseParams);
                    });

        }
    }

    public void initiateUpdatePurchaseFlow(@NonNull Activity activity, @NonNull SkuDetails skuDetails, String oldSKU, String oldPurchaseToken) {
        if (skuDetails.getType().equals(BillingClient.SkuType.SUBS) && areSubscriptionsSupported()
                || skuDetails.getType().equals(BillingClient.SkuType.INAPP)) {
            int oldPrice = 0;
            int newPrice = 0;
            String str = oldSkuDetails.getPrice().replaceAll("\\D+", "");
            Log.w("skuDetails", oldSkuDetails.getPrice() + "-->>" + str);
            if (!str.contains(".")) {
                oldPrice = Integer.parseInt(str);
            }


            String str2 = skuDetails.getPrice().replaceAll("\\D+", "");
            Log.w("skuDetails", skuDetails.getPrice() + "-->>" + str);
            if (!str.contains(".")) {
                newPrice = Integer.parseInt(str2);
            }


            Log.w("priceValues", oldPrice + "  " + newPrice);

            if (oldSkuDetails != null) {
                if (oldPrice > newPrice) {
                    Log.w("priceValues", "deffred");
                    BillingFlowParams purchaseParams = BillingFlowParams.newBuilder()
                            .setOldSku(oldSKU.trim(), oldPurchaseToken.trim())
                            .setReplaceSkusProrationMode(DEFERRED)
                            .setSkuDetails(skuDetails)
                            .build();

                    executeServiceRequest(
                            () -> {

                                int responseCode = myBillingClient.launchBillingFlow(activity, purchaseParams).getResponseCode();

                            });
                } else {
                    BillingFlowParams purchaseParams = BillingFlowParams.newBuilder()
                            .setOldSku(oldSKU, oldPurchaseToken)
                            .setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_PRORATED_PRICE)
                            .setSkuDetails(skuDetails)
                            .build();

                    executeServiceRequest(
                            () -> {
                                PrintLogging.printLog(TAG, "Launching in-app purchase flow.");
                                int responseCode = myBillingClient.launchBillingFlow(activity, purchaseParams).getResponseCode();
                                Log.w("responsCode-->>", responseCode + "");
                            });
                }
            } else {
                BillingFlowParams purchaseParams = BillingFlowParams.newBuilder()
                        .setOldSku(oldSKU, oldPurchaseToken)
                        .setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_PRORATED_PRICE)
                        .setSkuDetails(skuDetails)
                        .build();

                executeServiceRequest(
                        () -> {
                            PrintLogging.printLog(TAG, "Launching in-app purchase flow.");
                            int responseCode = myBillingClient.launchBillingFlow(activity, purchaseParams).getResponseCode();
                            Log.w("responsCode-->>", responseCode + "");
                        });
            }

        }
    }


    private boolean areSubscriptionsSupported() {
        final BillingResult billingResult =
                myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            PrintLogging.printLog(
                    TAG,
                    "areSubscriptionsSupported() got an error response: "
                            + billingResult.getResponseCode());
            if (inAppProcessListener != null) {
                inAppProcessListener.onBillingError(billingResult);
            }
            //  notifyBillingError(R.string.err_subscription_not_supported);
        }
        return billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    public void purchase(Activity activity, String sku, String developer_payload, String purchaseType) {
        this.purchaseType = purchaseType;
        if (purchaseType.equalsIgnoreCase(PurchaseType.PRODUCT.name())) {
            if (myBillingClient != null && myBillingClient.isReady()) {
                getProductSkuDetails(activity, sku);
            }
        } else {
            if (myBillingClient != null && myBillingClient.isReady()) {
                getSubscriptionSkuDetails(activity, sku);
            }
        }
    }

    String purchaseType = "";

    public void updatePurchase(Activity activity, String sku, String developer_payload, String purchaseType, String oldSKU, String oldPurchaseToken) {
        this.purchaseType = purchaseType;
        if (purchaseType.equalsIgnoreCase(PurchaseType.PRODUCT.name())) {
            if (myBillingClient != null && myBillingClient.isReady()) {
                //getProductSkuDetails(activity,sku);
            }
        } else {
            if (myBillingClient != null && myBillingClient.isReady()) {
                getUpdateSubscriptionSkuDetails(activity, sku, oldSKU, oldPurchaseToken);
            }
        }
    }

    public void getUpdateSubscriptionSkuDetails(Activity activity, String sku, String oldSKU, String oldPurchaseToken) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)) {
                                    initiateUpdatePurchaseFlow(activity, skuDetails, oldSKU, oldPurchaseToken);
                                }
                            }
                        }
                    }
                });
    }


    public void getProductSkuDetails(Activity activity, String sku) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)) {
                                    initiatePurchaseFlow(activity, skuDetails);
                                }
                            }
                        }
                    }
                });
    }

    public void getSubscriptionSkuDetails(Activity activity, String sku) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)) {
                                    initiatePurchaseFlow(activity, skuDetails);
                                }
                            }
                        }
                    }
                });
    }

    public SkuDetails getProductSkuDetail(Activity activity, String sku) {
        final SkuDetails[] skuDetail = new SkuDetails[1];
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)) {
                                    // initiatePurchaseFlow(activity,skuDetails);
                                    skuDetail[0] = skuDetails;
                                }
                            }
                        }
                    }
                });
        return skuDetails;
    }


    public SkuDetails getSubscriptionSkuDetail(Activity activity, String sku) {
        final SkuDetails[] skuDetail = new SkuDetails[1];
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                Log.w("skuDetails", sku + "-->>" + skuDetails.getSku());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)) {
                                    // initiatePurchaseFlow(activity,skuDetails);
                                    skuDetail[0] = skuDetails;
                                }
                            }
                        }
                    }
                });
        return skuDetails;
    }


    public boolean isReady() {
        if (myBillingClient != null) {
            return myBillingClient.isReady();
        } else {
            return false;
        }

    }

    public void endConnection() {
        if (myBillingClient != null && myBillingClient.isReady()) {
            myBillingClient.endConnection();
        }
    }

    List<SkuDetails> listOfllSkus;
    SKUsListListener skUsListListener;

    public void getAllSkuDetails(List<String> subSkuList, List<String> productSkuList, SKUsListListener callBack) {
        Log.w("sizess", subSkuList.size() + "<------>" + productSkuList.size());
        this.skUsListListener = callBack;
        listOfllSkus = new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            Log.w("debugLogic 1", skuDetailsList.size() + "");
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                listOfllSkus.add(skuDetails);
                            }
                            fetchAllProducts(productSkuList, listOfllSkus);
                            Log.w("debugLogic 2", skuDetailsList.size() + "");
                        } else {
                            Log.w("debugLogic 3", skuDetailsList.size() + "");
                            fetchAllProducts(productSkuList, listOfllSkus);
                        }
                    }
                });

    }

    private void fetchAllProducts(List<String> productSkuList, List<SkuDetails> listOfllSkus) {
        Log.w("sizess", productSkuList.size() + "<------>" + listOfllSkus.size());
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(productSkuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                listOfllSkus.add(skuDetails);
                            }
                            skUsListListener.onListOfSKU(listOfllSkus);
                        } else {
                            skUsListListener.onListOfSKU(listOfllSkus);
                        }
                    }
                });
    }

    public List<SkuDetails> getListOfllSkus() {
        return listOfllSkus;
    }

    public SkuDetails getLocalSubscriptionSkuDetail(Activity context, String identifier) {
        SkuDetails matchedSKUDetails = null;
        try {
            if (getListOfllSkus() != null && getListOfllSkus().size() > 0) {
                for (int i = 0; i < getListOfllSkus().size(); i++) {
                    Log.w("printIdentifier", identifier + "   " + getListOfllSkus().get(i).getSku());
                    if (identifier.equalsIgnoreCase(getListOfllSkus().get(i).getSku())) {
                        matchedSKUDetails = getListOfllSkus().get(i);
                        break;
                    }
                }
            }
        } catch (Exception ignored) {

        }

        return matchedSKUDetails;
    }

    public void getAllSkuSubscriptionDetails(List<String> subSkuList) {
        listOfllSkus = new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                listOfllSkus.add(skuDetails);
                            }
                        }
                        inAppProcessListener.onListOfSKUFetched(listOfllSkus);
                    }
                });
    }

    public boolean isOneTimePurchaseSupported() {
        final BillingResult billingResult = myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            return true;
        } else {
            return false;
        }
    }

    PurchaseDetailListener callBack;
    SkuDetails oldSkuDetails = null;

    public void queryPurchases(Activity context, PurchaseDetailListener call) {
        if (UserInfo.getInstance(context).isActive()) {
            if (myBillingClient != null) {
                callBack = call;
                final Purchase.PurchasesResult purchasesResult =
                        myBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

                final List<Purchase> purchases = new ArrayList<>();
                if (purchasesResult.getPurchasesList() != null) {
                    purchases.addAll(purchasesResult.getPurchasesList());
                }

                if (purchases.size() > 0) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add(purchases.get(0).getSku());
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    myBillingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                            //initiatePurchaseFlow(activity,skuDetails);
                                            oldSkuDetails = skuDetails;
                                            callBack.response(purchases.get(0));
                                        }
                                    } else {
                                        callBack.response(null);
                                    }
                                }
                            });

                } else {
                    callBack.response(null);
                }

                //PurchaseHandler.getInstance().checkPurchaseHistory(purchases,myBillingClient);
            }
        }

    }

    public void queryPurchases(Activity context) {
        if (UserInfo.getInstance(context).isActive()) {
            if (myBillingClient != null) {
                final Purchase.PurchasesResult purchasesResult =
                        myBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

                final List<Purchase> purchases = new ArrayList<>();
                if (purchasesResult.getPurchasesList() != null) {
                    purchases.addAll(purchasesResult.getPurchasesList());
                }

                if (purchases.size() > 0) {
                    for (Purchase purchaseItem : purchases) {
                        try {
                            JSONObject jsonObject = new JSONObject(purchaseItem.getOriginalJson());
                            Boolean isAcknowledged = jsonObject.getBoolean("acknowledged");
                            if (!isAcknowledged) {
                                acknowledgeNonConsumablePurchasesAsync(purchaseItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                }
            }
        }

    }

    public BillingClient getMyBillingClient() {
        return myBillingClient;
    }

    public void acknowledgeNonConsumablePurchasesAsync(Purchase purchase) {
        final AcknowledgePurchaseParams params =
                AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        final AcknowledgePurchaseResponseListener listener =
                billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        PrintLogging.printLog(
                                TAG,
                                "onAcknowledgePurchaseResponse: "
                                        + billingResult.getResponseCode());
                    } else {
                        PrintLogging.printLog(
                                TAG,
                                "onAcknowledgePurchaseResponse: "
                                        + billingResult.getDebugMessage());
                    }
                };
        executeServiceRequest(() -> myBillingClient.acknowledgePurchase(params, listener));
    }

}
