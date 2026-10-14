package com.spring_class.dscommerce.dto.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
        String name,
        @Pattern(regexp = "\\d{10,11}", message = "Phone number must be 10 or 11 digits")
        String phone,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        String password) {

}
