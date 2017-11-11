package ro.mobileacademy.newsreaderapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    public void insertArticle (Article article) {
        Log.d(TAG, "insertArticle");

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_ID, article.getId());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_TITLE, article.getTitle());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_DATE, article.getTime());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_URL, article.getUrl());
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_PUBLICATION_ID, article.getPublicationId());

        // insert operation into table

        long insertedId = database.insertWithOnConflict(MyDatabaseHelper.TABLE_ARTICLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "insertId = " + insertedId);
    }

    public List<Article> getAllArticlesByPublication(int publicationId) {

        List<Article> articles = new ArrayList<>();

        Cursor cursor = database.query(MyDatabaseHelper.TABLE_ARTICLE, allArticleColumns,
                MyDatabaseHelper.ARTICLE_COLUMN_PUBLICATION_ID + " = " + publicationId,
                null, null, null, null);

        if (cursor != null) {
            // go to first entry
            cursor.moveToFirst();

            while (cursor.moveToNext()) {
                Article article = cursorToArticle(cursor);
                articles.add(article);
                cursor.moveToNext();
            }

            // !!!! CLOSE CURSOR
            cursor.close();
        }

        return articles;
    }

    private Article cursorToArticle(Cursor cursor) {

        Article article = new Article();

        article.setId(cursor.getLong(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_ID)));
        article.setTitle(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_TITLE)));
        article.setTime(cursor.getLong(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_DATE)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_URL)));
        article.setPublicationId(cursor.getLong(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_PUBLICATION_ID)));

        return article;
    }
}
