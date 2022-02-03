package org.artisoft.domain.ModTask.reports;

import java.util.Date;

public class DownloadSession {
    public enum DownloadType {
        TASK,
        USER_CUSTOMER
    }

    private DownloadRequestTask downloadRequestTask;
    private DownloadRequestTaskUsers downloadRequestTaskUsers;
    private DownloadType downloadType;

    private Date createDate;
    private static final int  MAX_AGE_IN_SECONDS = 60;

    public DownloadRequestTask getDownloadRequestTask() {
        return downloadRequestTask;
    }

    public void setDownloadRequestTask(DownloadRequestTask downloadRequestTask) {
        this.downloadRequestTask = downloadRequestTask;
    }

    public DownloadRequestTaskUsers getDownloadRequestTaskUsers() {
        return downloadRequestTaskUsers;
    }

    public void setDownloadRequestTaskUsers(DownloadRequestTaskUsers downloadRequestTaskUsers) {
        this.downloadRequestTaskUsers = downloadRequestTaskUsers;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public static int getMaxAgeInSeconds() {
        return MAX_AGE_IN_SECONDS;
    }

    public DownloadType getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(DownloadType downloadType) {
        this.downloadType = downloadType;
    }

    public DownloadSession() {
        this.createDate = new Date();
    }

    public boolean IsValid() {
        return  ((new Date()).getTime() - this.createDate.getTime())/1000 < MAX_AGE_IN_SECONDS;
    }

    @Override
    public String toString() {
        return "DownloadSession{" +
                "downloadRequestTask=" + downloadRequestTask +
                ", downloadType=" + downloadType +
                ", createDate=" + createDate +
                '}';
    }
}
