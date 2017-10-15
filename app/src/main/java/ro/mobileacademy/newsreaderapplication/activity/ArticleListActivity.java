package ro.mobileacademy.newsreaderapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.R;

public class ArticleListActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayList<String> listOfStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        setData();

        listView = findViewById(R.id.listview);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfStrings);

        listView.setAdapter(adapter);
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
