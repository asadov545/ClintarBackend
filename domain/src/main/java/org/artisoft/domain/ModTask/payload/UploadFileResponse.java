package org.artisoft.domain.ModTask.payload;

import java.util.List;

public class UploadFileResponse {
    private List<Long> uploadFileIds;
    private String uploadKey;

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    public List<Long> getUploadFileIds() {
        return uploadFileIds;
    }

    public void setUploadFileIds(List<Long> uploadFileIds) {
        this.uploadFileIds = uploadFileIds;
    }
}
