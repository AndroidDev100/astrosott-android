package com.astro.sott.fragments.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.google.android.material.textfield.TextInputLayout;


public class CommonDialogFragment extends DialogFragment {
    private EditDialogListener editDialogListener;
    private EditText etDialog;
    private TextInputLayout inputLayoutDialog;
    private String fileId = "";
    private String strMessage = "";
    private String title_value = "", message = "", actnBtn = "";
    private BaseActivity baseActivity;

    public CommonDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance`method
    }

    public static CommonDialogFragment newInstance(String title, String message, String actnBtn) {
        CommonDialogFragment frag = new CommonDialogFragment();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.MESSAGE, message);
        args.putString(AppLevelConstants.ACTION_BTN, actnBtn);
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

        View view = inflater.inflate(R.layout.already_user_dialog_fragment, container);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                    .TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCancelable(false);
            // Get field from view
            inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);
            title_value = getArguments().getString(AppLevelConstants.TITLE);
            message = getArguments().getString(AppLevelConstants.MESSAGE);
            actnBtn = getArguments().getString(AppLevelConstants.ACTION_BTN);
            TextView btnOk = view.findViewById(R.id.btnOk);
            TextView description = view.findViewById(R.id.description);
            TextView title = view.findViewById(R.id.title);
            title.setText(title_value);
            description.setText(message);
            btnOk.setText(actnBtn);

//
            btnOk.setOnClickListener(v -> {
                editDialogListener.onActionBtnClicked();
                dismiss();
            });

            // Show soft keyboard automatically and request focus to field
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //  etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
            // etDialog.setOnEditorActionListener(this);


        }
        return view;
    }


    public void onResume() {
        int width = getResources().getDisplayMetrics().widthPixels-30;
        int height = getResources().getDimensionPixelSize(R.dimen.epiosode_dialog_fragment_height);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        // Call super onResume after sizing
        super.onResume();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface EditDialogListener {
        void onActionBtnClicked();
    }
}
