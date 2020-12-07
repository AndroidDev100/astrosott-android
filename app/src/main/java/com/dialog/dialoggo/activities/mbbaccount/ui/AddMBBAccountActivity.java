package com.dialog.dialoggo.activities.mbbaccount.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.mbbaccount.fragment.AddMBBAccountFragment;
import com.dialog.dialoggo.databinding.ActivityAddMbbaccountBinding;
import com.dialog.dialoggo.fragments.ChangeFragment.UI.ChangeFragment;
import com.dialog.dialoggo.fragments.DeleteFragment.UI.DeleteFragment;
import com.dialog.dialoggo.fragments.Success.UI.Success;
import com.dialog.dialoggo.fragments.otpFragment.DTVAccountOtpFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.ToastHandler;

public class AddMBBAccountActivity extends AppCompatActivity implements AddMBBAccountFragment.OnFragmentInteraction, DTVAccountOtpFragment.OnFragmentInteractionListener, Success.OnFragmentInteractionListener, ChangeFragment.OnFragmentInteractionListener, DeleteFragment.OnFragmentInteractionListener {

    private static final String TAG = "AddMBBAccountActivity";
    private ActivityAddMbbaccountBinding activityAddMbbaccountBinding;
    private FragmentManager mFragmentManager;
    private String type = "";
    private String accountNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentValues();
        activityAddMbbaccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_mbbaccount);
        mFragmentManager = getSupportFragmentManager();
        viewPagerIntialization();
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
        if (aBoolean) {
            activityAddMbbaccountBinding.noConnectionLayout.setVisibility(View.GONE);
        } else {
            noConnectionLayout();
        }
    }
    private void noConnectionLayout() {
        activityAddMbbaccountBinding.noConnectionLayout.setVisibility(View.VISIBLE);
        activityAddMbbaccountBinding.connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void intentValues() {
        try {
            if (getIntent().getExtras() != null) {
                type = getIntent().getExtras().getString(AppLevelConstants.FRAGMENTTYPE);
                accountNumber = getIntent().getExtras().getString(AppLevelConstants.MBB_ACCOUNT_NUM);
            }
        }catch (Exception e){
            Log.e("Exception",e.getMessage());
        }
    }

    private void viewPagerIntialization() {
        if(type.equalsIgnoreCase(AppLevelConstants.DTVFRAGMENT)) {
            goToAddMBBAccount(AppLevelConstants.MBB_FRAGMENT,"mbb");
        } else if(type.equalsIgnoreCase(AppLevelConstants.CHANGEFRAGMENT)){
            ChangeFragment changeFragment = new ChangeFragment();
            Bundle successBundle = new Bundle();
            successBundle.putString(AppLevelConstants.DTV_ACCOUNT_NUM, accountNumber);
            successBundle.putString(AppLevelConstants.FRAGMENT_TYPE, "mbb");
            changeFragment.setArguments(successBundle);
            mFragmentManager.beginTransaction().add(R.id.fragment_container, changeFragment, AppLevelConstants.CHANGEFRAGMENT).commitAllowingStateLoss();
        } else if(type.equalsIgnoreCase(AppLevelConstants.DELETEFRAGMENT)){
            DeleteFragment deleteFragment = new DeleteFragment();
            Bundle deleteBundle = new Bundle();
            deleteBundle.putString(AppLevelConstants.DTV_ACCOUNT_NUM, accountNumber);
            deleteBundle.putString(AppLevelConstants.FRAGMENT_TYPE, "mbb");
            deleteFragment.setArguments(deleteBundle);
            mFragmentManager.beginTransaction().add(R.id.fragment_container, deleteFragment, AppLevelConstants.DELETE_FRAGMENT).commitAllowingStateLoss();
        }
    }

    private void goToAddMBBAccount(String fragmentName,String fragmentType) {
        AddMBBAccountFragment addMBBAccountFragment = new AddMBBAccountFragment();

        Bundle bundle = new Bundle();
        bundle.putString(AppLevelConstants.SCREENFROM, fragmentName);
        bundle.putString(AppLevelConstants.FRAGMENT_TYPE, fragmentType);
        addMBBAccountFragment.setArguments(bundle);

        mFragmentManager.beginTransaction().add(R.id.fragment_container, addMBBAccountFragment, AppLevelConstants.MBB_FRAGMENT).commitAllowingStateLoss();
    }

    private void goToVerification(String fragment,String fragmentType, String msindn, int otp,String mbbAccountNum, String txnId) {
        Log.e("FINALMSIDN",mbbAccountNum);
        DTVAccountOtpFragment otpFragment = new DTVAccountOtpFragment();

        Bundle verificationBundle = new Bundle();
        verificationBundle.putString(AppLevelConstants.FRAGMENT_NAME, fragment);
        verificationBundle.putString(AppLevelConstants.FRAGMENT_TYPE, fragmentType);
        verificationBundle.putString(AppLevelConstants.USER_NAME, msindn);
        verificationBundle.putString(AppLevelConstants.ACCOUNT_NUMBER,mbbAccountNum);
        verificationBundle.putString(AppLevelConstants.OTP, Integer.toString(otp));
        verificationBundle.putString(AppLevelConstants.TXN_ID,txnId);
        otpFragment.setArguments(verificationBundle);

        mFragmentManager.beginTransaction().add(R.id.fragment_container, otpFragment, AppLevelConstants.OTP_FRAGMENT).commit();
    }

    private void goToSuccessFragment(String successMessage,String fragmentType) {
        goToMBBSuccessFragment(successMessage,fragmentType);
    }

    private void goToMBBSuccessFragment(String successMessage, String fragmentType) {
        Success success = new Success();
        Bundle bundle = new Bundle();
        bundle.putString(AppLevelConstants.SCREENFROM, successMessage);
        bundle.putString(AppLevelConstants.FRAGMENT_TYPE, fragmentType);
        success.setArguments(bundle);
        mFragmentManager.beginTransaction().add(R.id.fragment_container, success, AppLevelConstants.SUCCESS_FRAGMENT).commit();
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
                goToAddMBBAccount(AppLevelConstants.CHANGE_FRAGMENT,fragmentType);
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
