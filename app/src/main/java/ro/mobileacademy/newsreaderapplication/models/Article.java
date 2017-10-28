package ro.mobileacademy.newsreaderapplication.models;

/**
 * Created by valerica.plesu on 28/10/2017.
 */

public class Article {

    private long id;
    private String title;
    private String url;
    private long time;

    public Article (String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
