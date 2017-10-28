package ro.mobileacademy.newsreaderapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.adapters.ArticleAdapter;
import ro.mobileacademy.newsreaderapplication.events.ArticlesLoadEvent;
import ro.mobileacademy.newsreaderapplication.models.Article;
import ro.mobileacademy.newsreaderapplication.utils.HackerNewsAPI;
import ro.mobileacademy.newsreaderapplication.utils.VolleyRequestQueue;

public class ArticleListActivity extends AppCompatActivity implements Callback, ArticleAdapter.ArticleListener,
VolleyRequestQueue.VolleyCallback {

    private static final String TAG = ArticleListActivity.class.getSimpleName();

    private static final int NO_OF_ARTICLES = 20;
    private static final String EXTRA_ENDPOINT = "extra_endpoint";

    private ListView listView;

    private ArrayList<String> listOfStrings = new ArrayList<>();
    private ProgressDialog loadingDialog;
    private ArticleAdapter adapter;
    private ArrayList<Article> data;
    private String endpoint;


    public static void newInstance(Context context, String endPoint) {

        Intent intent = new Intent(context, ArticleListActivity.class);
        intent.putExtra(EXTRA_ENDPOINT, endPoint);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

//        setData();

        endpoint = getIntent().getStringExtra(EXTRA_ENDPOINT);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Loading");

        listView = findViewById(R.id.listview);

//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfStrings);

        //custom adapter
        data = new ArrayList<>();
        adapter = new ArticleAdapter(this, data);
        listView.setAdapter(adapter);

        // load new stories
        loadArticlesFromServer(endpoint);



        //todo check internet connection


//        JsonObjectRequest request = VolleyRequestQueue.getInstance().formatJsonGetRequest(endpoint, this);
        // using volley
//        VolleyRequestQueue.getInstance().addToRequestQueue(this, request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void loadArticlesFromServer(String url) {
        loadingDialog.show();
        try {
            HackerNewsAPI.retrieveStories(url, this);
        } catch (IOException e) {
            Log.e(TAG, "ERROR retrieve by url", e);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "failed to retrieve data", e);
        loadingDialog.dismiss();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String articleListJson = response.body().string();
        Log.d(TAG, "Received articles array " + articleListJson);

        new LoadArticlesAsync(loadingDialog).execute(articleListJson);
    }

    @Override
    public void onArticleSelected(Article article) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getUrl()));
        startActivity(i);
    }

    private static class LoadArticlesAsync extends AsyncTask<String, Void, ArrayList<Article>> {


        private final WeakReference<ProgressDialog> dialogWeakReference;

        private LoadArticlesAsync(ProgressDialog dialg) {
            // Use a WeakReference to ensure the progressDialog can be garbage collected
            dialogWeakReference = new WeakReference<ProgressDialog>(dialg);
        }

        @Override
        protected void onPreExecute() {
            final ProgressDialog dialog = dialogWeakReference.get();
            dialog.show();
        }

        protected ArrayList<Article> doInBackground(String... ids) {
            ArrayList<Article> articles = new ArrayList<>();
            try {
                JSONArray jsonArticlesArray = new JSONArray(ids[0]);
                //take the first NO_OF_ARTICLES articles
                for (int i = 0; i < Math.min(NO_OF_ARTICLES,jsonArticlesArray.length()); i++) {
                    String articleURL = HackerNewsAPI.getArticleById(jsonArticlesArray.getString(i));
                    String articleString = HackerNewsAPI.retrieveStories(articleURL);
                    if(articleString == null) continue;
                    JSONObject articleJson = new JSONObject(articleString);
                    articles.add(getNewsItemFromJSON(articleJson));
                }

            }catch (IOException |JSONException e){
                Log.e(TAG, "doInBackground: ", e);
            }
            return articles;

        }

        protected void onPostExecute(ArrayList<Article> result) {
            if (dialogWeakReference != null) {
                final ProgressDialog dialog = dialogWeakReference.get();
                dialog.dismiss();
            }

            // notify update list with new data
            EventBus.getDefault().post(new ArticlesLoadEvent(result));
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArticlesLoadEvent event) {
        Toast.makeText(this, "EventBus - event received!", Toast.LENGTH_SHORT).show();

        if(event.getData() != null) {
            data = event.getData();
            adapter.updateData(data);
        }
    }

    private static Article getNewsItemFromJSON(JSONObject json) {
        Article item = new Article();

        String title = json.optString("title");
        String url = json.optString("url");
        String time = json.optString("time");
        int id = json.optInt("id");

        item.setId(id);
        item.setUrl(url);
        item.setName(title);
        item.setTime(time);

        return item;
    }


    ///// volley callbacks
    @Override
    public void onResponse(String response) {
        Log.d(TAG, "onResponse - " + response);
    }

    @Override
    public void onJsonResponse(JSONObject response) {
        Log.d(TAG, "onJsonResponse - " + response);
    }

    @Override
    public void onError(VolleyError error) {
        Log.e(TAG, "onError, " + error);
    }

    private void showDialog() {
        loadingDialog.show();
    }

    private void hideDialog() {
        loadingDialog.dismiss();
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
}
