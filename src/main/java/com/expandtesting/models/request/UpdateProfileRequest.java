package com.expandtesting.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProfileRequest {

    private String name;
    private String phone;
    private String company;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String name, String phone, String company) {
        this.name = name;
        this.phone = phone;
        this.company = company;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
}

