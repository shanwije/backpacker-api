package com.shanwije.backpacker.security.response;

import com.shanwije.backpacker.security.documents.RoleDocument;

public class RoleResponse {

    private String id;
    private String authority;

    public RoleResponse(RoleDocument roleDocument) {
        if (roleDocument.getId() != null) this.id = roleDocument.getId();
        if (roleDocument.getAuthority() != null) this.authority = roleDocument.getAuthority();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
