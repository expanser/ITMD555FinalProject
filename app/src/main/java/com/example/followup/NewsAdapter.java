package com.example.followup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private List<NewsItem> newsSource = new ArrayList<>();

    public NewsAdapter(Context c, List<NewsItem> list) {
        mContext = c;
        mInflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        newsSource = list;
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
        TextView title;
        TextView source;

        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.news_item_layout,
                    parent,false);
        }

        title = convertView.findViewById(R.id.newsTitle);
        title.setText(newsSource.get(position).getTitle());
        source = convertView.findViewById(R.id.newsSource);
        source.setText(newsSource.get(position).getSource());
        new ImageLoader((ImageView) convertView.findViewById(R.id.newsThumb)).execute(newsSource.get(position).getThumbnail());

        return convertView;
    }
}
