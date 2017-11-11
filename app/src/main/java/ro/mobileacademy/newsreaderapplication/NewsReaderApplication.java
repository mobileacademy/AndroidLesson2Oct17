package ro.mobileacademy.newsreaderapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import ro.mobileacademy.newsreaderapplication.database.ArticleDataSource;
import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 14/10/2017.
 */

public class NewsReaderApplication extends Application {

    private static final String MyPREFERENCES = "pref";

    private int counter = 10;

    private static NewsReaderApplication sInstance;

    private static ArticleDataSource dataSource;

    public static NewsReaderApplication getInstance() {
        if(sInstance == null) {
            sInstance = new NewsReaderApplication();
        }

        return  sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("counter", counter);
        editor.commit();


        int c =  sharedpreferences.getInt("counter", -1);
        Toast.makeText(this, "COUNTER=" + c, Toast.LENGTH_LONG).show();

        // create database
        dataSource = new ArticleDataSource(this);

        // open databasse
        dataSource.open();

    }

    public ArticleDataSource getDataSource () {
        return dataSource;
    }
}
