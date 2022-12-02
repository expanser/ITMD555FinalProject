package com.example.followup;

public class EventItem {

    String title, thumbnail;
    Long updateTime;

    public EventItem(String title, Long updateTime, String thumbnail) {

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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    @Override
    public String toString() {
        return "EventItem{" +
                "title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
