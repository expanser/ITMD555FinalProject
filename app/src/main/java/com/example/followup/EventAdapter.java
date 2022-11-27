package com.example.followup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private ArrayList<EventItem> eventList = new ArrayList<>();

    public EventAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 10; i++) {
            eventList.add(new EventItem("event title " + (i + 1) + " eventeventeventeventeventeventeventeventeventeventevent", "update date", R.drawable.avatar));
        }
    }


    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) { return eventList.get(position); }

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
            convertView = mInflator.inflate(R.layout.event_item_layout,
                    parent,false);
        }

        thumbnail = convertView.findViewById(R.id.eventThumb);
        thumbnail.setImageResource(eventList.get(position).getThumbnail());
        title = convertView.findViewById(R.id.eventTitle);
        title.setText(eventList.get(position).getTitle());
        source = convertView.findViewById(R.id.eventDate);
        source.setText(eventList.get(position).getUpdateTime());

        return convertView;
    }
}
