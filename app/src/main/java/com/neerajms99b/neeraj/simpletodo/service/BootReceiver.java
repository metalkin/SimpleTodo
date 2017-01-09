package com.neerajms99b.neeraj.simpletodo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by neeraj on 9/1/17.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmService alarmService = new AlarmService();
        Cursor cursor = context.getContentResolver()
                .query(TodoContentProvider.uriTodo, null, null, null, null);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        Date currentDateTime = calendar.getTime();
        if (cursor.moveToFirst()) {
            do {
                String dateStr = cursor.getString(cursor.getColumnIndex(TodoContentProvider.COLUMN_WHEN));
                String todo = cursor.getString(cursor.getColumnIndex(TodoContentProvider.COLUMN_WHAT));
                int notifId = cursor.getInt(cursor.getColumnIndex(TodoContentProvider.KEY_ID));
                Date compareDate = getDate(dateStr);
                if (compareDate.after(currentDateTime)) {
                    alarmService.setAlarm(context, todo, compareDate, notifId);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private Date getDate(String dateStr) {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
