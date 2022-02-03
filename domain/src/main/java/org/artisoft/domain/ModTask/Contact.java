package org.artisoft.domain.ModTask;

import org.artisoft.domain.ValueLabel;

public class Contact {

    private long contactId;
    private ValueLabel<Long, String> contactType;
    private long userId;
    private String text;
    private long branchesId;

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public ValueLabel<Long, String> getContactType() {
        return contactType;
    }

    public void setContactType(ValueLabel<Long, String> contactType) {
        this.contactType = contactType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getBranchesId() {
        return branchesId;
    }

    public void setBranchesId(long branchesId) {
        this.branchesId = branchesId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", contactType=" + contactType +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                ", branchesId=" + branchesId +
                '}';
    }
}
