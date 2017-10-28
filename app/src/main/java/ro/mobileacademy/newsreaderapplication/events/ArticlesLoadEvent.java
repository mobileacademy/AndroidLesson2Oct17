package ro.mobileacademy.newsreaderapplication.events;

import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 25/10/2017.
 */

public class ArticlesLoadEvent {

    private ArrayList<Article> data;

    public ArticlesLoadEvent(ArrayList<Article> result) {
        data = result;
    }

    public ArrayList<Article> getData() {
        return data;
    }

    public void setData(ArrayList<Article> data) {
        this.data = data;
    }
}
