package com.francescocervone.movies.data.cache.disk;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.francescocervone.movies.data.cache.NowPlayingMoviesCache;
import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.francescocervone.movies.data.cache.disk.NowPlayingMoviesDbHelper.COLUMN_PAGE;
import static com.francescocervone.movies.data.cache.disk.NowPlayingMoviesDbHelper.COLUMN_PAGE_BODY;
import static com.francescocervone.movies.data.cache.disk.NowPlayingMoviesDbHelper.TABLE_NAME;

public class DbNowPlayingMoviesCache implements NowPlayingMoviesCache {
    private final Gson mGson;
    private NowPlayingMoviesDbHelper mDbHelper;

    public DbNowPlayingMoviesCache(NowPlayingMoviesDbHelper dbHelper) {
        mDbHelper = dbHelper;
        mGson = new Gson();
    }

    @Override
    public void put(int page, List<Movie> movies) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAGE, page);
        values.put(COLUMN_PAGE_BODY, mGson.toJson(movies));
        database.replace(TABLE_NAME, null, values);
        database.close();
    }

    @Override
    public List<List<Movie>> get() throws CacheMissException {
        List<List<Movie>> pages = new ArrayList<>();
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_PAGE, COLUMN_PAGE_BODY}, null, null, null, null, COLUMN_PAGE);
        int pageIndex = cursor.getColumnIndex(COLUMN_PAGE_BODY);
        while (cursor.moveToNext()) {
            String pageBody = cursor.getString(pageIndex);
            pages.add(mGson.fromJson(pageBody, new TypeToken<List<Movie>>() {
            }.getType()));
        }
        cursor.close();
        database.close();
        return pages;
    }

    @Override
    public void clear() {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
        database.close();
    }
}
