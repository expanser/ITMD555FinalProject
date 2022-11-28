package com.example.followup;

public class EventItem {

    String title;
    String updateTime;
    int thumbnail;

    public EventItem(String title, String updateTime, int thumbnail) {

        this.title = title;
        this.updateTime = updateTime;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
