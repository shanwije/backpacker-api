package com.shanwije.backpacker.security.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Document(collection = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDocument implements GrantedAuthority {
    @Id
    private String id;
    private String authority;

    public RoleDocument(String authority) {
        this.id = UUID.randomUUID().toString();
        this.authority = authority;
    }
}
