package com.astro.sott.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;

public class EditDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {


    private EditDialogListener editDialogListener;
    private EditText etDialog;
    private TextInputLayout inputLayoutDialog;
    private String strTitle = "";
    private String strMessage = "";
    private BaseActivity baseActivity;

    public EditDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance`method
    }

    public static EditDialogFragment newInstance(String title, String message) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(AppLevelConstants.TITLE, title);
        args.putString(AppLevelConstants.MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    public void setEditDialogCallBack(EditDialogListener editDialogListener) {
        this.editDialogListener = editDialogListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.edit_dialog_layout, container);

        View view = inflater.inflate(R.layout.edit_dialog_layout, container);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            // Get field from view
            inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);
            etDialog = view.findViewById(R.id.et_dialog);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            Button btnCancel = view.findViewById(R.id.btn_cancel);
            Button btnContinue = view.findViewById(R.id.btn_continue);
            // Fetch arguments from bundle and set title

            if (getArguments() != null) {
                strTitle = getArguments().getString(AppLevelConstants.TITLE);
                strMessage = getArguments().getString(AppLevelConstants.MESSAGE);

            }

//        getDialog().setTitle(title);


            // Show soft keyboard automatically and request focus to field
            etDialog.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


            tvTitle.setText(strTitle);
            etDialog.setText(strMessage);
        //    etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
            etDialog.setOnEditorActionListener(this);


            btnCancel.setOnClickListener(v -> dismiss());

            btnContinue.setOnClickListener(v -> {
                if (TextUtils.isEmpty(etDialog.getText().toString().trim())) {


                    inputLayoutDialog.setError(getText(R.string.pls_enter_device_name));
//                    inputLayoutDialog.setError(getString(R.string.pls_enter_device_name));

                    AppCommonMethods.requestFocus(getActivity(), etDialog);
//                    Toast.makeText(getActivity(), getResources().getString(R.string.pls_enter_device_name), Toast.LENGTH_SHORT).show();


                } else {
                    editDialogListener.onFinishEditDialog(etDialog.getText().toString().trim());

                    inputLayoutDialog.setErrorEnabled(false);

                    baseActivity.hideKeyboard(baseActivity);
                    dismiss();
//                            dialog.dismiss();
                }
            });
        }
        return view;
    }


//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        // Get field from view
//        inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);
//        etDialog = view.findViewById(R.id.et_dialog);
//        TextView tvTitle = view.findViewById(R.id.tv_title);
//        TextView tvMessage = view.findViewById(R.id.tv_message);
//        Button btnCancel = view.findViewById(R.id.btn_cancel);
//        Button btnContinue = view.findViewById(R.id.btn_continue);
//        // Fetch arguments from bundle and set title
//
//        String title = getArguments().getString(AppLevelConstants.TITLE);
//        String message = getArguments().getString(AppLevelConstants.MESSAGE);
//
////        getDialog().setTitle(title);
//
//        tvTitle.setText(title);
//        etDialog.setText(message);
//        etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
//        etDialog.setOnEditorActionListener(this);
//
////        // Show soft keyboard automatically and request focus to field
////        etDialog.requestFocus();
////        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//
//
////        final Handler handler = new Handler();
////        handler.postDelayed(() -> {
////            etDialog.requestFocus();
////            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
////        }, 400);
////
//
//        btnCancel.setOnClickListener(v -> dismiss());
//
//        btnContinue.setOnClickListener(v -> {
//            if (TextUtils.isEmpty(etDialog.getText().toString().trim())) {
//
//
//                inputLayoutDialog.setError(getText(R.string.pls_enter_device_name));
////                    inputLayoutDialog.setError(getString(R.string.pls_enter_device_name));
//
//                AppCommonMethods.requestFocus(getActivity(), etDialog);
////                    Toast.makeText(getActivity(), getResources().getString(R.string.pls_enter_device_name), Toast.LENGTH_SHORT).show();
//
//
//            } else {
//                editDialogListener.onFinishEditDialog(etDialog.getText().toString().trim());
//
//                inputLayoutDialog.setErrorEnabled(false);
//
//                ((BaseActivity) getActivity()).hideKeyboard(getActivity());
//                dismiss();
////                            dialog.dismiss();
//            }
//        });
//    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {

            if (!TextUtils.isEmpty(etDialog.getText().toString().trim())) {


                editDialogListener.onFinishEditDialog(etDialog.getText().toString().trim());

                inputLayoutDialog.setErrorEnabled(false);


                baseActivity.hideKeyboard(baseActivity);

                // Close the dialog and return back to the parent activity
                dismiss();
            } else {
                inputLayoutDialog.setError(getText(R.string.pls_enter_device_name));
                AppCommonMethods.requestFocus(getActivity(), etDialog);
//                    Toast.makeText(getActivity(), getResources().getString(R.string.pls_enter_device_name), Toast.LENGTH_SHORT).show();
            }

//            // Return input text back to activity through the implemented listener
//            EditDialogListener listener = (EditDialogListener) getActivity();
//            listener.onFinishEditDialog(etDialog.getText().toString().trim());

            return true;
        }
        return false;

    }


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
////        final Dialog dialogAlert = super.onCreateDialog(savedInstanceState);
////        // request a window without the title
////        dialogAlert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//
//        String title = getArguments().getString(AppLevelConstants.TITLE);
//        String message = getArguments().getString(AppLevelConstants.MESSAGE);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppAlertTheme);
//        alertDialogBuilder.setTitle(title);
////        alertDialogBuilder.setMessage("" + message);
//
//        // Edited: Overriding onCreateView is not necessary in your case
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View view = inflater.inflate(R.layout.edit_dialog_layout, null);
//
//        inputLayoutDialog = (TextInputLayout) view.findViewById(R.id.input_layout_dialog);
//        etDialog = (EditText) view.findViewById(R.id.et_dialog);
//
//        etDialog.setText(message);
//
//
//        setupFloatingLabelError();
//        etDialog.setOnEditorActionListener(this);
//
//        alertDialogBuilder.setView(view);
//
//        alertDialogBuilder.setPositiveButton(R.string.yes, null);
//        alertDialogBuilder.setNegativeButton(R.string.no, null);
//        final AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                btnPositive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (TextUtils.isEmpty(etDialog.getText().toString().trim())) {
//
//
//                            inputLayoutDialog.setError(getString(R.string.pls_enter_device_name));
//                            requestFocus(etDialog);
//
//                            inputLayoutDialog.setError(getString(R.string.pls_enter_device_name));
//                            requestFocus(etDialog);
////                    Toast.makeText(getActivity(), getResources().getString(R.string.pls_enter_device_name), Toast.LENGTH_SHORT).show();
//
//
//                        } else {
//                            editDialogListener.onFinishEditDialog(etDialog.getText().toString().trim());
//
//                            inputLayoutDialog.setErrorEnabled(false);
//
//                            dialogInterface.dismiss();
////                            dialog.dismiss();
//                        }
//                    }
//                });
//
//                btnNegative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogInterface.dismiss();
//                    }
//                });
//            }
//        });
//
////        // Show soft keyboard automatically and request focus to field
////        etDialog.requestFocus();
////        alertDialog.getWindow().setSoftInputMode(
////                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//
//        return alertDialog;
//
//    }

    public void onResume() {
        int width = getResources().getDimensionPixelSize(R.dimen.fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.fragment_width);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
        // Call super onResume after sizing
        super.onResume();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface EditDialogListener {
        void onFinishEditDialog(String text);
    }
}
