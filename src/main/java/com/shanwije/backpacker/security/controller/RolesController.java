package com.shanwije.backpacker.security.controller;

import com.shanwije.backpacker.security.request.RoleRequest;
import com.shanwije.backpacker.security.response.RoleResponse;
import com.shanwije.backpacker.security.service.RolesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolesController {

    private final RolesService rolesService;

    //    @IsSuperAdmin
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RoleResponse> create(@RequestBody RoleRequest request) {
        return rolesService.create(request);
    }

    //    @IsSuperAdmin
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return rolesService.delete(id);
    }
}
