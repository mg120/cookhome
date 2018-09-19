package com.ncookhom;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ncookhom.Chat.Chat;
import com.ncookhom.MyOrders.Orders;

import static com.ncookhom.NavFragments.AppController.TAG;

/**
 * Created by Ma7MouD on 4/30/2018.
 */

public class MyFCMService extends FirebaseMessagingService {

    int count = 0;
    int notification_id = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            Badges.setBadge(this, ++count);
        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            Log.d(TAG, badgesNotSupportedException.getMessage());
        }

//        String Ntitle = remoteMessage.getData().get("title");
//        String Nmessage = remoteMessage.getData().get("message");

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            NotificationHelper.getInstance(this).createNotification(title, message);
        }


    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = null;
        if (title.equals("طلب جديد")) {
            intent = new Intent(this, Orders.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notification_id++ /* ID of notification */, notificationBuilder.build());

            try {
                Badges.removeBadge(this);
                // Alternative way
                Badges.setBadge(this, ++count);
            } catch (BadgesNotSupportedException badgesNotSupportedException) {
                Log.d(TAG, badgesNotSupportedException.getMessage());
            }

        } else if (title.equals("رسالة جديدة")) {
            intent = new Intent(this, Chat.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notification_id++ /* ID of notification */, notificationBuilder.build());

            try {
                Badges.removeBadge(this);
                // Alternative way
                Badges.setBadge(this, ++count);
            } catch (BadgesNotSupportedException badgesNotSupportedException) {
                Log.d(TAG, badgesNotSupportedException.getMessage());
            }
        }
    }
}
