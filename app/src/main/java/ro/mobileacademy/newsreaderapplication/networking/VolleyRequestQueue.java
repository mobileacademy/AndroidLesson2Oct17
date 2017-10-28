package ro.mobileacademy.newsreaderapplication.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import ro.mobileacademy.newsreaderapplication.events.ArticleArrayDone;

/**
 * Created by valerica.plesu on 28/10/2017.
 */

public class VolleyRequestQueue {

    private static final String TAG = VolleyRequestQueue.class.getSimpleName();

    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized VolleyRequestQueue getInstance() {
        if(mInstance == null) {
            mInstance = new VolleyRequestQueue();
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue(Context context) {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    public <T>  void addToRequestQueue(Context context, Request<T> request) {
        getRequestQueue(context).add(request);
    }

    public JsonArrayRequest formatJsonGetRequest(String url) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "resp=" + response);

                EventBus.getDefault().post(new ArticleArrayDone(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error, ", error);
            }
        });

        return request;
    }

    public JsonObjectRequest formatPostRequest(String url, HashMap<String, String> params) throws JSONException{
        // params looks like

        /** "id": 12
         *  "name":abcd
         *  addresss: "bucharest"
         *
         */

        String stringJson = "{\n" +
                "  \"by\" : \"rl3\",\n" +
                "  \"descendants\" : 0,\n" +
                "  \"id\" : 10900279,\n" +
                "  \"score\" : 3,\n" +
                "  \"time\" : 1452762088,\n" +
                "  \"title\" : \"Ann Caracristi, who cracked codes, and the glass ceiling, at NSA, dies at 94\",\n" +
                "  \"type\" : \"story\",\n" +
                "  \"url\" : \"https://www.washingtonpost.com/national/ann-caracristi-who-excelled-at-code-breaking-and-management-dies-at-94/2016/01/11/b8187468-b80d-11e5-b682-4bb4dd403c7d_story.html\"\n" +
                "}";

        JSONObject obj = new JSONObject(stringJson);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return request;

    }

 }
