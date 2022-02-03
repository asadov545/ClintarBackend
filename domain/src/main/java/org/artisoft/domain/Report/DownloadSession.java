package org.artisoft.domain.Report;

import java.util.Date;

public class DownloadSession {
    private DownloadRequest downloadRequest;

    private Date createDate;
    private static final int  MAX_AGE_IN_SECONDS = 60;

    public DownloadSession() {
        this.createDate = new Date();
    }

    public boolean IsValid() {
        return  ((new Date()).getTime() - this.createDate.getTime())/1000 < MAX_AGE_IN_SECONDS;
    }

    public DownloadRequest getDownloadRequest() {
        return downloadRequest;
    }

    public void setDownloadRequest(DownloadRequest downloadRequest) {
        this.downloadRequest = downloadRequest;
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

    @Override
    public String toString() {
        return "DownloadSession{" +
                "downloadRequest=" + downloadRequest +
                ", createDate=" + createDate +
                '}';
    }
}
