package com.astro.sott.activities.loginActivity.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.google.android.material.textfield.TextInputLayout;

public class AccountBlockedDialog extends DialogFragment {
    private EditDialogListener editDialogListener;
    private EditText etDialog;
    private TextInputLayout inputLayoutDialog;
    private String validDate = "";
    private String strMessage = "";
    private BaseActivity baseActivity;

    public AccountBlockedDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance`method
    }

    public static AccountBlockedDialog newInstance(String title, String message) {
        AccountBlockedDialog frag = new AccountBlockedDialog();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.DATE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    public void setEditDialogCallBack(EditDialogListener editDialogListener) {
        this.editDialogListener = editDialogListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.edit_dialog_layout, container);

        View view = inflater.inflate(R.layout.account_blocked, container);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                    .TRANSPARENT));
            // Get field from view
            inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);
            TextView forgot = view.findViewById(R.id.forgot);
            ImageView closeIcon = view.findViewById(R.id.close_icon);
            closeIcon.setOnClickListener(v -> {
                dismiss();
            });
            // Fetch arguments from bundle and set title

//        getDialog().setTitle(title);

            forgot.setOnClickListener(v -> {
                dismiss();
                editDialogListener.onFinishEditDialog();
            });
            // Show soft keyboard automatically and request focus to field
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //  etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
            // etDialog.setOnEditorActionListener(this);


        }
        return view;
    }


    public void onResume() {
        int width = getResources().getDisplayMetrics().widthPixels - 100;
        int height = getResources().getDimensionPixelSize(R.dimen.blocked_fragment_height);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
        // Call super onResume after sizing
        super.onResume();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface EditDialogListener {
        void onFinishEditDialog();
    }
}
