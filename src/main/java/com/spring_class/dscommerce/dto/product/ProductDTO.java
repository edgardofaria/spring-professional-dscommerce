package com.spring_class.dscommerce.dto.product;

import java.math.BigDecimal;
import java.util.Set;

import com.spring_class.dscommerce.dto.category.CategoryDTO;

public record ProductDTO(Long id, String name, String description, BigDecimal price, String imgUrl, Set<CategoryDTO> categories) {

}
