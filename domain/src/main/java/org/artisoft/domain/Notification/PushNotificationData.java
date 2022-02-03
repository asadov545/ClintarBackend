package org.artisoft.domain.Notification;

public class PushNotificationData {
    private String sound;
    private String body;
    private String title;
    private  boolean content_available;
    private String priority;

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isContent_available() {
        return content_available;
    }

    public void setContent_available(boolean content_available) {
        this.content_available = content_available;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PushNotificationData{" +
                "sound='" + sound + '\'' +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", content_available=" + content_available +
                ", priority='" + priority + '\'' +
                '}';
    }
}
