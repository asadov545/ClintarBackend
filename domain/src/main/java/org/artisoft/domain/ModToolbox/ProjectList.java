package org.artisoft.domain.ModToolbox;

public class ProjectList {

    private long prjId;
    private  String prjCode;
    private String name;
    private String ptTitle;
    private long beginDate;
    private long endDate;
    private String supervisior;
    private long create_date;

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public long getPrjId() {
        return prjId;
    }

    public void setPrjId(long prjId) {
        this.prjId = prjId;
    }

    public String getPrjCode() {
        return prjCode;
    }

    public void setPrjCode(String prjCode) {
        this.prjCode = prjCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPtTitle() {
        return ptTitle;
    }

    public void setPtTitle(String ptTitle) {
        this.ptTitle = ptTitle;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getSupervisior() {
        return supervisior;
    }

    public void setSupervisior(String supervisior) {
        this.supervisior = supervisior;
    }

    @Override
    public String toString() {
        return "ProjectList{" +
                "prjId=" + prjId +
                ", prjCode='" + prjCode + '\'' +
                ", name='" + name + '\'' +
                ", ptTitle='" + ptTitle + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", supervisior='" + supervisior + '\'' +
                ", create_date=" + create_date +
                '}';
    }
}
