package com.dialog.dialoggo.activities.deviceMangment.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.deviceMangment.adapter.DeviceManagementAdapter;
import com.dialog.dialoggo.activities.home.HomeActivity;
import com.dialog.dialoggo.activities.login.ui.AddDeviceHold;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.LoginProcessCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.ItemDeleteListener;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.UpdateDeviceCallBack;
import com.dialog.dialoggo.databinding.ActivityDeviceManagementBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogFragment;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.fragments.dialog.EditDialogFragment;
import com.dialog.dialoggo.networking.deviceManagement.DeviceManagement;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.networking.refreshToken.RefreshKS;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.dialog.dialoggo.utils.helpers.UDID;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.HouseholdDevice;

import java.util.ArrayList;
import java.util.List;

public class DeviceManagementActivity extends BaseBindingActivity<ActivityDeviceManagementBinding> implements ItemDeleteListener, AlertDialogFragment.AlertDialogListener, AlertDialogSingleButtonFragment.AlertDialogListener, EditDialogFragment.EditDialogListener {
    private DeviceManagementAdapter adapter;
    private String from;
    private List<HouseholdDevice> deviceList;
    private List<HouseholdDevice> sorted;

    private boolean isDelete;
    private int position;
    private boolean isError;

    @Override
    public ActivityDeviceManagementBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityDeviceManagementBinding.inflate(inflater);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        from = getIntent().getStringExtra("from");

        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.device_management));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getBinding().include.toolbar.setNavigationOnClickListener(v -> finish());
        connectionObserver();

    }

    private void connectionObserver() {

        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
            PrintLogging.printLog(this.getClass(), "", "startsessionKs st" + KsPreferenceKey.getInstance(this).getStartSessionKs());
            PrintLogging.printLog(this.getClass(), "", "startsessionKs an" + KsPreferenceKey.getInstance(this).getAnonymousks());
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            getIntentData();
//            modelCall();
            UIinitialization();
//            loadDataFromModel();

        } else {
            noConnectionLayout();
        }


    }

    private void getIntentData() {
//        deviceList.clear();
        getDeviceList();
    }

    private void getDeviceList() {
        listAllAddedDevice();
    }

    private void listAllAddedDevice() {


        runOnUiThread(() -> {

            // Stuff that updates the UI
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

        });
        deviceList = new ArrayList<>();
        new DeviceManagement(DeviceManagementActivity.this).callDeviceList(from, new LoginProcessCallBack() {
            @Override
            public void response(CommonResponse response) {
                PrintLogging.printLog(this.getClass(), "", "responseOfList" + response.getStatus());


                runOnUiThread(() -> {

                    // Stuff that updates the UI
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

                });
                if (response.getStatus()) {
                    deviceList = response.getDeviceList();
                    PrintLogging.printLog(this.getClass(), "", "responseOfList" + deviceList.size());
                    runOnUiThread(() -> {
                        if (deviceList.size() > 0) {
                            setAdapter();
                        } else {
                            getBinding().emptyImg.setVisibility(View.VISIBLE);
                            getBinding().recyclerViewMore.setVisibility(View.GONE);
                        }

                    });
                } else {
                    if (response.getErrorCode() != null) {
                        PrintLogging.printLog(this.getClass(), "", "ksExpireError" + response.getErrorCode());
                        if (response.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                            PrintLogging.printLog(this.getClass(), "", "ksExpireError" + response.getErrorCode());
                            new RefreshKS(DeviceManagementActivity.this).refreshKS(response1 -> {
                                if (response1.getStatus()) {
                                    getDeviceList();
                                }
                            });
                        } else {
                            isError = true;
                            showDialog(response.getMessage());
                        }
                    } else {
                        if (response.getIsDeviceAdded() == 100) {
                            deviceList = response.getDeviceList();
                            runOnUiThread(() -> {
                                if (deviceList.size() > 0) {
                                    setAdapter();
                                }

                            });
                        }
                    }
                }
            }
        });
    }


    private void setAdapter() {
        sorted = sortList();
        String deviceLimit = KsPreferenceKey.getInstance(getApplicationContext()).getHouseHoldDeviceLimit();
        if (!from.equals("")) {
            if (!TextUtils.isEmpty(deviceLimit)) {
                if (sorted.size() >= Integer.parseInt(deviceLimit)) {
                    isDelete = false;
                    showDialog(getResources().getString(R.string.deviceconfirmation));


                }
            }


            if (getSupportActionBar() != null) {
                if (sorted.size() >= Integer.parseInt(deviceLimit)) {

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                }
            }

        }


        adapter = new DeviceManagementAdapter(this, sorted, DeviceManagementActivity.this);
        getBinding().recyclerViewMore.setAdapter(adapter);

    }

//    private void showpopUp() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        // need to cahnge message
//        builder.setMessage(this.getResources().getString(R.string.deviceconfirmation))
//                .setCancelable(true)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        dialog.cancel();
//
//                    }
//                });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//        bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blackColor));
//        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//        bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blackColor));
//
//    }

    private List<HouseholdDevice> sortList() {
        List<HouseholdDevice> list = new ArrayList<>();
        String udid = UDID.getDeviceId(DeviceManagementActivity.this, getContentResolver());

        PrintLogging.printLog(this.getClass(), "", "deviceIdIS" + udid);

        for (int i = 0; i < deviceList.size(); i++) {
            String resUdid = deviceList.get(i).getUdid();
            if (udid.equals(resUdid)) {
                list.add(0, deviceList.get(i));
            } else {
                list.add(i, deviceList.get(i));
            }
        }

        return list;
    }

