package com.gamesync.api.dto;

public class ErrorResponse {
    private long timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(int status, org.springframework.http.HttpStatus httpStatus, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.error = httpStatus.getReasonPhrase(); // Ex: "Not Found", "Bad Request"
        this.message = message;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}