package com.astro.sott.activities.addDTVAccountNumber.UI;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.astro.sott.fragments.Success.UI.Success;
import com.astro.sott.fragments.dtvAccount.dtvFragment;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.activities.addDTVAccountNumber.AddDTVViewModel;
import com.astro.sott.databinding.ActivityAddDtvaccountNumberBinding;
import com.astro.sott.fragments.ChangeFragment.UI.ChangeFragment;
import com.astro.sott.fragments.DeleteFragment.UI.DeleteFragment;
import com.astro.sott.fragments.otpFragment.DTVAccountOtpFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;

public class addDTVAccountNumberActivity extends AppCompatActivity implements dtvFragment.OnFragmentInteractionListener, DTVAccountOtpFragment.OnFragmentInteractionListener, Success.OnFragmentInteractionListener, ChangeFragment.OnFragmentInteractionListener, DeleteFragment.OnFragmentInteractionListener{
    private ActivityAddDtvaccountNumberBinding activityLoginBinding;
    private FragmentManager mFragmentManager;
    private AddDTVViewModel viewModel;
    private String type = "";
    private String accountNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            intentValues();
            activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_dtvaccount_number);
            mFragmentManager = getSupportFragmentManager();
            viewPagerIntialization();
            connectionObserver();
        }catch (Exception e){
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void connectionObserver() {

        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            activityLoginBinding.noConnectionLayout.setVisibility(View.GONE);
            modelCall();
//            UIinitialization();
//            loadDataFromModel();
        } else {
            noConnectionLayout();
        }


    }

    private void intentValues() {
        try {
            if (getIntent().getExtras() != null) {
                type = getIntent().getExtras().getString(AppLevelConstants.FRAGMENTTYPE);
                accountNumber = getIntent().getExtras().getString(AppLevelConstants.DTV_ACCOUNT_NUM);
            }
        }catch (Exception e){
            Log.e("Exception",e.getMessage());
        }
    }

    //Calling Model

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(AddDTVViewModel.class);
    }

    private void noConnectionLayout() {
        activityLoginBinding.noConnectionLayout.setVisibility(View.VISIBLE);

        activityLoginBinding.connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void viewPagerIntialization() {
        if(type.equalsIgnoreCase(AppLevelConstants.DTVFRAGMENT)) {
            goToAddDTVAccount("dtvFragment","dtv");
            //  mFragmentManager.beginTransaction().add(R.id.fragment_container, new dtvFragment(), "dtvFragment").commitAllowingStateLoss();
        } else if(type.equalsIgnoreCase(AppLevelConstants.CHANGEFRAGMENT)){

            ChangeFragment changeFragment = new ChangeFragment();
            Bundle successBundle = new Bundle();
            successBundle.putString(AppLevelConstants.DTV_ACCOUNT_NUM, accountNumber);
            successBundle.putString(AppLevelConstants.FRAGMENT_TYPE, "dtv");
            changeFragment.setArguments(successBundle);
            mFragmentManager.beginTransaction().add(R.id.fragment_container, changeFragment, "ChangeFragment").commitAllowingStateLoss();
        } else if(type.equalsIgnoreCase(AppLevelConstants.DELETEFRAGMENT)){

            DeleteFragment deleteFragment = new DeleteFragment();
            Bundle deleteBundle = new Bundle();
            deleteBundle.putString(AppLevelConstants.DTV_ACCOUNT_NUM, accountNumber);
            deleteBundle.putString(AppLevelConstants.FRAGMENT_TYPE, "dtv");
            deleteFragment.setArguments(deleteBundle);

            mFragmentManager.beginTransaction().add(R.id.fragment_container, deleteFragment, "DeleteFragment").commitAllowingStateLoss();
        }
    }

    private void goToAddDTVAccount(String fragmentName, String fragmentType) {
        dtvFragment dtvFragment = new dtvFragment();

        Bundle bundle = new Bundle();
        bundle.putString(AppLevelConstants.SCREENFROM, fragmentName);
        bundle.putString(AppLevelConstants.FRAGMENT_TYPE, fragmentType);
        dtvFragment.setArguments(bundle);

        mFragmentManager.beginTransaction().add(R.id.fragment_container, dtvFragment, "dtvFragment").commitAllowingStateLoss();
    }

    private void goToSuccessFragment(String successMessage, String fragmentType) {

        AddDTVAccount(successMessage, fragmentType);


    }

    private void AddDTVAccount(String successMessage, String fragmentType) {

        Success success = new Success();

        Bundle bundle = new Bundle();
        bundle.putString(AppLevelConstants.SCREENFROM, successMessage);
        bundle.putString(AppLevelConstants.FRAGMENT_TYPE, fragmentType);
        success.setArguments(bundle);


        mFragmentManager.beginTransaction().add(R.id.fragment_container, success, "SuccessFragment").commit();

    }

    private void goToVerification(String fragment,String fragmentType, String msindn, int otp,String dtvAccountNum, String txnId) {
        Log.e("FINALMSIDN",dtvAccountNum);
        DTVAccountOtpFragment otpFragment = new DTVAccountOtpFragment();

        Bundle verificationBundle = new Bundle();
        verificationBundle.putString(AppLevelConstants.FRAGMENT_NAME, fragment);
        verificationBundle.putString(AppLevelConstants.FRAGMENT_TYPE, fragmentType);
        verificationBundle.putString(AppLevelConstants.USER_NAME, msindn);
        verificationBundle.putString(AppLevelConstants.ACCOUNT_NUMBER,dtvAccountNum);
        verificationBundle.putString(AppLevelConstants.OTP, Integer.toString(otp));
        verificationBundle.putString(AppLevelConstants.TXN_ID,txnId);
        otpFragment.setArguments(verificationBundle);

        mFragmentManager.beginTransaction().add(R.id.fragment_container, otpFragment, "DTVAccountOtpFragment").commit();
    }

    @Override
    public void onFragmentInteraction(String fragment, String event, String fragmentType, String msindn, int otp, String dtvAccountNum, String txnId) {

        switch (event){
            case AppLevelConstants.CONTINUE:
                goToVerification(fragment,fragmentType, msindn, otp,dtvAccountNum,txnId);
                break;

            case AppLevelConstants.OK:
                goToSuccessFragment(AppLevelConstants.ADDSUCCESS,fragmentType);
                break;

            case AppLevelConstants.CHANGE:
                goToAddDTVAccount(AppLevelConstants.CHANGE_FRAGMENT,fragmentType);
                break;

            case AppLevelConstants.YES:
                goToVerification(fragment,fragmentType, msindn, otp,dtvAccountNum,txnId);
                break;

            case AppLevelConstants.DELETE_SUCCESS:
                goToSuccessFragment(AppLevelConstants.DELETE_SUCCESS, fragmentType);
                break;

            case AppLevelConstants.CHANGESUCCESS:
                goToVerification(AppLevelConstants.CHANGE_FRAGMENT, fragmentType, msindn, otp,dtvAccountNum,txnId);
                break;

            case AppLevelConstants.CHANGESUCCESSDIALOG:
                goToSuccessFragment(AppLevelConstants.CHANGESUCCESSDIALOG, fragmentType);
                break;
            default:
                ToastHandler.show(getApplicationContext().getResources().getString(R.string.something_went_wrong),getApplication().getApplicationContext());
                break;
        }
    }
}
