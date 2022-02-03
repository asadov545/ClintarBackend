package org.artisoft.domain.ModTask.tasks;

public class TaskLocationDoc {
    private long locFileId;
    private String filenameOriginal;
    private long upfid;
    private String fullPath;
    private int op;
    private String uploadKey;
    private String imgbase64;

    public String getImgbase64() {
        return imgbase64;
    }

    public void setImgbase64(String imgbase64) {
        this.imgbase64 = imgbase64;
    }

    public long getLocFileId() {
        return locFileId;
    }

    public void setLocFileId(long locFileId) {
        this.locFileId = locFileId;
    }

    public String getFilenameOriginal() {
        return filenameOriginal;
    }

    public void setFilenameOriginal(String filenameOriginal) {
        this.filenameOriginal = filenameOriginal;
    }

    public long getUpfid() {
        return upfid;
    }

    public void setUpfid(long upfid) {
        this.upfid = upfid;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    @Override
    public String toString() {
        return "TaskLocationDoc{" +
                "locFileId=" + locFileId +
                ", filenameOriginal='" + filenameOriginal + '\'' +
                ", upfid=" + upfid +
                ", fullPath='" + fullPath + '\'' +
                ", op=" + op +
                ", uploadKey='" + uploadKey + '\'' +
                '}';
    }
}
