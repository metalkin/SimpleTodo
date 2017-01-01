package com.neerajms99b.neeraj.simpletodo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by neeraj on 31/12/16.
 */

public class TodoContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.neerajms99b.neeraj.simpletodo.data";
    private static final String TABLE_NAME_TODO = "todotable";
    private static final String URL_TODO = "content://" + AUTHORITY + "/" + TABLE_NAME_TODO;
    public static final Uri uriTodo = Uri.parse(URL_TODO);
    private SQLiteDatabase database;

    public static final String KEY_ID = "_id";
    public static final String COLUMN_WHAT = "whattodo";
    public static final String COLUMN_WHEN = "whentodo";

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME_TODO, 1);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME_TODO + "/#", 2);
    }

    @Override
    public boolean onCreate() {
        TodoDbHelper dbHelper = new TodoDbHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return (database != null);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case 1:
                queryBuilder.setTables(TABLE_NAME_TODO);
                break;
            case 2:
                queryBuilder.setTables(TABLE_NAME_TODO);
                selection = KEY_ID + "=" + uri.getLastPathSegment();
                break;
            default:
                return null;
        }
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowId = database.insert(TABLE_NAME_TODO, null, contentValues);
        if (rowId > 0) {
            String url = String.valueOf(uri) + "/" + String.valueOf(rowId);
            Uri returnUri = Uri.parse(url);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String column, String[] args) {
        String whereClause = KEY_ID + "=" + uri.getLastPathSegment();
        return database.delete(TABLE_NAME_TODO, whereClause, args);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] args) {
        String whereClause = KEY_ID + "=" + uri.getLastPathSegment();
        return database.update(TABLE_NAME_TODO, contentValues, whereClause, args);
    }
}
