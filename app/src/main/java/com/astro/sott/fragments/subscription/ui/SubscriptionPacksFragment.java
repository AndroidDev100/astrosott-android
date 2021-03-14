package com.astro.sott.fragments.subscription.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.activities.search.ui.SearchKeywordActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentSubscriptionPacksBinding;
import com.astro.sott.fragments.subscription.adapter.SubscriptionAdapter;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionPacksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionPacksFragment extends BaseBindingFragment<FragmentSubscriptionPacksBinding> {
    private SubscriptionViewModel subscriptionViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String from;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubscriptionPacksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionPacksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionPacksFragment newInstance(String param1, String param2) {
        SubscriptionPacksFragment fragment = new SubscriptionPacksFragment();
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
            from = getArguments().getString("from");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UIinitialization();
        modelCall();
        getProducts();

    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void loadDataFromModel(List<ProductsResponseMessageItem> productsResponseMessage) {
        SubscriptionAdapter adapter = new SubscriptionAdapter(SubscriptionPacksFragment.this, productsResponseMessage);
        getBinding().recyclerView.setAdapter(adapter);

    }

    private void getProducts() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getProduct().observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getGetProductResponse() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage().size() > 0) {
                    loadDataFromModel(evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage());

                }
            } else {

            }
        });
    }

    private void UIinitialization() {
        if (from.equalsIgnoreCase("detail")) {
            getBinding().toolbar.setVisibility(View.GONE);
            getBinding().closeIcon.setVisibility(View.VISIBLE);
        }
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

    }

    @Override
    protected FragmentSubscriptionPacksBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSubscriptionPacksBinding.inflate(inflater);
    }


}