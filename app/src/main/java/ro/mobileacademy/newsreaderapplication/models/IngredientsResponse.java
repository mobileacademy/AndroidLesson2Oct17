package ro.mobileacademy.newsreaderapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by valerica.plesu on 29/10/2017.
 */

public class IngredientsResponse {

    @SerializedName("results")
    ArrayList<Ingredient> results;


    public ArrayList<Ingredient> getResults() {
        return results;
    }
}
