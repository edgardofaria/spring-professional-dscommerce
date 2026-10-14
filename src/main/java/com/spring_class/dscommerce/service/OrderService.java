package com.spring_class.dscommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring_class.dscommerce.dto.order.OrderDTO;
import com.spring_class.dscommerce.dto.order.OrderRequestDTO;
import com.spring_class.dscommerce.dto.orderItem.OrderItemRequestDTO;
import com.spring_class.dscommerce.entities.Order;
import com.spring_class.dscommerce.entities.OrderItem;
import com.spring_class.dscommerce.entities.OrderStatus;
import com.spring_class.dscommerce.entities.Product;
import com.spring_class.dscommerce.mapper.OrderMapper;
import com.spring_class.dscommerce.repositories.OrderRepository;
import com.spring_class.dscommerce.repositories.ProductRepository;
import com.spring_class.dscommerce.service.exceptions.OrderNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toOrderDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Transactional
    public OrderDTO insert(OrderRequestDTO request) {
        Order order = new Order(OrderStatus.WAITING_PAYMENT, userService.authenticate());
        for (OrderItemRequestDTO item : request.items()) {
            Product product = productRepository.getReferenceById(item.productId());
            OrderItem orderItem = new OrderItem(order, product, item.quantity(), product.getPrice());
            order.addItem(orderItem);
        }
        return mapper.toOrderDTO(repository.save(order));
    }

}
