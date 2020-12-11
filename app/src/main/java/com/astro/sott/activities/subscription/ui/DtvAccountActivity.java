package com.astro.sott.activities.subscription.ui;

import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.astro.sott.activities.subscription.listeners.DtvAccountActivityListener;
import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityDtvAccountBinding;

public class DtvAccountActivity extends BaseBindingActivity<ActivityDtvAccountBinding> implements DtvAccountActivityListener {

    private static final String TAG = "DtvAccountActivity";

    @Override
    public ActivityDtvAccountBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityDtvAccountBinding.inflate(inflater);
    }

    @Override
    public void navigateToScreen(int screenId) {

        try {
            switch (screenId) {
                case 1:
                    break;
                case 2:
                    Navigation.findNavController(DtvAccountActivity.this, R.id.containerFragmentAAA).navigate(R.id.action_premiumDtvAccountDialogFragment_to_premiumOtpDialogFragment, null);
                    break;
                case 3:
                    Navigation.findNavController(DtvAccountActivity.this, R.id.containerFragmentAAA).navigate(R.id.action_premiumOtpDialogFragment_to_premiumSuccessDialogFragment, null);
                    break;
            }
        } catch (Exception exc) {
            Log.d(TAG, "navigateToScreen: "+exc);
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
