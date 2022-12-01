package com.example.followup;

public class NewsItem {

    private String title,type,source,link,thumbnail,eventId;
    Long releaseTime;

    public NewsItem() {
    }

    public NewsItem(String title, String type, String source, String link, String thumbnail, String eventId, Long releaseTime) {
        this.title = title;
        this.type = type;
        this.source = source;
        this.link = link;
        this.thumbnail = thumbnail;
        this.eventId = eventId;
        this.releaseTime = releaseTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Long releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", link='" + link + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", eventId='" + eventId + '\'' +
                ", releaseTime=" + releaseTime +
                '}';
    }
}
