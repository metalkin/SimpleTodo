package com.neerajms99b.neeraj.simpletodo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        todoListAdapter = new TodoListAdapter(this, null);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(todoListAdapter);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), TodoContentProvider.uriTodo, null, null, null,TodoContentProvider.COLUMN_WHEN+" ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            todoListAdapter.swapCursor(data);
            todoListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoListAdapter.swapCursor(null);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public void doneClicked(int position) {
//        position = position + 1;
        Log.d("mainactfrag:", String.valueOf(position));
        Uri uri = Uri.parse(TodoContentProvider.uriTodo.toString() + "/" + position);
        getContext().getContentResolver().delete(uri, null, null);
//        todoListAdapter.swapCursor(null);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        todoListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Done with it", Toast.LENGTH_SHORT).show();
    }
}
