package com.spring_class.dscommerce.dto.product;

import java.math.BigDecimal;

public record ProductMinDTO(Long id, String name, BigDecimal price, String imgUrl) {

}
