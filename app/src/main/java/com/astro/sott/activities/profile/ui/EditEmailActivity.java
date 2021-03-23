package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditEmailBinding;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.List;

public class EditEmailActivity extends BaseBindingActivity<ActivityEditEmailBinding> implements BillingProcessor.IBillingHandler {
    private BillingProcessor billingProcessor;

    @Override
    protected ActivityEditEmailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditEmailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intializeBilling();
        getBinding().button.setOnClickListener(v -> {
            billingProcessor.purchase(this, "com.sott.astro.com.my.tvod.1290", "DEVELOPER PAYLOAD HERE");
         /* List<String> purchases = billingProcessor.listOwnedProducts();

            for (String purchase: purchases){
                Log.w("Purchased Item", purchase);
            }
*/
            //  billingProcessor.consumePurchase("com.sott.astro.com.my.tvod.1290");
        });
    }

    private void intializeBilling() {

        String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiyDBLi/JpQLoxikmVXqxK8M3ZhJNfW2tAdjnGnr7vnDiYOiyk+NomNLqmnLfQwkC+TNWn50A5XmA8FEuZmuqOzKNRQHw2P1Spl27mcZsjXcCFwj2Vy+eso3pPLjG4DfqCmQN2jZo97TW0EhsROdkWflUMepy/d6sD7eNfncA1Z0ECEDuSuOANlMQLJk7Ci5PwUHKYnUAIwbq0fU9LP6O8Ejx5BK6o5K7rtTBttCbknTiZGLo6rB+8RcSB4Z0v3Di+QPyvxjIvfSQXlWhRdyxAs/EZ/F4Hdfn6TB7mLZkKZZwI0xzOObJp2BiesclMi1wHQsNSgQ8pnZ8T52aJczpQIDAQAB";
        billingProcessor = new BillingProcessor(this, tempBase64, this);
        billingProcessor.initialize();

        billingProcessor.loadOwnedPurchasesFromGoogle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.w("billingProcessor_play", UserInfo.getInstance(this).getAccessToken() + "");
        Log.w("billingProcessor_play", "purchased" + productId + "  --- " + details);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.w("billingProcessor_play", "error");

    }

    @Override
    public void onBillingInitialized() {
        Log.w("billingProcessor_play", "intialized");

    }
}