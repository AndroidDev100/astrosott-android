package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityIsThisYouBinding;

public class IsThisYouActivity extends BaseBindingActivity<ActivityIsThisYouBinding> {

    @Override
    protected ActivityIsThisYouBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityIsThisYouBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}