package com.pos.multitanantpos.dto;

public class TenantRequest {
    private String name;
    private String companyEmail;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompanyEmail() { return companyEmail; }
    public void setCompanyEmail(String companyEmail) { this.companyEmail = companyEmail; }
}
