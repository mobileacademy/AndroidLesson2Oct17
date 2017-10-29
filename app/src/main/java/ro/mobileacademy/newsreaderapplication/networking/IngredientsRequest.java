package ro.mobileacademy.newsreaderapplication.networking;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import ro.mobileacademy.newsreaderapplication.models.IngredientsResponse;

/**
 * Created by valerica.plesu on 29/10/2017.
 */

public class IngredientsRequest extends JsonRequest<IngredientsResponse> {

    protected final Gson mGson = new Gson();

    public IngredientsRequest(int method, String url, String requestBody,
                              Response.Listener<IngredientsResponse> listener,
                              Response.ErrorListener errorListener) {

        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<IngredientsResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(mGson.fromJson(jsonString, IngredientsResponse.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
