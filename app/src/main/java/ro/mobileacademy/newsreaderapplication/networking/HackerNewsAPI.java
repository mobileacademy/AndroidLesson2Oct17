package ro.mobileacademy.newsreaderapplication.networking;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 28/10/2017.
 */

public class HackerNewsAPI {

    public static final String BASE_ENDPOINT = "https://hacker-news.firebaseio.com/v0/";
    public static final String TOP_STORIES_ENDPOINT = BASE_ENDPOINT + "topstories.json";
    public static final String NEW_STORIES_ENDPOINT = BASE_ENDPOINT + "newstories.json";
    public static final String ITEM_ENDPOINT = BASE_ENDPOINT + "item/";


    //declare okhttp instance
    private static OkHttpClient httpClientInstance;

    public static OkHttpClient getHttpClientInstance() {
        if(httpClientInstance == null) {
            httpClientInstance = new OkHttpClient();
        }
        return httpClientInstance;
    }

    public static String getTopStories (String url) {
        Request getRequest = new Request.Builder().url(url).build();

        Response response;
        try {

            response = getHttpClientInstance().newCall(getRequest).execute();
            return response.body().string();

        } catch (IOException e) {
            return null;
        }
    }

    public static String getArticle(String url) {
        Request getRequest = new Request.Builder().url(url).build();

        Response response;
        try {

            response = getHttpClientInstance().newCall(getRequest).execute();
            return response.body().string();

        } catch (IOException e) {
            return null;
        }
    }

    public static Article parseJsonArticle(JSONObject json) {

        String title = json.optString("title");

        Article item = new Article(title);


        String url = json.optString("url");
        long date = json.optLong("time");

        item.setTime(date);
        item.setUrl(url);

        return item;

    }

}
