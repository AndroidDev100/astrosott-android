package com.dialog.dialoggo.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.loginActivity.LoginActivity;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

public class LoginAlertDialogSingleButtonFragment extends DialogFragment {


    private AlertDialogListener alertDialogListener;

    private String strMessage = "",strTitle="";
    private String strPositiveButtonText = "";
    private LoginActivity baseActivity;


    public LoginAlertDialogSingleButtonFragment() {
    }

    public static LoginAlertDialogSingleButtonFragment newInstance(String title, String message, String positiveButtonText) {
        LoginAlertDialogSingleButtonFragment frag = new LoginAlertDialogSingleButtonFragment();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.MESSAGE, message);
        args.putString(AppLevelConstants.POSITIVE_BUTTON_TEXT, positiveButtonText);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (LoginActivity) context;
    }

    public void setAlertDialogCallBack(AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);


        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if (getArguments() != null) {
            strMessage = getArguments().getString(AppLevelConstants.MESSAGE);
            strTitle=getArguments().getString(AppLevelConstants.TITLE);
            strPositiveButtonText = getArguments().getString(AppLevelConstants.POSITIVE_BUTTON_TEXT);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(baseActivity, R.style.AppAlertTheme);
        // alertDialogBuilder.setTitle(getResources().getString(R.string.dialog));
        alertDialogBuilder.setTitle(strTitle);
        alertDialogBuilder.setMessage(strMessage);
        alertDialogBuilder.setPositiveButton(strPositiveButtonText, (dialog1, which) -> {
            // on success

//                AlertDialogListener alertDialogListener = (AlertDialogListener) getActivity();
            alertDialogListener.onFinishDialog();
            dialog1.dismiss();
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
    public interface AlertDialogListener {
        void onFinishDialog();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }
}

