package com.spring_class.dscommerce.dto.order;

import java.util.List;

import com.spring_class.dscommerce.dto.orderItem.OrderItemRequestDTO;

import jakarta.validation.constraints.NotEmpty;

public record OrderRequestDTO(
        @NotEmpty(message = "The order must have at least one item")
        List<OrderItemRequestDTO> items) {

}
