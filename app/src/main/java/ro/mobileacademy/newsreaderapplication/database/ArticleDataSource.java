package ro.mobileacademy.newsreaderapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 11/11/2017.
 */

// handles database operations
public class ArticleDataSource {

    private static final String TAG = ArticleDataSource.class.getSimpleName();

    // get db instance
    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;

    private String[] allArticleColumns = {MyDatabaseHelper.ARTICLE_COLUMN_ID,
                MyDatabaseHelper.ARTICLE_COLUMN_TITLE, MyDatabaseHelper.ARTICLE_COLUMN_DATE,
                MyDatabaseHelper.ARTICLE_COLUMN_URL, MyDatabaseHelper.ARTICLE_COLUMN_PUBLICATION_ID};

    public ArticleDataSource (Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public void open () {
        Log.d(TAG, "open databse");
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Article createArticle (Article article) {
        Log.d(TAG, "createArticle");

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_ID, article.getId());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_TITLE, article.getTitle());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_DATE, article.getTime());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_URL, article.getUrl());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_PUBLICATION_ID, article.getPublicationId());

        // insert operation into table

        database.insertWithOnConflict()

    }
}
