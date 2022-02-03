package org.artisoft.domain.ModToolbox.cpanel;

import java.util.List;

public class CreateCheckList {
    private long createId;
    private String title;
    private String listTypeCode;
    private int isCategory;
    private List<CreateCheckList> children;
    private List<CreateCheckListOption> options;

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
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

    public int getIsCategory() {
        return isCategory;
    }

    public void setIsCategory(int isCategory) {
        this.isCategory = isCategory;
    }

    public List<CreateCheckList> getChildren() {
        return children;
    }

    public void setChildren(List<CreateCheckList> children) {
        this.children = children;
    }

    public List<CreateCheckListOption> getOptions() {
        return options;
    }

    public void setOptions(List<CreateCheckListOption> options) {
        this.options = options;
    }
}
