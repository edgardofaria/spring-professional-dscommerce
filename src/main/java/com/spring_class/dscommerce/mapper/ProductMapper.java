package com.spring_class.dscommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.spring_class.dscommerce.dto.product.ProductDTO;
import com.spring_class.dscommerce.dto.product.ProductMinDTO;
import com.spring_class.dscommerce.dto.product.ProductRequestDTO;
import com.spring_class.dscommerce.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    ProductMinDTO toMinDTO(Product product);

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product toEntity(ProductRequestDTO request);

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateEntityFromDTO(ProductRequestDTO request, @MappingTarget Product product);
}
