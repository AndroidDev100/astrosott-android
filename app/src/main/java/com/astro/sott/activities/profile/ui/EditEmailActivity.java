package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.confirmPassword.ui.ConfirmPasswordActivity;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityEditEmailBinding;
import com.astro.sott.utils.billing.BillingProcessor;
import com.astro.sott.utils.billing.TransactionDetails;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class EditEmailActivity extends BaseBindingActivity<ActivityEditEmailBinding> implements BillingProcessor.IBillingHandler, View.OnClickListener {
    private BillingProcessor billingProcessor;
    private boolean alreadyEmail = false;
    private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected ActivityEditEmailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditEmailBinding.inflate(inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader();
        intializeBilling();
        setClicks();


        //   billingProcessor.purchase(this, "com.sott.astro.com.my.tvod.1290", "DEVELOPER PAYLOAD HERE");
         /* List<String> purchases = billingProcessor.listOwnedProducts();

            for (String purchase: purchases){
                Log.w("Purchased Item", purchase);
            }
*/
        // billingProcessor.consumePurchase("com.sott.astro.com.my.tvod.1290");
    }

    private void setHeader() {
        if (UserInfo.getInstance(this).getEmail().equalsIgnoreCase("")) {
            getBinding().title.setText(getResources().getString(R.string.set_email));
        } else {
            getBinding().layoutEmail.setVisibility(View.VISIBLE);
            getBinding().title.setText(getResources().getString(R.string.edit_email));
            alreadyEmail = true;
            getBinding().email.setText(UserInfo.getInstance(this).getEmail());

        }
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().button.setOnClickListener(v -> {
            String email = getBinding().newEmail.getText().toString();
            if (true) {
                if (alreadyEmail && UserInfo.getInstance(this).isPasswordExists()) {
                    Intent intent = new Intent(this, ConfirmPasswordActivity.class);
                    intent.putExtra("newEmail", email);
                    startActivity(intent);
                } else if (alreadyEmail && !UserInfo.getInstance(this).isPasswordExists()) {
                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, "email");
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, UserInfo.getInstance(this).getEmail());
                    intent.putExtra("newEmail", email);
                    intent.putExtra(AppLevelConstants.PASSWORD_KEY, "");
                    intent.putExtra(AppLevelConstants.FROM_KEY, AppLevelConstants.CONFIRM_PASSWORD_WITHOUT_PASSWORD);
                    startActivity(intent);
                } else if (!alreadyEmail && UserInfo.getInstance(this).isPasswordExists()) {
                    Intent intent = new Intent(this, ConfirmPasswordActivity.class);
                    intent.putExtra("newEmail", email);
                    startActivity(intent);
                } else if (!alreadyEmail && !UserInfo.getInstance(this).isPasswordExists()) {
                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, "email");
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email);
                    intent.putExtra("newEmail", email);
                    intent.putExtra(AppLevelConstants.PASSWORD_KEY, "");
                    intent.putExtra(AppLevelConstants.FROM_KEY, AppLevelConstants.CONFIRM_PASSWORD_WITHOUT_PASSWORD);
                    startActivity(intent);
                }
            } else {
                getBinding().errorEmail.setVisibility(View.VISIBLE);
                getBinding().errorEmail.setText(getResources().getString(R.string.email_error));
            }

        });
        getBinding().newEmail.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorEmail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                //  signIn();
                break;
            // ...
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    /* @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
         if (requestCode == 4001) {
             // The Task returned from this call is always completed, no need to attach
             // a listener.
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             handleSignInResult(task);
         }
     }*/
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.w("tag_google_signin", account + "");
            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // updateUI(null);
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