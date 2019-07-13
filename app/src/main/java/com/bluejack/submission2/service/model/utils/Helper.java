package com.bluejack.submission2.service.model.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bluejack.submission2.service.model.entity.DatabaseContract;

class Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "submission1_db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY ," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s REAL NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_FAV,
            DatabaseContract.FavColumn.UID,
            DatabaseContract.FavColumn.TITLE,
            DatabaseContract.FavColumn.DATE,
            DatabaseContract.FavColumn.DESCRIPTION,
            DatabaseContract.FavColumn.VOTE,
            DatabaseContract.FavColumn.COVER_PATH,
            DatabaseContract.FavColumn.BACKDROP_PATH,
            DatabaseContract.FavColumn.TYPE
    );

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAV);
        onCreate(db);
    }
}
