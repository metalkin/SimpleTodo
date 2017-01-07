package com.neerajms99b.neeraj.simpletodo.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
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
        String what = intent.getStringExtra(context.getString(R.string.key_what));
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        Log.d(TAG, "Alarm triggered");

        if (tag.equals(context.getString(R.string.tag_show_notification))) {
            wakeLock.acquire();
//            Log.d(TAG, "Alarm triggered");
            showNotification(context, what);
            wakeLock.release();
        }
    }

    public void setAlarm(Context context, String todo, Date date) {
        Log.d(TAG, "Alarm set");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(context.getString(R.string.key_tag), context.getString(R.string.tag_show_notification));
        intent.putExtra(context.getString(R.string.key_what), todo);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.d(TAG, calendar.getTime().toString());
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 15) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pendingIntent);
        }
    }

    public void showNotification(Context context, String todoText) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {100, 200};

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_add)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(todoText);

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        builder.setSound(uri);
        builder.setVibrate(v);
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(
                context.getString(R.string.notification_id)), builder.build());
    }
}
