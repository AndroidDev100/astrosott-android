package com.astro.sott.utils.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.subscription.ui.SubscriptionActivity;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.fragments.subscription.ui.NewSubscriptionPacksFragment;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.chaos.view.PinView;
import com.astro.sott.R;
import com.astro.sott.activities.subscription.ui.SingleLiveChannelSubscriptionActivity;

import java.util.ArrayList;

public class DialogHelper {
    private static ProgressDialog progressDialog;
    private static Dialog parentalPindialog;
    private static boolean isDialog = false;

    public static void openDialougeforGeoLocation(int type, Context context) {
        BaseActivity baseActivity = (BaseActivity) context;
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        String messages;
        if (type == 1) {
            messages = context.getResources().getString(R.string.geo_location_dialouge);
            AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(context.getResources().getString(R.string.dialog), messages, context.getResources().getString(R.string.ok));
            alertDialog.setAlertDialogCallBack(() -> alertDialog.dismiss());

            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        } else {
            messages = context.getResources().getString(R.string.play_back_error);
            AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(context.getResources().getString(R.string.dialog), messages, context.getResources().getString(R.string.ok));
            alertDialog.setAlertDialogCallBack(() -> baseActivity.finish());
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);

        }

    }

    public static void openDialougeForEntitleMent(final Activity context) {
        BaseActivity baseActivity = (BaseActivity) context;
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        boolean status = UserInfo.getInstance(baseActivity).isActive();
       /* if (status) {*/
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
            builder.setTitle(context.getResources().getString(R.string.become_vip)).setMessage(context.getResources().getString(R.string.subscribe_description))
                    .setCancelable(true)
                    .setPositiveButton(context.getResources().getString(R.string.got_it), (dialog, id) -> {
                        baseActivity.onBackPressed();
                        dialog.cancel();
                    });

            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(context, R.color.aqua_marine));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(context, R.color.aqua_marine));
        /*} else {
            showLoginDialog(context);
        }*/
    }

    public static void openDialougeFornonDialog(final Activity context, boolean isLiveChannel) {
        BaseActivity baseActivity = (BaseActivity) context;
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        boolean status = KsPreferenceKey.getInstance(context).getUserActive();
        if (status) {
            if (isLiveChannel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
                builder.setTitle(context.getResources().getString(R.string.dialog)).setMessage(context.getResources().getString(R.string.non_dialog_user_message))
                        .setCancelable(true)
                        .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> {
                            dialog.cancel();
                        });

                AlertDialog alert = builder.create();
                alert.show();
                Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                bn.setTextColor(ContextCompat.getColor(context, R.color.white));
                Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                bp.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
                builder.setTitle(context.getResources().getString(R.string.dialog)).setMessage(context.getResources().getString(R.string.non_dialog_user_message))
                        .setCancelable(true)
                        .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> {
                            dialog.cancel();
                        });

                AlertDialog alert = builder.create();
                alert.show();
                Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                bn.setTextColor(ContextCompat.getColor(context, R.color.white));
                Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                bp.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));
            }
        } else {
            showLoginDialog(context);
        }
    }

    public static void openDialougeForDtvAccount(final Activity context, boolean isAdded, boolean isLiveChannel) {
        BaseActivity baseActivity = (BaseActivity) context;
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        if (isAdded) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
            builder.setMessage(context.getResources().getString(R.string.subscription_dialouge_for_logged_in))
                    .setCancelable(true)
                    .setPositiveButton(context.getResources().getString(R.string.subscribe), (dialog, id) -> {
                        //dialog.cancel();
                        if (isLiveChannel) {
                            dialog.cancel();
                            new ActivityLauncher(context).singleChannelSubscriptionActivity(context, SingleLiveChannelSubscriptionActivity.class);

                        } else {
                            dialog.cancel();
                            new ActivityLauncher(context).subscriptionActivity(context, SubscriptionActivity.class, 0);
                        }

                    })
                    .setNegativeButton(context.getResources().getString(R.string.cancel), (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(context, R.color.cancel_text));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
            builder.setTitle(context.getResources().getString(R.string.dialog)).setMessage(context.getResources().getString(R.string.dtv_account_not_added))
                    .setCancelable(true)
                    .setPositiveButton(context.getResources().getString(R.string.subscribe_text), (dialog, id) ->
                            dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(context, R.color.white));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));

        }
    }

    public static void showLoginDialog(Activity context) {
        BaseActivity baseActivity = (BaseActivity) context;
        if (context != null && !context.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppAlertTheme);
            builder.setTitle(context.getResources().getString(R.string.lock_Episode)).setMessage(context.getResources().getString(R.string.purchase_dialouge_for_logged_in))
                    .setCancelable(true)
                    .setPositiveButton(context.getResources().getString(R.string.login), (dialog, id) -> {
                        new ActivityLauncher(context).astrLoginActivity(context, AstrLoginActivity.class, "");
                        baseActivity.onBackPressed();
                        dialog.cancel();
                    });
                   /* .setNegativeButton(context.getResources().getString(R.string.subscribe_text), (dialog, id) -> {
                        baseActivity.onBackPressed();
                        dialog.cancel();
                    });*/

            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(context, R.color.aqua_marine));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(context, R.color.aqua_marine));
        }
    }

    public static void showAlertDialog(Context mContext, String message, String buttonText, AlertDialogSingleButtonFragment.AlertDialogListener listener) {
        if (mContext instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) mContext;
            FragmentManager fm = baseActivity.getSupportFragmentManager();
            AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(mContext.getResources().getString(R.string.dialog), message, buttonText);
            alertDialog.setAlertDialogCallBack(listener);

            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
            isDialog = true;
        }
    }

    public static void playerAlertDialog(Context mContext, String message, String buttonText, AlertDialogSingleButtonFragment.AlertDialogListener listener) {
        if (mContext instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) mContext;
            FragmentManager fm = baseActivity.getSupportFragmentManager();
            AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(mContext.getResources().getString(R.string.dialog), message, buttonText);
            alertDialog.setAlertDialogCallBack(listener);
            alertDialog.setCancelable(false);
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
            isDialog = true;
        }
    }

    public static boolean isIsDialog() {
        return isDialog;
    }

    public static void setIsDialog(boolean isDialog) {
        DialogHelper.isDialog = isDialog;
    }

    public static void showValidatePinDialog(Context context, Boolean isValidatePin, String screenType, ParentalDialogCallbacks dialogInterface) {
        parentalPindialog = new Dialog(context);
        parentalPindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (screenType == AppLevelConstants.PLAYER) {
            parentalPindialog.setCancelable(false);

        }
        parentalPindialog.setContentView(R.layout.fragment_change_pin);
        Button btnOk = parentalPindialog.findViewById(R.id.btn_ok);
        Button btnCancel = parentalPindialog.findViewById(R.id.cancel);
        TextView textView = parentalPindialog.findViewById(R.id.validate_pin);
        PinView pinView = parentalPindialog.findViewById(R.id.txt_pin_entry);
        TextView resetView = parentalPindialog.findViewById(R.id.reset_pin);
        TextView defaultPin = parentalPindialog.findViewById(R.id.default_pin_text);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isValidatePin == null) {
            textView.setText(context.getResources().getString(R.string.validate_pin_description));
            resetView.setVisibility(View.VISIBLE);
        } else if (isValidatePin) {
            textView.setText(context.getResources().getString(R.string.validate_pin));
            defaultPin.setVisibility(View.VISIBLE);
            defaultPin.setText(context.getResources().getString(R.string.default_parental_text));
        } else {
            textView.setText(context.getResources().getString(R.string.set_pin));
            defaultPin.setVisibility(View.VISIBLE);
            defaultPin.setText(context.getResources().getString(R.string.set_pin_text));

        }

        new Handler().postDelayed(() -> {
            pinView.requestFocus();
            imm.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT);
            btnOk.setOnClickListener(v -> {
                if (pinView.getText().toString().length() == 0) {
                    Toast.makeText(context, context.getString(R.string.empty_pin), Toast.LENGTH_SHORT).show();
                } else if (pinView.getText().toString().length() > 0 && pinView.getText().toString().length() < 4) {
                    Toast.makeText(context, context.getString(R.string.message_pin_length), Toast.LENGTH_SHORT).show();
                } else {
                    dialogInterface.onPositiveClick(pinView.getText().toString());
                }
            });
            btnCancel.setOnClickListener(v -> {
                dialogInterface.onNegativeClick();
            });
        }, 200);
        parentalPindialog.setOnDismissListener(dialog -> {
            imm.hideSoftInputFromWindow(pinView.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        });

        parentalPindialog.show();
    }

    public static void hideValidatePinDialog() {
        if (parentalPindialog != null && parentalPindialog.isShowing())
            parentalPindialog.dismiss();
    }

    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
