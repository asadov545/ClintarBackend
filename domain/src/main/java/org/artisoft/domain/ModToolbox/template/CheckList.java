package org.artisoft.domain.ModToolbox.template;

public class CheckList {
    private long checkListId;
    private long templateId;
    private long pid;
    private String title;
    private String listTypeCode;
    private int weight;

    public long getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(long checkListId) {
        this.checkListId = checkListId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getListTypeCode() {
        return listTypeCode;
    }

    public void setListTypeCode(String listTypeCode) {
        this.listTypeCode = listTypeCode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
