package org.artisoft.domain;

public class UploadedFiles {

    private long upfid;
    private String basedir;
    private String relativePath;
    private String filename;
    private String originalFilemame;
    private long size;
    private String key;
    private String type;
    private int status;

    public long getUpfid() {
        return upfid;
    }

    public void setUpfid(long upfid) {
        this.upfid = upfid;
    }

    public String getBasedir() {
        return basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilemame() {
        return originalFilemame;
    }

    public void setOriginalFilemame(String originalFilemame) {
        this.originalFilemame = originalFilemame;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UploadedFiles{" +
                "upfid=" + upfid +
                ", basedir='" + basedir + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", filename='" + filename + '\'' +
                ", originalFilemame='" + originalFilemame + '\'' +
                ", size=" + size +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}
