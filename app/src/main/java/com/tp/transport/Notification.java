package com.tp.transport;

import java.util.List;

public class Notification {
    private String title;
    private String body;
    private List<String> photoUrls;
    private long timestamp;

    public Notification() {
    }

    public Notification(String title, String body, List<String> photoUrls, long timestamp) {
        this.title = title;
        this.body = body;
        this.photoUrls = photoUrls;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}