//    private void modelCall() {
//        viewModel = ViewModelProviders.of(this).get(DeviceManagementViewModel.class);
//    }

    private void UIinitialization() {
        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        getBinding().recyclerViewMore.addItemDecoration(itemDecor);
    }

//    private void loadDataFromModel() {
//
//        viewModel.getAllSampleData().observe(this, sectionDataModels -> {
//            if (sectionDataModels.size() > 0) {
//
//            }
//        });
//
//    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void showToast(final String message) {
//        if (apiType == 0) {
        runOnUiThread(() -> ToastHandler.show(message, getApplicationContext()));
//        }
    }


//    private void confirmDeletion(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(this.getResources().getString(R.string.remove_device))
//                .setCancelable(true)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        removeDevice(position);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//        bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
//        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//        bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
//    }

    private void removeDevice(final int position) {
        String udid = sorted.get(position).getUdid();

        PrintLogging.printLog(this.getClass(), "", "UDIDSSSS" + UDID.getDeviceId(DeviceManagementActivity.this, getContentResolver()) + "---->>" + udid);


        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        KsServices ksServices = new KsServices(getApplicationContext());
        ksServices.deleteHouseHoldDevice(udid, (status, message) -> {
            runOnUiThread(() -> {

                // Stuff that updates the UI
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

            });

            if (status) {
                if (TextUtils.isEmpty(from)) {
                    listAllAddedDevice();
                } else {
                    addDevice();
                }
            } else {
                showToast(message);
            }

        });

    }

    private void notifyAdapter(final int id) {
        runOnUiThread(() -> adapter.notifyItemRemoved(id));
    }

    private void addDevice() {

        runOnUiThread(() -> {

            // Stuff that updates the UI
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

        });
        new AddDeviceHold(getApplicationContext()).callGetHouseHold((status, apiType, list) -> {
            runOnUiThread(() -> {

                // Stuff that updates the UI
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

            });
            if (status) {
                setLoginUser();
            } else {
                showToast("");
            }
        });

    }

    private void setLoginUser() {
        runOnUiThread(() -> {
            from = "added";
            KsPreferenceKey.getInstance(getApplicationContext()).setUserActive(true);
            KsPreferenceKey.getInstance(getApplicationContext()).setParentalActive(true);
            new ActivityLauncher(DeviceManagementActivity.this).homeScreen(DeviceManagementActivity.this, HomeActivity.class);
            //listAllAddedDevice();

        });
    }


    @Override
    public void onBackPressed() {
        if (from.equals("")) {
            super.onBackPressed();
        } else if (from.equals("added")) {
            new ActivityLauncher(DeviceManagementActivity.this).homeScreen(DeviceManagementActivity.this, HomeActivity.class);
        }
    }

    @Override
    public void itemClicked(int position, String editDelete) {


        this.position = position;

        if (editDelete.equalsIgnoreCase(AppLevelConstants.EDIT)) {

//            this.householdDevice = householdDevice;
            showEditDialog(sorted.get(position).getName());

        } else if (editDelete.equalsIgnoreCase(AppLevelConstants.DELETE)) {

//            this.householdDevice = householdDevice;
            isDelete = true;
            showAlertDialog(getResources().getString(R.string.remove_device));
//            confirmDeletion(position);
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case android.R.id.home:
//
//                finish();
//
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(getResources().getString(R.string.dialog), msg, getResources().getString(R.string.yes), getResources().getString(R.string.no));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void showEditDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(getResources().getString(R.string.edit_device_name), message);
        editDialogFragment.setEditDialogCallBack(this);
        editDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onFinishDialog() {


        if (isDelete) {
            removeDevice(position);
        } else if (isError) {
            isError = false;
            finish();
        }
    }

    @Override
    public void onFinishEditDialog(String text) {
//        Toast.makeText(DeviceManagementActivity.this, "" + text, Toast.LENGTH_SHORT).show();


        updateDevice(text);

    }


    private void updateDevice(final String text) {
        String udid = sorted.get(position).getUdid();


        HouseholdDevice householdDevice = new HouseholdDevice();
        householdDevice.setName(text);
//        householdDevice.setActivatedOn(sorted.get(position).getActivatedOn());
//        householdDevice.setBrandId(sorted.get(position).getBrandId());
//        householdDevice.setHouseholdId(sorted.get(position).getHouseholdId());
//        householdDevice.setUdid(sorted.get(position).getUdid());

        PrintLogging.printLog(this.getClass(), "", "UDIDSSSS" + UDID.getDeviceId(this, getContentResolver()) + "---->>" + udid);


        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

        KsServices ksServices = new KsServices(getApplicationContext());
        ksServices.updateHouseHoldDevice(udid, householdDevice, new UpdateDeviceCallBack() {
            @Override
            public void updateStatus(boolean status, String message) {

                try {

                    runOnUiThread(() -> {

                        // Stuff that updates the UI
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    });

                    if (status) {

                        sorted.get(position).setName(text);
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    PrintLogging.printLog(this.getClass(), "Update Device Exception", "" + e);
                }

            }
        });


    }

}

