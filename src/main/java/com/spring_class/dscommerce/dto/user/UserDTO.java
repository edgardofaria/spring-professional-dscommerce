package com.spring_class.dscommerce.dto.user;

import java.time.LocalDate;
import java.util.Set;

public record UserDTO(Long id, String name, String email, String phone, LocalDate birthDate, Set<String> roles) {
}
