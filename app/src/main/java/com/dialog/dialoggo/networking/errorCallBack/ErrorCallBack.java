package com.dialog.dialoggo.networking.errorCallBack;

import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

public class ErrorCallBack {
    private String errorMessage;

    public  String ErrorMessage(String errorCode, String message){
        switch (errorCode){
            case AppLevelConstants.NOT_ENTITLED:
                errorMessage = "Sorry, you do not have permission to access this content .VIU_BE_3032";
                break;
            case AppLevelConstants.RECORDING_NOT_FOUND:
                errorMessage = "Sorry, seems like there's no recording available.VIU_BE_3039";
                break;
            case AppLevelConstants.PROGRAM_NOT_EXIST:
                errorMessage = "Sorry, seems like there's no content available.VIU_BE_4022";
                break;
            case AppLevelConstants.DEVICE_NOT_IN_DOMAIN:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_1003";
                break;
            case AppLevelConstants.RECORDING_STATUS_NOT_VALID:
                errorMessage = "Sorry, seems like there's no recording available.VIU_BE_3043";
                break;
            case AppLevelConstants.ADAPTER_APP_FAILURE:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_6012";
                break;
            case AppLevelConstants.ADAPETR_NOT_EXIST:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_10000";
                break;
            case AppLevelConstants.ADAPETR_URL_REQUIRED:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_5013";
                break;
            case AppLevelConstants.USER_SUSPENDED:
                errorMessage = "Sorry, you are not permitted to watch this at the moment.VIU_UE_2001";
                break;
            case AppLevelConstants.COUCURENCY_LIMITATION:
                errorMessage = "Looks like your account is in use on too many devices. Please stop playing on another device to continue.VIU_UE_4001";
                break;
            case AppLevelConstants.INVALID_ASSET_TYPE:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_4021";
                break;
            case AppLevelConstants.ACTION_NOT_RECOGNISED:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_4023";
                break;
            case AppLevelConstants.INVALID_ASSET_ID:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_4024";
                break;
            case AppLevelConstants.USER_NOT_EXIST_IN_DOMAIN:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_1020";
                break;
            case AppLevelConstants.INVALID_USER:
                errorMessage = "You have not subscribed to this channel. Get your subscription now.VIU_BE_1026";
                break;
            case AppLevelConstants.INVALID_KS:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_500015";
                break;
            case AppLevelConstants.KS_EXPIRED:
                errorMessage = "Oops, something went wrong. Please try again later.VIU_BE_500016";
                break;
            default:
                errorMessage = message;
                break;
        }


        return errorMessage;
    }
}
