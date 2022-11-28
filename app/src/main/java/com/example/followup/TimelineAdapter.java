package com.example.followup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimelineAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private ArrayList<NewsItem> timelineList = new ArrayList<>();

    public TimelineAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 10; i++) {
            timelineList.add(new NewsItem("Timeline News Title " + (i + 1) + " Extra-alarm fire sweeps through vacant Back of the Yards furniture warehouse", "CBS CHICAGO", LocalDateTime.of(2022, 11, 25, 22, 15, 0), R.drawable.avatar));
        }
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
        ImageView thumbnail;
        TextView title;
        TextView source;
        TextView createTime;

        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.timeline_item_layout,
                    parent,false);
        }

        thumbnail = convertView.findViewById(R.id.newsThumb);
        thumbnail.setImageResource(timelineList.get(position).getThumbnail());
        title = convertView.findViewById(R.id.newsTitle);
        title.setText(timelineList.get(position).getTitle());
        source = convertView.findViewById(R.id.newsSource);
        source.setText(timelineList.get(position).getSource());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy / HH:mm a");
        createTime = convertView.findViewById(R.id.newsDate);
        createTime.setText(dtf.format(timelineList.get(position).getCreateTime()));

        return convertView;
    }
}
