package com.example.followup;

public class EventBookmark {

    private String userId, eventId;

    public EventBookmark() {
    }

    public EventBookmark(String userId, String eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "EventBookmark{" +
                "userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
