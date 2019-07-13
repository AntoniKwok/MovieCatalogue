package com.bluejack.submission2.view.interfaces;

import android.database.Cursor;

public interface LoadFavoriteCallback {

    void preExecute();

    void postExecute(Cursor cursor);
}
