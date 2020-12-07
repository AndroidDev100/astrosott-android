package com.dialog.dialoggo.thirdParty.fcm;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerLib;
import com.google.firebase.messaging.FirebaseMessagingService;

public class WatchoFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}