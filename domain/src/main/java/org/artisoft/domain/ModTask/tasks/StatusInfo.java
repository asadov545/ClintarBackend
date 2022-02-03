package org.artisoft.domain.ModTask.tasks;

public class StatusInfo {
    private int canComplete;
    private int canClose;
    private int canEdit;


    private int titleIsEditable;
    private int descriptionIsEditable;
    private int photoIsEditable;
    private int assignmentIsEditable;
    private int beginDateIsEditable;
    private int endDateIsEditable;
    private int priorityIsEditable;
    private int locationIsEditable;

    public int getCanComplete() {
        return canComplete;
    }

    public void setCanComplete(int canComplete) {
        this.canComplete = canComplete;
    }

    public int getCanClose() {
        return canClose;
    }

    public void setCanClose(int canClose) {
        this.canClose = canClose;
    }

    public int getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(int canEdit) {
        this.canEdit = canEdit;
    }

    public int getTitleIsEditable() {
        return titleIsEditable;
    }

    public void setTitleIsEditable(int titleIsEditable) {
        this.titleIsEditable = titleIsEditable;
    }

    public int getDescriptionIsEditable() {
        return descriptionIsEditable;
    }

    public void setDescriptionIsEditable(int descriptionIsEditable) {
        this.descriptionIsEditable = descriptionIsEditable;
    }

    public int getPhotoIsEditable() {
        return photoIsEditable;
    }

    public void setPhotoIsEditable(int photoIsEditable) {
        this.photoIsEditable = photoIsEditable;
    }

    public int getAssignmentIsEditable() {
        return assignmentIsEditable;
    }

    public void setAssignmentIsEditable(int assignmentIsEditable) {
        this.assignmentIsEditable = assignmentIsEditable;
    }

    public int getBeginDateIsEditable() {
        return beginDateIsEditable;
    }

    public void setBeginDateIsEditable(int beginDateIsEditable) {
        this.beginDateIsEditable = beginDateIsEditable;
    }

    public int getEndDateIsEditable() {
        return endDateIsEditable;
    }

    public void setEndDateIsEditable(int endDateIsEditable) {
        this.endDateIsEditable = endDateIsEditable;
    }

    public int getPriorityIsEditable() {
        return priorityIsEditable;
    }

    public void setPriorityIsEditable(int priorityIsEditable) {
        this.priorityIsEditable = priorityIsEditable;
    }

    public int getLocationIsEditable() {
        return locationIsEditable;
    }

    public void setLocationIsEditable(int locationIsEditable) {
        this.locationIsEditable = locationIsEditable;
    }

    @Override
    public String toString() {
        return "StatusInfo{" +
                "canComplete=" + canComplete +
                ", canClose=" + canClose +
                ", canEdit=" + canEdit +
                ", titleIsEditable=" + titleIsEditable +
                ", descriptionIsEditable=" + descriptionIsEditable +
                ", photoIsEditable=" + photoIsEditable +
                ", assignmentIsEditable=" + assignmentIsEditable +
                ", beginDateIsEditable=" + beginDateIsEditable +
                ", endDateIsEditable=" + endDateIsEditable +
                ", priorityIsEditable=" + priorityIsEditable +
                ", locationIsEditable=" + locationIsEditable +
                '}';
    }
}
