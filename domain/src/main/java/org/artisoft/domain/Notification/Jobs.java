package org.artisoft.domain.Notification;

import org.artisoft.domain.User;
import org.artisoft.domain.ValueLabel;

import java.util.List;

public class Jobs {

    private long jobId;
    private List<ValueLabel<Long, String>> userlist;
    private List<ValueLabel<Long, String>> formlist;
    private List<ValueLabel<Long, String>> daylist;
    private ValueLabel<Long, String> hours;
    private int mail;
    private int push;
    private String title;
    private String message;

    public List<ValueLabel<Long, String>> getDaylist() {
        return daylist;
    }

    public void setDaylist(List<ValueLabel<Long, String>> daylist) {
        this.daylist = daylist;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public List<ValueLabel<Long, String>> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<ValueLabel<Long, String>> userlist) {
        this.userlist = userlist;
    }

    public List<ValueLabel<Long, String>> getFormlist() {
        return formlist;
    }

    public void setFormlist(List<ValueLabel<Long, String>> formlist) {
        this.formlist = formlist;
    }

    public ValueLabel<Long, String> getHours() {
        return hours;
    }

    public void setHours(ValueLabel<Long, String> hours) {
        this.hours = hours;
    }

    public int getMail() {
        return mail;
    }

    public void setMail(int mail) {
        this.mail = mail;
    }

    public int getPush() {
        return push;
    }

    public void setPush(int push) {
        this.push = push;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
