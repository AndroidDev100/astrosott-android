package com.astro.sott.fragments.subscription.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

public class UpgradeDialogFragment extends DialogFragment {
    private UpgradeDialogListener upgradeDialogListener;
    private EditText etDialog;
    private TextInputLayout inputLayoutDialog;
    private String fileId = "";
    private String strMessage = "";
    private String from = "";
    private BaseActivity baseActivity;

    public UpgradeDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance`method
    }

    public static UpgradeDialogFragment newInstance(String title, String message) {
        UpgradeDialogFragment frag = new UpgradeDialogFragment();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.FILE_ID_KEY, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    public void setEditDialogCallBack(UpgradeDialogListener upgradeDialogListener) {
        this.upgradeDialogListener = upgradeDialogListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.edit_dialog_layout, container);

        View view = inflater.inflate(R.layout.upgrade_dialog_fragment, container);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                    .TRANSPARENT));
            // Get field from view
            inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);
            from = getArguments().getString(AppLevelConstants.TITLE);
            fileId = getArguments().getString(AppLevelConstants.FILE_ID_KEY);
            ImageView btnCancel = view.findViewById(R.id.close_icon);
            TextView upgrade = view.findViewById(R.id.upgrade);
            TextView description = view.findViewById(R.id.description);

            TextView login = view.findViewById(R.id.login);

//        getDialog().setTitle(title);
            login.setOnClickListener(v -> {


            });
            upgrade.setOnClickListener(v -> {
                upgradeDialogListener.onUpgradeClick();
                dismiss();
            });

            // Show soft keyboard automatically and request focus to field
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //  etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
            // etDialog.setOnEditorActionListener(this);


            btnCancel.setOnClickListener(v -> {
                dismiss();
            });


        }
        return view;
    }


    public void onResume() {
        int width = getResources().getDimensionPixelSize(R.dimen.episode_dialog_fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.epiosode_dialog_fragment_height);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        // Call super onResume after sizing
        super.onResume();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface UpgradeDialogListener {
        void onUpgradeClick();
    }
}
