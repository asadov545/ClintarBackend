package org.artisoft.domain.ModTask.payload;

public class UploadFileRequest {
    private String uploadType; // {TASK_PHOTO, USER_PROFILE_PHOTO}
    private String uploadKey;

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }
}
