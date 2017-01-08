package com.neerajms99b.neeraj.simpletodo.ui;

import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by neeraj on 1/1/17.
 */

public class TodoListAdapter extends CursorRecyclerViewAdapter<TodoListAdapter.ViewHolder> {
    private final int FIRST_CARD = 1;
    private final int LAST_CARD = 2;
    private TodoListAdapter todoListAdapter;
    private MainActivityFragment callBack;

    public TodoListAdapter(MainActivityFragment mainActivityFragment, Cursor cursor) {
        super(mainActivityFragment.getContext(), cursor);
        callBack = mainActivityFragment;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView todoText;
        TextView dateTime;
        ImageView button;

        public ViewHolder(CardView cardView, TextView todoText, TextView dateTime, ImageButton button) {
            super(cardView);
            this.cardView = cardView;
            this.todoText = todoText;
            this.dateTime = dateTime;
            this.button = button;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor, final int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String dateToday = format.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String dateTomorrow = format.format(calendar.getTime());
        String dateTime = cursor.getString(
                cursor.getColumnIndex(TodoContentProvider.COLUMN_WHEN));
        StringTokenizer tokenizer = new StringTokenizer(dateTime, " ");
        String dateCompare = tokenizer.nextToken();
        String time = tokenizer.nextToken();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            Date dateObj = timeFormat.parse(time);
            time = new SimpleDateFormat("hh:mm aa").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateToday.equals(dateCompare)) {
            dateTime = "Today" + " " + time;
            if (Build.VERSION.SDK_INT >= 23) {
                viewHolder.dateTime.setTextColor(
                        callBack.getResources().getColor(R.color.todayColor, null));
            } else {
                viewHolder.dateTime.setTextColor(
                        callBack.getResources().getColor(R.color.todayColor));
            }
        } else if (dateTomorrow.equals(dateCompare)) {
            dateTime = "Tomorrow" + " " + time;
            if (Build.VERSION.SDK_INT >= 23) {
                viewHolder.dateTime.setTextColor(
                        callBack.getResources().getColor(R.color.tomorrowColor, null));
            } else {
                viewHolder.dateTime.setTextColor(
                        callBack.getResources().getColor(R.color.tomorrowColor));
            }
        } else {
            dateTime = dateCompare + " " + time;
            if (Build.VERSION.SDK_INT >= 23) {
                viewHolder.dateTime.setTextColor(
                        callBack.getResources().getColor(R.color.dateColor, null));
            } else {
                viewHolder.dateTime.setTextColor(
                        callBack.getResources().getColor(R.color.dateColor));
            }
        }

        viewHolder.todoText.setText(cursor.getString(
                cursor.getColumnIndex(TodoContentProvider.COLUMN_WHAT)));
        viewHolder.dateTime.setText(dateTime);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TodoContentProvider.KEY_ID)));
                notifyItemRemoved(position);
                callBack.doneClicked(id);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        float density = parent.getResources().getDisplayMetrics().density;
        int leftMargin = 8;
        int topMargin = 0;
        int rightMargin = 8;
        int bottomMargin = 8;
        if (viewType == FIRST_CARD) {
            topMargin = 8;
        } else if (viewType == LAST_CARD) {
            bottomMargin = 80;
        }
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) (leftMargin * density), (int) (topMargin * density),
                (int) (rightMargin * density), (int) (bottomMargin * density));
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams.setMarginStart((int) (leftMargin * density));
            layoutParams.setMarginEnd((int) (rightMargin * density));
        }
        cardView.setLayoutParams(layoutParams);
        TextView todoTextView = (TextView) cardView.findViewById(R.id.todo_description);
        TextView dateTimeTextView = (TextView) cardView.findViewById(R.id.todo_datetime);
        ImageButton button = (ImageButton) cardView.findViewById(R.id.done_button);
        ViewHolder viewHolder = new ViewHolder(cardView, todoTextView, dateTimeTextView, button);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST_CARD;
        } else if (position == getItemCount() - 1) {
            return LAST_CARD;
        }
        return 0;
    }
}
