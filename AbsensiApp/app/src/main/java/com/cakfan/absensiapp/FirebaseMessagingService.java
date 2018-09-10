package com.cakfan.absensiapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Vibrator vi;

        vi=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vi.vibrate(100);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String clickAction = remoteMessage.getNotification().getClickAction();
        String pesan = remoteMessage.getData().get("pesannya");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body);

        Intent resultIntent = new Intent(clickAction);
        resultIntent.putExtra("pesannya", pesan);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);


        int notifId = (int) System.currentTimeMillis();
        NotificationManager notifman =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifman.notify(notifId, builder.build());

    }
}
