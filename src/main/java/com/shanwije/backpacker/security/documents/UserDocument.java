package com.shanwije.backpacker.security.documents;

import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
@Document(collection = "users")
public class UserDocument implements UserDetails, CredentialsContainer {

    @Id
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String password;

    private boolean active = true;

    @DBRef
    @NonNull
    private List<? extends RoleDocument> authorities;


    public UserDocument(UserRegistrationRequest userRegistrationRequest) {
        this.setUserRegistrationRequest(userRegistrationRequest);
    }

    public void setUserRegistrationRequest(UserRegistrationRequest userRegistrationRequest){
         if(!StringUtils.isBlank(userRegistrationRequest.getUsername())){
            this.username = userRegistrationRequest.getUsername();
        }
        if(!StringUtils.isBlank(userRegistrationRequest.getPassword())){
            this.password = userRegistrationRequest.getPassword();
        }
    }

    @Override
    public void eraseCredentials() {
        this.password = "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
