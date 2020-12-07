package com.dialog.dialoggo.activities.subscription.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.callback.BottomSheetFragmentListener;
import com.dialog.dialoggo.baseModel.BaseActivity;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class ViewAllBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetFragmentListener mListener;
    private ImageView imvClose;
    private TextView packageTitle;
    private String subscriptionType;

    public ViewAllBottomSheetFragment(String vod) {
     this.subscriptionType = vod;
    }

    public void setBottomSheetFragmentListener(BottomSheetFragmentListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.90f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_all_bottom_sheet, container, false);
    }


    @NotNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {

            FrameLayout bottomSheet = ((BottomSheetDialog) dialog1).findViewById(com.google.android.material.R.id.design_bottom_sheet);

            final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);


            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING:  //1
                        case BottomSheetBehavior.STATE_SETTLING:  //2
                        case BottomSheetBehavior.STATE_EXPANDED:  //3
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:  // 4
                        case BottomSheetBehavior.STATE_HIDDEN:  // 5
                            dialog1.dismiss();
                            break;
                        case BottomSheetBehavior.STATE_HALF_EXPANDED:
                            break;
                    }

                    Log.d("CustomBottomSheetDialog", "onStateChanged: "+newState);
                }

                @Override
                public void onSlide(@NonNull View view, float v) {

                }
            });

        });
        return dialog;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewAllChannelFragment allChannelFragment = new ViewAllChannelFragment();
        allChannelFragment.setBottomSheetFragmentListener(mListener);
        getChildFragmentManager().beginTransaction().add(R.id.frameContent, allChannelFragment).commit();
        imvClose = view.findViewById(R.id.imvClose);
        packageTitle = view.findViewById(R.id.txtPackageTitle);
        setClickListener();

        if (subscriptionType.equalsIgnoreCase("VOD")){
            packageTitle.setText(getString(R.string.package_detail));
        }else if (subscriptionType.equalsIgnoreCase("Live")){
            packageTitle.setText(getString(R.string.live_package_detail));
        }else if (subscriptionType.equalsIgnoreCase("My Plans")){
            packageTitle.setText(getString(R.string.plan_details_bottom));
        }
    }

    private void setClickListener() {
        imvClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null && getView().getParent() != null){
            ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListener != null) {
            mListener.onSheetClosed();
        }
    }

    public  void openDialougeFornoData(final Activity context) {
        BaseActivity baseActivity = (BaseActivity) context;
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        boolean status = KsPreferenceKey.getInstance(context).getUserActive();
        if (status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
            builder.setTitle(context.getResources().getString(R.string.dialog)).setMessage(context.getResources().getString(R.string.plan_details_error))
                    .setCancelable(true)
                    .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> {
                        dialog.cancel();
                        closeBottomSheet();
                    });

            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(context, R.color.white));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));
        }
    }

    private void closeBottomSheet() {
        dismiss();
       // onDestroyView();
    }

}
