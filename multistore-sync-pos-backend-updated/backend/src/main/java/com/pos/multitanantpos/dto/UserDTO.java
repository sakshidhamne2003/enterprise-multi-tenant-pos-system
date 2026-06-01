package com.pos.multitanantpos.dto;

import com.pos.multitanantpos.model.Role;

public class UserDTO {
    private Long id;
    private String username;
    private Role role;
    private String tenantName;

    public UserDTO() {}

    public UserDTO(Long id, String username, Role role, String tenantName) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.tenantName = tenantName;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public String getTenantName() { return tenantName; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(Role role) { this.role = role; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
}
