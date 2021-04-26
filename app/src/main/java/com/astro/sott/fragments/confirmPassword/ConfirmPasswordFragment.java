package com.astro.sott.fragments.confirmPassword;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentConfirmPasswordBinding;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPasswordFragment extends BaseBindingFragment<FragmentConfirmPasswordBinding> {
    private SubscriptionViewModel subscriptionViewModel;

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
            checkCredential();
        });

    }

    private void checkCredential() {
        subscriptionViewModel.checkCredential(password, UserInfo.getInstance(getActivity()).getUserName()).observe(this, checkCredentialResponse -> {
            if (checkCredentialResponse != null && checkCredentialResponse.getResponse() != null && checkCredentialResponse.getResponse().getCheckCredentialsResponseMessage() != null && checkCredentialResponse.getResponse().getCheckCredentialsResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                createOtp();
            } else {
                Toast.makeText(getActivity(), checkCredentialResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOtp() {
        subscriptionViewModel.createOtp("email", UserInfo.getInstance(getActivity()).getUserName()).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                Intent intent = new Intent(getActivity(), VerificationActivity.class);
                intent.putExtra(AppLevelConstants.TYPE_KEY, "email");
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, UserInfo.getInstance(getActivity()).getUserName());
                intent.putExtra(AppLevelConstants.PASSWORD_KEY, password);
                intent.putExtra(AppLevelConstants.FROM_KEY, "confirmPassword");
                startActivity(intent);

            } else {
                Toast.makeText(getActivity(), evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected FragmentConfirmPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentConfirmPasswordBinding.inflate(inflater);
    }

}