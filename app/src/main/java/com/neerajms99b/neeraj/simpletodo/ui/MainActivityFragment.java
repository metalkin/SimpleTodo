package com.neerajms99b.neeraj.simpletodo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.neerajms99b.neeraj.simpletodo.R;
import com.neerajms99b.neeraj.simpletodo.data.TodoContentProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public int CURSOR_LOADER_ID = 0;
    private TodoListAdapter todoListAdapter;
    private MainActivityFragment fragment;
    private static final String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        fragment = this;
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        todoListAdapter = new TodoListAdapter(this, null);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(todoListAdapter);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), TodoContentProvider.uriTodo, null, null, null, TodoContentProvider.COLUMN_WHEN + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            todoListAdapter.swapCursor(data);
            todoListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                receiver, new IntentFilter(getString(R.string.key_intent_filter_mark_done)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoListAdapter.swapCursor(null);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.key_intent_filter_mark_done))) {
                Log.d(TAG, "broadcast recieved");
                int position = intent.getIntExtra(context.getString(R.string.key_notification_id), 0);
                todoListAdapter.notifyItemRemoved(position);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, fragment);
                todoListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void doneClicked(int position) {
//        position = position + 1;
        Log.d("mainactfrag:", String.valueOf(position));
        Uri uri = Uri.parse(TodoContentProvider.uriTodo.toString() + "/" + position);
        new AsyncTask<Uri, Void, Void>() {
            @Override
            protected Void doInBackground(Uri... uris) {
                getContext().getContentResolver().delete(uris[0], null, null);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, fragment);
                todoListAdapter.notifyDataSetChanged();
                Toast.makeText(fragment.getContext(), "Done with it", Toast.LENGTH_SHORT).show();
            }
        }.execute(uri);
    }
}
