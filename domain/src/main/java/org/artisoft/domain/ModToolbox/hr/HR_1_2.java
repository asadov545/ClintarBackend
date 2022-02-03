package org.artisoft.domain.ModToolbox.hr;

public class HR_1_2 {
    private long tb_prj_hr_1_2_id;
    private String title;
    private String body;

    public long getTb_prj_hr_1_2_id() {
        return tb_prj_hr_1_2_id;
    }

    public void setTb_prj_hr_1_2_id(long tb_prj_hr_1_2_id) {
        this.tb_prj_hr_1_2_id = tb_prj_hr_1_2_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HR_1_2{" +
                "tb_prj_hr_1_2_id=" + tb_prj_hr_1_2_id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
