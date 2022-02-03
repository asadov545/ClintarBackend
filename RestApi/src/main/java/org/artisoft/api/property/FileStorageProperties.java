package org.artisoft.api.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String taskPhotoDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getTaskPhotoDir() {
        return taskPhotoDir;
    }

    public void setTaskPhotoDir(String taskPhotoDir) {
        this.taskPhotoDir = taskPhotoDir;
    }
}