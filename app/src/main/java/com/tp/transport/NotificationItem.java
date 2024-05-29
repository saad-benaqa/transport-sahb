package com.tp.transport;

public class NotificationItem {
    private final String title;
    private final String content;

    public NotificationItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
