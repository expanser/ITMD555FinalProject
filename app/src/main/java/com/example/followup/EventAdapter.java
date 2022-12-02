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

public class EventAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private List<EventItem> eventList = new ArrayList<>();

    public EventAdapter(Context c, List<EventItem> list) {
        mContext = c;
        mInflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        eventList = list;
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
        TextView title;
        TextView eventDate;

        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.event_item_layout,
                    parent,false);
        }

        title = convertView.findViewById(R.id.eventTitle);
        title.setText(eventList.get(position).getTitle());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy / HH:mm a");
        eventDate = convertView.findViewById(R.id.eventDate);
        Long timestamp = eventList.get(position).getUpdateTime();
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                        ZoneId.of("America/Chicago"));
        eventDate.setText(dtf.format(triggerTime));

        new ImageLoader((ImageView) convertView.findViewById(R.id.eventThumb)).execute(eventList.get(position).getThumbnail());

        return convertView;
    }
}
