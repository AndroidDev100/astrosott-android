package com.dialog.dialoggo.thirdParty.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dialog.dialoggo.ApplicationMain;
import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.splash.ui.SplashActivity;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class DialogFirebaseMessagingService extends FirebaseMessagingService {
    private Long value;
    private String programScreenValue = null;
    String valueEmoji;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> pushPayload = remoteMessage.getData();

        for (String key : remoteMessage.getData().keySet()) {

            if(key.equalsIgnoreCase("type")){
                programScreenValue = remoteMessage.getData().get(key);

            } else if(key.equalsIgnoreCase("Id")){
                 value = Long.valueOf(remoteMessage.getData().get(key));


            }

        }

        sendNotification(notification, pushPayload);

    }

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data         FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        Context context = ApplicationMain.getInstance().getApplicationContext();
        Bitmap icon = BitmapFactory.decodeResource(ApplicationMain.getInstance().getResources(), R.mipmap.ic_launcher);

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(AppLevelConstants.ID,value);
        intent.putExtra("via",AppLevelConstants.FIREBASE_SCREEN);
        intent.putExtra(AppLevelConstants.SCREEN_NAME,programScreenValue);

         //valueEmoji = "Title \uD83D\uDE00";
       // valueEmoji= getEmijoByUnicode(Integer.parseInt(notification.getTitle().substring(2), 16));
//        String user = notification.getTitle();
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            valueEmoji = String.valueOf(Html.fromHtml(notification.getTitle(), Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            valueEmoji = String.valueOf(Html.fromHtml(notification.getTitle()));
//        }

       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(data.get("gcm_title"))
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher);
        try {
            String picture_url = data.get("gcm_image_url");
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(data.get("gcm_notificationType"))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("channel description");
            channel.canShowBadge();
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            notificationManager.createNotificationChannel(channel);

        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {

            //now we will have the token
            String token = FirebaseInstanceId.getInstance().getToken();

            Log.d("TokenIS",token);

            //for now we are displaying the token in the log
            //copy it as this method is called only when the new token is generated
            //and usually new token is only generated when the app is reinstalled or the data is cleared
            // Log.d("MyRefreshedToken", token);

        super.onNewToken(s);

    }

    public String getEmijoByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
