package ro.mobileacademy.newsreaderapplication.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.adapters.ArticleCustomAdapter;
import ro.mobileacademy.newsreaderapplication.events.ArticleArrayDone;
import ro.mobileacademy.newsreaderapplication.events.ArticleUpdate;
import ro.mobileacademy.newsreaderapplication.events.CountFinishEvent;
import ro.mobileacademy.newsreaderapplication.models.Article;
import ro.mobileacademy.newsreaderapplication.networking.HackerNewsAPI;
import ro.mobileacademy.newsreaderapplication.networking.IngredientsRequest;
import ro.mobileacademy.newsreaderapplication.networking.VolleyRequestQueue;

public class ArticleListActivity extends AppCompatActivity {

    private static final String TAG = ArticleListActivity.class.getSimpleName();

    ListView listView;

    private ArrayList<String> listOfStrings = new ArrayList<>();

    private ArticleCustomAdapter adapter;

    private ArrayList<Article> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        setData();
        setItems();

        listView = findViewById(R.id.listview);

//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfStrings);

        adapter = new ArticleCustomAdapter(this, items);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get latest articles from API
        //new LoadTopStoriesAsync().execute(HackerNewsAPI.TOP_STORIES_ENDPOINT);


        JsonArrayRequest request = VolleyRequestQueue.getInstance().formatJsonGetRequest(HackerNewsAPI.TOP_STORIES_ENDPOINT);

        VolleyRequestQueue.getInstance().addToRequestQueue(this, request);

        EventBus.getDefault().register(this);



        // handle POST request
//        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("id", "12");
//        paramMap.put("name", "test");
//
//
//        try {
//            JsonObjectRequest request1 = VolleyRequestQueue.getInstance().formatPostRequest("url",paramMap );
//
//            VolleyRequestQueue.getInstance().addToRequestQueue(this, request1);
//        } catch (JSONException e) {
//
//        }

        // TESTING CUSTOM GET REQUEST
        String url = "https://wger.de/api/v2/ingredient";
        IngredientsRequest ingredientsRequest = VolleyRequestQueue.getInstance().getIngredientsRequest(url);
        //handle get ingredients request
        VolleyRequestQueue.getInstance().addToRequestQueue(this, ingredientsRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(ArticleUpdate event) {
//        Toast.makeText(this, "EventBus - event received!", Toast.LENGTH_SHORT).show();
//
//        ArrayList<Article> list = event.getData();
//        if(list != null) {
//            adapter.updateData(list);
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArticleArrayDone event) {
        Toast.makeText(this, "EventBus - event received!", Toast.LENGTH_SHORT).show();

        JSONArray list = event.getListOfIds();
        if(list != null) {

            // TODO parse array and do a GET call for each id
        }
    }

    private void setData() {
        String article1 = "Title1";
        String article2 = "Title2";
        String article3 = "Title3";
        String article4 = "Title4";
        String article5 = "Title5";
        String article6 = "Title6";
        String article7 = "Title7";
        String article8 = "Title8";

        listOfStrings.add(article1);
        listOfStrings.add(article2);
        listOfStrings.add(article3);
        listOfStrings.add(article4);
        listOfStrings.add(article5);
        listOfStrings.add(article6);
        listOfStrings.add(article7);
        listOfStrings.add(article8);

    }

    private void setItems () {

        Article a1 = new Article("Article 1");
        Article a2 = new Article("Article 2");

        Article a3 = new Article("Article 3");

        Article a4 = new Article("Article 4");

        Article a5 = new Article("Article 5");

        Article a6 = new Article("Article 6");

        items.add(a1);
        items.add(a2);
        items.add(a3);
        items.add(a4);
        items.add(a5);
        items.add(a6);
    }


    private static class LoadTopStoriesAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        @Override
        protected ArrayList<Article> doInBackground(String... strings) {
            Log.d(TAG, "doInBackground");
            String url = strings[0];
            // background thread
            String response = HackerNewsAPI.getTopStories(url);
            ArrayList<Article> list = new ArrayList<>();

            if(response != null) {
                try {
                    JSONArray jsoArticlesArray = new JSONArray(response);
                    Log.d(TAG, "items: " + jsoArticlesArray.length());
                    for(int i=0; i< 20; i++) {
                        String articleUrl = HackerNewsAPI.ITEM_ENDPOINT + jsoArticlesArray.getLong(i) + ".json";
                        Log.i(TAG, "articleUrl = " + articleUrl);

                        String articleResponse = HackerNewsAPI.getArticle(articleUrl);
                        if(articleResponse == null) continue;

                        JSONObject articleJson = new JSONObject(articleResponse);
                        Log.i(TAG, "articleJson=" + articleJson);

                        Article article = HackerNewsAPI.parseJsonArticle(articleJson);
                        list.add(article);
                    }
                    Log.d(TAG, "list size = " + list.size());
                } catch (JSONException e) {
                    Log.e(TAG, "error parsing stories response, ", e);
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> list) {

            //mainThread
            Log.d(TAG, "response size = " + list.size());
            EventBus.getDefault().post(new ArticleUpdate(list
           ));
        }
    }
}
