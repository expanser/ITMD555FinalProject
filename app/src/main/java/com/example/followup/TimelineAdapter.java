package com.example.followup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private List<NewsItem> timelineList = new ArrayList<>();

    public TimelineAdapter(Context c, List<NewsItem> list) {
        mContext = c;
        mInflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        timelineList = list;
    }


    @Override
    public int getCount() {
        return timelineList.size();
    }

    @Override
    public Object getItem(int position) { return timelineList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup
            parent)
    {
        TextView title;
        TextView source;
        TextView newsDate;

        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.timeline_item_layout,
                    parent,false);
        }

        title = convertView.findViewById(R.id.newsTitle);
        title.setText(timelineList.get(position).getTitle());
        source = convertView.findViewById(R.id.newsSource);
        source.setText(timelineList.get(position).getSource());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy / HH:mm a");
        newsDate = convertView.findViewById(R.id.newsDate);
        Long timestamp = timelineList.get(position).getReleaseTime();
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                        ZoneId.of("America/Chicago"));
        newsDate.setText(dtf.format(triggerTime));

        new ImageLoader((ImageView) convertView.findViewById(R.id.newsThumb)).execute(timelineList.get(position).getThumbnail());

        return convertView;
    }
}
