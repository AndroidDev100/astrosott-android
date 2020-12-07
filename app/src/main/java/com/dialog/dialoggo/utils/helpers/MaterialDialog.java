package com.dialog.dialoggo.utils.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.callBacks.commonCallBacks.DialogInterface;

public class MaterialDialog {
    private final Activity activity;
    private DialogInterface dialogInterface;
    private Dialog dialog;
    public MaterialDialog(Activity context) {
        this.activity = context;
    }

    public void showDialog(String title, String message, DialogInterface listner) {
        try {

            dialogInterface = listner;
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
            AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AppAlertTheme);
            LayoutInflater li = LayoutInflater.from(activity);
            View view = li.inflate(R.layout.version_update_dialog, null);
            alert.setView(view);

            if (dialog == null) {
                dialog = alert.create();
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            LinearLayout negative_button = view.findViewById(R.id.negative_button);
            LinearLayout positive_button = view.findViewById(R.id.positive_button);

            if (title.equals("force")) {
                positive_button.setVisibility(View.GONE);
            }

            negative_button.setOnClickListener(view1 -> dialogInterface.negativeAction());

            positive_button.setOnClickListener(view12 -> {
                Log.w("positivebutton", "cliii");
                dialogInterface.positiveAction();

            });
            dialog.show();
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }

    }

    public void hide() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }
}
