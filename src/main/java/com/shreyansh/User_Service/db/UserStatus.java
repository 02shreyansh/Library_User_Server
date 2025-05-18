package com.shreyansh.User_Service.db;

public enum UserStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");
    
    private final String status;
    
    UserStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
}