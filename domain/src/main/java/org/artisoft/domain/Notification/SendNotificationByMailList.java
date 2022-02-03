package org.artisoft.domain.Notification;

public class SendNotificationByMailList {
    private long notifQueId;
private String email;
private String title;
private String content;

    public long getNotifQueId() {
        return notifQueId;
    }

    public void setNotifQueId(long notifQueId) {
        this.notifQueId = notifQueId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SendNotificationByMailList{" +
                "notifQueId=" + notifQueId +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
