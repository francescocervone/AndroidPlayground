package com.francescocervone.movies.data.cache.disk;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.francescocervone.movies.data.cache.SearchMoviesCache;
import com.francescocervone.movies.data.cache.CacheMissException;
import com.francescocervone.movies.domain.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SearchMoviesDiskCache implements SearchMoviesCache {
    private SearchMoviewDbHelper mDbHelper;
    private final Gson mGson;

    public SearchMoviesDiskCache(SearchMoviewDbHelper dbHelper) {
        mDbHelper = dbHelper;
        mGson = new Gson();
    }

    @Override
    public synchronized void put(String query, int page, List<Movie> movies) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SearchMoviewDbHelper.COLUMN_QUERY, query);
        contentValues.put(SearchMoviewDbHelper.COLUMN_PAGE, page);
        contentValues.put(SearchMoviewDbHelper.COLUMN_PAGE_BODY, mGson.toJson(movies));
        database.insert(SearchMoviewDbHelper.TABLE_NAME, null, contentValues);
        database.close();
    }

    @Override
    public synchronized List<List<Movie>> get(String query) throws CacheMissException {
        List<List<Movie>> movies = new ArrayList<>();
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = database.query(SearchMoviewDbHelper.TABLE_NAME,
                new String[]{SearchMoviewDbHelper.COLUMN_PAGE_BODY},
                SearchMoviewDbHelper.COLUMN_QUERY + "=?", new String[]{query}, null, null,
                SearchMoviewDbHelper.COLUMN_PAGE + " ASC");
        while (cursor.moveToNext()) {
            int bodyIndex = cursor.getColumnIndex(SearchMoviewDbHelper.COLUMN_PAGE_BODY);
            String json = cursor.getString(bodyIndex);
            List<Movie> page = mGson.fromJson(json, new TypeToken<List<Movie>>() {
            }.getType());
            movies.add(page);
        }
        cursor.close();
        database.close();
        if (movies.isEmpty()) {
            throw new CacheMissException();
        }
        return movies;
    }

    @Override
    public synchronized void clear(String query) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        database.delete(SearchMoviewDbHelper.TABLE_NAME, SearchMoviewDbHelper.COLUMN_QUERY + "=?",
                new String[]{query});
        database.close();
    }
}
