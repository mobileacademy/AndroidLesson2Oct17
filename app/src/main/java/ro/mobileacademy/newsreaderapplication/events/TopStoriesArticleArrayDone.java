package ro.mobileacademy.newsreaderapplication.events;

import org.json.JSONArray;

/**
 * Created by valerica.plesu on 28/10/2017.
 */

public class TopStoriesArticleArrayDone {

    private JSONArray listOfIds;

    public TopStoriesArticleArrayDone(JSONArray items) {
        listOfIds = items;
    }

    public JSONArray getListOfIds() {
        return listOfIds;
    }

}
