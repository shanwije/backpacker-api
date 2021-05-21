package com.shanwije.backpacker.security.response;


import com.shanwije.backpacker.security.documents.UserDocument;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserRegistrationResponse {

    private String id;
    private String username;
    private List<String> authorities;

    public UserRegistrationResponse(UserDocument userDocument) {
        this.id = userDocument.getId();
        this.username = userDocument.getUsername();
        this.authorities = userDocument.getAuthorities().stream().map(roleDocument -> roleDocument.getAuthority()).collect(Collectors.toList());
    }
}
