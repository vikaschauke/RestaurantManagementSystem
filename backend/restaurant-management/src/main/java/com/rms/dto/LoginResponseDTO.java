package com.rms.dto;

public class LoginResponseDTO {

    private String token;
    private CustomerDTO customer;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, CustomerDTO customer) {
        this.token = token;
        this.customer = customer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
}