package com.astro.sott.fragments.subscription.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentSubscriptionLandingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionLandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionLandingFragment extends BaseBindingFragment<FragmentSubscriptionLandingBinding> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubscriptionLandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionLandingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionLandingFragment newInstance(String param1, String param2) {
        SubscriptionLandingFragment fragment = new SubscriptionLandingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    protected FragmentSubscriptionLandingBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSubscriptionLandingBinding.inflate(inflater);
    }

}