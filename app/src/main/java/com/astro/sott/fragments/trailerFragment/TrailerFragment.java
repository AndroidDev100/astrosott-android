package com.astro.sott.fragments.trailerFragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.callBacks.commonCallBacks.TrailerAsset;
import com.astro.sott.databinding.FragmentTrailerBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.fragments.trailerFragment.adapter.TrailerAdapter;
import com.astro.sott.fragments.trailerFragment.viewModel.TrailerFragmentViewModel;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailerFragment extends BaseBindingFragment<FragmentTrailerBinding> implements TrailerAsset, AlertDialogSingleButtonFragment.AlertDialogListener {

    private RailCommonData railCommonData;
    private int errorCode = AppLevelConstants.NO_ERROR;
    private boolean isParentalLocked = false;
    private Asset asset;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private Map<String, MultilingualStringValueArray> map;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private String externalRefId;
    private TrailerAdapter trailerAdapter;
    private List<Asset> trailerData;
    private List<Asset> highLightData;


    private int assetRestrictionLevel;
    private String externalId = "";
    ArrayList<ParentalLevels> parentalLevels;


    private TrailerFragmentViewModel trailerFragmentViewModel;
    private boolean assetKey = false;

    public TrailerFragment() {
        // Required empty public constructor
    }


    @Override
    protected FragmentTrailerBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentTrailerBinding.inflate(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        railCommonData = getArguments().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
        // AllChannelManager.getInstance().setRailCommonData(railCommonData);
        if (railCommonData != null && railCommonData.getObject() != null)
            asset = railCommonData.getObject();
        map = asset.getTags();
        if (asset.getExternalId() != null)
            externalId = asset.getExternalId();
        parentalLevels = new ArrayList<>();
        modelCall();
        checkTrailerOrHighlights();
        //   getRefId(asset.getType(), asset.getMetas());

    }

    private void checkTrailerOrHighlights() {
        trailerData = trailerFragmentViewModel.getTrailer();
        highLightData = trailerFragmentViewModel.getHighLights();
        if (trailerData.size() > 0)
            setTrailerUiComponents();
        if (highLightData.size() > 0)
            setHighLightUiComponents();


    }

    private void modelCall() {
        trailerFragmentViewModel = ViewModelProviders.of(this).get(TrailerFragmentViewModel.class);
    }


    private void setTrailerUiComponents() {
//        getBinding().trailerText.setVisibility(View.VISIBLE);
        getBinding().trailerRecyclerView.setVisibility(View.VISIBLE);
        getBinding().trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        trailerAdapter = new TrailerAdapter(getActivity(), trailerData, this);
        getBinding().trailerRecyclerView.setAdapter(trailerAdapter);
    }

    private void setHighLightUiComponents() {
//        getBinding().highLightText.setVisibility(View.VISIBLE);
        getBinding().highLightREcycler.setVisibility(View.VISIBLE);
        getBinding().highLightREcycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        trailerAdapter = new TrailerAdapter(getActivity(), highLightData, this);
        getBinding().highLightREcycler.setAdapter(trailerAdapter);
    }

    private void playerChecks(final Asset railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(getActivity(), railData, (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response, railData);
                } else {
                    playerChecksCompleted = true;
                    checkErrors(railData);
                }
            } else {
                callProgressBar();
                showDialog(message);
            }
        });
    }

    private void checkEntitleMent(final Asset railCommonData) {
        String fileId = AppCommonMethods.getFileIdOfAssest(railCommonData);

        new EntitlementCheck().checkAssetType(getActivity(), fileId, (status, response, purchaseKey, errorCode1, message) -> {
            if (status) {
                playerChecksCompleted = true;
                if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                    errorCode = AppLevelConstants.NO_ERROR;
                    checkErrors(railCommonData);
                } else if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASED))) {
                    errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
                    checkErrors(railCommonData);
                    //not play
                } else {
                    errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
                    checkErrors(railCommonData);
                    //not play
                }
            } else {
                callProgressBar();
                if (message != "")
                    showDialog(message);
            }


        });

    }


    private void checkBlockingErrors(Response<ListResponse<UserAssetRule>> response, Asset
            railData) {
        if (response != null && response.results != null && response.results.getObjects() != null) {
            for (UserAssetRule userAssetRule :
                    response.results.getObjects()) {
                switch (userAssetRule.getRuleType()) {
                    case GEO:
                        assetRuleErrorCode = AppLevelConstants.GEO_LOCATION_ERROR;
                        playerChecksCompleted = true;
                        checkErrors(railData);
                        return;
//                    case PARENTAL:
//                        assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
//                        checkEntitleMent(railData);
//                        break;
                    default:
                        playerChecksCompleted = true;
                        checkErrors(railData);
                        break;
                }
            }
        }
    }

    @Override
    public void getTrailerAsset(Asset trailerAsset) {

        callProgressBar();
        playerChecks(trailerAsset);
        //  new ActivityLauncher(getActivity()).trailerDirection(trailerAsset, AppConstants.Rail5);

    }

    private void checkErrors(Asset asset) {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, getActivity()));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(getActivity()));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(getActivity()));
                callProgressBar();
            }
