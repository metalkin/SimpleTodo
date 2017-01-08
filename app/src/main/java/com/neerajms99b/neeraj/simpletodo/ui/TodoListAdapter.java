package com.neerajms99b.neeraj.simpletodo.ui;

import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;

/**
 * Created by neeraj on 1/1/17.
 */

public class TodoListAdapter extends CursorRecyclerViewAdapter<TodoListAdapter.ViewHolder> {
    private final int FIRST_CARD = 1;
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

        public ViewHolder(CardView cardView, TextView todoText, TextView dateTime, ImageView button) {
            super(cardView);
            this.cardView = cardView;
            this.todoText = todoText;
            this.dateTime = dateTime;
            this.button = button;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor, final int position) {
        viewHolder.todoText.setText(cursor.getString(
                cursor.getColumnIndex(TodoContentProvider.COLUMN_WHAT)));
        viewHolder.dateTime.setText(cursor.getString(
                cursor.getColumnIndex(TodoContentProvider.COLUMN_WHEN)));
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
        ImageView button = (ImageView) cardView.findViewById(R.id.done_button);
        ViewHolder viewHolder = new ViewHolder(cardView, todoTextView, dateTimeTextView, button);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST_CARD;
        }
        return 0;
    }
}
