package org.artisoft.api.common;

import org.springframework.http.HttpStatus;

public class Response<T> {
    private int code;
    private boolean success;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Response<T> generateResponse(HttpStatus httpStatus, T data){
        return this.generateResponse(httpStatus, data, httpStatus.getReasonPhrase());
    }
    public Response<T> generateResponse(HttpStatus httpStatus, T data, String message){
        this.setCode(httpStatus.value());
        this.setSuccess(httpStatus == HttpStatus.OK || httpStatus == HttpStatus.ACCEPTED || httpStatus == HttpStatus.CREATED);
        this.setMessage(message);
        this.setData(data);
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
