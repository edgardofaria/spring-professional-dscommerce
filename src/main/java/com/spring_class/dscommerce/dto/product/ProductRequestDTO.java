package com.spring_class.dscommerce.dto.product;

import java.math.BigDecimal;
import java.util.Set;

import com.spring_class.dscommerce.entities.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
        String name,
        @NotBlank(message = "Description is required")
        @Size(min = 10, message = "Description must have at least 10 characters")
        String description,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be a positive value")
        BigDecimal price,
        @NotBlank(message = "Image URL is required")
        String imgUrl,
        @NotEmpty(message = "At least one category is required")
        Set<Category> categories) {

}
