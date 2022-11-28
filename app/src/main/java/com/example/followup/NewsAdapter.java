package com.example.followup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private ArrayList<NewsItem> newsSource = new ArrayList<>();

    public NewsAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 10; i++) {
            newsSource.add(new NewsItem("News Title " + (i + 1) + " Thanksgiving holiday travel expected to reach nearly pre-pandemic levels", "CBS CHICAGO", LocalDateTime.of(2022, 11, 27, 18, 19, 0), R.drawable.avatar));
        }
    }


    @Override
    public int getCount() {
        return newsSource.size();
    }

    @Override
    public Object getItem(int position) { return newsSource.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup
            parent)
    {
        ImageView thumbnail;
        TextView title;
        TextView source;

        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.news_item_layout,
                    parent,false);
        }

        thumbnail = convertView.findViewById(R.id.newsThumb);
        thumbnail.setImageResource(newsSource.get(position).getThumbnail());
        title = convertView.findViewById(R.id.newsTitle);
        title.setText(newsSource.get(position).getTitle());
        source = convertView.findViewById(R.id.newsSource);
        source.setText(newsSource.get(position).getSource());

        return convertView;
    }
}
