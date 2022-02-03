package org.artisoft.domain.ModTask.permissions;

public class Privileges {
    private int privId;
    private String name;
    private String title;
    private int status;

    public int getPrivId() {
        return privId;
    }

    public void setPrivId(int privId) {
        this.privId = privId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Privileges{" +
                "privId=" + privId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }
}
