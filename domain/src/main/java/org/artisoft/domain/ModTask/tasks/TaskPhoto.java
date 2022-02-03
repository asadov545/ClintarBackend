package org.artisoft.domain.ModTask.tasks;

public class TaskPhoto {
    private long tpId;
    private String filenameOriginal;
    private long upfid;
    private String fullPath;
    private int op;
    private String uploadKey;

    public long getTpId() {
        return tpId;
    }

    public void setTpId(long tpId) {
        this.tpId = tpId;
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
        return "TaskPhoto{" +
                "tpId=" + tpId +
                ", filenameOriginal='" + filenameOriginal + '\'' +
                ", upfid=" + upfid +
                ", fullPath='" + fullPath + '\'' +
                ", op=" + op +
                ", uploadKey='" + uploadKey + '\'' +
                '}';
    }
}
