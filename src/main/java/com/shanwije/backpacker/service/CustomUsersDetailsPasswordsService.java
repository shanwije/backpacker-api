package com.shanwije.backpacker.service;

import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class CustomUsersDetailsPasswordsService implements ReactiveUserDetailsPasswordService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> updatePassword(final UserDetails user, final String newPassword) {
        final var userDocument = (UserDocument) user;
        userDocument.setPassword(newPassword);
        return userRepository.save(userDocument).cast(UserDetails.class);
    }
}
