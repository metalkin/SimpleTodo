package com.neerajms99b.neeraj.simpletodo.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return new CursorLoader(getActivity(), TodoContentProvider.uriTodo, null, null, null, null);
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
}
