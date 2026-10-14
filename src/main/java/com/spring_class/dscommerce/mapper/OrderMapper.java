package com.spring_class.dscommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spring_class.dscommerce.dto.order.OrderDTO;
import com.spring_class.dscommerce.dto.order.OrderRequestDTO;
import com.spring_class.dscommerce.entities.Order;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderDTO toOrderDTO(Order order);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "moment", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "products", ignore = true)
    Order toEntity(OrderRequestDTO request);

}
