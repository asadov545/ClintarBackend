package org.artisoft.domain.Notification;

public class  PushSendObject {

    private  PushNotificationData notification;
    private  PushNotificationData data;
    private String to;

    public PushNotificationData getData() {
        return data;
    }

    public void setData(PushNotificationData data) {
        this.data = data;
    }

    public PushNotificationData getNotification() {
        return notification;
    }

    public void setNotification(PushNotificationData notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "PushSendObject{" +
                "notification=" + notification +
                ", data=" + data +
                ", to='" + to + '\'' +
                '}';
    }
}
