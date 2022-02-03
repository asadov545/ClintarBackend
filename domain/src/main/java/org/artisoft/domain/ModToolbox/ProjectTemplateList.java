package org.artisoft.domain.ModToolbox;

public class ProjectTemplateList {
    private long ptId;
    private String category;
    private String tempName;
    private long createDate;

    public long getPtId() {
        return ptId;
    }

    public void setPtId(long ptId) {
        this.ptId = ptId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ProjectTemplateList{" +
                "ptId=" + ptId +
                ", category='" + category + '\'' +
                ", tempName='" + tempName + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
