package com.astro.sott.utils.billing;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.userInfo.UserInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AstroBillingProcessor implements PurchasesUpdatedListener {
    private static WeakReference<Activity> mActivity;
    private BillingClient myBillingClient = null;
    private SubscriptionViewModel subscriptionViewModel;
    public static final String TAG = BillingProcessor.class.getName();

    public AstroBillingProcessor(Activity activity) {
        mActivity = new WeakReference<>(activity);
        modelCall(activity);
    }

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

    private void destroy() {
        PrintLogging.printLog(TAG, "Destroying the billing manager.");
        if (myBillingClient.isReady()) {
            myBillingClient.endConnection();
        }
        // networkManager.removeCallback(this);
    }

    private void modelCall(Activity activity) {
        subscriptionViewModel = ViewModelProviders.of((FragmentActivity) activity).get(SubscriptionViewModel.class);
    }

    private void connectToPlayBillingService() {
        PrintLogging.printLog(TAG, "connectToPlayBillingService");
        if (!myBillingClient.isReady()) {
            startServiceConnection(
                    () -> {
                        // IAB is fully set up. Now, let's get an inventory of stuff we own.
                        PrintLogging.printLog(TAG, "Setup successful. Querying inventory.");
                       /* if (inAppProcessListener != null)
                            inAppProcessListener.onBillingInitialized();*/
                        // querySkuDetails();
                        // queryPurchaseHistoryAsync();
                        // queryPurchasesLocally();
                    });
        }
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

    public boolean isReady() {
        if (myBillingClient != null) {
            return myBillingClient.isReady();
        } else {
            return false;
        }

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

    private void logErrorType(BillingResult billingResult) {
        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                PrintLogging.printLog(
                        TAG,
                        "Billing unavailable. Make sure your Google Play app is setup correctly");
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                //notifyBillingError(R.string.err_service_disconnected);
                // connectToPlayBillingService();
                break;
            case BillingClient.BillingResponseCode.OK:
                PrintLogging.printLog(TAG, "Setup successful!");
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                PrintLogging.printLog(TAG, "User has cancelled Purchase!");
                break;
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                //notifyBillingError(R.string.err_no_internet);
                break;
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                PrintLogging.printLog(TAG, "Product is not available for purchase");
                break;
            case BillingClient.BillingResponseCode.ERROR:
                PrintLogging.printLog(TAG, "fatal error during API action");
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                PrintLogging.printLog(TAG, "Failure to purchase since item is already owned");
                // queryPurchasesLocally();
                break;
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                PrintLogging.printLog(TAG, "Failure to consume since item is not owned");
                break;
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                PrintLogging.printLog(TAG, "Billing feature is not supported on your device");
                break;
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                PrintLogging.printLog(TAG, "Billing service timeout occurred");
                break;
            default:
                PrintLogging.printLog(TAG, "Billing unavailable. Please check your device");
                break;
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
                                                     List<com.android.billingclient.api.SkuDetails> skuDetailsList) {
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
                            String accountId = jsonObject.getString("obfuscatedAccountId");
                            if (accountId.equalsIgnoreCase(UserInfo.getInstance(context).getCpCustomerId())) {
                                if (!isAcknowledged) {
                                    addSubscription(context, purchaseItem);
                                    //  inAppProcessListener.onAcknowledged(purchaseItem.getSku(), purchaseItem.getPurchaseToken(), purchaseItem.getOrderId());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                }

                queryPurchaseProduct(context);

            }
        }

    }

    private void addSubscription(Context context, Purchase purchaseItem) {
        subscriptionViewModel.addSubscription(UserInfo.getInstance(context).getAccessToken(), purchaseItem.getSku(), purchaseItem.getPurchaseToken(), purchaseItem.getOrderId(), "").observe((LifecycleOwner) context, addSubscriptionResponseEvergentCommonResponse -> {
            if (addSubscriptionResponseEvergentCommonResponse.isStatus()) {
                if (addSubscriptionResponseEvergentCommonResponse.getResponse().getAddSubscriptionResponseMessage().getMessage() != null) {
                }
            } else {

            }
        });
    }

    private void queryPurchaseProduct(Activity context) {
        final Purchase.PurchasesResult purchasesProductResult =
                myBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

        final List<Purchase> purchasesArraylist = new ArrayList<>();
        if (purchasesProductResult.getPurchasesList() != null) {
            purchasesArraylist.addAll(purchasesProductResult.getPurchasesList());
        }

        if (purchasesArraylist.size() > 0) {
            for (Purchase purchaseProductItem : purchasesArraylist) {
                try {

                    JSONObject jsonObject = new JSONObject(purchaseProductItem.getOriginalJson());
                    Boolean isAcknowledged = jsonObject.getBoolean("acknowledged");
                    String accountId = jsonObject.getString("obfuscatedAccountId");
                    if (accountId.equalsIgnoreCase(UserInfo.getInstance(context).getCpCustomerId())) {
                        if (!isAcknowledged) {
                            addSubscription(context, purchaseProductItem);
                            // inAppProcessListener.onAcknowledged(purchaseProductItem.getSku(), purchaseProductItem.getPurchaseToken(), purchaseProductItem.getOrderId());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {

    }
}
