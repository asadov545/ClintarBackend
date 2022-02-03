package org.artisoft.domain;

import java.util.List;

public class Users {
    private long userId;
    private ValueLabel<Long, String> userType;
    private String username;
    private String password;
    private String fullname;
    private ValueLabel<Long, String> branches;
    private Address address;
    private String email;
    private String mobile;
    private long modId;
    private ValueLabel<Long, String> customer;
    private String position;
    private ValueLabel<Long, String> mainprj;

    public ValueLabel<Long, String> getMainprj() {
        return mainprj;
    }

    public void setMainprj(ValueLabel<Long, String> mainprj) {
        this.mainprj = mainprj;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ValueLabel<Long, String> getUserType() {
        return userType;
    }

    public void setUserType(ValueLabel<Long, String> userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public ValueLabel<Long, String> getBranches() {
        return branches;
    }

    public void setBranches(ValueLabel<Long, String> branches) {
        this.branches = branches;
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

    public long getModId() {
        return modId;
    }

    public void setModId(long modId) {
        this.modId = modId;
    }

    public ValueLabel<Long, String> getCustomer() {
        return customer;
    }

    public void setCustomer(ValueLabel<Long, String> customer) {
        this.customer = customer;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", userType=" + userType +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", branches=" + branches +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", modId=" + modId +
                ", customer=" + customer +
                ", position='" + position + '\'' +
                '}';
    }
}
