package com.francescocervone.movies.data.cache.disk;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NowPlayingMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "now_playing_movies";

    public static final int VERSION = 1;

    public static final String COLUMN_PAGE = "page_number";

    public static final String COLUMN_PAGE_BODY = "page_body";

    public static final String TABLE_NAME = "movies";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            COLUMN_PAGE + " INTEGER PRIMARY KEY, " +
            COLUMN_PAGE_BODY + " TEXT " +
            ")";

    public NowPlayingMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
