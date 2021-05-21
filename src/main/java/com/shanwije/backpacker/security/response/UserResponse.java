package com.shanwije.backpacker.security.response;


import com.shanwije.backpacker.security.documents.UserDocument;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserResponse {
    private String id;
    private String username;
    private List<String> authorities;

    public UserResponse(UserDocument userDocument) {
        this.id = userDocument.getId();
        this.username = userDocument.getUsername();
        this.authorities = userDocument.getAuthorities().stream().map(roleDocument -> roleDocument.getAuthority()).collect(Collectors.toList());
    }
}
