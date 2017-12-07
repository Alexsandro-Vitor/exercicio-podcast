package br.ufpe.cin.if710.podcast.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Alexsandro on 07/12/2017.
 */

public class DownloadCompleteNotification extends BroadcastReceiver {
    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.services.action.DOWNLOAD_COMPLETE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DOWNLOAD_COMPLETE)) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Podcast")
                    .setContentText("O download de " + intent.getStringExtra("uri") + " terminou");
            NotificationManagerCompat mNotifyManager = NotificationManagerCompat.from(context);
            mNotifyManager.notify(1, mBuilder.build());
        }
    }
}
