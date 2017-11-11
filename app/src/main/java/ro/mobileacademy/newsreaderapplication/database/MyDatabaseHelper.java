package ro.mobileacademy.newsreaderapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by valerica.plesu on 11/11/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = MyDatabaseHelper.class.getSimpleName();

    // ARTICLE table
    private static final String TABLE_ARTICLE = "article";
    public static final String ARTICLE_COLUMN_ID = "id";
    public static final String ARTICLE_COLUMN_TITLE = "title";
    public static final String ARTICLE_COLUMN_DATE = "date";
    public static final String ARTICLE_COLUMN_URL = "url";
    public static final String ARTICLE_COLUMN_PUBLICATION_ID = "pub_id";

    // PUBLICATION table
    private static final String TABLE_PUBLICATION = "publication";
    public static final String PUBLICATION_COLUMN_ID = "id";
    public static final String PUBLICATION_COLUMN_TITLE = "title";

    private static final String DATABASE_NAME = "newsreader.db";
    private static final int DATABASE_VERSION = 1;

    // CREATE ARTICLE SQL STATEMENT
    private static final String DATABASE_ARTICLE_CREATE = "create table "
            + TABLE_ARTICLE + "("
            + ARTICLE_COLUMN_ID + " integer primary key not null, "
            + ARTICLE_COLUMN_TITLE + " text not null, "
            + ARTICLE_COLUMN_DATE + " long, "
            + ARTICLE_COLUMN_URL + " text, "
            + ARTICLE_COLUMN_PUBLICATION_ID + " integer not null);";

    // CREATE PUBLICATION TABLE
    private static final String DATABASE_PUBLICATION_CREATE = "create table "
            + TABLE_PUBLICATION + "("
            + PUBLICATION_COLUMN_ID + " integer primary key not null, "
            + PUBLICATION_COLUMN_TITLE + " text not null);";

    public MyDatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "create " + DATABASE_NAME + " " + DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_ARTICLE_CREATE);
        Log.d(TAG, DATABASE_ARTICLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to version " + newVersion);


        // drop tables and db create
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLICATION);

        onCreate(sqLiteDatabase);
    }
}
