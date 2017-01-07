package com.neerajms99b.neeraj.simpletodo.ui;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;
import com.neerajms99b.neeraj.simpletodo.service.AlarmService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private DatePicker datePicker;
    private EditText whatEditText;
    private Context context;
    private static final String TAG = AddTodoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        whatEditText = (EditText) findViewById(R.id.what_edittext);
        datePicker = (DatePicker) findViewById(R.id.date_input);
        timePicker = (TimePicker) findViewById(R.id.time_input);
        context = this;
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = whatEditText.getText().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String date = String.format("%02d", day) + "-"
                        + String.format("%02d", month) + "-"
                        + String.format("%04d", year);
                int hour;
                int minute;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                String dateTime = date + " " + time;
                new InsertIntoDb().execute(todo, dateTime);
            }
        });
    }

    public class InsertIntoDb extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String what = strings[0];
            String when = strings[1];
            int notifId = (int) System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TodoContentProvider.COLUMN_WHAT, what);
            contentValues.put(TodoContentProvider.COLUMN_WHEN, when);
            contentValues.put(TodoContentProvider.COLUMN_NOTIFICATION_ID, notifId);
            Uri uri = getContentResolver().insert(TodoContentProvider.uriTodo, contentValues);
            if (uri != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {
                    Date date = format.parse(when);
                    new AlarmService().setAlarm(context, what, date, notifId);
                    Log.d(TAG, date.toString());
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());

                }
//                Toast.makeText(context, "Insert successful", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }
}
