package com.spring_class.dscommerce.mapper;

import org.mapstruct.Mapper;

import com.spring_class.dscommerce.dto.category.CategoryDTO;
import com.spring_class.dscommerce.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);
}
