package com.astro.sott.fragments.subscription.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentSubscriptionLandingBinding;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionLandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionLandingFragment extends BaseBindingFragment<FragmentSubscriptionLandingBinding> {


    private ArrayList<String> productIdList;
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
        productIdList = (ArrayList<String>) getArguments().getSerializable("productList");


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setClick();

    }


    private void setClick() {
        getBinding().continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSubscriptionPacksFragment someFragment = new NewSubscriptionPacksFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "more");
                bundle.putSerializable("productList", productIdList);
                someFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
    }

    @Override
    protected FragmentSubscriptionLandingBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSubscriptionLandingBinding.inflate(inflater);
    }

}