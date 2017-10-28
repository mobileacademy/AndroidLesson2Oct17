package ro.mobileacademy.newsreaderapplication.events;

import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 28/10/2017.
 */

public class ArticleUpdate {

    private ArrayList<Article> data;

    public ArticleUpdate(ArrayList<Article> list) {
        data = list;
    }

    public ArrayList<Article> getData() {
        return data;
    }
}
