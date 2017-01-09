package com.neerajms99b.neeraj.simpletodo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.ui.MainActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by neeraj on 1/1/17.
 */

public class AlarmService extends BroadcastReceiver {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final String TAG = AlarmService.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String tag = intent.getStringExtra(context.getString(R.string.key_tag));
        if (tag.equals(context.getString(R.string.tag_show_notification))) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            String what = intent.getStringExtra(context.getString(R.string.key_what));
            int notifId = intent.getIntExtra(context.getString(R.string.key_notification_id), 0);
            wakeLock.acquire();
            showNotification(context, what, notifId);
            wakeLock.release();
        }
    }

    public void setAlarm(Context context, String todo, Date date, int notifId) {
        Log.d(TAG, "Alarm set");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(context.getString(R.string.key_tag), context.getString(R.string.tag_show_notification));
        intent.putExtra(context.getString(R.string.key_what), todo);
        intent.putExtra(context.getString(R.string.key_notification_id), notifId);
        pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.d(TAG, calendar.getTime().toString());
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void showNotification(Context context, String todoText, int notifId) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {100, 200};
        String GROUP_KEY_TODO = "group_key_todo";
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= 20) {
            Intent intent = new Intent(context, MarkDone.class);
            intent.putExtra(context.getString(R.string.key_notification_id), notifId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_action_done,
                            "Done", pendingIntent)
                            .build();
            if (Build.VERSION.SDK_INT >= 24) {
                builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_done)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .addAction(action)
                        .setGroup(GROUP_KEY_TODO)
                        .setGroupSummary(true)
                        .setContentText(todoText);
            } else {
                builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_done)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .addAction(action)
                        .setContentText(todoText);
            }
        } else {
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_action_done)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(todoText);
        }


        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        notifId,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        builder.setSound(uri);
        builder.setVibrate(v);
        builder.setAutoCancel(true);
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notifId, builder.build());
    }
}
