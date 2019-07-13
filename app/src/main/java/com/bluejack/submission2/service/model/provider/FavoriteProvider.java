package com.bluejack.submission2.service.model.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bluejack.submission2.service.model.utils.FavoriteHelper;
import com.bluejack.submission2.view.activity.MainActivity;

import static com.bluejack.submission2.service.model.entity.DatabaseContract.AUTHORITY;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.CONTENT_URI;
import static com.bluejack.submission2.service.model.entity.DatabaseContract.TABLE_FAV;

public class FavoriteProvider extends ContentProvider {

    private static final int FAV = 1;
    private static final int FAV_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {
        // content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV, FAV);
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV + "/#", FAV_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                cursor = favoriteHelper.queryProvider();
                break;
            case FAV_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoriteHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                added = favoriteHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        if(getContext()!=null){
            getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoriteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAV_ID:
                deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        if(getContext()!=null){
            getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        }


        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoriteHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAV_ID:
                updated = favoriteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        if(getContext()!=null){
            getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        }


        return updated;
    }
}
