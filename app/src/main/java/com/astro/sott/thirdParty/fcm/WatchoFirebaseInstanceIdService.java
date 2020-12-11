package com.astro.sott.thirdParty.fcm;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class WatchoFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}