package com.astro.sott.fragments.transactionhistory.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentTransactionHistoryBinding;
import com.astro.sott.fragments.transactionhistory.adapter.TransactionAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionHistory extends BaseBindingFragment<FragmentTransactionHistoryBinding> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionHistory newInstance(String param1, String param2) {
        TransactionHistory fragment = new TransactionHistory();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UIinitialization();

    }
    private void loadDataFromModel() {
        TransactionAdapter adapter = new TransactionAdapter(TransactionHistory.this);
        getBinding().recyclerView.setAdapter(adapter);

    }

    private void UIinitialization() {
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        loadDataFromModel();

    }
    @Override
    protected FragmentTransactionHistoryBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentTransactionHistoryBinding.inflate(inflater);
    }


}