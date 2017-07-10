package com.example.anirudh.lookup.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anirudh on 08/07/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lookup.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HistoryTable.CMD_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + HistoryTable.TABLE_NAME);
        db.execSQL(HistoryTable.CMD_CREATE_TABLE);


    }
}

