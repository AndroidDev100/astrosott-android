package com.dialog.dialoggo.activities.parentalControl.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.notification.adapter.NotificationAdapter;
import com.dialog.dialoggo.activities.notification.ui.NotificationActivity;
import com.dialog.dialoggo.activities.parentalControl.adapter.RestrictionAdapter;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.callBacks.commonCallBacks.RestrictionClickListner;
import com.dialog.dialoggo.databinding.ActivityParentalControlBinding;
import com.dialog.dialoggo.databinding.ActivityViewingRestrictionBinding;
import com.dialog.dialoggo.modelClasses.dmsResponse.ParentalDescription;
import com.dialog.dialoggo.modelClasses.dmsResponse.ParentalLevels;
import com.dialog.dialoggo.modelClasses.dmsResponse.ResponseDmsModel;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViewingRestrictionActivity extends BaseBindingActivity<ActivityViewingRestrictionBinding> implements RestrictionClickListner {
private RestrictionAdapter restrictionAdapter;
    ArrayList<ParentalDescription> restrictionArrayList;

    @Override
    protected ActivityViewingRestrictionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityViewingRestrictionBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().include.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.viewing_restrictions));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        restrictionArrayList = new ArrayList<>();
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getApplicationContext());
        restrictionArrayList =responseDmsModel.getParentalDescriptions();
        Log.e("StringArrayList",new Gson().toJson(restrictionArrayList));

        connectionObserver();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if(aBoolean){
            UIinitialization();
            loadData();

        }else {
            noConnectionLayout();
        }

    }

    private void loadData() {
        restrictionAdapter = new RestrictionAdapter(ViewingRestrictionActivity.this,restrictionArrayList,ViewingRestrictionActivity.this);
        getBinding().recyclerview.setAdapter(restrictionAdapter);
    }

    private void UIinitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

       // DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
       // getBinding().recyclerview.addItemDecoration(itemDecor);
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Callback from Adapter
    @Override
    public void onClick(boolean itemClick, String parentalLevel) {
        if(itemClick) {
            setParentalText(parentalLevel);
            restrictionAdapter.notifyRestrictionAdapter(restrictionArrayList);
            ToastHandler.display(getApplicationContext().getResources().getString(R.string.restricton_update), getApplicationContext());
        }else {
            setParentalText(parentalLevel);
        }
    }
    // Updating Text as per User Selection
    private void setParentalText(String parentalText) {
        if(parentalText.equalsIgnoreCase(AppLevelConstants.ALL)){
            getBinding().restrictionText.setText(getApplicationContext().getString(R.string.All_parental_level));
        }else if(parentalText.equalsIgnoreCase(AppLevelConstants.THIRTEEN_PLUS)){
            getBinding().restrictionText.setText(getApplicationContext().getString(R.string.Thirteen_plus_level));
        }else if(parentalText.equalsIgnoreCase(AppLevelConstants.EIGHTEEN_PLUS)){
            getBinding().restrictionText.setText(getApplicationContext().getString(R.string.Eighteen_plus_level));
        }
    }
}
