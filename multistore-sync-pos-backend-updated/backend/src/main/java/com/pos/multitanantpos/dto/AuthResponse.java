package com.pos.multitanantpos.dto;

import com.pos.multitanantpos.model.Role;

public class AuthResponse {
    private String token;
    private String username;
    private Role role;
    private String tenantName;
    private Long tenantId;

    public AuthResponse() {}

    public AuthResponse(String token, String username, Role role, String tenantName, Long tenantId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.tenantName = tenantName;
        this.tenantId = tenantId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
}
