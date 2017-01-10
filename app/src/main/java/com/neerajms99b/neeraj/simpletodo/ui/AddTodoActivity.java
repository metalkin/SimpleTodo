package com.neerajms99b.neeraj.simpletodo.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;
import com.neerajms99b.neeraj.simpletodo.service.AlarmService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {
    private TextView timeTextView;
    private TextView dateTextView;
    private EditText whatEditText;
    private Context context;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private static final String TAG = AddTodoActivity.class.getSimpleName();
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        whatEditText = (EditText) findViewById(R.id.what_edittext);
        dateTextView = (TextView) findViewById(R.id.date_input);
        timeTextView = (TextView) findViewById(R.id.time_input);
        context = this;
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineTimeSetListener();
                setTime();
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineDateSetListener();
                setDate();
            }
        });
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = whatEditText.getText().toString();
                String date = dateTextView.getText().toString();
                String time = timeTextView.getText().toString();
                if (!todo.equals("") && !date.equals("") && !time.equals("")) {
                    try {
                        Date displayDate = new SimpleDateFormat("d MMM yyyy").parse(date);
                        date = new SimpleDateFormat("yyyy-MM-dd").format(displayDate);
                        Date displayTime = new SimpleDateFormat("hh:mm aa").parse(time);
                        time = new SimpleDateFormat("HH:mm").format(displayTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String dateTime = date + " " + time;
                    new InsertIntoDb().execute(todo, dateTime);
                    finish();
                } else {
                    Toast.makeText(context, getString(R.string.empty_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setDate() {
        Calendar calendarDate = Calendar.getInstance();
        new DatePickerDialog(this, dateSetListener, calendarDate.get(Calendar.YEAR),
                calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void defineDateSetListener() {
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String dateStr = dateFormat.format(calendar.getTime());
                setTodoDateTextView(dateStr);
            }
        };
    }

    public void setTodoDateTextView(String dateStr) {
        dateTextView.setText(dateStr);
    }

    public void defineTimeSetListener() {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                String timeStr = timeFormat.format(calendar.getTime());
                setTodoTimeTextView(timeStr);
            }
        };
    }

    public void setTodoTimeTextView(String timeStr) {
        timeTextView.setText(timeStr);
    }

    public void setTime() {
        Calendar calendarTime = Calendar.getInstance();
        new TimePickerDialog(this, timeSetListener,
                calendarTime.get(Calendar.HOUR_OF_DAY), calendarTime.get(Calendar.MINUTE), false).show();
    }

    public class InsertIntoDb extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String what = strings[0];
            String when = strings[1];
            ContentValues contentValues = new ContentValues();
            contentValues.put(TodoContentProvider.COLUMN_WHAT, what);
            contentValues.put(TodoContentProvider.COLUMN_WHEN, when);
            Uri uri = getContentResolver().insert(TodoContentProvider.uriTodo, contentValues);
            int id = Integer.parseInt(uri.getLastPathSegment());
            if (uri != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date date = format.parse(when);
                    new AlarmService().setAlarm(context, what, date, id);
                    Log.d(TAG, date.toString());
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());

                }
            }
            return null;
        }
    }
}
