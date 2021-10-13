package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditNameBinding;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.userInfo.UserInfo;

public class EditNameActivity extends BaseBindingActivity<ActivityEditNameBinding> {
    private SubscriptionViewModel subscriptionViewModel;
    private boolean alreadyName = false;

    @Override
    protected ActivityEditNameBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditNameBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelCall();

        if (UserInfo.getInstance(this).getFirstName().equalsIgnoreCase("")) {
            getBinding().title.setText(getResources().getString(R.string.set_name));
        } else {
            getBinding().title.setText(getResources().getString(R.string.edit_name));
            getBinding().layoutName.setVisibility(View.VISIBLE);
            getBinding().name.setText(UserInfo.getInstance(this).getFirstName());
            alreadyName = true;
        }
        setClicks();
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().button.setOnClickListener(v -> {
            String name = getBinding().newName.getText().toString().trim();
            if (alreadyName) {
                updateProfile(name);
            } else {
                if (name.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(name);
                }
            }
        });

    }

    private void updateProfile(String name) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        String acessToken = UserInfo.getInstance(this).getAccessToken();
        subscriptionViewModel.updateProfile("name", name, acessToken).observe(this, updateProfileResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (updateProfileResponse.getResponse() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                onBackPressed();
                AppCommonMethods.namePushCleverTap(this, name);
                Toast.makeText(this, getResources().getString(R.string.name_updated_message), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, updateProfileResponse.getErrorMessage() + "", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }
}