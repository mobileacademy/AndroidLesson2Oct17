package ro.mobileacademy.newsreaderapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.NewsReaderApplication;
import ro.mobileacademy.newsreaderapplication.models.Article;
import ro.mobileacademy.newsreaderapplication.networking.HackerNewsAPI;
import ro.mobileacademy.newsreaderapplication.utils.GlobalUtils;

/**
 * Created by danielastamati on 17/05/16.
 */
public class FetchArticlesService extends IntentService {

    private static final String TAG = "FetchArticlesService";
    private int NO_OF_ARTICLES = 20;

    public FetchArticlesService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ArrayList<Article> topStoriesList;
        ArrayList<Article> newStoriesList;

        String idsArrayHN = HackerNewsAPI.getStories(HackerNewsAPI.TOP_STORIES_ENDPOINT);
        topStoriesList = fetchArticlesByIdsArray(idsArrayHN);
        String idsArrayFC = HackerNewsAPI.getStories(HackerNewsAPI.NEW_STORIES_ENDPOINT);
        newStoriesList = fetchArticlesByIdsArray(idsArrayFC);

        saveToDb(GlobalUtils.HACKER_NEWS_ID, topStoriesList);
        saveToDb(GlobalUtils.FAST_COMPANY_ID, newStoriesList);

    }

    private void saveToDb(int publicationId, ArrayList<Article> articles) {
        for (Article article : articles) {
            //TODO: can be set in a single DB transaction
            article.setPublicationId(publicationId);
            NewsReaderApplication.getInstance().getDatasource().createArticle(article.getId(), article.getTitle(), article.getTime(), article.getUrl(), article.getPublicationId());
        }
    }

    private Article getNewsItemFromJSON(JSONObject json) {
        Article item = new Article();

        String title = json.optString("title");
        String url = json.optString("url");
        int id = json.optInt("id");

        item.setId(id);
        item.setUrl(url);
        item.setTitle(title);

        return item;
    }


    private ArrayList<Article> fetchArticlesByIdsArray(String idsArray) {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            JSONArray jsonArticlesArray = new JSONArray(idsArray);
            //take the first NO_OF_ARTICLES articles
            for (int i = 0; i < Math.min(NO_OF_ARTICLES, jsonArticlesArray.length()); i++) {
                String articleURL = jsonArticlesArray.getString(i) + ".json";
                String articleString = HackerNewsAPI.getArticle(articleURL);
                if (articleString == null) continue;
                JSONObject articleJson = new JSONObject(articleString);
                articles.add(getNewsItemFromJSON(articleJson));
            }

        } catch (JSONException e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return articles;
    }
}
