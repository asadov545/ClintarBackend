package org.artisoft.domain.ModTask.tasks;

import java.util.List;

public class Task {
    private long taskId;
    private int taskStatusId;
    private String title;
    private String code;
    private String description;
    private long beginDate;
    private long dueDate;
    private int priorityId;
    private List<TaskAssignees> taskAssigneesList;
    private  List<TaskPhoto> taskPhotos;
    private long createDate;
    private  long createUserId;
    private StatusInfo statusInfo;
    private String color;
    private String icon1;
    private String icon2;
    private long locId;
    private  String locationMarkup;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(int taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public List<TaskAssignees> getTaskAssigneesList() {
        return taskAssigneesList;
    }

    public void setTaskAssigneesList(List<TaskAssignees> taskAssigneesList) {
        this.taskAssigneesList = taskAssigneesList;
    }

    public List<TaskPhoto> getTaskPhotos() {
        return taskPhotos;
    }

    public void setTaskPhotos(List<TaskPhoto> taskPhotos) {
        this.taskPhotos = taskPhotos;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public long getLocId() {
        return locId;
    }

    public void setLocId(long locId) {
        this.locId = locId;
    }

    public String getLocationMarkup() {
        return locationMarkup;
    }

    public void setLocationMarkup(String locationMarkup) {
        this.locationMarkup = locationMarkup;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskStatusId=" + taskStatusId +
                ", title='" + title + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", beginDate=" + beginDate +
                ", dueDate=" + dueDate +
                ", priorityId=" + priorityId +
                ", taskAssigneesList=" + taskAssigneesList +
                ", taskPhotos=" + taskPhotos +
                ", createDate=" + createDate +
                ", createUserId=" + createUserId +
                ", statusInfo=" + statusInfo +
                ", color='" + color + '\'' +
                ", icon1='" + icon1 + '\'' +
                ", icon2='" + icon2 + '\'' +
                ", locId=" + locId +
                ", locationMarkup='" + locationMarkup + '\'' +
                '}';
    }
}
