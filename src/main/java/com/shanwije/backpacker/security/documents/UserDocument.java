package com.shanwije.backpacker.security.documents;

import com.shanwije.backpacker.security.request.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import java.util.*;


@Document(collection = "users")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument implements UserDetails, CredentialsContainer {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private List<RoleDocument> authorities = new ArrayList<>();
    private Set<String> refreshTokenIds = new HashSet<>();

    public UserDocument(SignUpRequest req, RoleDocument defaultRole) {
        this.authorities.add(defaultRole);
        setUserRegistrationRequest(req);
    }

    public void setUserRegistrationRequest(SignUpRequest req) {
        this.id = UUID.randomUUID().toString();
        this.username = req.getUsername();
        this.password = req.getPassword();
        this.email = req.getEmail();
        this.firstName = req.getFirstName();
        this.lastName = req.getLastName();
        this.active = true;
    }

    public UserDocument addRefreshTokenId(String tokenId) {
        this.refreshTokenIds.add(tokenId);
        return this;
    }

    public UserDocument removeRefreshTokenId(String tokenId) {
        this.refreshTokenIds.remove(tokenId);
        return this;
    }

    @Override
    public void eraseCredentials() {
        this.password = "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    } //NOSONAR

    @Override
    public boolean isAccountNonLocked() {
        return active;
    } //NOSONAR

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    } //NOSONAR

    @Override
    public boolean isEnabled() {
        return active;
    } //NOSONAR
}
