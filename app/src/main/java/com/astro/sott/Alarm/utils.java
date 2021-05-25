package com.astro.sott.Alarm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.widget.RemoteViews;


import com.astro.sott.activities.splash.ui.SplashActivity;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;

import java.util.Random;


public class utils {

    public static NotificationManager mManager;
    private static int generatedPassword;

    @SuppressWarnings("static-access")
    public static void generateNotification(Context context, String name, String description, Long id, String screen_name, int requestcode) {

        String CHANNEL_ID = "my_channel_01";
        String channel_name = "hello";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        Random random = new Random();
        generatedPassword = Integer.parseInt(String.format("%05d", random.nextInt(10000)));

        NotificationCompat.Builder nb = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Log.d("OnConditionCall", "versionCall");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
//
//            nb = new NotificationCompat.Builder(context, CHANNEL_ID)
//                    .setContentTitle(name)
//                    .setContentText(description)
//                    .setSmallIcon(R.mipmap.ic_launcher);
//
//            notificationManager.notify(generatedPassword, nb.build());
//


        }


        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.pushnotification);
        contentView.setImageViewResource(R.id.image, R.drawable.favorite_24_px);
        contentView.setTextViewText(R.id.title, name);
        contentView.setTextViewText(R.id.text, description);


        Log.d("OnConditionCall", "ViewGenerated");
        nb = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.favorite_24_px)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContent(contentView);


        Notification notification = nb.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;


        Intent resultIntent = new Intent(context, SplashActivity.class);


        resultIntent.putExtra(AppLevelConstants.Title, name);
        resultIntent.putExtra(AppLevelConstants.DESCRIPTION, description);
        resultIntent.putExtra(AppLevelConstants.ID, id);
        resultIntent.putExtra(AppLevelConstants.SCREEN_NAME, screen_name);
        resultIntent.putExtra("via", "reminder");


//        resultIntent.putExtra(AppConstants.SCREEN_NAME,screen_name);
//        resultIntent.putExtra(AppConstants.ID,id);
        TaskStackBuilder TSB = TaskStackBuilder.create(context);
        TSB.addParentStack(SplashActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        requestcode,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(generatedPassword, nb.build());
        Log.d("OnConditionCall", "notifyCall");


    }
}