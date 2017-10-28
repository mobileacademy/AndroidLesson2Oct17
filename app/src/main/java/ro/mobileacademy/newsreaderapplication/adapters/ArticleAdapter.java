package ro.mobileacademy.newsreaderapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.models.Article;

/**
 * Created by valerica.plesu on 14/10/2017.
 */

public class ArticleAdapter extends BaseAdapter {

    private static final String TAG = ArticleAdapter.class.getSimpleName();

    private ArrayList<Article> data;
    private LayoutInflater inflater;
    private ArticleListener listener;

    public ArticleAdapter(Context context, ArrayList<Article> myData) {
        this.data = myData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listener = (ArticleListener) context;
    }

    public void updateData (ArrayList<Article> items) {
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Log.i(TAG, "getView");
        final Article article = getItem(position);

        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.article_list_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.layout = (LinearLayout) view.findViewById(R.id.item_layout);
            viewHolder.titleTextView = (TextView) view.findViewById(R.id.title_tv);
            viewHolder.createdDateTextView = (TextView) view.findViewById(R.id.created_date_tv);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.titleTextView.setText(article.getName());
        viewHolder.createdDateTextView.setText(article.getTime());

        String timestamp = article.getTime();
        if(timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(timestamp));

            viewHolder.createdDateTextView.setText(sdf.format(cal.getTime()));
        }

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onArticleSelected(article);
                }
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Article getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        LinearLayout layout;
        TextView titleTextView;
        TextView createdDateTextView;
    }

    public interface ArticleListener {
        void onArticleSelected(Article article);
    }
}
