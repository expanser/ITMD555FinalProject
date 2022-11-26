package com.example.followup;

public class NewsItem {

    String title;
    String source;
    String createTime;
    int thumbnail;

    public NewsItem(String title, String source, String createTime, int thumbnail) {

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String price) {
        this.createTime = price;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
