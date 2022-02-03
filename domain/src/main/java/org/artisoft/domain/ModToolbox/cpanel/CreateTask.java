package org.artisoft.domain.ModToolbox.cpanel;

import java.util.List;

public class CreateTask {
    private long createId;
    private String taskTitle;
    private String hazardTitle;
    private String controlTitle;
    private String riskBefore;
    private String riskAfter;
    private List<List<Long>> checkList;

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getHazardTitle() {
        return hazardTitle;
    }

    public void setHazardTitle(String hazardTitle) {
        this.hazardTitle = hazardTitle;
    }

    public String getControlTitle() {
        return controlTitle;
    }

    public void setControlTitle(String controlTitle) {
        this.controlTitle = controlTitle;
    }

    public String getRiskBefore() {
        return riskBefore;
    }

    public void setRiskBefore(String riskBefore) {
        this.riskBefore = riskBefore;
    }

    public String getRiskAfter() {
        return riskAfter;
    }

    public void setRiskAfter(String riskAfter) {
        this.riskAfter = riskAfter;
    }

    public List<List<Long>> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<List<Long>> checkList) {
        this.checkList = checkList;
    }
}
