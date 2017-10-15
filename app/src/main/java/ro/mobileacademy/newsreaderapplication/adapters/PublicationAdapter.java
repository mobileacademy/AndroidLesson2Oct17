package ro.mobileacademy.newsreaderapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.models.Publication;

/**
 * Created by valerica.plesu on 14/10/2017.
 */

public class PublicationAdapter extends BaseAdapter {

    private static final String TAG = PublicationAdapter.class.getSimpleName();

    private ArrayList<Publication> data;
    private LayoutInflater inflater;

    public PublicationAdapter(Context context, ArrayList<Publication> myData) {
        this.data = myData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Log.i(TAG, "getView");
        Publication publication = getItem(position);

        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.gridview_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.picture);
            viewHolder.titleTextView = (TextView) view.findViewById(R.id.title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.imageView.setImageResource(publication.getPictureResource());
        viewHolder.titleTextView.setText(publication.getName());

        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Publication getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView imageView;
        TextView titleTextView;
    }
}
