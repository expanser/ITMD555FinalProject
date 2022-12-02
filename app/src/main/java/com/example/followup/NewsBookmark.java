package com.example.followup;

public class NewsBookmark {

    private String userId, newsId;

    public NewsBookmark() {
    }

    public NewsBookmark(String userId, String newsId) {
        this.userId = userId;
        this.newsId = newsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    @Override
    public String toString() {
        return "NewsBookmark{" +
                "userId='" + userId + '\'' +
                ", newsId='" + newsId + '\'' +
                '}';
    }
}
