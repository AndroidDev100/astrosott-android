package com.astro.sott.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.ActivitySubscriptionDetailBinding;
import com.astro.sott.fragments.detailRailFragment.DetailRailFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;

public class SubscriptionDetailActivity extends BaseBindingActivity<ActivitySubscriptionDetailBinding> implements BillingProcessor.IBillingHandler, CardCLickedCallBack {
    private BillingProcessor billingProcessor;

    @Override
    protected ActivitySubscriptionDetailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySubscriptionDetailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intializeBilling();
        setPackFragment();
    }

    private void setPackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SubscriptionPacksFragment subscriptionPacksFragment = new SubscriptionPacksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", "detail");
        subscriptionPacksFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frameContent, subscriptionPacksFragment).commitNow();
    }

    private void intializeBilling() {

        String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiyDBLi/JpQLoxikmVXqxK8M3ZhJNfW2tAdjnGnr7vnDiYOiyk+NomNLqmnLfQwkC+TNWn50A5XmA8FEuZmuqOzKNRQHw2P1Spl27mcZsjXcCFwj2Vy+eso3pPLjG4DfqCmQN2jZo97TW0EhsROdkWflUMepy/d6sD7eNfncA1Z0ECEDuSuOANlMQLJk7Ci5PwUHKYnUAIwbq0fU9LP6O8Ejx5BK6o5K7rtTBttCbknTiZGLo6rB+8RcSB4Z0v3Di+QPyvxjIvfSQXlWhRdyxAs/EZ/F4Hdfn6TB7mLZkKZZwI0xzOObJp2BiesclMi1wHQsNSgQ8pnZ8T52aJczpQIDAQAB";
        billingProcessor = new BillingProcessor(this, tempBase64, this);
        billingProcessor.initialize();
        billingProcessor.loadOwnedPurchasesFromGoogle();
    }

    public SkuDetails getSubscriptionDetail(String productId) {
        return billingProcessor.getSubscriptionListingDetails(productId);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onCardClicked(String productId) {
        onBackPressed();
    }
}