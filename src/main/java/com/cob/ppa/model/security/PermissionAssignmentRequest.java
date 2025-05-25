package com.cob.ppa.model.security;

import lombok.Data;

@Data
public  class PermissionAssignmentRequest {
    private String roleName;
    private String permissionName;
}