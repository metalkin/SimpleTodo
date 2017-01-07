package com.neerajms99b.neeraj.simpletodo.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;

/**
 * Created by neeraj on 7/1/17.
 */

public class MarkDone extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("mark done", "notification");
        int notifId = intent.getIntExtra(context.getString(R.string.key_notification_id), 0);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifId);
        Uri uri = Uri.parse(TodoContentProvider.uriTodo.toString() + "/notifid/" + notifId);
        context.getContentResolver().delete(uri, null, null);
    }
}
