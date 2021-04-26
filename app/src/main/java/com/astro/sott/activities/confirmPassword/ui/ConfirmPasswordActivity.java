package com.astro.sott.activities.confirmPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityConfirmPasswordBinding;
import com.astro.sott.fragments.confirmPassword.ConfirmPasswordFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;

public class ConfirmPasswordActivity extends BaseBindingActivity<ActivityConfirmPasswordBinding> {

    @Override
    protected ActivityConfirmPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityConfirmPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment();
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmPasswordFragment confirmPasswordFragment = new ConfirmPasswordFragment();
        fm.beginTransaction().replace(R.id.frameContent, confirmPasswordFragment).commitNow();
    }
}