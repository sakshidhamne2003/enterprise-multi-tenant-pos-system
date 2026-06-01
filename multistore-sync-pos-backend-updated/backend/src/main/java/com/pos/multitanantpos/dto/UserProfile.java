package com.pos.multitanantpos.dto;

import com.pos.multitanantpos.model.Role;

public class UserProfile {
    private String username;
    private Role role;
    private String tenantName;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
}
