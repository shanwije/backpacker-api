package com.shanwije.backpacker.security.authority;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')") //OR @PreAuthorize("hasRole('ADMIN')")
public @interface IsAdmin {
}
