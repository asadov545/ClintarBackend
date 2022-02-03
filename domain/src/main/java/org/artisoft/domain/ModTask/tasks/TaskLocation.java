package org.artisoft.domain.ModTask.tasks;

public class TaskLocation {
    private long locId;
    private String title;
    private long pid;
    private TaskLocationDoc taskLocationDoc;


    public long getLocId() {
        return locId;
    }

    public void setLocId(long locId) {
        this.locId = locId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public TaskLocationDoc getTaskLocationDoc() {
        return taskLocationDoc;
    }

    public void setTaskLocationDoc(TaskLocationDoc taskLocationDoc) {
        this.taskLocationDoc = taskLocationDoc;
    }

    @Override
    public String toString() {
        return "TaskLocation{" +
                "locId=" + locId +
                ", title='" + title + '\'' +
                ", pid=" + pid +
                ", taskLocationDoc" + taskLocationDoc +
                '}';
    }
}
