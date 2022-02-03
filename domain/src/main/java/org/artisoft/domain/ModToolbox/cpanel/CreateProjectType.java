package org.artisoft.domain.ModToolbox.cpanel;

public class CreateProjectType {
    private long categoryId;
    private int isNewCategory;
    private String categoryText;
    private String templateName;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsNewCategory() {
        return isNewCategory;
    }

    public void setIsNewCategory(int isNewCategory) {
        this.isNewCategory = isNewCategory;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