//            else if (assetRuleErrorCode == AppLevelConstants.PARENTAL_BLOCK) {
//                isParentalLocked = true;
//                if (KsPreferenceKey.getInstance(getActivity()).getUserActive())
//                    validateParentalPin(asset);
//                else
//                    startPlayer(asset);
//            }
//            else if (errorCode == AppLevelConstants.NO_ERROR && (assetRuleErrorCode == AppLevelConstants.NO_ERROR || assetRuleErrorCode == -1)) {
//                if (KsPreferenceKey.getInstance(getActivity()).getUserActive())
//                    checkOnlyDevice(asset);
//                else {
//                    startPlayer(asset);
//                }
//            } else {
//                PrintLogging.printLog("", "elseValuePrint-->>" + assetRuleErrorCode + "  " + errorCode);
//            }
            else if (errorCode == AppLevelConstants.NO_ERROR) {
                if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                    parentalCheck(asset);
                } else {
                    startPlayer(asset);
                }
            }
        } else {
            callProgressBar();
            DialogHelper.showAlertDialog(getActivity(), getString(R.string.play_check_message), getString(R.string.ok), this);
        }
    }


    private void parentalCheck(Asset asset) {
        if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
            if (KsPreferenceKey.getInstance(getActivity()).getParentalActive()) {
                ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getActivity());
                defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
                userSelectedParentalRating = KsPreferenceKey.getInstance(getActivity()).getUserSelectedRating();
                if (!userSelectedParentalRating.equalsIgnoreCase("")) {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), userSelectedParentalRating, getActivity());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(asset);
                    } else {
                        validateParentalPin(asset);
                    }

                } else {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), defaultParentalRating, getActivity());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(asset);
                    } else {
                        validateParentalPin(asset);
                    }
                }
            } else {
                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                checkOnlyDevice(asset);
            }
        }
    }


    private void checkOnlyDevice(Asset railData) {
        new HouseHoldCheck().checkHouseholdDevice(getActivity(), commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    getActivity().runOnUiThread(() -> {
                        startPlayer(railData);
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(getActivity()).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }

        });
    }

    private void checkDevice(final Asset railData) {
        new HouseHoldCheck().checkHouseholdDevice(getActivity(), commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    getActivity().runOnUiThread(() -> checkEntitleMent(railData));
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(getActivity()).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }
        });
    }


    private void validateParentalPin(Asset asset) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(getActivity(), null, "TRAILER", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(getActivity()).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(getActivity(), pinText).observe(getActivity(), commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                // checkErrors(asset);
                                checkOnlyDevice(asset);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onNegativeClick() {
                        DialogHelper.hideValidatePinDialog();
                        callProgressBar();
                    }
                });
            }
        });
    }

    private void showDialog(String message) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onFinishDialog() {

    }

    private void startPlayer(Asset asset) {
        callProgressBar();
        RailCommonData railCommonData = new RailCommonData();
        railCommonData.setObject(asset);
        railCommonData.setProgress(0);
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
        startActivity(intent);
    }

    private void callProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getBinding().includeProgressbar.progressBar.getVisibility() == View.VISIBLE) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
