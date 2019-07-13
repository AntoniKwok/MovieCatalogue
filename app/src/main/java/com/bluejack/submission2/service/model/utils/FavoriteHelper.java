package com.bluejack.submission2.service.model.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bluejack.submission2.service.model.entity.Favorite;

import java.util.ArrayList;

import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.BACKDROP_PATH;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.COVER_PATH;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.DATE;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.DESCRIPTION;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.TITLE;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.TYPE;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.UID;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.VOTE;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.TABLE_FAV;

public class FavoriteHelper {

    private static final String DATABASE_TABLE = TABLE_FAV;
    private static Helper dataBaseHelper;
    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        dataBaseHelper = new Helper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    private void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    //view
    public ArrayList<Favorite> getAllFavoritebyType(String type) {
        ArrayList<Favorite> arrayList = new ArrayList<>();
        String whereClause = "type LIKE '" + type + "'";
        database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_FAV, null,
                whereClause,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Favorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setUid(cursor.getString(cursor.getColumnIndexOrThrow(UID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                favorite.setVote(cursor.getFloat(cursor.getColumnIndexOrThrow(VOTE)));
                favorite.setCover_photo(cursor.getString(cursor.getColumnIndexOrThrow(COVER_PATH)));
                favorite.setBackdrop_photo(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                favorite.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        } else {
            Log.d("Data", "No Data");
        }
        cursor.close();
        return arrayList;
    }

    //view
    public ArrayList<Favorite> getAllFavorite() {
        ArrayList<Favorite> arrayList = new ArrayList<>();
        database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_FAV, null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Favorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setUid(cursor.getString(cursor.getColumnIndexOrThrow(UID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                favorite.setVote(cursor.getFloat(cursor.getColumnIndexOrThrow(VOTE)));
                favorite.setCover_photo(cursor.getString(cursor.getColumnIndexOrThrow(COVER_PATH)));
                favorite.setBackdrop_photo(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                favorite.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        } else {
            Log.d("Data", "No Data");
        }
        cursor.close();
        return arrayList;
    }

    //view
    public Favorite getAllFavoriteById(String uid) {
//        ArrayList<Favorite> arrayList = new ArrayList<>();
        String whereClause = "uid LIKE '" + uid + "'";
        database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_FAV, null,
                whereClause,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Favorite favorite = null;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setUid(cursor.getString(cursor.getColumnIndexOrThrow(UID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                favorite.setVote(cursor.getFloat(cursor.getColumnIndexOrThrow(VOTE)));
                favorite.setCover_photo(cursor.getString(cursor.getColumnIndexOrThrow(COVER_PATH)));
                favorite.setBackdrop_photo(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                favorite.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
//                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        } else {
            Log.d("Data", "No Data");
        }
        cursor.close();
        return favorite;
    }

    public void addFavorite(Favorite favorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, favorite.getUid());
        contentValues.put(TITLE, favorite.getTitle());
        contentValues.put(DESCRIPTION, favorite.getDesc());
        contentValues.put(DATE, favorite.getReleaseDate());
        contentValues.put(VOTE, favorite.getVote());
        contentValues.put(COVER_PATH, favorite.getCover_photo());
        contentValues.put(BACKDROP_PATH, favorite.getBackdrop_photo());
        contentValues.put(TYPE, favorite.getType());
        database.insert(TABLE_FAV, null, contentValues);
        close();
    }

    public void removeFavorite(String id) {
        database.delete(DATABASE_TABLE, UID + " = '" + id + "'", null);
    }

    public boolean checkFavorite(String id) {
        return findId(id).size() > 0;
    }

    private ArrayList<Favorite> findId(String id) {
        ArrayList<Favorite> arrayList = new ArrayList<>();
        String whereClause = "uid LIKE '" + id + "'";
        database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_FAV, null,
                whereClause,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Favorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setUid(cursor.getString(cursor.getColumnIndexOrThrow(UID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                favorite.setVote(cursor.getFloat(cursor.getColumnIndexOrThrow(VOTE)));
                favorite.setCover_photo(cursor.getString(cursor.getColumnIndexOrThrow(COVER_PATH)));
                favorite.setBackdrop_photo(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                favorite.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        } else {
            Log.d("Data", "No Data");
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , UID + " LIKE ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , null);
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, UID + " LIKE ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, UID + " LIKE ?", new String[]{id});
    }


}
