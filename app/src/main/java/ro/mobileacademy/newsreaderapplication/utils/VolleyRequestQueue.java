package ro.mobileacademy.newsreaderapplication.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by valerica.plesu on 23/10/2017.
 */

public class VolleyRequestQueue {

    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized VolleyRequestQueue getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Context context, Request<T> req) {
        getRequestQueue(context).add(req);
    }

    public StringRequest formatGetRequest(String url, final VolleyCallback callback) {

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        callback.onError(error);
                    }
                });

        return stringRequest;
    }

    public JsonObjectRequest formatJsonGetRequest(String url, final VolleyCallback callback) {
        Log.d("volley", "formatJsonGetRequest");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onJsonResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);

                    }
                });

        return jsObjRequest;
    }

    public interface VolleyCallback {
        void onResponse(String response);
        void onJsonResponse(JSONObject response);
        void onError(VolleyError error);
    }
}
