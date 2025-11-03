package com.transport.dto.user;

import lombok.Data;

@Data
public class UserSearchRequest {
    private String keyword;      // tìm theo username, fullName, phone, idNumber
    private String role;       
    private Boolean isActive;    
}