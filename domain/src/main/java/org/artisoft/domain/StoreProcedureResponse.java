package org.artisoft.domain;

public class StoreProcedureResponse {

    private int id;
    private int errorCode;
    private String errorMsg;
    private String sqlState;

    @Override
    public String toString() {
        return "StoreProcedureResponse{" +
                "id=" + id +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", sqlState='" + sqlState + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

}
