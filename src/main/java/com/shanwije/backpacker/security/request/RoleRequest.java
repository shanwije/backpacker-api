package com.shanwije.backpacker.security.request;

public class RoleRequest {

    private String authority;

    public RoleRequest() {
    }

    public RoleRequest(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
