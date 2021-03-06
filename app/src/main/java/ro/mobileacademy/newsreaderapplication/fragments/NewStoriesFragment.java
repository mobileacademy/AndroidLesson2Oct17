package ro.mobileacademy.newsreaderapplication.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.mobileacademy.newsreaderapplication.NewsReaderApplication;
import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.adapters.ArticleCustomAdapter;
import ro.mobileacademy.newsreaderapplication.events.NewStoriesArticleUpdate;
import ro.mobileacademy.newsreaderapplication.events.TopStoriesArticleUpdate;
import ro.mobileacademy.newsreaderapplication.events.NewStoriesArticleArrayDone;
import ro.mobileacademy.newsreaderapplication.models.Article;
import ro.mobileacademy.newsreaderapplication.networking.HackerNewsAPI;
import ro.mobileacademy.newsreaderapplication.networking.VolleyRequestQueue;
import ro.mobileacademy.newsreaderapplication.utils.NewsReaderAppPref;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewStoriesFragment extends Fragment {

    private ListView listView;
    private ArticleCustomAdapter adapter;
    private ArrayList<Article> articleList = new ArrayList<>();
    private String downloadUrl;

    private ProgressDialog loadingDialog;

    public NewStoriesFragment() {
        // Required empty public constructor
    }

    public static NewStoriesFragment getInstance(String url) {
        NewStoriesFragment fragment = new NewStoriesFragment();
        // send params to fragment

        Bundle args = new Bundle();
        args.putString("url_key", url);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArticleCustomAdapter(getActivity(), articleList);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Downloading...");


        Bundle args = getArguments();
        downloadUrl = args.getString("url_key");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_stories, container, false);

        listView = (ListView) view.findViewById(R.id.article_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                Article article = articleList.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.getUrl()));
                getActivity().startActivity(intent);
            }
        });

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

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void loadArticlesFromServer() {
        loadingDialog.show();

        JsonArrayRequest request = VolleyRequestQueue.getInstance().formatJsonGetRequest(downloadUrl);
        VolleyRequestQueue.getInstance().addToRequestQueue(getActivity(), request);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NewStoriesArticleArrayDone event) {
        Toast.makeText(getActivity(), "EventBus - event received!", Toast.LENGTH_SHORT).show();

        JSONArray list = event.getListOfIds();
        if (list != null) {

            // save download last time
            NewsReaderAppPref pref = new NewsReaderAppPref(getActivity());
            long currentTime = System.currentTimeMillis();

            long lastDownloadedTime = pref.getLong("last_download_time");

            if (lastDownloadedTime != 0 && (lastDownloadedTime > currentTime + 5 * 3600 * 1000)) {

                pref.saveLong("last_download_time", currentTime);

            }

            //start async task
            new LoadArticleAsync(lastDownloadedTime).execute(list);

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NewStoriesArticleUpdate event) {
        Toast.makeText(getActivity(), "EventBus - Articles done!", Toast.LENGTH_SHORT).show();

        //dismiss progress dialog
        loadingDialog.dismiss();

        if (event.getData() != null) {
            adapter.updateData(event.getData());
        }
    }

    private static class LoadArticleAsync extends AsyncTask<JSONArray, Void, ArrayList<Article>> {

        private long lastDownloadedTime = 0;

        public LoadArticleAsync(long lastDownTime) {
            lastDownloadedTime = lastDownTime;
        }

        @Override
        protected ArrayList<Article> doInBackground(JSONArray... jsonArrays) {

            JSONArray jsonElements = jsonArrays[0];

            ArrayList<Article> result = new ArrayList<>();

            long currentTime = System.currentTimeMillis();

            if (lastDownloadedTime != 0 && (lastDownloadedTime > currentTime + 5 * 3600 * 1000)) {
                try {
                    for (int i = 0; i < 20; i++) {
                        String articleUrl = HackerNewsAPI.ITEM_ENDPOINT + jsonElements.getLong(i) + ".json";

                        String articleResponse = HackerNewsAPI.getArticle(articleUrl);
                        if (articleResponse == null) {
                            continue;
                        }

                        JSONObject articleJson = new JSONObject(articleResponse);

                        Article newArticle = HackerNewsAPI.parseJsonArticle(articleJson);
                        newArticle.setPublicationId(HackerNewsAPI.PUBLICATION_BIZIDAY_ID);

                        //insert just downloaded article into table
                        NewsReaderApplication.getInstance().getDataSource().insertArticle(newArticle);

                        result.add(newArticle);
                    }
                } catch (JSONException e) {
                    //error
                }

            } else {
                Log.d("@@@@@asynctask@@@@@@", "No need for fresh download");

                // read local data from db
                result = NewsReaderApplication.getInstance().getDataSource().
                        getAllArticlesByPublication(HackerNewsAPI.PUBLICATION_BIZIDAY_ID);
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            EventBus.getDefault().post(new NewStoriesArticleUpdate(articles));
        }
    }
}
