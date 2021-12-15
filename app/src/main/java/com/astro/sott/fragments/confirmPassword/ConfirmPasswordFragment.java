package com.astro.sott.fragments.confirmPassword;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.FragmentConfirmPasswordBinding;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.userInfo.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPasswordFragment extends BaseBindingFragment<FragmentConfirmPasswordBinding> {
    private SubscriptionViewModel subscriptionViewModel;
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";
    private String newEmail = "", newMobile = "";
    private boolean passwordVisibility = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmPasswordFragment newInstance(String param1, String param2) {
        ConfirmPasswordFragment fragment = new ConfirmPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            if (getArguments().getString("newEmail") != null)
                newEmail = getArguments().getString("newEmail");
            if (getArguments().getString("newMobile") != null)
                newMobile = getArguments().getString("newMobile");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modelCall();

        setClicks();
    }

    private String password;

    private void setClicks() {
        getBinding().update.setOnClickListener(v -> {

            password = getBinding().passwordEdt.getText().toString();
            if (!password.equalsIgnoreCase("") && password.matches(PASSWORD_REGEX)) {
                checkCredential();
            } else {
                getBinding().errorPasssword.setVisibility(View.VISIBLE);
            }
        });
        getBinding().eyeIconConfirmPassword.setOnClickListener(view -> {
            if (passwordVisibility) {
                getBinding().eyeIconConfirmPassword.setBackgroundResource(R.drawable.ic_outline_visibility_off_light);
                getBinding().passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordVisibility = false;
            } else {
                passwordVisibility = true;
                getBinding().passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                getBinding().eyeIconConfirmPassword.setBackgroundResource(R.drawable.ic_outline_visibility_light);

            }
            getBinding().passwordEdt.setSelection(getBinding().passwordEdt.getText().length());
        });
        getBinding().passwordEdt.addTextChangedListener(new CustomTextWatcher(getActivity(), new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorPasssword.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));

    }

    String email_mobile = "", type = "";

    private void checkCredential() {
        if (!newEmail.equalsIgnoreCase("")) {
            if (UserInfo.getInstance(getActivity()).getEmail().equalsIgnoreCase("")) {
                type = "Mobile";
                email_mobile = UserInfo.getInstance(getActivity()).getMobileNumber();
            } else {
                type = "Email";
                email_mobile = UserInfo.getInstance(getActivity()).getEmail();
            }
        } else {
            if (UserInfo.getInstance(getActivity()).getMobileNumber().equalsIgnoreCase("")) {
                type = "Email";
                email_mobile = UserInfo.getInstance(getActivity()).getEmail();
            } else {
                type = "Mobile";
                email_mobile = UserInfo.getInstance(getActivity()).getMobileNumber();
            }
        }
        getBinding().progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.checkCredential(password, email_mobile, type).observe(this, checkCredentialResponse -> {
            if (checkCredentialResponse != null && checkCredentialResponse.getResponse() != null && checkCredentialResponse.getResponse().getCheckCredentialsResponseMessage() != null && checkCredentialResponse.getResponse().getCheckCredentialsResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                createOtp();
            } else {
                getBinding().progressBar.setVisibility(View.GONE);
                ToastHandler.show(   checkCredentialResponse.getErrorMessage(),
                        requireActivity());
            }
        });
    }

    String email = "";

    private void createOtp() {
        if (!newEmail.equalsIgnoreCase("")) {
            type = "Email";
            email=newEmail;
            /*if (!UserInfo.getInstance(getActivity()).getEmail().equalsIgnoreCase("")) {
                email = UserInfo.getInstance(getActivity()).getEmail();
            } else {
                email = newEmail;
            }*/
        } else {
            type = "Mobile";
            email=newMobile;
           /* if (!UserInfo.getInstance(getActivity()).getMobileNumber().equalsIgnoreCase("")) {
                email = UserInfo.getInstance(getActivity()).getMobileNumber();
            } else {
                email = newMobile;
            }*/
        }
        subscriptionViewModel.createOtp(type, email).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                Intent intent = new Intent(getActivity(), VerificationActivity.class);
                intent.putExtra(AppLevelConstants.TYPE_KEY, type);
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email);
                if (!newEmail.equalsIgnoreCase(""))
                    intent.putExtra("newEmail", newEmail);
                if (!newMobile.equalsIgnoreCase(""))
                    intent.putExtra("newMobile", newMobile);
                intent.putExtra(AppLevelConstants.PASSWORD_KEY, password);
                intent.putExtra(AppLevelConstants.FROM_KEY, AppLevelConstants.CONFIRM_PASSWORD);
                startActivity(intent);
            } else {
                ToastHandler.show(evergentCommonResponse.getErrorMessage(),
                        requireActivity());
            }
        });

    }

    @Override
    protected FragmentConfirmPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentConfirmPasswordBinding.inflate(inflater);
    }

}