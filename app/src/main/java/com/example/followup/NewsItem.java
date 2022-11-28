package com.example.followup;

import java.time.LocalDateTime;

public class NewsItem {

    String title;
    String source;
    LocalDateTime createTime;
    int thumbnail;

    public NewsItem(String title, String source, LocalDateTime createTime, int thumbnail) {

        this.title = title;
        this.source = source;
        this.createTime = createTime;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String artist) {
        this.source = artist;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
