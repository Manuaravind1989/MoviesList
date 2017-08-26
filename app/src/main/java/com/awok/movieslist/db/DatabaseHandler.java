package com.awok.movieslist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.awok.movieslist.moviesDetails.MoviesDetailModel;

import java.util.ArrayList;

/**
 * Created by Manu on 8/26/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movielist";
    private static final String TABLE_NAME = "movietable";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "releasedate";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String BUDGET = "budget";
    private static final String REVENUE = "revenue";
    private static final String STATUS = "status";
    private static final String POSTER_IMAGE = "posterimage";
    private static final String BACKDROP_IMAGE = "backdropimage";
    private static final String OVERVIEW = "overview";
    private static final String POPULARITY = "popularity";
    private static final String HOME_PAGE = "homePage";
    // private static final String


    Context c;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + RELEASE_DATE + " TEXT,"
                + ORIGINAL_TITLE + " TEXT," + VOTE_COUNT + " TEXT," + VOTE_AVERAGE + " TEXT," + BUDGET + " TEXT," + STATUS + " TEXT,"
                + POSTER_IMAGE + " TEXT," + BACKDROP_IMAGE + " TEXT," + OVERVIEW + " TEXT," + POPULARITY + " TEXT," + HOME_PAGE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    public void addMovies(MoviesDetailModel resultDetailModelsEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, resultDetailModelsEntity.getTitle());
        contentValues.put(TITLE, resultDetailModelsEntity.getTitle());
        contentValues.put(RELEASE_DATE, resultDetailModelsEntity.getRelease_date());
        contentValues.put(ORIGINAL_TITLE, resultDetailModelsEntity.getOriginal_title());
        contentValues.put(VOTE_COUNT, resultDetailModelsEntity.getVote_count());
        contentValues.put(VOTE_AVERAGE, resultDetailModelsEntity.getVote_average());
        contentValues.put(BUDGET, resultDetailModelsEntity.getBudget());
        contentValues.put(REVENUE, resultDetailModelsEntity.getRevenue());
        contentValues.put(STATUS, resultDetailModelsEntity.getStatus());

        contentValues.put(POSTER_IMAGE, resultDetailModelsEntity.getPoster_path());
        contentValues.put(BACKDROP_IMAGE, resultDetailModelsEntity.getBackdrop_path());
        contentValues.put(OVERVIEW, resultDetailModelsEntity.getOverview());

        contentValues.put(POPULARITY, resultDetailModelsEntity.getPopularity());
        contentValues.put(HOME_PAGE, resultDetailModelsEntity.getHomepage());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public ArrayList<MoviesDetailModel> getFavouriteMovieList() {
        ArrayList<MoviesDetailModel> _ArrayList = new ArrayList<MoviesDetailModel>();
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MoviesDetailModel item = new MoviesDetailModel();
                // item.setRow_id(cursor.getString(cursor.getColumnIndex(ROW_ID)));
                item.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                item.setTitle(cursor.getString(cursor
                        .getColumnIndex(TITLE)));
                item.setRelease_date(cursor.getString(cursor
                        .getColumnIndex(RELEASE_DATE)));
                item.setOriginal_title(cursor.getString(cursor
                        .getColumnIndex(ORIGINAL_TITLE)));
                item.setVote_count(cursor.getInt(cursor
                        .getColumnIndex(VOTE_COUNT)));
                item.setVote_average(cursor.getInt(cursor
                        .getColumnIndex(VOTE_AVERAGE)));
                item.setBudget(cursor.getInt(cursor
                        .getColumnIndex(BUDGET)));
                item.setRevenue(cursor.getInt(cursor
                        .getColumnIndex(REVENUE)));

                item.setPoster_path(cursor.getString(cursor
                        .getColumnIndex(POSTER_IMAGE)));
                item.setBackdrop_path(cursor.getString(cursor
                        .getColumnIndex(BACKDROP_IMAGE)));
                item.setOverview(cursor.getString(cursor
                        .getColumnIndex(OVERVIEW)));

                item.setPopularity(cursor.getInt(cursor
                        .getColumnIndex(POPULARITY)));
                item.setHomepage(cursor.getString(cursor
                        .getColumnIndex(HOME_PAGE)));
                // adding to todo list
                _ArrayList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return _ArrayList;
    }








}
