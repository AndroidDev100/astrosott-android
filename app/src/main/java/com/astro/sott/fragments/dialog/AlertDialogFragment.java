package com.astro.sott.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.utils.helpers.AppLevelConstants;

public class AlertDialogFragment extends DialogFragment {


    private AlertDialogListener alertDialogListener;

    private String strMessage = "", alertTitle = "";

    private String strPositiveButtonText = "";
    private String strNegativeButtonText = "";
    private BaseActivity baseActivity;


    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance(String title, String message, String positiveButtonText, String negativeButtonText) {
        AlertDialogFragment frag = new AlertDialogFragment();
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

    public void setAlertDialogCallBack(AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        if (getArguments() != null) {
            alertTitle = getArguments().getString(AppLevelConstants.TITLE);
            strMessage = getArguments().getString(AppLevelConstants.MESSAGE);
            strPositiveButtonText = getArguments().getString(AppLevelConstants.POSITIVE_BUTTON_TEXT);
            strNegativeButtonText = getArguments().getString(AppLevelConstants.NEGATIVE_BUTTON_TEXT);

        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(baseActivity, R.style.AppAlertTheme);
        alertDialogBuilder.setTitle(alertTitle);
        alertDialogBuilder.setMessage("" + strMessage);
        alertDialogBuilder.setPositiveButton(strPositiveButtonText, (dialog, which) -> {
            alertDialogListener.onFinishDialog();
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton(strNegativeButtonText, (dialog, which) -> dialog.dismiss());

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
    public interface AlertDialogListener {
        void onFinishDialog();
    }
}
