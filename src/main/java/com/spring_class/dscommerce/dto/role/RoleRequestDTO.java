package com.spring_class.dscommerce.dto.role;

import com.spring_class.dscommerce.entities.Authority;

import jakarta.validation.constraints.NotNull;

public record RoleRequestDTO(
                @NotNull(message = "Authority cannot be null") 
                Authority authority) {

}
