package org.artisoft.domain.ModTask;

import org.artisoft.domain.Address;
import org.artisoft.domain.ValueLabel;

public class Branches {
    private long branchesId;
    private String title;
    private ValueLabel<Long, String> pid;
    private Address address;
    private String email;
    private String mobile;

    public long getBranchesId() {
        return branchesId;
    }

    public void setBranchesId(long branchesId) {
        this.branchesId = branchesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ValueLabel<Long, String> getPid() {
        return pid;
    }

    public void setPid(ValueLabel<Long, String> pid) {
        this.pid = pid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Branches{" +
                "branchesId=" + branchesId +
                ", title='" + title + '\'' +
                ", pid=" + pid +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
