package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditNameBinding;
import com.astro.sott.utils.userInfo.UserInfo;

public class EditNameActivity extends BaseBindingActivity<ActivityEditNameBinding> {

    private boolean alreadyName = false;

    @Override
    protected ActivityEditNameBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditNameBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserInfo.getInstance(this).getFirstName().equalsIgnoreCase("")) {
            getBinding().title.setText(getResources().getString(R.string.set_name));
        } else {
            getBinding().title.setText(getResources().getString(R.string.edit_name));
            getBinding().layoutName.setVisibility(View.VISIBLE);
            alreadyName = true;
        }
        setClicks();
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().button.setOnClickListener(v -> {
            if (alreadyName) {

            } else {
                String name = getBinding().newName.getText().toString().trim();
                if (name.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

    }
}