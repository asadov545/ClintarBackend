package org.artisoft.domain.ModTask.Insight;

public class InsightTaskStatusList {
    private long id;
    private String title;
    private Integer value;
    private String color;
    private String icon1;
    private String icon2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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

    @Override
    public String toString() {
        return "InsightTaskStatusList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", value=" + value +
                ", color='" + color + '\'' +
                ", icon1='" + icon1 + '\'' +
                ", icon2='" + icon2 + '\'' +
                '}';
    }
}
