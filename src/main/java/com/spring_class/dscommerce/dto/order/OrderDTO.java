package com.spring_class.dscommerce.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.spring_class.dscommerce.dto.orderItem.OrderItemDTO;
import com.spring_class.dscommerce.dto.payment.PaymentDTO;
import com.spring_class.dscommerce.dto.user.UserMinDTO;

public record OrderDTO(Long id, Instant moment, String status, UserMinDTO client, PaymentDTO payment, List<OrderItemDTO> items, BigDecimal total) {

}
