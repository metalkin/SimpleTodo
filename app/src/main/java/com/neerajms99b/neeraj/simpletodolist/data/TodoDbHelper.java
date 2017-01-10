package com.neerajms99b.neeraj.simpletodolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by neeraj on 31/12/16.
 */

public class TodoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tododb";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_TODO = "todotable";
    private static final String KEY_ID = "_id";
    private static final String COLUMN_WHAT = "whattodo";
    private static final String COLUMN_WHEN = "whentodo";


    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_NAME_TODO
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_WHAT + " TEXT, "
            + COLUMN_WHEN + " TEXT);";

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO:when you upgrade
    }
}
