package org.artisoft.domain;

public class Address {
    private long addressId;
    private ValueLabel<Long, String> country;
    private String addressLine;
    private String zipCode;
    private int status;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public ValueLabel<Long, String> getCountry() {
        return country;
    }

    public void setCountry(ValueLabel<Long, String> country) {
        this.country = country;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", country=" + country +
                ", addressLine='" + addressLine + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", status=" + status +
                '}';
    }
}
