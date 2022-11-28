package com.example.followup;

import java.time.LocalDateTime;

public class EventItem {

    String title;
    LocalDateTime updateTime;
    int thumbnail;

    public EventItem(String title, LocalDateTime updateTime, int thumbnail) {

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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
