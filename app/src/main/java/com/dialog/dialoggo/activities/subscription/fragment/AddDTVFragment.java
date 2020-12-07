package com.dialog.dialoggo.activities.subscription.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.ui.SingleLiveChannelSubscriptionActivity;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;


public class AddDTVFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;

    public AddDTVFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_dtv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnContinue = (Button)view.findViewById(R.id.btnContinue);
        TextView txtDontAccount = (TextView)view.findViewById(R.id.txtDontAccount);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnContinue.setOnClickListener(v -> {
//            Navigation.createNavigateOnClickListener(R.id.action_addDTVFragment_to_premiumOTPDtvFragment,null);
            Navigation.findNavController(view).navigate(R.id.action_addDTVFragment_to_premiumOTPDtvFragment);

        });
        txtDontAccount.setOnClickListener(v -> {
//            new ActivityLauncher(getActivity()).singleChannelSubscriptionActivity(getActivity(), SingleLiveChannelSubscriptionActivity.class);
            getActivity().finish();
        });
        btnCancel.setOnClickListener(v -> getActivity().finish());
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
