package com.astro.sott.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;

public class AlertDialogNetworkFragment extends DialogFragment {


    private AlertDialogNetworkFragment.AlertDialogNetworkListener alertDialogListener;

    private String strMessage = "";
    private String strPositiveButtonText = "", strTitle = "";
    private String strNegativeButtonText = "";
    private BaseActivity baseActivity;


    public AlertDialogNetworkFragment() {
    }

    public static AlertDialogNetworkFragment newInstance(String title, String message, String positiveButtonText, String negativeButtonText) {
        AlertDialogNetworkFragment frag = new AlertDialogNetworkFragment();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.MESSAGE, message);
        args.putString(AppLevelConstants.POSITIVE_BUTTON_TEXT, positiveButtonText);
        args.putString(AppLevelConstants.NEGATIVE_BUTTON_TEXT, negativeButtonText);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    public void setAlertDialogCallBack(AlertDialogNetworkFragment.AlertDialogNetworkListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        if (getArguments() != null) {
            strTitle = getArguments().getString(AppLevelConstants.TITLE);
            strMessage = getArguments().getString(AppLevelConstants.MESSAGE);
            strPositiveButtonText = getArguments().getString(AppLevelConstants.POSITIVE_BUTTON_TEXT);
            strNegativeButtonText = getArguments().getString(AppLevelConstants.NEGATIVE_BUTTON_TEXT);

        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(baseActivity, R.style.AppAlertTheme);
        alertDialogBuilder.setTitle(getResources().getString(R.string.dialog));
        alertDialogBuilder.setMessage("" + strMessage);
        alertDialogBuilder.setPositiveButton(strPositiveButtonText, (dialog, which) -> {
            if (alertDialogListener != null) {
                alertDialogListener.onFinishDialog(true);
            }
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton(strNegativeButtonText, (dialog, which) -> {
            if (alertDialogListener != null) {
                alertDialogListener.onFinishDialog(false);
            }
            dialog.dismiss();
        });

        return alertDialogBuilder.create();

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (baseActivity != null) {
            baseActivity = null;
        }
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface AlertDialogNetworkListener {
        void onFinishDialog(boolean status);
    }
}
