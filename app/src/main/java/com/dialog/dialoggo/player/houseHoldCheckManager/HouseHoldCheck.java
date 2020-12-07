package com.dialog.dialoggo.player.houseHoldCheckManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.core.content.ContextCompat;
import android.widget.Button;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.home.HomeActivity;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.kalturaCallBacks.HouseHoldDevice;
import com.dialog.dialoggo.networking.errorCallBack.ErrorCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;

public class HouseHoldCheck {
    private HouseHoldDevice houseHoldDevice;

    public void checkHouseholdDevice(final Activity activity, HouseHoldDevice callBack) {
        houseHoldDevice = callBack;
        new KsServices(activity).checkHouseholdDevice(commonResponse -> {
            if (!commonResponse.getStatus()) {
                switch (commonResponse.getErrorCode()) {
                    case AppLevelConstants.DEVICE_EXIST_ERROR_CODE:
                        activity.runOnUiThread(() -> {

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppAlertTheme);
                            builder.setTitle(activity.getResources().getString(R.string.dialog));
                            builder.setMessage(activity.getResources().getString(R.string.logged_out_message))
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", (dialog, id) -> {
                                        KsPreferenceKey.getInstance(activity).setUserActive(false);
                                        new ActivityLauncher(activity).homeScreen(activity, HomeActivity.class);

                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                            bn.setTextColor(ContextCompat.getColor(activity, R.color.blue));
                            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            bp.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                        });

                        break;
                    case AppLevelConstants.LOGGED_OUT_ERROR_CODE:
                        activity.runOnUiThread(() -> {

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppAlertTheme);
                            builder.setTitle(activity.getResources().getString(R.string.dialog));
                            builder.setMessage(activity.getResources().getString(R.string.logged_out_message))
                                    .setCancelable(true)
                                    .setPositiveButton(activity.getResources().getString(R.string.ok), (dialog, id) -> {
                                        KsPreferenceKey.getInstance(activity).setUserActive(false);
                                        new ActivityLauncher(activity).homeScreen(activity, HomeActivity.class);

                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                            bn.setTextColor(ContextCompat.getColor(activity, R.color.blue));
                            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            bp.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                        });

                        break;
                    case AppLevelConstants.KS_EXPIRE:
                        CommonResponse commonRes = new CommonResponse();
                        commonRes.setStatus(false);
                        commonRes.setErrorCode(AppLevelConstants.KS_EXPIRE);
                        houseHoldDevice.response(commonRes);
                        break;
                    default:
                        CommonResponse commonRes1 = new CommonResponse();
                        commonRes1.setStatus(false);
                        commonRes1.setErrorCode(commonResponse.getErrorCode());
                        commonRes1.setMessage(new ErrorCallBack().ErrorMessage(commonResponse.getErrorCode(),commonResponse.getMessage()));
                        houseHoldDevice.response(commonRes1);
                        break;
                }
            } else {
                CommonResponse commonRes = new CommonResponse();
                commonRes.setStatus(true);
                houseHoldDevice.response(commonRes);
            }
        });
    }
}
