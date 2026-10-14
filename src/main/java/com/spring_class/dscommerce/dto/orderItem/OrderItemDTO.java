package com.spring_class.dscommerce.dto.orderItem;

import java.math.BigDecimal;

public record OrderItemDTO(Long productId, String name, BigDecimal price, Integer quantity, String imageUrl) {

    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
