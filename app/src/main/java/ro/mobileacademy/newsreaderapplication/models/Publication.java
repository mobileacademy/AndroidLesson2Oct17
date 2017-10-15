package ro.mobileacademy.newsreaderapplication.models;

import android.support.annotation.DrawableRes;

/**
 * Created by valerica.plesu on 14/10/2017.
 */

public class Publication {

    private int id;
    private String name;
    private int pictureResource;

    //TODO to create a new model Article
    // private ArrayList<Article> articleList;

    public Publication(int id, String name, @DrawableRes int pictureRes) {
        this.id  = id;
        this.name = name;
        this.pictureResource = pictureRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureResource() {
        return pictureResource;
    }

    public void setPictureResource(int pictureResource) {
        this.pictureResource = pictureResource;
    }
}
