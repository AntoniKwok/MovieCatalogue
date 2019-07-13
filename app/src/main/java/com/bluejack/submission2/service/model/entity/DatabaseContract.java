package com.bluejack.submission2.service.model.entity;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_FAV = "favorite";

    public static final String AUTHORITY = "com.bluejack.submission2";
    private static final String SCHEME = "content";

    public static final class FavColumn implements BaseColumns {

        public static final String UID = "uid";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String VOTE = "vote";
        public static final String COVER_PATH = "cover_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String TYPE = "type";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAV)
                .build();
    }


}
