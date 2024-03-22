package com.emi.onlineshop.aspect;

import com.emi.onlineshop.models.Role;
import com.emi.onlineshop.models.User;
import com.emi.onlineshop.repositories.RoleRepository;
import com.emi.onlineshop.services.AuthenticationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class HasAuthoritiesAspect {

    private final RoleRepository roleRepository;

    private final AuthenticationService authenticationService;

    public HasAuthoritiesAspect(RoleRepository roleRepository, AuthenticationService authenticationService) {
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
    }

    @Pointcut("@annotation(com.emi.onlineshop.aspect.HasAuthorities)")
    public void executeHasAuthorities() {
    }

    @Before("executeHasAuthorities()")
    public void hasAuthorities(JoinPoint joinPoint) {

        User user = authenticationService.getAuthenticatedUser();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        HasAuthorities annotation = methodSignature.getMethod().getAnnotation(HasAuthorities.class);

        Set<Optional<Role>> optionalRequiredRoles = Arrays.stream(annotation.roles())
                .map(roleRepository::findByName)
                .collect(Collectors.toSet());
        Set<Role> requiredRoles = optionalRequiredRoles
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (optionalRequiredRoles.size() != requiredRoles.size()) {
            throw new IllegalArgumentException("Some of the roles don't exist");
        }

        boolean hasAuthority = requiredRoles
                .stream()
                .anyMatch(role -> user.getRoles()
                        .stream()
                        .anyMatch(r -> role.getName().equals(r.getName())));

        if (!hasAuthority) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
    }
}
