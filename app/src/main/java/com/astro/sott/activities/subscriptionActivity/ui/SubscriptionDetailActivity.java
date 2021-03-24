package com.astro.sott.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.subscription.adapter.PlanAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.subscriptionmodel.SubscriptionModel;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.ActivitySubscriptionDetailBinding;
import com.astro.sott.fragments.detailRailFragment.DetailRailFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.utils.billing.BillingProcessor;
import com.astro.sott.utils.billing.SkuDetails;
import com.astro.sott.utils.billing.TransactionDetails;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.kaltura.client.types.Subscription;

import java.util.Collections;
import java.util.Comparator;

public class SubscriptionDetailActivity extends BaseBindingActivity<ActivitySubscriptionDetailBinding> implements BillingProcessor.IBillingHandler, CardCLickedCallBack {
    private BillingProcessor billingProcessor;
    private SubscriptionViewModel subscriptionViewModel;

    String fileId = "";

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

        getSubsriptionActionList();
        setPackFragment();
    }

    private void getSubsriptionActionList() {

        subscriptionViewModel.getSubscriptionPackageList(fileId).observe(this, subscriptionList -> {

            if (subscriptionList != null) {
                if (subscriptionList.size() > 0) {


                }
            }
        });
    }

    private void setPackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SubscriptionPacksFragment subscriptionPacksFragment = new SubscriptionPacksFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppLevelConstants.FROM_KEY, "detail");
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