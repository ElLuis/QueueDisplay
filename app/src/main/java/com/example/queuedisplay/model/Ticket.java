package com.example.queuedisplay.model;

public class Ticket {
    private String userId;
    private String ticketId;
    private String status;
    private String message;
    private String createdDate;

    @Override
    public String toString() {
        return "Ticket{" +
                "userId='" + userId + '\'' +
                ", ticketId='" + ticketId + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }

    public Ticket(String userId, String ticketId, String status, String message, String createdDate) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.status = status;
        this.message = message;
        this.createdDate = createdDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
