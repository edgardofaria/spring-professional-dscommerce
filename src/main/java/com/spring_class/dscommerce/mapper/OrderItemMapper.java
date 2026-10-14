package com.spring_class.dscommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spring_class.dscommerce.dto.orderItem.OrderItemDTO;
import com.spring_class.dscommerce.entities.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "imageUrl", source = "product.imgUrl")
    OrderItemDTO toDTO(OrderItem orderItem);

}
