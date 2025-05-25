package com.cob.ppa.model.security;

import lombok.Data;

@Data
public class RoleAssignmentRequest {
    private String username;
    private String roleName;
}