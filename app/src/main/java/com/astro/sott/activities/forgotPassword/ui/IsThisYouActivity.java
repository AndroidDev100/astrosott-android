package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.detailConfirmation.DetailConfirmationActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityIsThisYouBinding;

public class IsThisYouActivity extends BaseBindingActivity<ActivityIsThisYouBinding> {

    @Override
    public ActivityIsThisYouBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityIsThisYouBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClicks();
    }

    private void setClicks() {
        getBinding().backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getBinding().proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), DetailConfirmationActivity.class);
                startActivity(i);
            }
        });
    }

}