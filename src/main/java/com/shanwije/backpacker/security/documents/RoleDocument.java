package com.shanwije.backpacker.security.documents;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection = "roles")
@Data
public class RoleDocument implements GrantedAuthority {
    @Id
    private String id;
    @NonNull
    private String authority;
}
