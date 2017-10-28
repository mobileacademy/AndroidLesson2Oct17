package ro.mobileacademy.newsreaderapplication.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danielastamati on 21/04/16.
 */
public class Article {

    @SerializedName("id")
    private int id;

    @SerializedName("time")
    private String time;

    @SerializedName("name")
    private String name;

    @SerializedName("pictureResource")
    private int pictureResource;

    @SerializedName("url")
    private String url;

    @SerializedName("publicationId")
    private int publicationId;

    public Article() {}
    public Article(String name){
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }
}
