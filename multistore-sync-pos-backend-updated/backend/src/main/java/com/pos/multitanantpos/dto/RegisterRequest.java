package com.pos.multitanantpos.dto;

import com.pos.multitanantpos.model.Role;

public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
    private Long tenantId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
}
