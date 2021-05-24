package com.shanwije.backpacker.security.service;

import com.shanwije.backpacker.security.documents.RoleDocument;
import com.shanwije.backpacker.security.repository.RolesRepository;
import com.shanwije.backpacker.security.request.RoleRequest;
import com.shanwije.backpacker.security.response.RoleResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;

    public Mono<RoleResponse> create(RoleRequest request) {
        return rolesRepository.save(new RoleDocument(request.getAuthority())).flatMap(roleDocument -> Mono.just(new RoleResponse(roleDocument)));
    }

    public Mono<Void> delete(String id) {
        return rolesRepository.findById(id).flatMap(rolesRepository::delete);
    }
}
