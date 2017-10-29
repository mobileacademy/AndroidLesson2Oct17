package ro.mobileacademy.newsreaderapplication.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by valerica.plesu on 29/10/2017.
 */

/**
 * {
 "id": 9217,
 "license_author": "quintern",
 "status": "1",
 "creation_date": "2017-02-26",
 "update_date": "2017-02-26",
 "name": "365 Organic Green Lentils - Raw",
 "energy": 360,
 "protein": "26.000",
 "carbohydrates": "60.000",
 "carbohydrates_sugar": "2.000",
 "fat": "1.000",
 "fat_saturated": "0.000",
 "fibres": null,
 "sodium": "0.000",
 "license": 2,
 "language": 2
 }
 */
public class Ingredient {
    @SerializedName("id") String id;
    @SerializedName("license_author") String author;
    @SerializedName("status") String status;
    @SerializedName("creation_date") String creationDate;
    @SerializedName("update_date") String updateDate;
    @SerializedName("name") String name;

    public String getName() {
        return name;
    }
}
