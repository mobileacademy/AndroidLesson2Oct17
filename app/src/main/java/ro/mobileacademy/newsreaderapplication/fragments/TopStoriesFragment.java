package ro.mobileacademy.newsreaderapplication.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.adapters.ArticleCustomAdapter;
import ro.mobileacademy.newsreaderapplication.events.ArticleArrayDone;
import ro.mobileacademy.newsreaderapplication.events.ArticleUpdate;
import ro.mobileacademy.newsreaderapplication.models.Article;
import ro.mobileacademy.newsreaderapplication.networking.HackerNewsAPI;
import ro.mobileacademy.newsreaderapplication.networking.VolleyRequestQueue;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopStoriesFragment extends Fragment {

    private ListView listView;
    private ArticleCustomAdapter adapter;
    private ArrayList<Article> articleList = new ArrayList<>();

    private ProgressDialog loadingDialog;

    public TopStoriesFragment() {
        // Required empty public constructor
    }

    public static TopStoriesFragment getInstance() {
        TopStoriesFragment fragment = new TopStoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArticleCustomAdapter(getActivity(), articleList);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Downloading...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_top_stories, container, false);

        listView = (ListView) view.findViewById(R.id.article_list);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        loadArticlesFromServer();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

        if(loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void loadArticlesFromServer() {
        loadingDialog.show();

        JsonArrayRequest request = VolleyRequestQueue.getInstance().formatJsonGetRequest(HackerNewsAPI.TOP_STORIES_ENDPOINT);
        VolleyRequestQueue.getInstance().addToRequestQueue(getActivity(), request);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArticleArrayDone event) {
        Toast.makeText(getActivity(), "EventBus - event received!", Toast.LENGTH_SHORT).show();

        JSONArray list = event.getListOfIds();
        if(list != null) {
            //start async task
            new LoadArticleAsync().execute(list);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArticleUpdate event) {
        Toast.makeText(getActivity(), "EventBus - Articles done!", Toast.LENGTH_SHORT).show();

        //dismiss progress dialog
        loadingDialog.dismiss();

        if(event.getData() != null) {
            adapter.updateData(event.getData());
        }
    }

    private static class LoadArticleAsync extends AsyncTask<JSONArray, Void, ArrayList<Article>> {

        @Override
        protected ArrayList<Article> doInBackground(JSONArray... jsonArrays) {
            JSONArray jsonElements = jsonArrays[0];

            ArrayList<Article> result = new ArrayList<>();
            try {
                for(int i=0; i< 20; i++) {
                    String articleUrl = HackerNewsAPI.ITEM_ENDPOINT + jsonElements.getLong(i) + ".json";

                    String articleResponse = HackerNewsAPI.getArticle(articleUrl);
                    if(articleResponse == null) {
                        continue;
                    }

                    JSONObject articleJson = new JSONObject(articleResponse);
                    result.add(HackerNewsAPI.parseJsonArticle(articleJson));
                }
            } catch (JSONException e) {
                //error
            }
           return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            EventBus.getDefault().post(new ArticleUpdate(articles));
        }
    }
}
