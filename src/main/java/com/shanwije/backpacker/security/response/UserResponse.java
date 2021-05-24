package com.shanwije.backpacker.security.response;


import com.shanwije.backpacker.security.documents.RoleDocument;
import com.shanwije.backpacker.security.documents.UserDocument;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserResponse {
    private String id;
    private String username;
    private List<String> authorities;
    private String email;
    private String firstName;
    private String lastName;

    public UserResponse(UserDocument ud) {
        this.id = ud.getId();
        this.username = ud.getUsername();
        this.email = ud.getEmail();
        this.firstName = ud.getFirstName();
        this.lastName = ud.getLastName();
        this.authorities = ud.getAuthorities().stream()
                .map(RoleDocument::getAuthority).collect(Collectors.toList());
    }
}
