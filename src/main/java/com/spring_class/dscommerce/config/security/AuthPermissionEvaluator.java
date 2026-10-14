package com.spring_class.dscommerce.config.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.spring_class.dscommerce.dto.order.OrderDTO;
import com.spring_class.dscommerce.entities.Authority;
import com.spring_class.dscommerce.entities.User;
import com.spring_class.dscommerce.service.OrderService;
import com.spring_class.dscommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthPermissionEvaluator implements PermissionEvaluator {

    private final UserService userService;
    private final OrderService orderService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        User user = userService.authenticate();

        if (user.hasRole(Authority.ROLE_ADMIN)) {
            return true;
        }

        return switch (targetType) {
            case "USER" ->
                user.getId().equals(targetId);
            case "ORDER" -> {
                OrderDTO order = orderService.findById((Long) targetId);
                yield order.client().id().equals(user.getId());
            }
            default ->
                throw new IllegalArgumentException("Unknown targetType: " + targetType);
        };
    }

}
