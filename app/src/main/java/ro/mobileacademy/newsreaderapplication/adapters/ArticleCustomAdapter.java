package ro.mobileacademy.newsreaderapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 28/10/2017.
 */

public class ArticleCustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Article> myData;

    public ArticleCustomAdapter(Context context, ArrayList<Article> data) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myData = data;
    }

    public void updateData (ArrayList<Article> items) {
        myData.clear();
        myData.addAll(items);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Article getItem(int i) {
        return myData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        // ViewHolder pattern
        if(view == null) {

            // do layout inflate
            view = inflater.inflate(R.layout.article_list_item, viewGroup, false);

            // init viewholder views
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.date = (TextView) view.findViewById(R.id.date);

            view.setTag(viewHolder); // used to re-use views
        } else {
           viewHolder = (ViewHolder) view.getTag();
        }

        Article article = myData.get(position);

        // set article details into views
        viewHolder.title.setText(article.getTitle());
        viewHolder.date.setText(article.getTime() + "");


        return view;
    }

    class ViewHolder {
        TextView title;
        TextView date;
    }
}
