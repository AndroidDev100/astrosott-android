package com.astro.sott.fragments.manageSubscription.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.google.android.material.textfield.TextInputLayout;

public class PlanNotUpdated extends DialogFragment {
    private PlanUpdatedListener editDialogListener;
    private EditText etDialog;
    private TextInputLayout inputLayoutDialog;
    private String strTitle = "";
    private String strMessage = "";
    private BaseActivity baseActivity;

    public PlanNotUpdated() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance`method
    }

    public static PlanNotUpdated newInstance(String title, String message) {
        PlanNotUpdated frag = new PlanNotUpdated();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    public void setEditDialogCallBack(PlanUpdatedListener editDialogListener) {
        this.editDialogListener = editDialogListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.edit_dialog_layout, container);

        View view = inflater.inflate(R.layout.plan_not_upated, container);
        if (getDialog().getWindow() != null) {
            strTitle = getArguments().getString(AppLevelConstants.TITLE);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCancelable(false);
            // Get field from view
            inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);

            TextView btnCancel = view.findViewById(R.id.not_yet);
            TextView title = view.findViewById(R.id.title);
            // Fetch arguments from bundle and set title
            title.setText(strTitle);
//        getDialog().setTitle(title);


            // Show soft keyboard automatically and request focus to field
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //  etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
            // etDialog.setOnEditorActionListener(this);


            btnCancel.setOnClickListener(v -> {
                editDialogListener.onPlanUpdated();
                dismiss();
            });


        }
        return view;
    }


    public void onResume() {
        int width = getResources().getDimensionPixelSize(R.dimen.updated_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.updated_dialog_height);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
        // Call super onResume after sizing
        super.onResume();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface PlanUpdatedListener {
        void onPlanUpdated();
    }
}
