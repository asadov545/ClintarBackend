package org.artisoft.domain.ModTask.reports;

public class DownloadRequestTask {
    private String type;
    private TaskSearchInfo taskSearchInfo;
    private String fileType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskSearchInfo getTaskSearchInfo() {
        return taskSearchInfo;
    }

    public void setTaskSearchInfo(TaskSearchInfo taskSearchInfo) {
        this.taskSearchInfo = taskSearchInfo;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "DownloadRequestTask{" +
                "type='" + type + '\'' +
                ", taskSearchInfo=" + taskSearchInfo +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
