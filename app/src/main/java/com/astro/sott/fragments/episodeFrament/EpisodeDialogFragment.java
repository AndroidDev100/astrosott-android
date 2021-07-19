package com.astro.sott.fragments.episodeFrament;

import android.content.Context;
import android.content.Intent;
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
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.fragments.manageSubscription.ui.CancelDialogFragment;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.google.android.material.textfield.TextInputLayout;

public class EpisodeDialogFragment extends DialogFragment {
    private EpisodeDialogFragment.EditDialogListener editDialogListener;
    private EditText etDialog;
    private TextInputLayout inputLayoutDialog;
    private String fileId = "";
    private String strMessage = "";
    private String from = "";
    private BaseActivity baseActivity;

    public EpisodeDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance`method
    }

    public static EpisodeDialogFragment newInstance(String title, String message) {
        EpisodeDialogFragment frag = new EpisodeDialogFragment();
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

    public void setEditDialogCallBack(EpisodeDialogFragment.EditDialogListener editDialogListener) {
        this.editDialogListener = editDialogListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.edit_dialog_layout, container);

        View view = inflater.inflate(R.layout.episode_dialog_fragment, container);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            // Get field from view
            inputLayoutDialog = view.findViewById(R.id.input_layout_dialog);
            from = getArguments().getString(AppLevelConstants.TITLE);
            fileId = getArguments().getString(AppLevelConstants.FILE_ID_KEY);
            ImageView btnCancel = view.findViewById(R.id.close_icon);
            TextView subscribe = view.findViewById(R.id.subscribe);
            TextView login = view.findViewById(R.id.login);

            // Fetch arguments from bundle and set title

//        getDialog().setTitle(title);
            login.setOnClickListener(v -> {
                if (from.equalsIgnoreCase("player")) {
                    baseActivity.onBackPressed();
                    new ActivityLauncher(baseActivity).signupActivity(baseActivity, SignUpActivity.class, CleverTapManager.PLAYER_LOCK);
                } else {
                    new ActivityLauncher(baseActivity).signupActivity(baseActivity, SignUpActivity.class, CleverTapManager.DETAIL_PAGE_LOCK);
                    dismiss();
                }


            });
            subscribe.setOnClickListener(v -> {
                if (from.equalsIgnoreCase("player"))
                    baseActivity.onBackPressed();
                Intent intent = new Intent(baseActivity, SubscriptionDetailActivity.class);
                intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                startActivity(intent);
                dismiss();
            });

            // Show soft keyboard automatically and request focus to field
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //  etDialog.addTextChangedListener(new CustomTextWatcher(inputLayoutDialog));
            // etDialog.setOnEditorActionListener(this);


            btnCancel.setOnClickListener(v -> dismiss());


        }
        return view;
    }


    public void onResume() {
        int width = getResources().getDimensionPixelSize(R.dimen.episode_dialog_fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.epiosode_dialog_fragment_height);
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